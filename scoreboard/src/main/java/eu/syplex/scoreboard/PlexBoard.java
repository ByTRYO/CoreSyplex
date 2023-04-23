package eu.syplex.scoreboard;

import eu.syplex.scoreboard.exception.DuplicateTeamException;
import eu.syplex.scoreboard.exception.LineTooLongException;
import eu.syplex.scoreboard.exception.TeamNameTooLongException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Represent an abstract implementation of scoreboards.
 */
public abstract class PlexBoard {

    private final List<PlexTeam> teams = new ArrayList<>();
    private final List<UUID> activePlayers = new ArrayList<>();
    private final Map<Scoreboard, List<Component>> previousLines = new HashMap<>();
    private final int maxLineLength = 128;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    // MARK: Public API

    /**
     * Add a player to the scoreboard.
     *
     * @param player The player to add
     */
    public void addPlayer(@NotNull Player player) {
        if (activePlayers.contains(player.getUniqueId())) return;
        activePlayers.add(player.getUniqueId());
    }

    /**
     * Remove the player from the {@link PlexBoard} and remove any teams they may be member of.
     * This will reset their scoreboard to the main scoreboard from bukkit.
     *
     * @param player The player to remove
     */
    public void removePlayer(@NotNull Player player) {
        activePlayers.remove(player.getUniqueId());

        Validate.notNull(Bukkit.getScoreboardManager());
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        teams.forEach(team -> {
            if (team.isOnTeam(player.getUniqueId())) team.remove(player);
        });
    }

    /**
     * Find a team using a name.
     *
     * @param name The name to search for
     * @return The {@link PlexTeam} found, if any. Will return null if no team exists
     */
    public @NotNull Optional<PlexTeam> findTeam(@NotNull String name) {
        return teams.stream()
                .filter(team -> team.name().equals(name))
                .findAny();
    }

    /**
     * Create a team on the scoreboard.
     *
     * @param name The name for the new team. This name cannot be longer than 16 characters
     * @return The created {@link PlexTeam}
     * @throws DuplicateTeamException   If a team with that name already exists
     * @throws TeamNameTooLongException If the team's name is longer than 16 characters
     */
    public PlexTeam create(@NotNull String name, Component display) throws DuplicateTeamException, TeamNameTooLongException {
        for (PlexTeam team : teams) {
            if (team.name().equals(name)) throw new DuplicateTeamException(name);
        }

        if (name.length() > 16) throw new TeamNameTooLongException(name);

        PlexTeam team = new PlexTeam(name, display, this);
        team.refresh();
        teams.add(team);

        return team;
    }

    /**
     * Remove a team from the scoreboard.
     *
     * @param team The team to remove from the scoreboard
     */
    public void remove(@NotNull PlexTeam team) {
        if (team.board() != this) return;

        team.destroy();
        teams.remove(team);
    }

    /**
     * Destroy the scoreboard. This will reset all players to the server's main scoreboard, and clear all teams.
     * You should call this method inside your plugin's {@link JavaPlugin#onDisable()} method.
     */
    public void destroy() {
        for (UUID uuid : activePlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

        for (PlexTeam team : teams) team.destroy();

        activePlayers.clear();
        teams.clear();
    }

    /**
     * Returns all teams registered in a {@link PlexBoard}.
     *
     * @return A list of registered teams
     */
    protected @NotNull List<PlexTeam> teams() {
        return teams;
    }

    // MARK: Internal API

    /**
     * Update a scoreboard with a list of lines. These lines must be in reverse order.
     *
     * @throws LineTooLongException If a component's content within the lines array is over 64 characters, this exception is thrown.
     */
    protected void updateScoreboard(@NotNull Scoreboard scoreboard, List<Component> lines) throws LineTooLongException {
        Objective objective = dummyObjective(scoreboard);
        Component title = title(scoreboard);

        objective.displayName(title);

        if (previousLines.containsKey(scoreboard)) {
            if (previousLines.get(scoreboard).equals(lines)) { // Are the lines the same? Don't take up server resources to change absolutely nothing
                updateTeams(scoreboard); // Update the teams anyway
                return;
            }

            // Size difference means unregister objective to reset and re-register teams correctly
            if (previousLines.get(scoreboard).size() != lines.size()) {
                scoreboard.clearSlot(DisplaySlot.SIDEBAR);
                scoreboard.getEntries().forEach(scoreboard::resetScores);
                scoreboard.getTeams().forEach(team -> {
                    if (team.getName().contains("line")) team.unregister();
                });
            }
        }

        // This is a copy instead of reference to prevent previousLines equality check from unexpectedly failing
        previousLines.put(scoreboard, new ArrayList<>(lines));

        List<Component> reversed = new ArrayList<>(lines);
        Collections.reverse(reversed);

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<Component> options = colorOptions(reversed.size());

        int score = 1;
        for (Component entry : reversed) {
            String serialized = miniMessage.serialize(entry);
            if (serialized.length() > 16) throw new LineTooLongException(serialized, serialized.length());

            Team team = scoreboard.getTeam("line" + score);
            if (team != null) {
                team.getEntries().forEach(team::removeEntry);
                team.addEntry(miniMessage.serialize(options.get(score)));

            } else {
                team = scoreboard.registerNewTeam("line" + score);
                team.addEntry(miniMessage.serialize(options.get(score)));
                objective.getScore(miniMessage.serialize(options.get(score))).setScore(score);
            }

            score++;
        }
        updateTeams(scoreboard);
    }

    /**
     * Update the teams on the scoreboard.
     *
     * @param scoreboard The Bukkit scoreboard to use
     * @see PlexTeam#refresh(Scoreboard)
     */
    private void updateTeams(@NotNull Scoreboard scoreboard) {
        teams.forEach(team -> team.refresh(scoreboard));
    }

    /**
     * Generate a list of unique color code combinations to use as scoreboard entries.
     * This is done to ensure that...
     * 1. Duplicate lines can be created
     * 2. The content of a scoreboard line is stored in the team prefix + suffix, rather than the entry itself
     *
     * @param amountOfLines The amount of lines, and by proxy the amount of color combinations, to be generated
     * @return The list of unique color combinations
     */
    private @NotNull List<Component> colorOptions(int amountOfLines) {
        List<Component> options = new ArrayList<>();
        for (NamedTextColor first : NamedTextColor.NAMES.values()) {
            for (NamedTextColor second : NamedTextColor.NAMES.values()) {
                Component option = Component.text("", first).append(Component.text("", second));

                if (first != second && !options.contains(option)) {
                    options.add(option);

                    if (options.size() == amountOfLines) break;
                }
            }
        }

        return options;
    }


    /**
     * Returns all players on this scoreboard. The list may be empty if no player or entity is present on it.
     *
     * @return A list of all present entities on this scoreboard
     */
    protected @NotNull List<UUID> activePlayers() {
        return activePlayers;
    }

    /**
     * Translates a string into a {@link Component} using the adventure api and its colors.
     *
     * @param string The string to translate
     * @return The colored component
     */
    protected Component color(@NotNull String string) {
        return MiniMessage.miniMessage().deserialize(string, StandardTags.color(), StandardTags.gradient());
    }

    /**
     * Ges the title for a particular {@link Scoreboard}.
     *
     * @param scoreboard The bukkit scoreboard that requires a title
     * @return The title as a {@link Component}
     */
    protected abstract Component title(@NotNull Scoreboard scoreboard);

    /**
     * Returns a dummy objective if present on the scoreboard. Otherwise, it will be created.
     *
     * @param scoreboard The scoreboard
     * @return The dummy objective
     */
    private @NotNull Objective dummyObjective(@NotNull Scoreboard scoreboard) {
        Objective objective = scoreboard.getObjective("dummy");
        if (objective == null) {
            objective = scoreboard.registerNewObjective("dummy", Criteria.DUMMY, Component.text("dummy"));
        }
        return objective;
    }
}

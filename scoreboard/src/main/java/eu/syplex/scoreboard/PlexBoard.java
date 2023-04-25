package eu.syplex.scoreboard;

import eu.syplex.common.exception.NotTranslatableException;
import eu.syplex.common.translator.ComponentTranslator;
import eu.syplex.scoreboard.exception.LineTooLongException;
import eu.syplex.scoreboard.util.LegacyColorUtil;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class PlexBoard {

    private final List<UUID> associatedPlayers = new ArrayList<>();
    private final Map<Scoreboard, List<String>> legacyLines = new HashMap<>();

    private final int maxLineLength = 128;

    // MARK: Public API

    /**
     * Adds a player to the scoreboard if not present on it yet.
     *
     * @param player The player to add
     * @throws NotTranslatableException Thrown if a line could not be translated into a {@link Component}
     * @throws LineTooLongException     Thrown if a line is longer than the maximum of allowed chars
     */
    public void addPlayer(@NotNull Player player) throws NotTranslatableException, LineTooLongException {
        if (associatedPlayers.contains(player.getUniqueId())) return;
        associatedPlayers.add(player.getUniqueId());
    }

    /**
     * Removes the player from the scoreboard if present on it and resets to the main bukkit board.
     *
     * @param player The player to remove
     */
    public void removePlayer(@NotNull Player player) {
        associatedPlayers.remove(player.getUniqueId());
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    /**
     * Destroys the scoreboard and resets all players boards to the bukkit main scoreboard.
     * Note: Call this method only in {@link JavaPlugin#onDisable()}.
     */
    public void destroy() {
        for (UUID uuid : associatedPlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) break;

            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

        associatedPlayers.clear();
    }

    // MARK: Internal API

    /**
     * Updates a scoreboard with a list of lines. These have to be in reversed order and colors have to be in the legacy format.
     *
     * @param scoreboard The scoreboard to update
     * @param lines      The list of lines
     * @throws LineTooLongException     Thrown if a line was too long
     * @throws NotTranslatableException Thrown if the line cannot be translated into a component
     */
    protected void updateScoreboard(Scoreboard scoreboard, @NotNull List<String> lines) throws LineTooLongException, NotTranslatableException {
        Objective objective = dummyObjective(scoreboard);
        objective.displayName(title(scoreboard));

        if (legacyLines.containsKey(scoreboard)) {
            if (legacyLines.get(scoreboard).equals(lines)) return;

            if (legacyLines.get(scoreboard).size() != lines.size()) {
                scoreboard.clearSlot(DisplaySlot.SIDEBAR);
                scoreboard.getEntries().forEach(scoreboard::resetScores);
                scoreboard.getTeams().stream().filter(team -> team.getName().contains("line")).forEach(Team::unregister);
            }
        }
        legacyLines.put(scoreboard, new ArrayList<>(lines));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<String> reversedLines = new ArrayList<>(lines);
        Collections.reverse(reversedLines);

        List<String> colorCodeOptions = colorOptions(reversedLines.size());

        int score = 1;
        for (String entry : reversedLines) {
            if (entry.length() > maxLineLength) throw new LineTooLongException(entry, maxLineLength);

            entry = ComponentTranslator.translator().serializeLegacy(color(entry));

            Team team = scoreboard.getTeam("line" + score);
            String prefix;
            String suffix = "";

            int cutOff = 64;
            if (entry.length() <= cutOff) {
                prefix = entry;

            } else {
                prefix = entry.substring(0, cutOff);

                if (prefix.endsWith(String.valueOf(LegacyColorUtil.LEGACY_CHAR))) {
                    prefix = prefix.substring(0, prefix.length() - 1);
                    suffix = LegacyColorUtil.LEGACY_CHAR + suffix;
                }

                suffix = StringUtils.left(LegacyColorUtil.lastColor(prefix) + suffix + entry.substring(cutOff), cutOff);
            }

            if (team != null) {
                team.getEntries().forEach(team::removeEntry);
                team.addEntry(colorCodeOptions.get(score));

            } else {
                team = scoreboard.registerNewTeam("line" + score);
                team.addEntry(colorCodeOptions.get(score));
                objective.getScore(colorCodeOptions.get(score)).setScore(score);
            }

            team.prefix(ComponentTranslator.translator().translate(prefix));
            team.suffix(ComponentTranslator.translator().translate(suffix));

            score++;
        }

    }

    /**
     * Generate a list of unique color code combinations to use as scoreboard entries using the legacy class {@link ChatColor}.
     * This is done to ensure that
     * <ol>
     *     <li>Duplicate lines can be created</li>
     *     <li>The content of a scoreboard line is stored in the team prefix + suffix, rather than the entry itself</li>
     * </ol>
     *
     * @param amount The amount of lines, and by proxy the amount of color combinations, to be generated
     * @return The list of unique color combinations
     */
    @SuppressWarnings("deprecation")
    private List<String> colorOptions(int amount) {
        List<String> colorCodeOptions = new ArrayList<>();

        for (ChatColor first : ChatColor.values()) {
            if (first.isFormat()) continue;

            for (ChatColor second : ChatColor.values()) {
                if (second.isFormat()) continue;

                String option = first + String.valueOf(second);

                if (first != second && !colorCodeOptions.contains(option)) {
                    colorCodeOptions.add(option);

                    if (colorCodeOptions.size() == amount) break;
                }
            }
        }

        return colorCodeOptions;
    }


    /**
     * Returns a list containing all entities associated with this scoreboard. This list can be empty if no entity belongs to the board.
     *
     * @return The list with all entities on this board
     */
    protected @NotNull List<UUID> associatedPlayers() {
        return associatedPlayers;
    }

    /**
     * Translates a string into a colored {@link Component}.
     *
     * @param string The string to translate
     * @return The colored component
     * @see ComponentTranslator#translateLegacy(String)
     */
    protected @NotNull Component color(String string) {
        return ComponentTranslator.translator().translateLegacy(string);
    }

    /**
     * Returns the title if a particular scoreboard.
     *
     * @param scoreboard The scoreboard
     * @return The title
     */
    protected abstract @NotNull Component title(Scoreboard scoreboard);

    /**
     * Returns a dummy objective. It will be created if not present on the scoreboard, otherwise only returned.
     *
     * @param scoreboard The scoreboard
     * @return A dummy objective
     */
    private @NotNull Objective dummyObjective(Scoreboard scoreboard) {
        Objective objective = scoreboard.getObjective("dummy");

        if (objective == null) {
            objective = scoreboard.registerNewObjective("dummy", Criteria.DUMMY, Component.empty());
        }

        return objective;
    }

}

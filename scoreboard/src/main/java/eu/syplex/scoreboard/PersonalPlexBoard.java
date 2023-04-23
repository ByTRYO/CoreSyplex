package eu.syplex.scoreboard;

import eu.syplex.scoreboard.exception.LineTooLongException;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

/**
 * Represents a {@link PlexBoard} displayed to a single player on a paper server.
 */
public class PersonalPlexBoard extends PlexBoard {

    private Function<Player, Component> title;
    private Function<Player, List<Component>> lines;

    private final Map<UUID, Scoreboard> playerScoreboard = new HashMap<>();

    /**
     * Instantiates a new {@link PersonalPlexBoard} with a title and a list of lines for a specific player.
     *
     * @param title The title
     * @param lines The list aof lines
     */
    public PersonalPlexBoard(Function<Player, Component> title, Function<Player, List<Component>> lines) {
        this.title = title;
        this.lines = lines;
    }

    // MARK: Public API


    /**
     * {@inheritDoc}
     *
     * @param player The player to add
     */
    @Override
    public void addPlayer(@NotNull Player player) {
        activePlayers().add(player.getUniqueId());

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(scoreboard);
        playerScoreboard.put(player.getUniqueId(), scoreboard);

        try {
            updateScoreboard();
        } catch (LineTooLongException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param player The player to remove
     */
    @Override
    public void removePlayer(@NotNull Player player) {
        super.removePlayer(player);
        playerScoreboard.remove(player.getUniqueId());
    }

    /**
     * Get the bukkit {@link Scoreboard} for a specific player.
     * This will not change in between updates, but will change if the player is removed and re-added to the {@link PlexBoard}.
     *
     * @param player The player to get the scoreboard for
     * @return The bukkit Scoreboard
     */
    public Scoreboard toBukkitScoreboard(@NotNull Player player) {
        return playerScoreboard.get(player.getUniqueId());
    }

    /**
     * Call this when the contents of the scoreboard should change in some way.
     *
     * @throws LineTooLongException Thrown if the scoreboard contains a line over 64 characters
     */
    public void updateScoreboard() throws LineTooLongException {
        if (title == null) return; // Line generator is not ready yet

        for (UUID uuid : activePlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            List<Component> lines = this.lines.apply(player);
            if (lines == null) lines = new ArrayList<>();

            updateScoreboard(player.getScoreboard(), lines);
        }
    }

    // MARK: Internal API

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component title(@NotNull Scoreboard scoreboard) {
        return title.apply(playerScoreboard(scoreboard));
    }

    /**
     * Gets the player based on the scoreboard or null if the player belongs to no scoreboard.
     *
     * @param scoreboard The scoreboard the player belongs to
     * @return The player
     */
    private @Nullable Player playerScoreboard(Scoreboard scoreboard) {
        if (!playerScoreboard.containsValue(scoreboard)) return null;
        Player player = null;

        for (Map.Entry<UUID, Scoreboard> entry : playerScoreboard.entrySet()) {
            if (entry.getValue().equals(scoreboard)) {
                player = Bukkit.getPlayer(entry.getKey());
                break;
            }
        }

        return player;
    }

    /**
     * Sets the lines to the given list of new lines.
     *
     * @param lines The list of new lines
     */
    protected void lines(Function<Player, List<Component>> lines) {
        this.lines = lines;
    }

    /**
     * Sets the title to the given new title.
     *
     * @param title The new title
     */
    protected void title(Function<Player, Component> title) {
        this.title = title;
    }
}

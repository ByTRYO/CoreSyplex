package eu.syplex.scoreboard;

import eu.syplex.common.exception.NotTranslatableException;
import eu.syplex.scoreboard.exception.LineTooLongException;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Represents a {@link PlexBoard} which is displayed for all players online on a paper server.
 */
public class GlobalPlexBoard extends PlexBoard {

    private Supplier<Component> title;
    private Supplier<List<Component>> lines;

    private Scoreboard scoreboard;

    /**
     * Instantiates a new {@link GlobalPlexBoard} with a supplier for the title and lines.
     *
     * @param title The supplier for the title
     * @param lines The supplier for the lines.
     */
    public GlobalPlexBoard(@NotNull Supplier<Component> title, @NotNull Supplier<List<Component>> lines) {
        this.title = title;
        this.lines = lines;
    }

    // MARK: Public API


    /**
     * {@inheritDoc}
     *
     * @param player The player to add
     * @throws NotTranslatableException Is thrown if {@link #updateScoreboard()} fails with this exception
     * @throws LineTooLongException     Is thrown if {@link #updateScoreboard()} fails with this exception
     */
    @Override
    public void addPlayer(@NotNull Player player) throws NotTranslatableException, LineTooLongException {
        super.addPlayer(player);
        updateScoreboard();

        player.setScoreboard(scoreboard);
    }

    /**
     * Get the Bukkit Scoreboard that backs this {@link GlobalPlexBoard}.
     *
     * @return The Bukkit Scoreboard
     */
    public Scoreboard toBukkitScoreboard() {
        return scoreboard;
    }

    /**
     * Update the scoreboard. Call this when the scoreboard contents should change in some way.
     *
     * @throws LineTooLongException     Thrown if {@link #updateScoreboard(Scoreboard, List)} fails
     * @throws NotTranslatableException Thrown if {@link #updateScoreboard(Scoreboard, List)} fails with this exception
     */
    public void updateScoreboard() throws LineTooLongException, NotTranslatableException {
        createIfNull();
        if (lines != null) updateScoreboard(toBukkitScoreboard(), lines.get());
    }

    // MARK: Internal API:

    /**
     * Creates the Bukkit Scoreboard for this scoreboard to use
     */
    private void createIfNull() {
        if (scoreboard != null) return;
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        for (UUID uuid : activePlayers()) {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null) continue;
            player.setScoreboard(scoreboard);
        }
    }

    /**
     * Sets the title to the given new title.
     *
     * @param title The new title
     */
    protected void title(Supplier<Component> title) {
        this.title = title;
    }

    /**
     * Sets the lines to the given list of new lines.
     *
     * @param lines The list of new lines
     */
    protected void lines(Supplier<List<Component>> lines) {
        this.lines = lines;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component title(@NotNull Scoreboard scoreboard) {
        return title.get();
    }
}

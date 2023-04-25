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
 * Represents a {@link PlexBoard} visible for all players on the paper server.
 *
 * @version 1.0.0
 * @since 1.2.0
 */
public class GlobalScoreboard extends PlexBoard {

    private @NotNull Supplier<Component> title;
    private @NotNull Supplier<List<String>> lines;

    private Scoreboard scoreboard;

    /**
     * Instantiates a new {@link GlobalScoreboard} with  suppliers for the title and lines.
     *
     * @param title The title
     * @param lines The list of lines
     */
    public GlobalScoreboard(@NotNull Supplier<Component> title, @NotNull Supplier<List<String>> lines) {
        this.title = title;
        this.lines = lines;
    }

    // MARK: Public API

    /**
     * {@inheritDoc}
     *
     * @throws NotTranslatableException Thrown if a line could not be translated into a {@link Component}
     * @throws LineTooLongException     Thrown if a line is longer than the maximum of allowed chars
     * @see PlexBoard#updateScoreboard(Scoreboard, List)
     */
    public void updateScoreboard() throws NotTranslatableException, LineTooLongException {
        createIfNull();
        updateScoreboard(toBukkitScoreboard(), lines.get());
    }

    /**
     * {@inheritDoc}
     *
     * @param player The player to add
     * @throws NotTranslatableException Thrown if a line could not be translated into a {@link Component}
     * @throws LineTooLongException     Thrown if a line is longer than the maximum of allowed chars
     * @see #updateScoreboard()
     */
    @Override
    public void addPlayer(@NotNull Player player) throws NotTranslatableException, LineTooLongException {
        super.addPlayer(player);

        createIfNull();
        player.setScoreboard(scoreboard);
    }

    /**
     * Gets this scoreboard as a bukkit {@link Scoreboard}.
     *
     * @return A bukkit scoreboard
     */
    public Scoreboard toBukkitScoreboard() {
        return scoreboard;
    }

    // MARK: Internal API

    /**
     * Creates a bukkit scoreboard for this implementation to use if not present.
     */
    private void createIfNull() {
        if (scoreboard != null) return;

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        for (UUID uuid : associatedPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            player.setScoreboard(scoreboard);
        }
    }

    /**
     * Sets the lines of the scoreboard to the list of lines supplied by this method.
     *
     * @param lines The list of lines
     */
    protected void lines(@NotNull Supplier<List<String>> lines) {
        this.lines = lines;
    }

    /**
     * Sets the title of the scoreboard to the new value supplied by this method.
     *
     * @param title The title supplier
     */
    protected void title(@NotNull Supplier<Component> title) {
        this.title = title;
    }

    /**
     * {@inheritDoc}
     *
     * @param scoreboard The scoreboard
     * @return The title of the scoreboard as a {@link Component}
     */
    @Override
    protected @NotNull Component title(Scoreboard scoreboard) {
        return title.get();
    }
}

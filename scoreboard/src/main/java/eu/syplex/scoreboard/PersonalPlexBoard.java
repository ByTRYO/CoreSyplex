package eu.syplex.scoreboard;

import eu.syplex.common.exception.NotTranslatableException;
import eu.syplex.scoreboard.exception.LineTooLongException;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class PersonalPlexBoard extends PlexBoard {

    private @NotNull Function<@NotNull Player, @NotNull Component> title;
    private @NotNull Function<@NotNull Player, @NotNull List<String>> lines;

    private final Map<UUID, Scoreboard> scoreboards = new HashMap<>();

    /**
     * Instantiates a new {@link PersonalPlexBoard} from functions to generate the title and lines.
     *
     * @param title The function to generate the title. The player is the receiver of the board
     * @param lines The function to generate the lines. The player is the receiver of the board
     */
    public PersonalPlexBoard(@NotNull Function<@NotNull Player, @NotNull Component> title, @NotNull Function<@NotNull Player, @NotNull List<String>> lines) {
        this.title = title;
        this.lines = lines;
    }

    /**
     * Updates the content of the scoreboard.
     *
     * @throws NotTranslatableException Thrown if a line could not be translated into a {@link Component}
     * @throws LineTooLongException     Thrown if a line is longer than the maximum of allowed chars
     * @see PlexBoard#updateScoreboard(Scoreboard, List)
     */
    public void updateScoreboard() throws NotTranslatableException, LineTooLongException {
        for (UUID uuid : associatedPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            List<String> lines = this.lines.apply(player);
            updateScoreboard(player.getScoreboard(), lines);
        }
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
        associatedPlayers().add(player.getUniqueId());

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(scoreboard);
        scoreboards.put(player.getUniqueId(), scoreboard);

        updateScoreboard();
    }

    /**
     * {@inheritDoc}
     *
     * @param player The player to remove
     */
    @Override
    public void removePlayer(@NotNull Player player) {
        super.removePlayer(player);
        scoreboards.remove(player.getUniqueId());
    }

    /**
     * Gets the {@link PersonalPlexBoard} as a bukkit {@link Scoreboard}. May be null if the player has no personal board.
     *
     * @param player The player to which the scoreboard belongs to
     * @return The bukkit scoreboard
     */
    public @Nullable Scoreboard toBukkitScoreboard(@NotNull Player player) {
        return scoreboards.get(player.getUniqueId());
    }

    /**
     * Sets the lines to the given list of new lines.
     *
     * @param lines The list of lines
     */
    protected void lines(@NotNull Function<@NotNull Player, @NotNull List<String>> lines) {
        this.lines = lines;
    }

    /**
     * Sets the title to the given new one.
     *
     * @param title The new title
     */
    protected void title(@NotNull Function<@NotNull Player, @NotNull Component> title) {
        this.title = title;
    }

    /**
     * {@inheritDoc}
     *
     * @param scoreboard The scoreboard
     * @return The title of the board
     */
    @Override
    protected @NotNull Component title(Scoreboard scoreboard) {
        Player player = playerFromScoreboard(scoreboard);
        Validate.notNull(player);

        return title.apply(player);
    }

    /**
     * Gets the player by the scoreboard. May be null.
     *
     * @param scoreboard The scoreboard
     * @return The player that belongs to that board
     */
    private @Nullable Player playerFromScoreboard(Scoreboard scoreboard) {
        if (!scoreboards.containsValue(scoreboard)) return null;

        Player player = null;
        for (Map.Entry<UUID, Scoreboard> entry : scoreboards.entrySet()) {
            if (entry.getValue().equals(scoreboard)) {
                player = Bukkit.getPlayer(entry.getKey());
                break;
            }
        }
        return player;
    }
}

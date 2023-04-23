package eu.syplex.scoreboard;

import eu.syplex.scoreboard.exception.TeamNameTooLongException;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a team applied to a {@link PlexBoard}.
 */
public class PlexTeam {

    private String name;
    private Component display;
    private final List<UUID> entities = new ArrayList<>();
    private final PlexBoard board;

    /**
     * Instantiates a new {@link PlexTeam} with a name, display and the scoreboard it will belong to.
     *
     * @param name    The name
     * @param display The display of the team
     * @param board   the scoreboard
     */
    protected PlexTeam(String name, Component display, PlexBoard board) {
        this.name = name;
        this.display = display;
        this.board = board;
    }

    /**
     * Returns all entities on this team if present or an empty list.
     *
     * @return A list with all entities on this team
     */
    public @NotNull List<UUID> entities() {
        return entities;
    }

    /**
     * Returns the current name of the team.
     *
     * @return The current name
     */
    public @NotNull String name() {
        return name;
    }

    /**
     * Returns the current display.
     *
     * @return The current display
     */
    public @NotNull Component display() {
        return display;
    }

    /**
     * Sets the display of the team to the given component.
     *
     * @param display The new display
     */
    public void display(@NotNull Component display) {
        this.display = display;
        refresh();
    }

    /**
     * Set the name of the team to the given name.
     *
     * @param name The new name
     * @throws TeamNameTooLongException If the length of the new name is larger than 16 chars, this exception is thrown
     */
    public void name(@NotNull String name) throws TeamNameTooLongException {
        if (name.length() > 16) throw new TeamNameTooLongException(name);

        this.name = name;
        refresh();
    }

    /**
     * Refreshes the scoreboard.
     *
     * @see #refresh(Scoreboard)
     */
    public void refresh() {
        if (board instanceof PersonalPlexBoard) {
            for (UUID uuid : entities) {
                Player player = Bukkit.getPlayer(uuid);

                if (player == null) continue;
                refresh(player.getScoreboard());
            }
        } else {
            refresh(((GlobalPlexBoard) board).toBukkitScoreboard());
        }
    }

    /**
     * Refreshes the scoreboard.
     *
     * @param scoreboard The scoreboard to refresh
     */
    public void refresh(@NotNull Scoreboard scoreboard) {
        Team team = toBukkitTeam(scoreboard);

        for (UUID uuid : entities) {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null && !team.hasEntry(player.getName())) team.addEntry(player.getName());
            else if (player == null) {
                if (!team.hasEntry(uuid.toString())) team.addEntry(uuid.toString());
            }
        }
    }

    /**
     * Removes the player from the team if present on it.
     *
     * @param player The player to remove
     * @see #remove(Entity)
     */
    public void remove(@NotNull Player player) {
        if (!isOnTeam(player.getUniqueId())) return;
    }

    /**
     * Removes the entity from the team if present.
     *
     * @param entity The entity to remove
     * @see #remove(UUID)
     */
    public void remove(@NotNull Entity entity) {
        if (!isOnTeam(entity.getUniqueId())) return;
        remove(entity.getUniqueId());
        handleRemoval(entity.getUniqueId().toString());
    }

    /**
     * Removes the entity with the given uuid from the scoreboard and refreshes it.
     *
     * @param uuid The UUID to remove
     */
    public void remove(@NotNull UUID uuid) {
        entities.remove(uuid);
        refresh();
    }

    /**
     * Destroys the team. This will remove the team from the scoreboard and clears all entries.
     */
    public void destroy() {
        if (board instanceof PersonalPlexBoard) {
            for (UUID uuid : entities) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;

                Team team = player.getScoreboard().getTeam(name);
                if (team != null) team.unregister();
            }
        }
    }

    // MARK: Internal API

    /**
     * Returns weather an uuid is registered on this team.
     *
     * @param uuid The uuid
     * @return {@code true} if the uuid belongs to this team. Otherwise {@code false}
     */
    public boolean isOnTeam(@NotNull UUID uuid) {
        return entities.contains(uuid);
    }

    /**
     * Removes the entry from the scoreboard.
     *
     * @param entry The entry to remove
     */
    private void handleRemoval(String entry) {
        if (board instanceof GlobalPlexBoard) {
            Team team = toBukkitTeam(((GlobalPlexBoard) board).toBukkitScoreboard());
            team.removeEntry(entry);
            return;
        }

        PersonalPlexBoard personalPlexBoard = (PersonalPlexBoard) board;
        for (UUID uuid : personalPlexBoard.activePlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            Scoreboard scoreboard = personalPlexBoard.toBukkitScoreboard(player);
            Team team = toBukkitTeam(scoreboard);

            team.removeEntry(entry);
        }
    }

    /**
     * Converts this {@link PlexTeam} into a {@link Team} from bukkit or creates a new one if a team with the name is not registered on the scoreboard.
     *
     * @param scoreboard The scoreboard where the team is registered on
     * @return The bukkit team
     */
    public @NotNull Team toBukkitTeam(@NotNull Scoreboard scoreboard) {
        Team team = scoreboard.getTeam(name);

        if (team == null) return scoreboard.registerNewTeam(name);
        return team;
    }

    /**
     * Returns the {@link PlexBoard} to witch the team belongs.
     *
     * @return The {@link PlexBoard}
     */
    public @NotNull PlexBoard board() {
        return board;
    }
}

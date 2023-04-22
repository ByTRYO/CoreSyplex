package eu.syplex.scoreboard

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.apache.commons.lang3.Validate
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import java.util.*

abstract class AbstractScoreboard {

    private val teams = ArrayList<Team>()
    private val activePlayers = ArrayList<UUID>()
    private val previousLines = HashMap<Scoreboard, List<Component>>()

    private val maxLineLength = 128

    // MARK: Public API

    /**
     * Add a player to the scoreboard
     * @param player The player to add
     */
    open fun addPlayer(player: Player) {
        if (activePlayers.contains(player.uniqueId)) return
        activePlayers.add(player.uniqueId)
    }

    /**
     * Remove the player from the eu.syplex.scoreboard.AbstractScoreboard, and remove any teams they may be a member of.
     * This will reset their scoreboard to the main scoreboard.
     * @param player The player to remove
     */
    open fun removePlayer(player: Player) {
        activePlayers.remove(player.uniqueId)
        Validate.notNull(Bukkit.getScoreboardManager())
        player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard

        teams.forEach { team ->
            if (!team.isOnTeam(player.uniqueId)) return@forEach
            team.removePlayer(player)
        }
    }

    /**
     * Find a team by its name.
     * @param name The name to search for
     * @return The eu.syplex.scoreboard.AbstractScoreboard found, if any. Will return null if no team exists
     */
    fun findTeam(name: String): Optional<Team> {
        return teams.stream()
            .filter { team -> name.equals(team.name, true) }
            .findAny()
    }


    /**
     * Create a team on the scoreboard. Color.WHITE is used as the color for the team.
     * @param name The name for the new team. This name cannot be longer than 16 characters
     * @return The created eu.syplex.scoreboard.Team
     */
    fun createTeam(name: String, display: Component): Team {
        return createTeam(name, display, Color.WHITE)
    }

    /**
     * Create a team on the scoreboard.
     * @param name The name for the new team. This name cannot be longer than 16 characters
     * @return The created eu.syplex.scoreboard.Team
     */
    fun createTeam(name: String, display: Component, color: Color): Team {
        for (team in teams) {
            if (!team.name.equals(name, true)) continue
            throw IllegalArgumentException("A team with this name already exists! ($name)")
        }

        if (name.length > 16) throw IllegalArgumentException("The length of a team name cannot be longer than 16 chars! (${name.length} chars given)")

        val team = Team(name, display, color, this)
        team.refresh()
        teams.add(team)
        return team
    }

    /**
     * Remove a team from the scoreboard.
     * @param team The team to remove from the scoreboard
     */
    fun removeTeam(team: Team) {
        if (team.scoreboard() != this) return

        team.destroy()
        teams.remove(team)
    }


    /**
     * Destroy the scoreboard. This will reset all players to the server's main scoreboard, and clear all teams.
     * You should call this method inside your plugin's onDisable method.
     */
    fun destroy() {
        for (uuid in activePlayers) {
            val player = Bukkit.getPlayer(uuid) ?: continue
            player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        }

        teams.forEach { team -> team.destroy() }

        activePlayers.clear()
        teams.clear()
    }

    /**
     * Get the teams registered on the Scoreboard.
     * @return The teams registered on the scoreboard
     */
    fun teams(): List<Team> {
        return teams
    }

    // MARK: Internal API


    /**
     * Update a scoreboard with a list of lines.
     * These lines must be in reverse order!
     */
    fun updateScoreboard(scoreboard: Scoreboard, lines: List<Component>) {
        var objective = scoreboard.getObjective("dummy")
        if (objective == null) objective = scoreboard.registerNewObjective("dummy", Criteria.DUMMY, Component.empty())

        objective.displayName(title(scoreboard))

        if (previousLines.containsKey(scoreboard)) {
            if (previousLines[scoreboard]!! == lines) {
                updateTeams(scoreboard)
                return
            }

            if (previousLines[scoreboard]!!.size != lines.size) {
                scoreboard.clearSlot(DisplaySlot.SIDEBAR)
                scoreboard.entries.forEach(scoreboard::resetScores)
                scoreboard.teams.forEach { team ->
                    if (!team.name.contains("line")) return@forEach
                    team.unregister()
                }
            }
        }

        val linesCopy = ArrayList(lines)

        previousLines[scoreboard] = linesCopy
        linesCopy.reverse()

        objective.displaySlot = DisplaySlot.SIDEBAR

        var score = 1
        for (entry in linesCopy) {
            val minimized = MiniMessage.miniMessage().serialize(entry)
            if (minimized.length > maxLineLength) throw IllegalArgumentException("The name of the team cannot be longer than 128 chars! (${minimized.length} chars present)")

            var team = scoreboard.getTeam("line$score")
            var prefix: Component
            var suffix = Component.empty()

            if (minimized.length <= 64) prefix = entry
            else {
                prefix = Component.text(minimized.substring(0, 64))
                suffix = suffix.append(Component.text(minimized.substring(64)))
            }

            if (team != null) {
                team.entries.forEach(team::removeEntry)
                team.addEntry(MiniMessage.miniMessage().serialize(linesCopy[score]))

            } else {
                team = scoreboard.registerNewTeam("line$score")
                team.addEntry(MiniMessage.miniMessage().serialize(linesCopy[score]))
                objective.getScore(MiniMessage.miniMessage().serialize(linesCopy[score])).score = score
            }

            team.prefix(prefix)
            team.suffix(suffix)

            score++
        }

        updateTeams(scoreboard)
    }

    /**
     * Update the teams on the scoreboard.
     *
     * @param scoreboard The bukkit scoreboard to use
     * @see Team.refresh
     */
    private fun updateTeams(scoreboard: Scoreboard) {
        teams.forEach { team -> team.refresh(scoreboard) }
    }

    protected fun activePlayers(): List<UUID> {
        return activePlayers
    }

    protected abstract fun title(scoreboard: Scoreboard): Component

}

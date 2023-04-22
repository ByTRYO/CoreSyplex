package eu.syplex.scoreboard

import net.kyori.adventure.text.Component
import org.apache.commons.lang3.Validate
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import java.util.*
import java.util.function.Function

open class PersonalScoreboard(private var title: Function<Player?, Component>, private var lines: Function<Player?, List<Component>>) : AbstractScoreboard() {

    private val playerScoreboardMapping = HashMap<UUID, Scoreboard>()

    // MARK: Public API

    /**
     * Call this when the contents of the scoreboard should change in some way.
     *
     */
    fun updateScoreboard() {
        for (uuid in activePlayers()) {
            val player = Bukkit.getPlayer(uuid) ?: continue

            val lines = this.lines.apply(player)
            updateScoreboard(player.scoreboard, lines)
        }
    }

    /**
     * Add a player to the scoreboard.
     *
     * @param player The player to add
     */
    override fun addPlayer(player: Player) {
        super.addPlayer(player)
        Validate.notNull(Bukkit.getScoreboardManager())

        val scoreboard = Bukkit.getScoreboardManager().newScoreboard
        player.scoreboard = scoreboard
        playerScoreboardMapping[player.uniqueId] = scoreboard

        updateScoreboard()
    }

    /**
     * Remove a player from the scoreboard.
     *
     * @param player The player to remove
     */
    override fun removePlayer(player: Player) {
        super.removePlayer(player)
        playerScoreboardMapping.remove(player.uniqueId)
    }

    /**
     * Get the Bukkit Scoreboard for a specific player.
     * This will not change in between updates, but will change if the player is removed and re-added to the AbstractScoreboard.
     *
     * @param player The player to get the scoreboard for
     * @return The Bukkit Scoreboard
     */
    fun toBukkitScoreboard(player: Player): Scoreboard? {
        return playerScoreboardMapping[player.uniqueId]
    }

    // MARK: Internal API

    override fun title(scoreboard: Scoreboard): Component {
        return title.apply(playerForScoreboard(scoreboard))
    }

    private fun playerForScoreboard(scoreboard: Scoreboard): Player? {
        if (!playerScoreboardMapping.containsValue(scoreboard)) return null
        var player: Player? = null

        for (entry in playerScoreboardMapping.entries) {
            if (entry.value != scoreboard) continue
            player = Bukkit.getPlayer(entry.key)
            break
        }

        return player
    }

    protected fun lines(lines: Function<Player?, List<Component>>) {
        this.lines = lines
    }

    protected fun title(title: Function<Player?, Component>) {
        this.title = title
    }

}
package eu.syplex.scoreboard

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import java.util.*

open class Team(var name: String, private var display: Component, var color: Color, private val scoreboard: AbstractScoreboard) {

    private val entities: List<UUID> = ArrayList()

    fun refresh(bukkitScoreboard: Scoreboard) {}

    fun refresh() {}

    fun removePlayer(player: Player) {}


    /**
     * Destroy the team. Will remove the team from the scoreboard and clear entries.
     */
    fun destroy() {
        if (scoreboard is PersonalScoreboard) {
            for (uuid in entities) {
                val player = Bukkit.getPlayer(uuid) ?: continue
                val team = player.scoreboard.getTeam(name) ?: continue
                team.unregister()
            }
        } else {
//            val scoreboard = ((eu.syplex.scoreboard.GlobalScoreboard) this.scoreboard).toBukkitScoreboard()

        }
    }

    fun scoreboard(): AbstractScoreboard {
        return scoreboard
    }

    fun entities(): List<UUID> {
        return entities
    }

    fun isOnTeam(uuid: UUID): Boolean {
        return entities.contains(uuid)
    }

}
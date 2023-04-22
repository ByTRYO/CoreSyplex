package eu.syplex.scoreboard

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import java.util.*

open class Team(var name: String, private var display: Component, private val scoreboard: AbstractScoreboard) {

    private val entities: ArrayList<UUID> = ArrayList()

    fun refresh() {
        if (scoreboard is PersonalScoreboard) {
            for (uuid in entities) {
                val player = Bukkit.getPlayer(uuid) ?: continue

                refresh(player.scoreboard)
            }
        } else {
            refresh((scoreboard as GlobalScoreboard).toBukkitScoreboard())
        }
    }

    fun refresh(bukkitScoreboard: Scoreboard) {
        val team = toBukkitTeam(bukkitScoreboard)

        for (uuid in entities) {
            val player = Bukkit.getPlayer(uuid)

            if (player != null && !team.hasEntry(player.name)) {
                team.addEntry(player.name)

            } else if (player == null) {
                if (team.hasEntry(uuid.toString())) continue
                team.addEntry(uuid.toString())
            }

        }
    }

    fun toBukkitTeam(bukkitScoreboard: Scoreboard): Team {
        return if (bukkitScoreboard.getTeam(name) != null) bukkitScoreboard.getTeam(name)!! else bukkitScoreboard.registerNewTeam(name)
    }

    fun addPlayer(player: Player) {
        addEntity(player)
    }

    fun addEntity(entity: Entity) {
        addEntity(entity.uniqueId)
    }

    fun addEntity(uuid: UUID) {
        if (entities.contains(uuid)) return

        entities.add(uuid)
        refresh()
    }

    fun removePlayer(player: Player) {
        if (!isOnTeam(player.uniqueId)) return
        removeEntity(player)
    }

    fun removeEntity(entity: Entity) {
        if (!isOnTeam(entity.uniqueId)) return

        removeEntity(entity.uniqueId)
        handleRemoval(entity.uniqueId.toString())
    }

    fun removeEntity(uuid: UUID) {
        entities.remove(uuid)
        refresh()
    }

    private fun handleRemoval(entry: String) {
        if (scoreboard is GlobalScoreboard) {
            val team = toBukkitTeam(scoreboard.toBukkitScoreboard())
            team.removeEntry(entry)
            return
        }

        val personalScoreboard = scoreboard as PersonalScoreboard
        for (uuid in personalScoreboard.activePlayers()) {
            val player = Bukkit.getPlayer(uuid) ?: continue
            val scoreboard = personalScoreboard.toBukkitScoreboard(player) ?: continue
            val team = toBukkitTeam(scoreboard) ?: continue

            team.removeEntry(entry)
        }
    }


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
            val scoreboard = (this.scoreboard as GlobalScoreboard).toBukkitScoreboard()
            scoreboard.getTeam(name)?.unregister()
        }

        entities.clear()
    }

    fun scoreboard(): AbstractScoreboard {
        return scoreboard
    }

    fun name(name: String) {
        if (name.length > 16) throw IllegalArgumentException("The name of the team cannot be longer than 16 chars! (${name.length} present)")
        this.name = name
    }

    fun name(): String {
        return name
    }

    fun display(): Component {
        return display
    }

    fun display(display: Component) {
        this.display = display
    }

    fun entities(): List<UUID> {
        return entities
    }

    fun isOnTeam(uuid: UUID): Boolean {
        return entities.contains(uuid)
    }

}
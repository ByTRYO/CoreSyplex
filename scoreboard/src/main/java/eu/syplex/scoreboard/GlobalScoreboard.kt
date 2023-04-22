package eu.syplex.scoreboard

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import java.util.function.Supplier

open class GlobalScoreboard(private var title: Supplier<Component>, private var lines: Supplier<List<Component>>) : AbstractScoreboard() {

    private lateinit var scoreboard: Scoreboard


    // MARK: Public API

    /**
     * Update the scoreboard. Call this when the scoreboard contents should change in some way.
     */
    fun updateScoreboard() {
        createIfNull()
        if (lines != null) updateScoreboard(toBukkitScoreboard(), lines.get())
    }

    /**
     * Add a player to the scoreboard.
     *
     * @param player The player to add
     */
    override fun addPlayer(player: Player) {
        super.addPlayer(player)
        createIfNull()
        player.scoreboard = scoreboard
    }

    /**
     * Get the Bukkit Scoreboard that backs this AbstractScoreboard.
     *
     * @return The Bukkit Scoreboard
     */
    fun toBukkitScoreboard(): Scoreboard {
        return scoreboard
    }

    // MARK: Internal API

    /**
     * Creates the Bukkit Scoreboard for this scoreboard to use
     */
    private fun createIfNull() {
        if (this.scoreboard != null) return

        val scoreboardManager = Bukkit.getServer().scoreboardManager ?: return
        scoreboard = scoreboardManager.newScoreboard

        for (uuid in activePlayers()) {
            val player = Bukkit.getPlayer(uuid) ?: continue
            player.scoreboard = scoreboard
        }
    }

    protected fun linesSupplier(lines: Supplier<List<Component>>) {
        this.lines = lines
    }

    protected fun titleSupplier(title: Supplier<Component>) {
        this.title = title
    }

    override fun title(scoreboard: Scoreboard): Component {
        return title.get()
    }
}
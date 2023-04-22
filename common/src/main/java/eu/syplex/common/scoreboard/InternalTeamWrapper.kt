package eu.syplex.common.scoreboard

import org.bukkit.ChatColor
import org.bukkit.scoreboard.Team

abstract class InternalTeamWrapper {

    abstract fun color(team: Team, color: ChatColor)

}
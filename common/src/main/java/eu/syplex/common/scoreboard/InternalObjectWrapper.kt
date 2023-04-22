package eu.syplex.common.scoreboard

import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard

abstract class InternalObjectWrapper {

    abstract fun nameHealthObjective(scoreboard: Scoreboard): Objective

    abstract fun tabHealthObjective(wrappedHealthStyle: WrappedHealthStyle, scoreboard: Scoreboard): Objective

    abstract fun dummyObjective(scoreboard: Scoreboard): Objective

}
package test

import scala.tools.nsc
import nsc.Global
import nsc.Phase
import nsc.plugins.Plugin
import nsc.plugins.PluginComponent

class PhaseConflictPlugin(val global: Global) extends Plugin {
  plugin =>
    
  import global._

  val name = "phaseConflict"
  val description = "Demonstrates an (unnecessary) phase conflict"
    
  val components = List[PluginComponent](Component)
  
  // Main component
  private object Component extends PluginComponent {
    
    val global: plugin.global.type = plugin.global
    
    val runsAfter = List[String]("parser")
    override val runsBefore = List[String]("typer")
    override val runsRightAfter = Some("parser")
    
    val phaseName = plugin.name
    override val description = plugin.description
    
    def newPhase(prev: Phase) = new ConflictingPhase(prev)
    
    class ConflictingPhase(prev: Phase) extends StdPhase(prev) {
      override def name = plugin.name
      def apply(unit: CompilationUnit) {}
    }
  }
}

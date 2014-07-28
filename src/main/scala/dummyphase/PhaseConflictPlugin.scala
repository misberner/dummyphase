package dummyphase

import scala.tools.nsc
import nsc.Global
import nsc.Phase
import nsc.plugins.Plugin
import nsc.plugins.PluginComponent

class DummyPhasePlugin(val global: Global) extends Plugin {
  plugin =>

  implicit class Regex(sc: StringContext) {
    def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }
    
  import global._

  val name = "dummyphase"
  val description = "Demonstrates problems in Phase Assembly"

  val compBuf = collection.mutable.ListBuffer.empty[PluginComponent]

  val components = {
    val sp = new scala.sys.SystemProperties()
    sp.get("dummyphase.phases") match {
      case Some(opts) =>
        opts.split("\\s*\\|\\s*").toList flatMap { option =>
          option match {
            case r"(\w+)${name}\(([^;]*)${after};([^;]*)${rightAfter};([^;]*)${before}\)" =>
              val afterLst = after.split(",").toList.filterNot(_.isEmpty)
              val beforeLst = before.split(",").toList.filterNot(_.isEmpty)
              val rightAfterOpt = if (rightAfter.isEmpty) None else Some(rightAfter)
              val c = new DummyComponent(name, afterLst, beforeLst, rightAfterOpt)
              Some(c)
            case _ => None
          }
        }
      case None => List.empty[PluginComponent]
    }
  }

  private class DummyComponent(val phaseName: String,
                               val runsAfter: List[String],
                               override val runsBefore: List[String],
                               override val runsRightAfter: Option[String])
      extends PluginComponent { component =>

    val global: plugin.global.type = plugin.global

    def newPhase(prev: Phase) = new DummyPhase(prev)

    class DummyPhase(prev: Phase) extends StdPhase(prev) {
      override def name = component.phaseName
      def apply(unit: CompilationUnit) {}
    }

  }
  
}

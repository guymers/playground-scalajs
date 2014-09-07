package me.guymer.playground.scalajs

import org.scalajs.dom
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.JSApp
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.ReactVDom._
import japgolly.scalajs.react.vdom.ReactVDom.all._
import org.scalajs.dom.HTMLInputElement
import scala.util.Try
import ScalazReact._

@JSExport
object JsMain extends JSApp {

  override def main(): Unit = {
    dom.console.log("main")

    val widget = Widget()
    React.renderComponent(WidgetView(widget), dom.document.getElementById("container"))
  }

  object Days extends Enumeration {
      type Days = Value
      val Mon = Value("monday")
      val Wed = Value("wednesday")
      val Fri = Value("friday")
    }

  case class Widget(quantity: Int = 1, day: Days.Days = Days.Mon, weeks: Int = 1)
  val WidgetState = ReactS.Fix[Widget]
  type WidgetS = ReactS[Widget, Unit]

  val WidgetView = ReactComponentB[Widget]("WidgetView")
    .getInitialState(widget => widget)
    .renderS { (scope, props, widget) =>
      div(
        className := "widget",
        br,
        QuantityView(QuantityProps(widget.quantity, { newQuantity =>
          dom.console.log("WidgetView updateQuantity")
          scope.setState(widget.copy(quantity = newQuantity), () => ())
        })),
        br,
        DayView(DayProps(widget.day, { newDay =>
          dom.console.log("WidgetView updateDay")
          scope.setState(widget.copy(day = newDay), () => ())
        })),
        br,
        NumberOfWeeksView(NumberOfWeeksProps(widget.weeks, { newWeeks =>
          dom.console.log("WidgetView updateWeeks")
          scope.setState(widget.copy(weeks = newWeeks), () => ())
        })),
        br,
        SummaryView(widget)
      )
    }
    .create

//  class QuantityBackend(t: BackendScope[_, Widget]) {
//    def onChange(e: SyntheticEvent[HTMLInputElement]) = {
//      val newQuantity = Try(e.target.value.toInt).getOrElse(0)
//      t.modState(_.copy(quantity = newQuantity))
//    }
//  }
//
//  def quantityChange(e: SyntheticEvent[HTMLInputElement]): WidgetS = {
//    val newQuantity = Try(e.target.value.toInt).getOrElse(0)
//    WidgetState.mod(_.copy(quantity = newQuantity))
//  }

  case class QuantityProps(quantity: Int, updateQuantity: Int => Unit)
  val QuantityView = ReactComponentB[QuantityProps]("QuantityView")
    .render { props =>
      div(
        className := "quantity",
        label(id := "quantity", "Quantity"),
        input(
          id := "quantity",
          name := "quantity",
          value := props.quantity,
          onchange ==> { e: SyntheticEvent[HTMLInputElement] =>
            val newQuantity = Try(e.target.value.toInt).getOrElse(0)
            props.updateQuantity(newQuantity)
          }
        )
      )
    }
    .create

  val days = List(Days.Mon, Days.Wed, Days.Fri)
  case class DayProps(day: Days.Days, updateDay: Days.Days => Unit)
  val DayView = ReactComponentB[DayProps]("DayView")
    .render { props =>
      def createDay(day: Days.Days) = {
        val selected = props.day == day

        div(
          key := day.toString,
          label(day.toString),
          input(
            `type` := "radio",
            name := "day",
            checked := props.day == day,
            onchange ==> { e: SyntheticEvent[HTMLInputElement] =>
              props.updateDay(day)
            }
          ),
          br
        )
      }

      div(
        className := "number-of-weeks",
        days map createDay
      )
    }
    .create

  case class NumberOfWeeksProps(weeks: Int, updateWeeks: Int => Unit, totalNumWeeks: Option[Int] = None)
  val NumberOfWeeksView = ReactComponentB[NumberOfWeeksProps]("NumberOfWeeksView")
    .render { props =>
      val totalNumWeeks = props.totalNumWeeks.getOrElse(4)
      def createWeek(week: Int) = {
        val selectedClassName = if (props.weeks == week) "selected" else ""

        div(
          key := week,
          a(
            href := "",
            className := selectedClassName,
            onclick ==> { e: SyntheticEvent[HTMLInputElement] =>
              e.preventDefault()
              props.updateWeeks(week)
            },
            s"$week Weeks"
          ),
          br
        )
      }

      div(
        className := "number-of-weeks",
        (1 to totalNumWeeks) map createWeek
      )
    }
    .create

  val SummaryView = ReactComponentB[Widget]("SummaryView")
    .render { widget =>
      footer(
        p(s"Quantity: ${widget.quantity}"),
        p(s"Day: ${widget.day}"),
        p(s"Number of weeks: ${widget.weeks}")
      )
    }
    .create

}

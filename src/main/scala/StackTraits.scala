
trait Observer[State]:
  def receiveUpdate(state: State): Unit

trait Subject[State]:
  private var observers: Vector[Observer[State]] = Vector.empty

  def addObserver(observer: Observer[State]): Unit =
    observers.synchronized { observers :+= observer }

  def notifyObservers(state: State): Unit =
    observers foreach (_.receiveUpdate(state))

//--------------------------

trait Clickable:
  def click(): String = updateUI()
  protected def updateUI(): String

trait ObservableClicks extends Clickable with Subject[Clickable]:
   override abstract def click(): String =
    val result = super.click()
    notifyObservers(this)
    result

abstract class Widget

abstract class Button(val label: String) extends Widget with Clickable

trait CountObserver[State] extends Observer[State]:
  var count = 0
  def receiveUpdate(state: State): Unit = count.synchronized { count += 1 }

@main def StackTraitsMain : Unit = {
  // No override of "click" in Button required.
  val button = new Button("Button") with ObservableClicks:
    def updateUI(): String = s"$label clicked"
    override def click(): String = { println("HH"); super.click() }

  val cco = new CountObserver[Clickable] {}
  button.addObserver(cco)

  (1 to 5) foreach (_ => button.click())

  println(cco.count)
}
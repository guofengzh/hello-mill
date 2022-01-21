package typepartial

trait T {       // 存在一个类型X
  type X        // 我们之所以在此处声明一个type X，是因为后面的value必须有类型才能被声明，即静态类型化语言所需
  val value: X 
  def op2: Boolean
} 

val x = new T() {  // 我们是实现者，所以，我们要知道类型X，但用户就不需要知道了
    type X = Int
    val value = 10 ;
    def op2 = value % 2 == 0
}

val y = new T() { // 我们是实现者，所以，我们要知道类型X，但用户就不需要知道了
    type X = Char
    val value = 'D'
    def op2 = '0' <= value && value <= '9'
}

@main def TMain: Unit = {
    println(x.op2)
    println(y.op2)

}
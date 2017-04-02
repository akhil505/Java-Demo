package Sample.Test

object Trai {

  def main(args: Array[String]): Unit = {

    // println("Hello")
    // var ln = List(1, 2, 3, 4, 5)
    //   ln.foreach(println)
    var f = (x: Int) => println("Ananoumous funcation " + x)
    f(21)
    var add = (x: Int, y: Int) => x + y
    println(add(2, 4))
    //var a = List.range(1, 5, 2)
    // a.foreach(println)
   // var a = List.fill(4)(3)
  //  a.foreach(println)
    var i = 1

    i match {
      case 1 => println("Matched 1")
      case _ =>
        println("Default")

    }
    println(closure(5)(3))

    def closure(x: Int) = { (y: Int) => y + x }
  }

}
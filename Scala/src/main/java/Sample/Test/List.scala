package Sample.Test

object List {

  def main(args: Array[String]): Unit = {
    var li = Map("Jhon" -> 123, "Tests" -> 222)
    li += ("HHH" -> 22)
    println(li("HHH"))
    for ((k, v) <- li)
      println("Key %s, value %s", k, v)

  }
}
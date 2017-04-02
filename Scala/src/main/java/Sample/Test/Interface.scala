package Sample.Test

object Interface {
  def main(args: Array[String]): Unit = {
    var obj = new one()
    obj.show()
    obj.display()
  }

}

class one extends sample {

  def show() {
    println("In side class")
  }

  def display() {

    println("In side display ")
  }
}

trait sample {

  def display()
}
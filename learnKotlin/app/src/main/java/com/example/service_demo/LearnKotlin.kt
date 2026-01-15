package com.example.service_demo

import com.example.service_demo.ui.theme.SingleTon
import com.example.service_demo.ui.theme.Study

fun main () {
    println("Hello Kotlin!")

    var a = 10
    a = a * 10
    println("a = " + a)

    val result = getScore("TomJack");
    println("result =" + result)

    var p1 = Person("11", 19)


    var st = Stu("tb", 33)
    st.readBooks()
    st.doHomework()

//    for (i in 0 until 10) {
//        println("i =" + i)
//    }

    /// 从 0 到 10(不包含 10)，每次递增 2
//    for (i in 0 until 10 step 2) {
//        println("i =" + i)
//    }

    // 降序便利
    for (i in 10 downTo 1) {
        println("i =" + i)
    }

    var stu = Student("tb", 33)
    stu.print()

    var stu1 = Student1("1234", 14, "tb", 33)
    stu1.print()


    /// 数据类
    val cellPhone1 = CellPhone("apple", 123333.99)
    val cellPhone2 = CellPhone("Samsung", 123333.99)
    println(cellPhone1)
    println("cellphone1 equals cellphone2 " + (cellPhone1 == cellPhone2))

    ///单例类
    SingleTon.singletonTest()

    //Lambam
    val list = mutableListOf("Apple", "Banana", "Orange", "Pear", "Geape")
    list.add("Watermelon")
    for (fruit in list) {
        println(fruit)
    }

    var map = mapOf("Apple" to 1, "Banana" to 2, "Orange" to 3, "Grape" to 5)
    for ((fruit, number) in map) {
        println("fruit is" + fruit + ", number is " + number)
    }

}

/// ?.操作符表示对象为空时什么都不做，对象不为空时就调用let
//函数，而let函数会将study对象本身作为参数传递到Lambda表达式中，此时的study对象肯
//定不为空了，我们就能放心地调用它的任意方法
fun doStudy(study: Study?) {
    study?.let {
        it.readBooks()
        it.doHomework()
    }
}

fun getScore(name: String) = when {
    name.startsWith("Tom") -> 86
    name == "Jim" -> 77
    name == "Jack" -> 95
    name == "Lily" -> 100
    else -> 0
}

open class Person (
    val name: String, val age: Int) {
}

class Student1(val sno: String, val grade: Int, name: String, age: Int) : Person(name, age) {
    init {
        println("sno is " + sno)
        println("grade is " + grade)
    }

    fun print() {
        println("1111sno: = " + sno + " grade: = " + grade)
    }
}

class Stu(name: String, age: Int) : Person(name, age), Study {
    override fun readBooks() {
        println(name + " is reading.")
    }

//    override fun doHomework() {
//        println(name + "is doing homework")
//    }

}

class Student(name: String, age: Int) : Person(name, age) {
    var sno = "11"
    var grade = 9

    fun print() {
        println("sno: = " + sno + " grade: = " + grade)
    }
}
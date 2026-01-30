package com.example.service_demo

interface Study {
    fun readBooks()
    fun doHomework() {
        println("doing homework")
    }
}

object SingleTon {
    fun singletonTest() {
        println("SingleTon test")
    }
}

object Util {
    fun doAction2() {
        println("Util doAction2")
    }
}

data class CellPhone(val brand: String, val price: Double)

class Money(val value: Int) {
    operator fun plus(money: Money): Money {
        return Money(value + money.value)
    }
    operator fun plus(newValue: Int): Money {
        return Money(value + newValue)
    }
}

operator fun String.times(n: Int): String {
    return this.repeat(n)
}

fun String.lettersCount(): Int {
    return this.count { it.isLetter() }
}

fun main() {
    println("Hello Kotlin!")

    var a = 10
    a = a * 10
    println("a = $a")

    val result = getScore("TomJack")
    println("result = $result")

    var p1 = Person("11", 19)

    var st = Stu("tb", 33)
    st.readBooks()
    st.doHomework()

    for (i in 10 downTo 1) {
        println("i = $i")
    }

    var stu = Student("tb", 33)
    stu.print()

    var stu1 = Student1("1234", 14, "tb", 33)
    stu1.print()

    val cellPhone1 = CellPhone("apple", 123333.99)
    val cellPhone2 = CellPhone("Samsung", 123333.99)
    println(cellPhone1)
    println("cellphone1 equals cellphone2 ${cellPhone1 == cellPhone2}")

    SingleTon.singletonTest()
    Util.doAction2()

    val list = mutableListOf("Apple", "Banana", "Orange", "Pear", "Geape")
    list.add("Watermelon")
    for (fruit in list) {
        println(fruit)
    }

    var map = mapOf("Apple" to 1, "Banana" to 2, "Orange" to 3, "Grape" to 5)
    for ((fruit, number) in map) {
        println("fruit is $fruit, number is $number")
    }

    val count = "AAArrrr1233885k545klg,,..".lettersCount()
    println("Letter count: $count")

    val money = Money(5)
    val money1 = Money(3)
    val money2 = money1 + money
    println("money2: ${money2.value}")

    val money3 = money + 5
    println("money3: ${money3.value}")

    val str = "11223" * 3
    println("str: $str")
}

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

open class Person(val name: String, val age: Int)

class Student1(val sno: String, val grade: Int, name: String, age: Int) : Person(name, age) {
    init {
        println("sno is $sno")
        println("grade is $grade")
    }

    fun print() {
        println("1111sno: = $sno grade: = $grade")
    }
}

class Stu(name: String, age: Int) : Person(name, age), Study {
    override fun readBooks() {
        println("$name is reading.")
    }
}

class Student(name: String, age: Int) : Person(name, age) {
    var sno = "11"
    var grade = 9

    fun print() {
        println("sno: = $sno grade: = $grade")
    }
}

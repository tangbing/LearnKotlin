package com.example.service_demo.ui.theme


fun main() {

//    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
//    val builder = StringBuilder()
//    builder.append("Start eating fruits.\n")
//    for (fruit in list) {
//        builder.append(fruit).append("\n")
//    }
//    builder.append("Ate all fruits.")
//    val result = builder.toString()
//    println(result)

//    // 用 with 会直接使用 Lambda 表达式的最后一行作为返回值返回
//    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
//    val result = with(StringBuilder()) {
//        append("Start eating fruit\n")
//        for (fruit in list) {
//            append(fruit).append("\n")
//        }
//        append("Ate all fruits")
//        toString()
//    }
//    println(result)


    // 用 run, 会直接使用 Lambda 表达式的最后一行作为返回值返回
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Graph")
    val result = StringBuilder().run {
        append("Start eating fruits.\n")
        for (fruit in list) {
            append(fruit).append("\n")
        }
        append("Ate all fruits")
        toString()
    }
    println(result)
}

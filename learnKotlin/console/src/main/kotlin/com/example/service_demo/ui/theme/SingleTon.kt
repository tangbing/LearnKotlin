package com.example.service_demo.ui.theme

// 单例类
object SingleTon {
    fun singletonTest() {
        println("singletonTest is called")
    }
}

class Util {
    fun doAction() {
        println("do action")
    }

    // 如果给单利类，或 companion object中的方法加上
    // @JvmStatic 注解，那么Kotlin 编译器会将这些方法编译成真正的静态方法
    // @JvmStatic 智能加在单利类或者 companion object 中的方法上，如果你尝试加在
    //一个普通方法上，会直接提示语法错误
    companion object {
        @JvmStatic
        fun doAction2() {
            println("do action2")
        }
    }
}
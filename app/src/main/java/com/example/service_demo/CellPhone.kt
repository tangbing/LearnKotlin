package com.example.service_demo

/// 当一个类前面声明了 data 关键字时，就表明希望这个类是一个数据类，kotlin 就会
/// 根据主构造函数中的参数帮你将 equals(),hashCode(),toString()等固定且无实际
/// 逻辑意义的方法自动生成，从而大大减少了开发的工作量

data class CellPhone(val brand: String, val price: Double) {
}
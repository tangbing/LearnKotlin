# Android Kotlin / Retrofit 笔记

## 1. 这行代码是什么意思

```kotlin
fun <T> create(serviceClass: Class<T>): T = retrofit.create<T>(serviceClass)
```

这是一层对 Retrofit `create` 方法的封装。

作用是：

- 传入一个接口类型
- 返回这个接口的实现对象

等价写法：

```kotlin
fun <T> create(serviceClass: Class<T>): T {
    return retrofit.create(serviceClass)
}
```

拆开理解：

- `fun <T>`：这是一个泛型函数，`T` 代表任意类型
- `serviceClass: Class<T>`：参数是 `T` 对应的 Java `Class` 对象
- `: T`：函数返回值类型是 `T`
- `retrofit.create(serviceClass)`：让 Retrofit 根据接口动态生成实现类

示例：

```kotlin
interface UserService {
    @GET("users")
    suspend fun getUsers(): List<User>
}

val userService = create(UserService::class.java)
```

此时 `userService` 就是 Retrofit 生成的 `UserService` 实现对象。

## 2. `UserService::class.java` 是什么意思

```kotlin
UserService::class.java
```

它表示：拿到 `UserService` 这个类在 Java 世界中的 `Class` 对象。

拆开看：

- `UserService::class`：拿到 Kotlin 的类引用，类型是 `KClass<UserService>`
- `.java`：把 Kotlin 的 `KClass` 转成 Java 的 `Class<UserService>`

Retrofit 的 `create(...)` 需要的正是 `Class<T>`，所以要这样写：

```kotlin
retrofit.create(UserService::class.java)
```

## 3. `::` 是什么意思

`::` 是 Kotlin 的引用操作符，意思是：拿到“这个东西本身的引用”，而不是直接使用或执行它。

常见场景：

### 类引用

```kotlin
UserService::class
```

表示拿到类引用。

### 函数引用

```kotlin
::test
```

如果有：

```kotlin
fun test() {}
```

那么 `::test` 表示函数 `test` 的引用，可以作为参数传递。

### 属性引用

```kotlin
::name
```

表示变量或属性 `name` 的引用。

一句话记忆：

`::` = 引用它，而不是执行它。

## 4. `inline fun <reified T> create(): T` 是什么意思

代码：

```kotlin
inline fun <reified T> create(): T = create(T::class.java)
```

它的目标是把这种调用：

```kotlin
create(UserService::class.java)
```

简化成：

```kotlin
create<UserService>()
```

## 5. `inline` 是什么

`inline` 表示内联函数。

意思是：编译器会把函数体直接展开到调用处，而不是保留普通函数调用。

示意：

```kotlin
inline fun run(block: () -> Unit) {
    println("start")
    block()
    println("end")
}
```

调用：

```kotlin
run { println("hello") }
```

可以近似理解成编译后直接变成：

```kotlin
println("start")
println("hello")
println("end")
```

## 6. `reified` 是什么

`reified` 的作用是：让泛型类型在函数内部可以拿到真实类型信息。

普通泛型因为类型擦除，运行时通常拿不到 `T` 的真实类型，所以这种写法不行：

```kotlin
fun <T> test() {
    println(T::class.java)
}
```

但配合 `inline` 和 `reified` 后可以：

```kotlin
inline fun <reified T> test() {
    println(T::class.java)
}
```

调用：

```kotlin
test<UserService>()
```

可以近似理解为编译器在调用处直接替换成：

```kotlin
println(UserService::class.java)
```

所以 `reified` 必须和 `inline` 一起使用。

## 7. 为什么这个写法更方便

原始写法：

```kotlin
fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
```

调用时需要手动传类：

```kotlin
val service = create(UserService::class.java)
```

增强写法：

```kotlin
inline fun <reified T> create(): T = create(T::class.java)
```

调用时可以直接写：

```kotlin
val service = create<UserService>()
```

这样代码更简洁，也更符合 Kotlin 风格。

## 8. 一组最终记忆

- `Class<T>`：Java 里的类对象
- `UserService::class`：Kotlin 类引用
- `UserService::class.java`：Java 类对象
- `::`：引用操作符
- `inline`：把函数内容展开到调用处
- `reified`：让泛型 `T` 在函数内部可拿到真实类型
- `create<UserService>()`：依赖 `inline + reified` 才能写出来

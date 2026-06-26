# Android 布局与 Fragment 入门笔记

这份笔记整理的是 SunnyWeather 项目里遇到的几个基础问题：

- `LinearLayout` 和 `FrameLayout` 有什么区别
- XML 里为什么有些标签成对出现，有些标签用 `/>` 结束
- `fragment` 是什么
- 为什么推荐使用 `FragmentContainerView`
- 为什么有些控件标签要写完整包名

## 1. XML 标签的两种写法

Android 布局文件本质上是 XML，所以控件标签常见有两种写法。

### 1.1 成对标签

如果一个控件里面还要放子控件，就用成对标签：

```xml
<FrameLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello" />

</FrameLayout>
```

这里 `FrameLayout` 是容器，里面放了一个 `TextView`。

### 1.2 自闭合标签

如果控件里面不放子控件，就可以用自闭合写法：

```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Hello" />
```

它等价于：

```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Hello">
</TextView>
```

只是因为里面没有内容，所以简写成 `/>`。

简单规则：

```text
里面要放子控件：<xxx> ... </xxx>
里面不放子控件：<xxx ... />
```

## 2. View 和 ViewGroup

Android 里的界面元素大致可以分成两类：

```text
View：普通控件
ViewGroup：容器控件
```

常见 `View`：

```text
TextView
ImageView
Button
EditText
```

常见 `ViewGroup`：

```text
LinearLayout
FrameLayout
ConstraintLayout
DrawerLayout
RecyclerView
```

`ViewGroup` 可以包含子控件，`View` 一般不能包含子控件。

## 3. LinearLayout

`LinearLayout` 是线性布局，用来让子控件按一个方向排列。

垂直排列：

```xml
<LinearLayout
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView ... />
    <Button ... />

</LinearLayout>
```

效果类似：

```text
TextView
Button
```

水平排列：

```xml
<LinearLayout
    android:orientation="horizontal"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <TextView ... />
    <Button ... />

</LinearLayout>
```

效果类似：

```text
TextView  Button
```

适合场景：

```text
从上到下排版
从左到右排版
简单表单
标题 + 内容 + 按钮
```

## 4. FrameLayout

`FrameLayout` 更像一个“容器”或“画布”。

它的特点是：子控件默认会叠在一起，后面的控件可能盖在前面的控件上。

示例：

```xml
<FrameLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ImageView ... />
    <TextView ... />

</FrameLayout>
```

可能的效果：

```text
TextView 盖在 ImageView 上面
```

适合场景：

```text
Fragment 容器
全屏内容区域
加载中遮罩
悬浮按钮
背景图 + 前景文字
```

## 5. LinearLayout 包 FrameLayout 是否必要

例如：

```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <fragment
            android:id="@+id/placeFragment"
            android:name="com.example.sunnyweather.ui.place.PlaceFragment"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </FrameLayout>

</LinearLayout>
```

这里外层 `LinearLayout` 没有明显作用，因为：

- 它只有一个子控件
- 子控件 `FrameLayout` 已经是 `match_parent`
- 没有用到线性排列能力

可以简化成：

```xml
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <fragment
        android:id="@+id/placeFragment"
        android:name="com.example.sunnyweather.ui.place.PlaceFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</FrameLayout>
```

## 6. Fragment 是什么

`Fragment` 可以理解成：

```text
Activity 里面的一块可复用页面区域
```

Activity 是完整页面，例如：

```text
MainActivity
WeatherActivity
```

Fragment 是页面里的一个模块，例如：

```text
PlaceFragment：城市搜索模块
WeatherFragment：天气展示模块
MenuFragment：侧边栏模块
```

Fragment 自己可以拥有：

```text
自己的布局 XML
自己的生命周期
自己的点击事件
自己的 ViewModel
自己的 RecyclerView
自己的数据展示逻辑
```

但是 Fragment 不能单独显示，它必须依附在 Activity 里。

## 7. `<fragment>` 标签

这种写法：

```xml
<fragment
    android:id="@+id/placeFragment"
    android:name="com.example.sunnyweather.ui.place.PlaceFragment"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

意思是：

```text
在当前 Activity 的布局里，直接加载 PlaceFragment
```

其中：

```xml
android:name="com.example.sunnyweather.ui.place.PlaceFragment"
```

表示要创建哪个 Fragment 类。

## 8. FragmentContainerView

现在更推荐用：

```xml
<androidx.fragment.app.FragmentContainerView
    android:id="@+id/placeFragment"
    android:name="com.example.sunnyweather.ui.place.PlaceFragment"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

它也是 Fragment 容器，但比旧的 `<fragment>` 标签更适合现代 Android 的 Fragment 管理。

推荐记法：

```text
旧写法：<fragment>
新写法：FragmentContainerView
```

## 9. 为什么有些标签要写完整包名

有些控件可以直接写短名字：

```xml
<TextView />
<Button />
<FrameLayout />
<LinearLayout />
```

这些是 Android 系统常见控件。

有些控件必须写完整包名：

```xml
<androidx.fragment.app.FragmentContainerView />
<androidx.recyclerview.widget.RecyclerView />
<androidx.drawerlayout.widget.DrawerLayout />
<com.google.android.material.card.MaterialCardView />
```

原因是它们来自 AndroidX、Material 或其他库，不是系统内置短标签。

完整包名对应 Kotlin/Java 里的类路径：

```text
androidx.fragment.app.FragmentContainerView
androidx.recyclerview.widget.RecyclerView
com.google.android.material.card.MaterialCardView
```

## 10. 简单总结

```text
LinearLayout
按方向排列子控件，适合从上到下或从左到右的简单布局。

FrameLayout
容器型布局，子控件可以叠放，常用来装 Fragment。

Fragment
Activity 里的可复用页面模块，不能单独存在，必须依附 Activity。

<fragment>
旧的 XML 静态加载 Fragment 写法。

FragmentContainerView
新的 Fragment 容器写法，更推荐。

成对标签
用于里面还要放子控件的容器。

自闭合标签
用于里面没有子控件的控件。

完整包名标签
通常表示控件来自 AndroidX、Material 或自定义 View。
```

## 11. 当前项目建议

如果只是显示 `PlaceFragment`，可以用：

```xml
<androidx.fragment.app.FragmentContainerView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/placeFragment"
    android:name="com.example.sunnyweather.ui.place.PlaceFragment"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

如果后面要动态切换多个 Fragment，可以用一个空容器：

```xml
<androidx.fragment.app.FragmentContainerView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/fragmentContainer"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

然后在 Activity 里用 FragmentTransaction 动态添加或替换 Fragment。

## 12. Kotlin 中的 class、data class、object

在 Android 项目里，经常会看到三种 Kotlin 写法：

```kotlin
class WeatherActivity : AppCompatActivity()
data class Place(...)
object PlaceDao
```

它们的用途不一样。

### 12.1 class

`class` 是普通类，用来描述一个对象。

它适合表达：

```text
页面
组件
适配器
管理器
有行为和逻辑的对象
```

项目里的例子：

```kotlin
class PlaceFragment : Fragment()
class PlaceAdapter(...) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>()
class WeatherActivity : AppCompatActivity()
```

这些类不是单纯装数据，而是有自己的行为：

```text
PlaceFragment
负责地点搜索页面逻辑。

PlaceAdapter
负责 RecyclerView 列表展示和点击事件。

WeatherActivity
负责天气页面展示和刷新。
```

普通 `class` 默认不会按属性内容比较对象。

例如：

```kotlin
class User(val name: String, val age: Int)

val user1 = User("Tom", 18)
val user2 = User("Tom", 18)

println(user1 == user2) // false
```

因为 `user1` 和 `user2` 是两个不同对象。

### 12.2 data class

`data class` 是数据类，专门用来保存数据。

项目里的例子：

```kotlin
data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(
    val name: String,
    val location: Location,
    val address: String
)

data class Location(val lng: String, val lat: String)
```

这些类主要是用来表示接口返回的数据。

`data class` 会自动生成：

```text
toString()
equals()
hashCode()
copy()
componentN()
```

所以两个内容一样的数据类对象会被认为相等：

```kotlin
data class User(val name: String, val age: Int)

val user1 = User("Tom", 18)
val user2 = User("Tom", 18)

println(user1 == user2) // true
```

还可以方便地复制对象：

```kotlin
val user1 = User("Tom", 18)
val user2 = user1.copy(age = 20)
```

适合使用 `data class` 的场景：

```text
接口返回数据
数据库实体
列表 item 数据
页面状态
配置数据
纯数据模型
```

判断标准：

```text
如果这个类主要是保存一组字段，优先用 data class。
```

### 12.3 object

`object` 是 Kotlin 的单例对象。

单例的意思是：

```text
整个 App 里只有这一份对象。
```

项目里的例子：

```kotlin
object PlaceDao
object Repository
object SunnyWeatherNetwork
object ServiceCreator
```

使用 `object` 时，不需要手动创建对象：

```kotlin
PlaceDao.savePlace(place)
Repository.searchPlaces(query)
SunnyWeatherNetwork.getWeather(lng, lat)
```

而不是：

```kotlin
val dao = PlaceDao()
```

适合使用 `object` 的场景：

```text
工具类
全局管理类
数据访问对象
网络请求入口
不需要多个实例的对象
```

例如 `PlaceDao`：

```kotlin
object PlaceDao {
    fun savePlace(place: Place) { ... }
    fun getSavedPlace(): Place { ... }
    fun isPlaceSaved(): Boolean { ... }
}
```

它负责保存和读取地点，全局一份就够了，所以适合写成 `object`。

### 12.4 三者对比

```text
class
普通类，重点是行为和逻辑。
例如 Activity、Fragment、Adapter。

data class
数据类，重点是保存数据。
例如 Place、Location、Weather。

object
单例对象，重点是全局唯一。
例如 PlaceDao、Repository、ServiceCreator。
```

### 12.5 在 SunnyWeather 项目里的理解

```text
PlaceFragment 是 class
因为它是一个页面模块，有生命周期和 UI 逻辑。

PlaceAdapter 是 class
因为它负责 RecyclerView 的创建、绑定和点击处理。

Place 是 data class
因为它只是表示一个地点的数据。

Location 是 data class
因为它只是表示经纬度数据。

PlaceDao 是 object
因为它是本地存储工具，全局一份即可。

Repository 是 object
因为它是统一的数据仓库入口，全局一份即可。
```

简单记忆：

```text
class：做事情的对象。
data class：装数据的对象。
object：全局唯一的对象。
```

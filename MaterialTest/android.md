# Android Notes

这份文档记录了当前 `MaterialTest` 项目里几个容易混淆的 Android 基础点，主要来自这次排查和接入 `CircleImageView` 时遇到的问题。

## 1. `gradle/libs.versions.toml` 和 `app/build.gradle.kts` 的区别

这两个文件职责不同：

- `gradle/libs.versions.toml`
  - 负责集中声明依赖版本、依赖别名、插件别名。
  - 类似“依赖字典”。
- `app/build.gradle.kts`
  - 负责配置 `app` 模块如何构建，以及这个模块实际使用哪些依赖。
  - 类似“模块施工图”。

当前项目里的例子：

- 版本和别名定义在 [gradle/libs.versions.toml](./gradle/libs.versions.toml)
- 实际依赖使用在 [app/build.gradle.kts](./app/build.gradle.kts)

例如：

```toml
# gradle/libs.versions.toml
[versions]
material = "1.1.0"
circleimageview = "3.0.1"

[libraries]
google-material = { group = "com.google.android.material", name = "material", version.ref = "material" }
hdodenhof-circleimageview = { group = "de.hdodenhof", name = "circleimageview", version.ref = "circleimageview" }
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation(libs.google.material)
    implementation(libs.hdodenhof.circleimageview)
}
```

### 结论

- `toml` 管“定义”
- `build.gradle.kts` 管“使用”

## 2. 想加一个库，应该怎么做

当前项目已经启用了 Version Catalog，所以推荐这样加依赖：

### 第一步：在 `libs.versions.toml` 里定义版本和别名

```toml
[versions]
material = "1.1.0"
circleimageview = "3.0.1"

[libraries]
google-material = { group = "com.google.android.material", name = "material", version.ref = "material" }
hdodenhof-circleimageview = { group = "de.hdodenhof", name = "circleimageview", version.ref = "circleimageview" }
```

### 第二步：在 `app/build.gradle.kts` 里使用

```kotlin
dependencies {
    implementation(libs.google.material)
    implementation(libs.hdodenhof.circleimageview)
}
```

### 直接写字符串也可以，但这是 Kotlin DSL 语法

当前项目不是 Groovy 的 `build.gradle`，而是 Kotlin DSL 的 `build.gradle.kts`，所以字符串依赖要这样写：

```kotlin
dependencies {
    implementation("com.google.android.material:material:1.1.0")
    implementation("de.hdodenhof:circleimageview:3.0.1")
}
```

不要写成：

```groovy
implementation 'de.hdodenhof:circleimageview:3.0.1'
```

那是旧的 Groovy DSL 写法。

## 3. 头像图为什么建议用正方形

头像资源放在 `drawable-xxhdpi` 时，建议使用正方形位图，原因是后续要给 `CircleImageView` 做圆形裁切。

如果原图是正方形：

- 圆形裁切更自然
- 不容易把耳朵、下巴裁掉
- 居中显示更稳定

当前项目头像资源位置：

- [app/src/main/res/drawable-xxhdpi/nav_icon.png](./app/src/main/res/drawable-xxhdpi/nav_icon.png)

使用位置：

- [app/src/main/res/layout/nav_header.xml](./app/src/main/res/layout/nav_header.xml)

## 4. `mipmap-*dpi` 和 `drawable-*dpi` 的区别

这两个目录组都和屏幕密度有关，但用途不同。

### `mipmap-*dpi`

主要放应用图标资源，例如当前项目里的：

- `app/src/main/res/mipmap-mdpi/ic_launcher.webp`
- `app/src/main/res/mipmap-hdpi/ic_launcher.webp`
- `app/src/main/res/mipmap-xhdpi/ic_launcher.webp`
- `app/src/main/res/mipmap-xxhdpi/ic_launcher.webp`
- `app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp`

以及圆角图标：

- `ic_launcher_round.webp`

还有 Android 8.0+ 的自适应图标配置：

- [app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml](./app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml)
- [app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml](./app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml)

### `drawable-*dpi`

主要放应用界面中使用的普通图片资源，例如：

- 头像
- 背景图
- 插图
- 按钮图片
- 内容图片

当前项目里头像资源就是放在 `drawable-*dpi`：

- `app/src/main/res/drawable-mdpi/nav_icon.png`
- `app/src/main/res/drawable-hdpi/nav_icon.png`
- `app/src/main/res/drawable-xhdpi/nav_icon.png`
- `app/src/main/res/drawable-xxhdpi/nav_icon.png`
- `app/src/main/res/drawable-xxxhdpi/nav_icon.png`

### 这些目录名里的 `mdpi`、`hdpi`、`xhdpi`、`xxhdpi`、`xxxhdpi` 是什么

它们对应不同屏幕密度：

- `mdpi` = 1x
- `hdpi` = 1.5x
- `xhdpi` = 2x
- `xxhdpi` = 3x
- `xxxhdpi` = 4x

Android 会根据设备密度，自动选择最合适的那一份资源。

### 自定义 PNG 应该放哪里

如果是应用内部普通图片，一般放 `drawable-*dpi`，不要放 `mipmap-*dpi`。

例如头像图：

```xml
android:src="@drawable/nav_icon"
```

这时 Android 会自动从不同密度目录里选 `nav_icon.png`。

### 当前头像资源的尺寸方案

当前项目头像在布局中显示为 `70dp`，所以对应生成了这套资源：

- `drawable-mdpi/nav_icon.png` = `70x70`
- `drawable-hdpi/nav_icon.png` = `105x105`
- `drawable-xhdpi/nav_icon.png` = `140x140`
- `drawable-xxhdpi/nav_icon.png` = `210x210`
- `drawable-xxxhdpi/nav_icon.png` = `280x280`

### 结论

- 应用图标优先放 `mipmap-*dpi`
- 普通 PNG 资源优先放 `drawable-*dpi`
- 同一个资源名可以在多个密度目录里各放一份
- 代码和 XML 中仍然只需要引用一次，例如 `@drawable/nav_icon`

## 5. `CircleImageView` 是什么

`de.hdodenhof.circleimageview.CircleImageView` 是第三方库提供的一个自定义控件，本质上是一个“圆形头像用的 ImageView”。

它来自依赖：

```kotlin
implementation(libs.hdodenhof.circleimageview)
```

或原始坐标：

```kotlin
implementation("de.hdodenhof:circleimageview:3.0.1")
```

它在 XML 中的写法：

```xml
<de.hdodenhof.circleimageview.CircleImageView
    android:id="@+id/iconImage"
    android:layout_width="70dp"
    android:layout_height="70dp"
    android:src="@drawable/nav_icon" />
```

## 6. 为什么 XML 里不用 `import`

Android XML 和 Kotlin/Java 代码不是一套规则。

### 在 Kotlin/Java 里

要 `import`：

```kotlin
import de.hdodenhof.circleimageview.CircleImageView
```

### 在 XML 里

不写 `import`，直接写类名：

```xml
<de.hdodenhof.circleimageview.CircleImageView ... />
```

原因是布局解析器会直接按类名去创建 View 实例。

### 系统控件为什么能写短名

例如：

```xml
<TextView ... />
```

实际上是：

```xml
<android.widget.TextView ... />
```

系统控件允许省略包名；第三方控件和自定义控件通常要写完整包名。

## 7. 为什么有的属性是 `android:`，有的属性是 `app:`

这是命名空间不同导致的。

### `android:`

表示系统自带属性，例如：

- `android:id`
- `android:layout_width`
- `android:layout_height`
- `android:src`

### `app:`

表示当前 app 或第三方库自己定义的自定义属性，例如：

- `app:civ_border_width`
- `app:civ_border_color`

`CircleImageView` 里的边框属性不是系统 `ImageView` 原生自带的，而是这个库自己加的，所以必须写 `app:`

示例：

```xml
<de.hdodenhof.circleimageview.CircleImageView
    android:id="@+id/iconImage"
    android:layout_width="70dp"
    android:layout_height="70dp"
    android:src="@drawable/nav_icon"
    android:layout_centerInParent="true"
    app:civ_border_color="@android:color/white"
    app:civ_border_width="2dp" />
```

## 8. `xmlns:app="http://schemas.android.com/apk/res-auto"` 是什么

这句是 XML 命名空间声明，用来告诉 Android：

- 这个布局里如果出现 `app:xxx`
- 请去当前 app 或依赖库定义的属性里找

也就是说，这句是让 `app:` 前缀合法化。

没有这句时，下面这种写法会报错：

```xml
app:civ_border_width="2dp"
app:civ_border_color="@android:color/white"
```

对照理解：

- `xmlns:android="http://schemas.android.com/apk/res/android"`
  - 注册 `android:`
  - 用于系统属性
- `xmlns:app="http://schemas.android.com/apk/res-auto"`
  - 注册 `app:`
  - 用于自定义属性

### 位置为什么有时不同

`xmlns:app` 可以写在：

- 根布局上
- 某个具体控件上

区别只在作用范围：

- 写在根布局上：整个布局里的子节点都能使用 `app:`
- 写在某个控件上：只有这个控件及其子节点能使用 `app:`

当前项目更推荐写在根布局上，因为可读性更好，也方便后续其他控件继续使用 `app:` 属性。

## 9. 怎么知道 `CircleImageView` 支持哪些属性

有 4 种常见办法。

### 方法 1：看 Android Studio 自动补全

前提：

- 依赖已经写对
- 已经执行 Gradle Sync

在 XML 中输入：

```xml
app:
```

IDE 通常会提示这个控件支持的自定义属性。

### 方法 2：看官方 GitHub README

官方仓库：

- https://github.com/hdodenhof/CircleImageView

README 里能直接看到基础用法和部分属性示例。

### 方法 3：看库里的 `declare-styleable`

这是最准确的方法。

本地解析到的 `CircleImageView` 属性定义里包含：

- `civ_border_width`
- `civ_border_color`
- `civ_border_overlay`
- `civ_circle_background_color`

### 方法 4：看源码中的 `R.styleable.CircleImageView`

源码里通常会把 XML 属性读出来，例如：

- `R.styleable.CircleImageView_civ_border_width`
- `R.styleable.CircleImageView_civ_border_color`

这能帮助理解属性最终会影响什么行为。

## 10. `CircleImageView` 常用属性

当前版本常用属性：

- `app:civ_border_width`
  - 边框宽度
- `app:civ_border_color`
  - 边框颜色
- `app:civ_border_overlay`
  - 边框是否覆盖在图片上
- `app:civ_circle_background_color`
  - 圆形区域背景色

示例：

```xml
<de.hdodenhof.circleimageview.CircleImageView
    android:id="@+id/iconImage"
    android:layout_width="70dp"
    android:layout_height="70dp"
    android:src="@drawable/nav_icon"
    android:layout_centerInParent="true"
    app:civ_border_color="@android:color/white"
    app:civ_border_width="2dp" />
```

## 11. 为什么第一次写没有代码提示

常见原因：

- 依赖没有加
- 依赖坐标写错
- 还没执行 Gradle Sync
- Android Studio 还没完成索引

这次实际遇到过的典型错误就是依赖坐标拼错：

错误写法：

```toml
hdodenhof-circleimageview = { group = "de.hdodenhof.", name = "circleimageview", version.ref = "circleimageview" }
```

正确写法：

```toml
hdodenhof-circleimageview = { group = "de.hdodenhof", name = "circleimageview", version.ref = "circleimageview" }
```

多出来的那个 `.` 会导致 Gradle 解析失败，IDE 也可能无法正常提示。

## 12. 这次遇到的几个典型易错点

### 1. Kotlin DSL 和 Groovy DSL 混用

当前项目是 `build.gradle.kts`，不要直接照抄：

```groovy
implementation 'xxx:yyy:1.0.0'
```

要改成 Kotlin DSL 形式。

### 2. 第三方依赖坐标拼错

`de.hdodenhof` 后面不要多写一个点。

### 3. XML 自定义属性没有声明命名空间

使用 `app:civ_border_width` 之前，要先声明：

```xml
xmlns:app="http://schemas.android.com/apk/res-auto"
```

### 4. `dp` 格式写错

正确：

```xml
app:civ_border_width="2dp"
```

错误：

```xml
app:civ_border_width="2.dp"
```

### 5. 以为 XML 里也需要 `import`

XML 不需要，直接写完整类名即可。

## 13. 当前项目里最相关的文件

- [gradle/libs.versions.toml](./gradle/libs.versions.toml)
- [app/build.gradle.kts](./app/build.gradle.kts)
- [app/src/main/res/layout/nav_header.xml](./app/src/main/res/layout/nav_header.xml)
- [app/src/main/res/layout/layout_main.xml](./app/src/main/res/layout/layout_main.xml)
- [app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml](./app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml)
- [app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml](./app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml)
- [app/src/main/res/drawable-xxhdpi/nav_icon.png](./app/src/main/res/drawable-xxhdpi/nav_icon.png)

## 14. 一句话总结

- `libs.versions.toml` 负责定义依赖
- `build.gradle.kts` 负责使用依赖
- 应用图标通常放 `mipmap-*dpi`
- 普通 PNG 图片通常放 `drawable-*dpi`
- XML 里自定义控件不用 `import`
- 系统属性用 `android:`
- 自定义属性用 `app:`
- `xmlns:app="http://schemas.android.com/apk/res-auto"` 是让 `app:` 前缀生效的命名空间声明
- `CircleImageView` 适合配正方形头像图，再做圆形显示

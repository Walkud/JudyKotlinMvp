# JudyKotlinMvp

[![](https://img.shields.io/badge/DownloadApk-1.3.0-blue.svg)](https://www.pgyer.com/x2An)

## 首先声明

本项目是参考 [git-xuhao/KotlinMvp](https://github.com/git-xuhao/KotlinMvp) ，对原项目 Mvp 代码及 Adapter 代码按照自己的想法进行了重构，布局 (xml) 文件(除 fragment_mine.xml )、工具类、自定义 View 都直接使用的原项目的文件，本项目的主要目的是为了 Kotlin 学习，将自己对 Java 版 Mvp 的理解用 Kotlin 实现。

2019 年使用 Flutter 实现,有兴趣的可以了解下。([传送门](https://github.com/Walkud/JudyFlutter))

## 说明

Kotlin 出来这么久了，现在才把自己平时 Java 的开发方式过渡到 Kotlin ；才开始使用的时候感觉很多地方都想吐槽(比如泛型、反射)，但把整个项目重构完成后，对 Kotlin 有了个全新的认识，转变了一些看法。

对于新手这个项目值得你细看，在简书上也写下了我自己的一些理解。

[Kotlin + Mvp + RxJava + Retrofit 心得体会](https://www.jianshu.com/p/4cb1a56acf9e)

2022 年 6 月已使用 [Lemon](https://github.com/Walkud/Lemon) 库替换掉了 RxJava + Retrofit +OkHttp，并修复了一些已知逻辑问题。

## 解决痛点

#### 抽象
在实际开发中对每次的需求都需要进行抽象(写接口)这一步会浪费很多时间，还会增加过多的类，需求随时可能在变，修改实现时都需要先去修改接口，这个问题我想也是很多人在使用 Mvp 时的一个痛点。

项目中 Mvp 方式与网上的有一些差别，P 和 V 之间相互都依赖的具体实现，省去了抽象这一步，把 Mvc 中 Activity 中承担的业务逻辑搬到了 P 中,而 Activity 只承担了处理界面交互的能力，并且再也不用去写那些无用的接口，当然这种方式也违背了一些设计原则，但个人认为过于看重这些原则，导致开发效率降低也有些得不偿失。

项目中创建 M 实例是用的Java反射，省去在各业务的 P 中单独创建,由于 Kotlin 反射需要额外的依赖，重要的是 Kotlin 反射是相当的慢，官方的说法的暂时不会去优化,所以在项目中添加了 Java 的反射代码。

#### 异步导致的内存泄漏
异步时间过长而界面已退出，回调中依然隐式的持有 Activity 实例，这个问题在开发中很常见，<del>所以本项目中使用了 RxJava (异步利器)来处理异步问题，由于它的灵活性，在加上另外一位大神开源库 [RxLifecycle](https://github.com/trello/RxLifecycle) 可以很方便的处理这个问题，而且使代码也非常美观，最重要的是处理时机可控(比如在 onStop 或 onDestory )。</del> 本项目使用 Lemon 提供的 LemonSpace 来处理这个问题，代码使用简单，处理时机可控。

项目BasePresenter中的 view 没有允许为空，所以在 onDestory中并没有去置空view, 这里的实现方式也一定差别，异步的情况按上述方式进行控制可以有效的避免内存泄漏问题。

#### 进度UI的控制

在异步加载时一般都需要显示一个进度 UI 告知用户等待，而且一般都会有多种进度，比如下拉刷新、上拉加载、提交进度、布局内嵌入的进度条等，通常的做法是请求发起前调用进度显示，异步回调后，隐藏或关闭进度，<del>使用了 RxJava 后使用自定义 ObservableTransformer 可以很好的解决控制问题</del> 本项目使用 Lemon 提供的 LemonSpace 来控制请求进度显示与隐藏、内容布局切换，代码逻辑简洁美观，最重要的是在复杂的业务中，尽可能的避免关闭时机错误的 Bug。


[参考 ProgressTransformer](./app/src/main/java/com/walkud/app/rx/transformer/ProgressTransformer.kt)


#### 交互都由Activity来处理

有没有同学遇到修改同事 Bug 找某个点击事件的时候，要跳转N个类，最后发现事件监听里面发了一个 Event 事件，然后又得找是谁消费了事件，发现居然有 N 个类都有消费(一脸懵逼，我在之前的项目遇到太多次。。。)
，本项目的处理方式是将最终的监听都放在 Activity 的 addListener方法中，找交互事件的时候可以快速定位。

## 效果图

<img src="./material/JudyKotlinMvpGif.gif" width="30%">

## Demo 下载地址

![](https://www.pgyer.com/app/qrcode/Gz7E)

## 接入的第三方开源库

 - [Lemon](https://github.com/Walkud/Lemon)
 - [EasyPermissions](https://github.com/googlesamples/easypermissions)
 - [Glide](https://github.com/bumptech/glide)
 - [Logger](https://github.com/orhanobut/logger)
 - [FlycoTabLayout](https://github.com/H07000223/FlycoTabLayout)
 - [Flexbox-layout](https://github.com/google/flexbox-layout)
 - [RealtimeBlurView](https://github.com/mmin18/RealtimeBlurView)
 - [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)
 - [BGABanner-Android](https://github.com/bingoogolapple/BGABanner-Android)
 - [GSYVideoPlayer](https://github.com/CarGuo/GSYVideoPlayer)
 - [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
 - [LeakCanary](https://github.com/square/leakcanary)

## 声明
**项目中的 API 均来自开眼视频，纯属学习交流使用，不得用于商业用途！**

## License

```
Copyright [2018] [Walkud]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
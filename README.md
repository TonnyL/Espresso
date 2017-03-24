# Espresso Android App
Espresso is an express delivery tracking app designed with Material Design style, built on MVP(Model-View-Presenter) architecture with RxJava2, Retrofit2 and Realm database.

The source code in this repository reflects the app which supports mobile devices running Android 5.0+ and wearable devices running Android Wear 2.0+.

### Features
With this app, you can:

+ Add packages by inputting the package number or just scanning the express sheet.
+ View the package's detais such as the latest location
+ Search infomation about the express companies.
+ Get important notification when the package is on delivery.
+ Send feedback on using experience from your devices.
+ Support Android Wear devices.

### How to Work with the Source
I hope the source code for this app is useful for you as a reference or starting point for creating your own app. Here is some instructions to help you better build and run the code in Android Studio.

Clone the Repository:

```
git clone https://github.com/TonnyL/Espresso.git
```

Check out the master branch:

```
git checkout master
```

Notice: If you want to review a different branch, replace the `master` with the name you want to checkout if it does exist. Finally open the `Espresso/` directory in Android Studio.

Suggestion: It is better for you to update your Android Studio to version 2.3 when you open this project.

### Todos
This project is still in progress. Here are the some features that I will finish in the future.

- [x] Refreshing package details by accessing the network.
- [x] Launcher app widgets.
- [x] App Shortcuts on devices that running Android 7.1+.
- [x] Material design app onboarding pages.
- [ ] Chrome Custom Tabs.
- [ ] Alphabet indexing for companies list.
- [ ] Search packages and companies.
- [ ] Day and night mode.
- [x] Service to build notifications.
- [x] Settings and about page.
- [ ] Supporting Android Wear.
- [ ] UI test and unit test.

### Libraries Used in This App
Name | Introduction | Version
----- | ------ | ---
[Android Support Libraries](https://developer.android.com/topic/libraries/support-library/alphabet.html) | The Android Support Library offers a number of features that are not built into the framework. These libraries offer backward-compatible versions of new features, provide useful UI elements that are not included in the framework, and provide a range of utilities that apps can draw on. | 25.2.0
[CircleImageView](https://github.com/hdodenhof/CircleImageView) | A circular ImageView for Android | 2.1.0
[Gson](https://github.com/google/gson) | A Java serialization/deserialization library that can convert Java Objects into JSON and back. | 2.7
[MaterialDateTimePicker](https://github.com/wdullaer/MaterialDateTimePicker) | Pick a date or time on Android in style. | 3.1.3
[Realm](https://github.com/realm/realm-java) | Realm is a mobile database: a replacement for SQLite & ORMs. | 3.0.0
[Retrofit](https://github.com/square/retrofit) | Type-safe HTTP client for Android and Java by Square, Inc. | 2.2.0
[RxAndroid](https://github.com/ReactiveX/RxAndroid) | RxJava bindings for Android. | 2.0.1
[RxJava](https://github.com/ReactiveX/RxJava) | RxJava – Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM. | 2.0.1
[ZXing](https://github.com/zxing/zxing) | ZXing ("zebra crossing") is an open-source, multi-format 1D/2D barcode image processing library implemented in Java, with ports to other languages. | 3.3.0

### Thanks to
+ [Express 100](https://www.kuaidi100.com/)
+ [googlesamples](https://github.com/googlesamples) - [android-architecture](https://github.com/googlesamples/android-architecture)
+ [google](https://github.com/google) - [iosched](https://github.com/google/iosched)
+ [fython](https://github.com/fython) - [PackageTracker](https://github.com/fython/PackageTracker)
+ [YoulunZhai](https://plus.google.com/+YoulunZhai) - The posters.
+ Other people who help me solve the problems when I met some difficult bugs.

### License
```
Copyright 2017 lizhaotailang

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

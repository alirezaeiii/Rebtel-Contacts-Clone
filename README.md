# LibertyContacts
A sample to showcase a customized contact list divided into sections with group header. It needed to parse contact provider items in a background thread using RxJava. This sample used [getItemViewType](https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.Adapter#getItemViewType(int)) in order to create RecyclerView with multiple view types. Contact items are separated by sticky header.

## Features
* MVVM Architecture + Repository design Pattern.
* Jetpack Libraries and Architecture Components.

## Libraries
* [Android Jetpack](https://developer.android.com/jetpack)
   * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) ViewModel is designed to store and manage UI-related data in a lifecycle conscious way. This allows data to survive configuration changes such as screen rotations.
   * [DataBinding](https://developer.android.com/topic/libraries/data-binding/) is a Library in the support library that allows you to bind UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
   * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) is lifecycle-aware, meaning it respects the lifecycle of other app components updating app component observers that are in an active lifecycle state.
* [Dagger](https://github.com/google/dagger) is a fully static, compile-time dependency injection framework for Java, Kotlin, and Android.
* [RxJava](https://github.com/ReactiveX/RxJava) is a library for composing asynchronous code using observable sequences.
* [Butterknife](https://github.com/JakeWharton/butterknife) which Binds Android views and callbacks to fields and methods.
* [libphonenumber](https://github.com/google/libphonenumber) Google library for parsing, formatting, and validating international phone numbers.
* [CharityLayout](https://github.com/Ali-Rezaei/CharityLayout) is a Custom ViewGroup that lays out child views with equal distance.

## Licence
    MIT License

    Copyright (c) 2018 Mohammadali Rezaei

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

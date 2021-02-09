# PoC of Android

## Overview
- Resource files added by Android Studio plugin.  
[Android Drawable Importer](https://plugins.jetbrains.com/plugin/7658-android-drawable-importer)  
1種類の解像度の画像をAndroidに対応した各解像度に変換して、自動に配置してくれるプラグイン

- Architecture  
The app uses a Model-View-ViewModel (MVVM) architecture.  
This sample showcases the following Architecture Components:  
[Room](https://developer.android.com/topic/libraries/architecture/room.html)  
[ViewModels](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html)  
[LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData.html)  

Activityは回転させない想定で作っている。  

## Library
### Usage policy
- 基本的に、google純正ライブラリを使用
- rxjava2(ReactiveX)とretrofit2(square)、SNS連携用のライブラリ
- ライブラリは全て最新バージョンを使用

### Usage library list
バージョン等の詳細は、app/build.gradle、versions.gradle を参照
```
// Kotlin Standard Library
// https://kotlinlang.org/api/latest/jvm/stdlib/index.html
org.jetbrains.kotlin:kotlin-stdlib-jdk7:$versions.kotlin

// Android Jetpack関連
// AndroidX (Android Support Libraryの代替の拡張ライブラリ)
// https://developer.android.com/jetpack/androidx/
androidx.recyclerview:recyclerview:$versions.support
androidx.cardview:cardview:$versions.support
com.google.android.material:material:$versions.support
androidx.constraintlayout:constraintlayout:$versions.constraint_layout

// Android KTX (Kotlin 拡張機能セット、主にコーディング補助)
androidx.core:core-ktx:1.0.1
androidx.fragment:fragment-ktx:1.0.0

// LifeCycle (ライフサイクルのステータスに応じてアクションを実行してくれる)
// https://developer.android.com/jetpack/androidx/releases/lifecycle
androidx.lifecycle:lifecycle-runtime:$versions.lifecycle
androidx.lifecycle:lifecycle-extensions:$versions.lifecycle
androidx.lifecycle:lifecycle-compiler:$versions.lifecycle
androidx.lifecycle:lifecycle-viewmodel-ktx:$versions.lifecycle

// Room (SQLiteライク)
// https://developer.android.com/jetpack/androidx/releases/room
androidx.room:room-runtime:$versions.room
androidx.room:room-compiler:$versions.room
androidx.room:room-rxjava2:$versions.room
io.reactivex.rxjava2:rxandroid:$versions.rx_android
io.reactivex.rxjava2:rxkotlin:$versions.rxkotlin

// Navigation Architecture Component (fragmentの画面遷移を制御するライブラリ)
// https://developer.android.com/guide/navigation/
// 使ってみたが、optionMenuやBottomNavigationViewでFragment遷移する仕様とは、合わないと思われる。
android.arch.navigation:navigation-runtime:$versions.navigation
android.arch.navigation:navigation-fragment:$versions.navigation

// retrofit2 (API関連)
com.squareup.retrofit2:retrofit:$versions.retrofit
com.squareup.retrofit2:converter-gson:$versions.retrofit
com.squareup.retrofit2:adapter-rxjava:$versions.retrofit
com.squareup.okhttp3:logging-interceptor:$versions.okhttp

// snsログイン連携 (今はアイコンを出すためだけに取り込んでいる)
com.facebook.android:facebook-login:4.40.0
com.twitter.sdk.android:twitter-core:3.1.1
com.twitter.sdk.android:twitter:3.1.1
com.twitter.sdk.android:twitter-mopub:3.1.1
com.linecorp:linesdk:5.0.1

// GoogleMapを使うために追加する(実際は未使用)
com.google.android.gms:play-services-maps:16.1.0
```

## Development tools
開発で利用したツール一覧
- Android Studio
- Atom(Editor)
- Xcode(kotlin native確認用)
- Chrome(API確認用)
- Postman(API確認用)
- FileAlpaca(画像編集用)
- Digital Color Meter(Mac純正のカラーピッカー)
- Sourcetree(ソース管理、簡易diff)
  ※ソース管理はgit-flowで行っていますが、ところどころ雑になっています。

## Note
### About “extract_data_binding_error.rb”
dataBinding関連のビルドエラーは、build logからは追いづらい。
以下のコマンドを実行して、エラー個所を抽出することで、解析している。
```
./gradlew assembleDebug 2>&1 | ./extract_data_binding_error.rb
```

### API
- モックサーバとして、JSON serverで確認していいます。
  (master branch) MyInsurancePortal/11_api_sample
- 作成しているモック
  - 1_契約(Self) - 02.契約一覧取得(Self) (/api/v1/policies/self)
  - 1_契約(Self) - 03.生保契約詳細取得(Self) (/api/v1/policies/life/self/1)
  - 2_契約(Wise) - 01.契約一覧取得(Wise) (/api/v1/policies/wise)
  - 2_契約(Wise) - 02.生保契約詳細取得(Wise) (/api/v1/policies/life/wise/1)
    ※POST以外全て
- 「あなたの保有契約」画面で、「1_契約(Self) - 02.契約一覧取得(Self) 」が、
  保有契約詳細画面で、「1_契約(Self) - 03.生保契約詳細取得(Self) 」がよばれます。
- WiseのAPIは、用意しただけな状況です。
- 取得できない場合は、内部DBのデータを表示します。
- 内部DBとの連携が中途半端な状態です。（先に内部DBを用意したため、APIと項目があっていない箇所があり、統一しきれていないです）

# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# 디버깅을 위한 스택 트레이스 라인 정보 유지
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ====================================
# Retrofit & OkHttp
# ====================================
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

# Retrofit
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations
-keepattributes EnclosingMethod

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keepclassmembernames interface * {
    @retrofit2.http.* <methods>;
}

# Retrofit API 인터페이스 명시적으로 유지
-keep interface inu.appcenter.bjj_android.core.data.remote.APIService { *; }
-keep interface * {
    @retrofit2.http.* <methods>;
}

# Retrofit Response 제네릭 타입 보존
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# 제네릭 시그니처 보존 (이미 있지만 강화)
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# OkHttp Platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# ====================================
# Gson
# ====================================
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Gson uses generic type information stored in a class file when working with fields.
-keepattributes Signature

# Gson specific classes
-dontwarn com.google.gson.**

# 모든 데이터 클래스 유지 (API 응답 모델)
-keep class inu.appcenter.bjj_android.model.** { *; }

# ====================================
# Koin (DI)
# ====================================
-keep class org.koin.** { *; }
-keep class org.koin.core.** { *; }
-keep class org.koin.dsl.** { *; }
-keepnames class * extends org.koin.core.module.Module
-keepclassmembers class * {
    public <init>(...);
}

# ====================================
# Jetpack Compose
# ====================================
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Compose Runtime
-keep class androidx.compose.runtime.** { *; }
-keep interface androidx.compose.runtime.** { *; }

# ====================================
# Kotlin
# ====================================
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# Coroutines with Retrofit (suspend 함수 지원)
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}
-keep class kotlin.coroutines.Continuation

# ====================================
# AndroidX & DataStore
# ====================================
-keep class androidx.datastore.** { *; }
-keepclassmembers class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite {
    <fields>;
}

# ====================================
# Coil (이미지 로딩)
# ====================================
-keep class coil.** { *; }
-keep interface coil.** { *; }
-dontwarn coil.**

# ====================================
# Firebase
# ====================================
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Firebase Messaging
-keep class com.google.firebase.messaging.** { *; }
-keep class inu.appcenter.bjj_android.core.notification.** { *; }

# FCM Service - 반드시 유지해야 함
-keep class inu.appcenter.bjj_android.core.notification.FcmService { *; }
-keep class * extends com.google.firebase.messaging.FirebaseMessagingService { *; }

# ====================================
# WebView with JavaScript
# ====================================
# WebView 클래스 유지
-keep class android.webkit.WebView { *; }
-keep class android.webkit.WebViewClient { *; }
-keep class android.webkit.WebChromeClient { *; }
-keep class android.webkit.WebSettings { *; }
-keep class android.webkit.WebResourceRequest { *; }
-keep class android.webkit.WebResourceError { *; }

# WebViewClient 메서드 유지
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
    public boolean *(android.webkit.WebView, java.lang.String);
    public void *(android.webkit.WebView, android.webkit.WebResourceRequest, android.webkit.WebResourceError);
}

# WebChromeClient 메서드 유지
-keepclassmembers class * extends android.webkit.WebChromeClient {
    public void *(android.webkit.WebView, java.lang.String);
}

# JavascriptInterface 유지
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# OAuth 로그인 Dialog 관련 클래스 유지
-keep class inu.appcenter.bjj_android.feature.auth.presentation.login.SocialLoginDialog** { *; }

# ====================================
# Parcelable
# ====================================
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ====================================
# Serializable
# ====================================
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ====================================
# Navigation Component
# ====================================
-keep class androidx.navigation.** { *; }
-keepnames class androidx.navigation.**
-keepclassmembers class androidx.navigation.** {
    *;
}

# ====================================
# ViewModel
# ====================================
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}

# ====================================
# 프로젝트별 규칙 (Google feature-based 아키텍처)
# ====================================
# Application 클래스 유지
-keep class inu.appcenter.bjj_android.di.KoinApp { *; }

# Core 모듈 유지
-keep class inu.appcenter.bjj_android.core.** { *; }
-keep interface inu.appcenter.bjj_android.core.** { *; }

# Feature 모듈의 ViewModel 클래스들 유지
-keep class inu.appcenter.bjj_android.feature.**.presentation.**.*ViewModel { *; }
-keep class * extends inu.appcenter.bjj_android.core.presentation.BaseViewModel { *; }

# Feature 모듈의 Repository 클래스들 유지
-keep class inu.appcenter.bjj_android.feature.**.data.**Repository* { *; }
-keep interface inu.appcenter.bjj_android.feature.**.data.**Repository* { *; }

# Shared 모듈 유지
-keep class inu.appcenter.bjj_android.shared.** { *; }

# ====================================
# 일반 경고 무시
# ====================================
-dontwarn java.lang.instrument.ClassFileTransformer
-dontwarn sun.misc.SignalHandler
-dontwarn java.lang.management.**
-dontwarn javax.lang.model.**
-dontwarn org.jetbrains.annotations.**
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/mac/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# optimize level
-optimizationpasses 5

# package name no uppercase
-dontusemixedcaseclassnames

#
-dontskipnonpubliclibraryclasses

#
-dontoptimize

#
-dontpreverify

#
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# Retain annotation
-keepattributes *Annotation*

# Retain Whisper
-keep class io.buzznerd.varys.whisper.Whisper {
    public <methods>;
}
-keep class io.buzznerd.varys.whisper.Whisper* { *; }

-keep interface io.buzznerd.varys.whisper.component.Callbackable {
    *;
}

# ignore warning
-ignorewarning

# v4, v7 support lib
-dontwarn android.support.**

# Retain native method
-keepclasseswithmembernames class * {
    native <methods>;
}

# Retain Parcelable
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Retain Serializable
-keepnames class * implements java.io.Serializable

# Retain Serializable and enum
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Retain resource R
-keepclassmembers class **.R$* {
    public static <fields>;
}

##### 3rd party ######

## Retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

#okhttp3
-keepattributes Signature
-keepattributes Annotation
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

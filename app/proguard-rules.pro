# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Applications/Android Studio.app/sdk/tools/proguard/proguard-android.txt
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


# removes all logs
#-assumenosideeffects class android.util.Log {
#    public static boolean isLoggable(java.lang.String, int);
#    public static int v(...);
#    public static int i(...);
#    public static int w(...);
#    public static int d(...);
#    public static int e(...);
#}

-keepclassmembers class ** {
    public void onEvent*(**);
#    public void register*(**);
#    public void registerSticky*(**);
}


-keepclassmembernames class ** {
    public void onEvent*(**);
#    public void register*(**);
#    public void registerSticky*(**);
}
#-dontskipnonpubliclibraryclasses

# listviewanimation
#-keep class com.nhaarman.listviewanimations.** { *; }
#-keep interface com.nhaarman.listviewanimations.** { *; }
-dontwarn com.nhaarman.listviewanimations.**
#-keep class se.emilsjolander.stickylistheaders.** { *; }
#-keep interface se.emilsjolander.stickylistheaders.** { *; }
-dontwarn se.emilsjolander.stickylistheaders.**

# web
#-keep class com.squareup.okhttp.internal.** { *; }
#-keep interface com.squareup.okhttp.internal.** { *; }
-dontwarn com.squareup.okhttp.internal.**

-dontwarn okio.Okio
-dontwarn okio.DeflaterSink
-dontwarn retrofit.**


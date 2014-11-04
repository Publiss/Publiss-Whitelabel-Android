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
-assumenosideeffects class android.util.Log { *; }
#-assumenosideeffects class android.util.Log {
#    public static boolean isLoggable(java.lang.String, int);
#    public static int v(...);
#    public static int i(...);
#    public static int w(...);
#    public static int d(...);
#    public static int e(...);
#}

# eventbus
-keepclassmembers class ** {
    public void onEvent*(**);
}

# content provider (serialization)
-keepattributes *Annotation*,Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# retrofit
-keep class com.viselabs.aquariummanager.util.seneye.SeneyeService { *; }
-keep class com.viselabs.aquariummanager.util.seneye.model.* { *; }
-keep class retrofit.http.* { *; }


# only ignore warning, class keeping is handled by gradle
-dontwarn com.nhaarman.listviewanimations.**
-dontwarn se.emilsjolander.stickylistheaders.**
-dontwarn com.squareup.okhttp.internal.**
-dontwarn okio.Okio
-dontwarn okio.DeflaterSink
-dontwarn retrofit.**

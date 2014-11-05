Publiss-Whitelabel-Android
==========================

The publiss whitelabel Android app.


Setup instructions
==================

* Clone `git clone git@github.com:Publiss/Publiss-Whitelabel-Android.git`
* Copy _publiss-android-core-x.x.x.aar_, _fadingactionbar-x.x.x.aar_ and _pspdfkit-x.x.x.aar_ to `$PROJ_DIR/app/aar/`
* Replace app icons in all densities in `$PROJ_DIR/app/src/main/res/drawable-???dpi`
* Personalize app_name and colors and configure app_secret and app_token in `$PROJ_DIR/app/src/main/res/values/publiss_config.xml`
* Set a new `PACKAGE_NAME` in `$PROJ_DIR/app/build.gradle`
* Run a clean build

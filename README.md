Publiss-Whitelabel-Android
==========================

The publiss whitelabel Android app.


Setup instructions
==================

* Clone `git clone git@github.com:Publiss/Publiss-Whitelabel-Android.git`
* Replace app icons in all densities in `$PROJ_DIR/app/src/main/res/drawable-???dpi`
* Personalize app_name and colors and configure app_secret and app_token in `$PROJ_DIR/app/src/main/res/values/publiss_config.xml`
* Set `PACKAGE_NAME` in `$PROJ_DIR/app/build.gradle` to Bundle Id available at Publiss online
* Run a clean build

Customizations
==============

1. App icon (in 4 different sizes) in `$PROJ_DIR/app/src/main/res/drawable-???dpi/ic_launcher.png`
2. App colors in `$PROJ_DIR/app/src/main/res/values/publiss_config.xml`
3. Sidebar menu items (See KioskActivity.java for custom actions; translations should be set in `$PROJ_DIR/app/src/main/res/values-XX/`)
4. Sidebar menu header layout can be found in `$PROJ_DIR/app/src/main/res/layout/`

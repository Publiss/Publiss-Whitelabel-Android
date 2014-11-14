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

1. App icon
2. App colors
3. Sidebar menu items (See KioskActivity.java and $PROJ_DIR/app/src/main/res/values/core_menu_items.xml for custom actions; translations should be set in values-XX)
4. Sidebar menu header layout
5. About Text
6. optional: Actionbar Font

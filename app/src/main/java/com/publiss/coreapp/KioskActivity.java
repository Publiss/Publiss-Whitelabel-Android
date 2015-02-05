package com.publiss.coreapp;

import android.app.AlertDialog;
import android.os.Bundle;

import com.publiss.core.PublissConfig;
import com.publiss.core.ui.menu.*;

import java.util.Locale;

public class KioskActivity extends com.publiss.core.ui.KioskActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Uncomment to opt-in to publiss localization features.
        PublissConfig.getInstance().setPreferredLanguage(Locale.getDefault().getLanguage());
        //PublissConfig.getInstance().setFallbackLanguage("en");
        //PublissConfig.getInstance().setShowAnyLocalizedDocumentIfThereIsNoFallback(false);
        //PublissConfig.getInstance().setShowUnlocalizedDocuments(true);

        final KioskActivity self = this;

        MenuItem homepageMenuItem = new MenuItem(getString(R.string.menu_item_title_homepage), false, new MenuItemCallbacks() {
            @Override
            public void menuItemSelected(MenuItem menuItem) {
                String url = getString(com.publiss.core.R.string.publiss_hompage_link);
                startOpenInBrowserActivity(url);
            }
        });

        MenuItem aboutMenuItem = new MenuItem(getString(R.string.menu_item_title_about), false, new MenuItemCallbacks() {
            @Override
            public void menuItemSelected(MenuItem menuItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(self);
                builder.setTitle(getString(com.publiss.core.R.string.app_name));
                builder.setMessage("Publiss " + com.publiss.core.BuildConfig.VERSION_NAME + " (30.01.2015)");
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        MenuItem contactMenuItem = new MenuItem(getString(R.string.menu_item_title_contact), false, new MenuItemCallbacks() {
            @Override
            public void menuItemSelected(MenuItem menuItem) {
                String mailBody = getString(com.publiss.core.R.string.feedback_body);
                String receiver = getString(com.publiss.core.R.string.feedback_to);
                String subject = getString(com.publiss.core.R.string.feedback_subject);
                startFeedbackActivity(receiver, subject, mailBody);
            }
        });

        LoginLogoutMenuItem loginLogoutMenuItem = new CustomLoginLogoutMenuItem(this, getString(R.string.menu_item_title_login), getString(R.string.menu_item_title_logout), false, new MenuItemCallbacks() {
            @Override
            public void menuItemSelected(MenuItem menuItem) {
                openLoginActivityOrLogout();
            }
        });

        MenuItemManager.getInstance().clearMenuItems();
        MenuItemManager.getInstance().addMenuItem(homepageMenuItem);
        MenuItemManager.getInstance().addMenuItem(aboutMenuItem);
        MenuItemManager.getInstance().addMenuItem(contactMenuItem);
        MenuItemManager.getInstance().addMenuItem(loginLogoutMenuItem);
    }

}

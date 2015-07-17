package com.publiss.whitelabel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.publiss.core.PublissConfig;
import com.publiss.core.ui.menu.LoginLogoutMenuItem;
import com.publiss.core.ui.menu.MenuItem;
import com.publiss.core.ui.menu.MenuItemCallbacks;
import com.publiss.core.ui.menu.MenuItemManager;
import com.publiss.core.ui.menu.MenuItemOpenHtmlCallback;
import com.publiss.core.ui.menu.MenuItemOpenUrlCallback;

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

        String url = getString(R.string.publiss_hompage_link);
        MenuItem homepageMenuItem = new MenuItem(getString(R.string.menu_item_title_homepage), false, new MenuItemOpenUrlCallback(getString(R.string.browser_title_homepage), url, false));

        String facebookFeedUrl = getString(R.string.publiss_facebook_feed_url);
        MenuItem facebookFeedMenuItem = new MenuItem(getString(R.string.menu_item_title_facebook_feed), false, new MenuItemOpenUrlCallback(getString(R.string.browser_title_facebook_feed), facebookFeedUrl, false));

        MenuItem aboutMenuItem = new MenuItem(getString(R.string.menu_item_title_about), false, new MenuItemCallbacks() {
            @Override
            public void menuItemSelected(MenuItem menuItem, Activity activity) {
                AlertDialog.Builder builder = new AlertDialog.Builder(self);
                builder.setTitle(getString(com.publiss.core.R.string.app_name));
                builder.setMessage("Publiss " + BuildConfig.VERSION_NAME + "\n" + "PublissCore " + com.publiss.core.BuildConfig.VERSION_NAME);
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        MenuItem contactMenuItem = new MenuItem(getString(R.string.menu_item_title_contact), false, new MenuItemCallbacks() {
            @Override
            public void menuItemSelected(MenuItem menuItem, Activity activity) {
                String mailBody = getString(com.publiss.core.R.string.feedback_body);
                String receiver = getString(com.publiss.core.R.string.feedback_to);
                String subject = getString(com.publiss.core.R.string.feedback_subject);
                startFeedbackActivity(receiver, subject, mailBody);
            }
        });

        LoginLogoutMenuItem loginLogoutMenuItem = new CustomLoginLogoutMenuItem(this, getString(R.string.menu_item_title_login), getString(R.string.menu_item_title_logout), false, new MenuItemCallbacks() {
            @Override
            public void menuItemSelected(MenuItem menuItem, Activity activity) {
                openLoginActivityOrLogout();
            }
        });

        MenuItem rssMenuItem = new MenuItem(getString(R.string.menu_item_title_news), false, new MenuItemCallbacks() {
            @Override
            public void menuItemSelected(MenuItem menuItem, Activity activity) {
                Intent rssIntent = new Intent(KioskActivity.this, CustomRssFeedActivity.class);
                startActivity(rssIntent);
            }
        });

        MenuItemManager.getInstance().clearMenuItems();
        if(!getResources().getString(R.string.bporssreader_rss_feed_url).isEmpty()) {
            MenuItemManager.getInstance().addMenuItem(rssMenuItem);
        }

        if (!getResources().getString(R.string.publiss_facebook_feed_url).isEmpty()) {
            MenuItemManager.getInstance().addMenuItem(facebookFeedMenuItem);
        }

        MenuItemManager.getInstance().addMenuItem(homepageMenuItem);
        MenuItemManager.getInstance().addMenuItem(aboutMenuItem);
        MenuItemManager.getInstance().addMenuItem(contactMenuItem);
        MenuItemManager.getInstance().addMenuItem(loginLogoutMenuItem);
    }

}

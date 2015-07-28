package com.publiss.whitelabel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bytepoets.bpobarcodescanner.BarcodeScannerHandler;
import com.bytepoets.bpobarcodescanner.ScannerResultInterface;
import com.bytepoets.bpobarcodescanner.configuration.BarcodeScannerConfiguration;
import com.bytepoets.bpobarcodescanner.configuration.BarcodeScannerUiConfiguration;
import com.bytepoets.bpobarcodescanner.model.ScanResult;
import com.google.zxing.BarcodeFormat;
import com.publiss.core.PublissConfig;
import com.publiss.core.ui.WebViewActivity;
import com.publiss.core.ui.menu.LoginLogoutMenuItem;
import com.publiss.core.ui.menu.MenuItem;
import com.publiss.core.ui.menu.MenuItemCallbacks;
import com.publiss.core.ui.menu.MenuItemManager;
import com.publiss.core.ui.menu.MenuItemOpenUrlCallback;

import java.util.Locale;

public class KioskActivity extends com.publiss.core.ui.KioskActivity {

    private BarcodeScannerHandler barcodeScannerHandler;

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

        barcodeScannerHandler = new BarcodeScannerHandler();
        barcodeScannerHandler.setScannerResultInterface(new ScannerResultInterface() {
            @Override
            public void onScannerResult(ScanResult scanResult) {
                if (scanResult.getFormat() == BarcodeFormat.EAN_13) {
                    String url = getResources().getString(R.string.ean13_url_pattern, scanResult.getText());
                    openInAppBrowserWithUrl(url, getResources().getString(R.string.morawa_shop));
                }
            }

            @Override
            public void onScannerCanceled() {

            }
        });

        MenuItem scannerMenuItem = new MenuItem(getResources().getString(R.string.menu_item_title_scanner), false, new MenuItemCallbacks() {
            @Override
            public void menuItemSelected(MenuItem menuItem, Activity activity) {
                BarcodeScannerConfiguration configuration = BarcodeScannerConfiguration.builder()
                        .addAllowedFormat(BarcodeFormat.EAN_13)
                        .useScannerViewDirectly()
                        .build();
                barcodeScannerHandler.startBarcodeScanner(KioskActivity.this,configuration);

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
        MenuItemManager.getInstance().addMenuItem(scannerMenuItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!barcodeScannerHandler.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

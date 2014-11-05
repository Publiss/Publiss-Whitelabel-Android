package com.publiss.publissplaystore;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;


import com.publiss.core.ui.navigation.MenuDrawerFragment;

public class KioskActivity extends com.publiss.core.ui.KioskActivity {

    @Override
    public void onMenuDrawerItemSelected(Integer selectedMenuItem) {
        if (selectedMenuItem == 0) {
            String url = getString(com.publiss.core.R.string.publiss_hompage_link);
            startOpenInBrowserActivity(url);
        }
        else if (selectedMenuItem == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(com.publiss.core.R.string.app_name));
            builder.setMessage("Version " + com.publiss.core.BuildConfig.VERSION_NAME);
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (selectedMenuItem == 2) {
            String mailBody = getString(com.publiss.core.R.string.feedback_body);
            String receiver = getString(com.publiss.core.R.string.feedback_to);
            String subject = getString(com.publiss.core.R.string.feedback_subject);
            startFeedbackActivity(receiver, subject, mailBody);
        }
    }
}

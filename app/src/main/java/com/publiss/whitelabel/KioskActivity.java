package com.publiss.whitelabel;

import android.app.AlertDialog;

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
            CharSequence[] versions = {"Version " + com.publiss.whitelabel.BuildConfig.VERSION_NAME, "Core Version " + com.publiss.core.BuildConfig.VERSION_NAME};
            builder.setItems(versions, null);
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

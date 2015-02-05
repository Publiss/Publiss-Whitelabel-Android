package com.publiss.whitelabel;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.publiss.core.PublissConfig;
import com.publiss.core.PublissPreferences;
import com.publiss.core.ui.menu.LoginLogoutMenuItem;
import com.publiss.core.ui.menu.MenuItemCallbacks;
import com.publiss.whitelabel.responses.CheckPermissionTestServerResponse;

public class CustomLoginLogoutMenuItem extends LoginLogoutMenuItem{

    public CustomLoginLogoutMenuItem(Context context, String loginTitle, String logoutTitle, Boolean hasSeperatorAbove, MenuItemCallbacks listener) {
        super(context, loginTitle, logoutTitle, hasSeperatorAbove, listener);
    }

    @Override
    public String getSubTitle() {
        if (!PublissPreferences.isLoggedIn(context)) {
            return getLoginSubTitle();
        }
        else {
            if (getLogoutSubTitle().isEmpty()) {
                String rawResponse = PublissConfig.getLoginResponseRawValue(context);
                if (!rawResponse.isEmpty()) {
                    try {
                        CheckPermissionTestServerResponse response = new Gson().fromJson(rawResponse, CheckPermissionTestServerResponse.class);
                        if (null != response.username) {
                            return response.username;
                        }
                    } catch (JsonSyntaxException e) {
                        Log.d("JSON", "Wrong response from server", e);
                        return "";
                    }
                }
            }
            return getLogoutSubTitle();
        }
    }
}

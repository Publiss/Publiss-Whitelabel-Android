package com.publiss.coreapp.network.responses;

import com.google.gson.annotations.SerializedName;
import com.publiss.core.network.responses.CheckPermissionResponse;

public class CheckPermissionTestServerResponse extends CheckPermissionResponse {
    @SerializedName("username")
    public String username;
    @SerializedName("password")
    public String password;
    @SerializedName("access_token")
    public String accessToken;
}

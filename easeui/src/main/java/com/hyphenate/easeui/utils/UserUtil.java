package com.hyphenate.easeui.utils;

import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserUtil implements EaseUI.EaseUserProfileProvider {

   private  Map<String,EaseUser> easeUserMap;

    public UserUtil(Map<String, EaseUser> easeUserMap) {
        this.easeUserMap = easeUserMap;
    }

    public Map<String, EaseUser> getEaseUserMap() {
        return easeUserMap;
    }

    public void setEaseUserMap(Map<String, EaseUser> easeUserMap) {
        this.easeUserMap = easeUserMap;
    }

    @Override
    public EaseUser getUser(String username) {
        EaseUser easeUser = easeUserMap.get(username);
        return easeUser;
    }
}

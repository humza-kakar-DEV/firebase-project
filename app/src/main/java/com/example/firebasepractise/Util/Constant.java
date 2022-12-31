package com.example.firebasepractise.Util;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;

import androidx.core.content.ContextCompat;

import com.example.firebasepractise.R;

import java.io.File;

public class Constant {
    public static final String AUTH_TYPE = "com.example.firebasepractise";
    public static final String PLANNER_ROLE = "PlannerRole";
    public static final String ADMIN_ROLE = "AdminRole";
    public static final String USER_ROLE = "UserRole";
    public static final String PLANS_TAG = "Plans";
    public static final String APPROVED_PLANS_TAG = "ApprovedPlans";
    public static final String ROOT_FRAGMENT = "RootFragment";
    public static final int RC_SIGN_IN = 1;
}

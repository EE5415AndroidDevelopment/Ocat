package com.android.ocat.global;

public class Constant {
//    public static final String URL = "http://47.97.82.78:8080";
    public static final String URL = "http://192.168.1.118:8080";
    public static final String URL_CURRENCY_RATE = "https://www.mycurrency.net/US.json";

    // SMS interval
    public static final int INTERVAL = 1000;
    public static final int PERIOD = 60000;

    // Preference Filename
    public static final String FILE_NAME = "userInfo";
    public static final String CURRENCY_RATE_FILE_NAME = "currencyRate";
    public static final String FINANCE_RECORD_FILE_NAME = "financeRecord";
    // Preference Parameter
    public static final String IS_SGININ = "isSignIn";
    public static final String USER_JSON = "userJson";

    // URL Request
    public static final String LOGIN = "/user/login";
    public static final String REGISTER = "/user/register";
    public static final String SEND_CODE = "/user/sendCode";
    public static final String FORGET_1 = "/user/forget1";
    public static final String FORGET_2 = "/user/forget2";
    public static final String UPDATE = "/user/update";
    public static final String FIANCE_SELECT_MONTHLY = "/finance/selectByMonth";

    // URL Request Parameters
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String CODE = "code";
    public static final String PHONE = "phone";
    public static final String CODE_METHOD = "method";
    public static final String CODE_METHOD_NEW = "0"; // for new user register, update new email
    public static final String CODE_METHOD_OLD = "1"; // for old user change password, update info
    public static final String MONTH = "month";
    public static final String YEAR = "year";
}

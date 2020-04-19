package com.android.ocat.global;

public class Constant {
    public static final String URL = "http://47.97.82.78:8080";
//    public static final String URL = "http://192.168.1.118:8080";
    public static final String URL_CURRENCY_RATE = "https://www.mycurrency.net/US.json";
    public static final String URL_TRANSLATION = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    public static final String URL_NEWS_FINANCE = "https://3g.163.com/touch/reconstruct/article/list/BA8EE5GMwangning/0-20.html";
    public static final String URL_NEWS_FILM = "https://3g.163.com/touch/reconstruct/article/list/BD2A9LEIwangning/0-20.html";
    public static final String URL_NEWS_ENTERTAINMENT = "https://3g.163.com/touch/reconstruct/article/list/BA10TA81wangning/0-20.html";
    public static final String URL_NEWS_TV = "https://3g.163.com/touch/reconstruct/article/list/BD2A86BEwangning/0-20.html";

    // NEWS KEY
    public static final String KEY_NEWS_FINANCE = "BA8EE5GMwangning";
    public static final String KEY_NEWS_FILM = "BD2A9LEIwangning";
    public static final String KEY_NEWS_ENTERTAINMENT = "BA10TA81wangning";
    public static final String KEY_NEWS_TV = "BD2A86BEwangning";

    // SMS interval
    public static final int INTERVAL = 1000;
    public static final int PERIOD = 30000;

    // MIN Module Number
    public static final int MIN_RECORD_NOW = 5;

    // Preference Filename
    public static final String FILE_NAME = "userInfo";

    // Preference Parameter
    public static final String IS_SGININ = "isSignIn";
    public static final String USER_JSON = "userJson";
    public static final String ALL_DATES = "allDates";
    public static final String MONTHLY_RECORD = "monthlyRecord";
    public static final String REFRESH_NOW = "refreshNow";
    public static final String DB_INITIALIZE = "dbInitialize";
    public static final String FINANCE_SUM = "financeSum";
    public static final String HAS_RECORD = "hasRecord";
    public static final String HAS_CONNECTION = "hasConnection";
    public static final String LANGUAGE = "language";

    // URL Request
    public static final String LOGIN = "/user/login";
    public static final String REGISTER = "/user/register";
    public static final String SEND_CODE = "/user/sendCode";
    public static final String FORGET_1 = "/user/forget1";
    public static final String FORGET_2 = "/user/forget2";
    public static final String UPDATE = "/user/update";
    public static final String FINANCE_SELECT = "/finance/select";
    public static final String FINANCE_SELECT_DATE_RANGE = "/finance/selectDateRange";
    public static final String FINANCE_INSERT_ONE = "/finance/insertOne";
    public static final String FINANCE_DELETE_ONE = "/finance/deleteOne";
    public static final String FINANCE_SELECT_SUM = "/finance/selectSum";
    public static final String COURSE_SELECT_ALL = "/course/selectAll";
    public static final String COURSE_INSERT_ONE = "/course/insertOne";
    public static final String COURSE_DELETE_COURSE = "/course/deleteCourse";

    // URL Request Parameters
    public static final String USERNAME = "username";
    public static final String UID = "uid";
    public static final String ID = "id";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String CODE = "code";
    public static final String PHONE = "phone";
    public static final String CODE_METHOD = "method";
    public static final String CODE_METHOD_NEW = "0"; // for new user register, update new email
    public static final String CODE_METHOD_OLD = "1"; // for old user change password, update info
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String CATE_ID = "cateId";
    public static final String IN_OUT = "inOut";
    public static final String CURRENCY_CODE = "currencyCode";
    public static final String CURRENCY_VALUE = "currencyValue";
    public static final int FINANCE_IN = 0;
    public static final int FINANCE_OUT = 1;
    public static final String TRANSLATION_LANGUAGE_EN = "en";
    public static final String TRANSLATION_LANGUAGE_ZH = "zh";
    public static final String TRANSLATION_ID = "20200331000409724";
    public static final String TRANSLATION_SALT = "512";
    public static final String TRANSLATION_SECRET_KEY = "Yo6x6cy_Slykk3s7_OIt";
    public static final String COURSE_NAME = "courseName";
    public static final String TEACHER = "teacher";
    public static final String CLASS_ROOM = "classRoom";
    public static final String DAY = "day";
    public static final String CLASS_START = "classStart";
    public static final String CLASS_END = "classEnd";

    // SUCCESS Code
    public static final int SUCCESS = 0;

    // ERROR Code
    public static final int USERNAME_EMPTY = 1;
    public static final int PASSWORD_EMPTY = 2;
    public static final int USER_NOT_EXIST = 3;
    public static final int INVALID_PASSWORD = 4;
    public static final int USER_EMPTY = 5;
    public static final int EMAIL_EMPTY = 6;
    public static final int PHONE_EMPTY = 7;
    public static final int ANSWER_EMPTY = 8;
    public static final int USER_ALREADY_EXISTS = 9;
    public static final int EMAIL_ALREADY_EXISTS = 10;
    public static final int PHONE_ALREADY_EXISTS = 11;
    public static final int REGISTER_FAIL = 12;
    public static final int PASSWORD_TOO_SHORT = 13;
    public static final int FORGET_FAIL = 14;
    public static final int CODE_EMPTY = 15;
    public static final int INVALID_CODE = 16;
    public static final int CODE_FAIL = 17;
    public static final int EMAIL_NOT_EXISTS = 18;
    public static final int UPDATE_FAIL = 19;
    public static final int PARAM_EMPTY = 20;
    public static final int ILLEGAL_PARAM = 21;
    public static final int NO_RECORD = 22;
    public static final int DELETE_FAIL = 23;
    public static final int INSERT_FAIL = 24;

    public static final int SOUND_EFFECT_SUCCESS = 0;
    public static final int SOUND_EFFECT_ERROR = 1;

}

package com.android.ocat.global.utils;

import android.content.Context;
import android.widget.Toast;

import com.android.ocat.R;
import com.android.ocat.global.Constant;

public class ToastUtil {
    public static void createToast(Context context, int statusCode) {
        switch (statusCode) {
            case Constant.INVALID_PASSWORD:
                Toast.makeText(context, context.getResources().getString(R.string.invalidPassword), Toast.LENGTH_LONG).show();
                break;
            case Constant.USER_ALREADY_EXISTS:
                Toast.makeText(context, context.getResources().getString(R.string.userAlreadyExists), Toast.LENGTH_LONG).show();
                break;
            case Constant.EMAIL_ALREADY_EXISTS:
                Toast.makeText(context, context.getResources().getString(R.string.emailAlreadyExists), Toast.LENGTH_LONG).show();
                break;
            case Constant.PHONE_ALREADY_EXISTS:
                Toast.makeText(context, context.getResources().getString(R.string.phoneAlreadyExists), Toast.LENGTH_LONG).show();
                break;
            case Constant.REGISTER_FAIL:
                Toast.makeText(context, context.getResources().getString(R.string.registerFail), Toast.LENGTH_LONG).show();
                break;
            case Constant.FORGET_FAIL:
                Toast.makeText(context, context.getResources().getString(R.string.forgetFail), Toast.LENGTH_LONG).show();
                break;
            case Constant.INVALID_CODE:
                Toast.makeText(context, context.getResources().getString(R.string.invalidCode), Toast.LENGTH_LONG).show();
                break;
            case Constant.EMAIL_NOT_EXISTS:
                Toast.makeText(context, context.getResources().getString(R.string.emailNotExists), Toast.LENGTH_LONG).show();
                break;
            case Constant.UPDATE_FAIL:
                Toast.makeText(context, context.getResources().getString(R.string.updateFail), Toast.LENGTH_LONG).show();
                break;
            case Constant.ILLEGAL_PARAM:
                Toast.makeText(context, context.getResources().getString(R.string.illegalParams), Toast.LENGTH_LONG).show();
                break;
            case Constant.NO_RECORD:
                Toast.makeText(context, context.getResources().getString(R.string.noRecord), Toast.LENGTH_LONG).show();
                break;
            case Constant.DELETE_FAIL:
                Toast.makeText(context, context.getResources().getString(R.string.deleteFail), Toast.LENGTH_LONG).show();
                break;
        }
    }
}

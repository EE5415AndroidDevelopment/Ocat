package com.android.ocat.ui.hotspots;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.TranslateJson;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.google.gson.Gson;

import org.apache.commons.codec.digest.DigestUtils;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class TranslationFragment extends Fragment {
    private TextView tv;
    private EditText et;
    private Button btn;
    private String q = "";//请求翻译query

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_translation, container, false);

        tv = (TextView) view.findViewById(R.id.tv_text);
        et = (EditText) view.findViewById(R.id.et_text);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                q = et.getText().toString();
                translate();
            }
        });
        return view;
    }

    private void translate() {//POST请求
        RequestBody requestBody = new FormBody.Builder()
                .add("q", q)
                .add("from", Constant.TRANSLATION_LANGUAGE_EN)
                .add("to", Constant.TRANSLATION_LANGUAGE_ZH).add("appid", Constant.TRANSLATION_ID)
                .add("salt", Constant.TRANSLATION_SALT)
                .add("sign", DigestUtils.md5Hex(Constant.TRANSLATION_ID + q + Constant.TRANSLATION_SALT + Constant.TRANSLATION_SECRET_KEY))
                .build();
        OkHttpUtil.post(Constant.URL_TRANSLATION, requestBody, new MyCallBack() {
            @Override
            public void onFinish(String status, String json) {
                super.onFinish(status, json);
                Gson gson = new Gson();
                // parse json
                TranslateJson translateJson = gson.fromJson(json, TranslateJson.class);
                // get translation result
                tv.setText(translateJson.getTransResult().get(0).getDst());
            }
        });
    }

}

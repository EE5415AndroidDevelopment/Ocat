package com.android.ocat.ui.hotspots;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
import com.android.ocat.global.entity.TranslateJson;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;
import com.google.gson.Gson;

import org.springframework.util.DigestUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslationFragment extends Fragment {
    private TextView tv;
    private EditText et;
    private Button btn;
    private String q = "";//请求翻译query
    private static final int MESSAGE_CODE = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_CODE:
                    String str = (String) msg.obj;
                    tv.setText(str);
            }
        }
    };

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
                if (TextUtils.isEmpty(q)) {
                    Toast.makeText(getContext(), getResources().getString(R.string.inputEmpty), Toast.LENGTH_SHORT).show();
                } else {
                    translate();
                }
            }
        });

        return view;
    }

    private void translate() {//POST请求
        String sign = Constant.TRANSLATION_ID + q + Constant.TRANSLATION_SALT + Constant.TRANSLATION_SECRET_KEY;
        System.out.println(q);
        RequestBody requestBody = new FormBody.Builder()
                .add("q", q)
                .add("from", Constant.TRANSLATION_LANGUAGE_EN)
                .add("to", Constant.TRANSLATION_LANGUAGE_ZH).add("appid", Constant.TRANSLATION_ID)
                .add("salt", Constant.TRANSLATION_SALT)
                .add("sign", DigestUtils.md5DigestAsHex(sign.getBytes()))
                .build();
        OkHttpUtil.post(Constant.URL_TRANSLATION, requestBody, new MyCallBack() {

            @Override
            public void onFinish(String status, String json) {
                super.onFinish(status, json);
                Gson gson = new Gson();
                // parse json
                TranslateJson translateJson = gson.fromJson(json, TranslateJson.class);
                String result = translateJson.getTransResult().get(0).getDst();
                Message message = new Message();
                message.what = MESSAGE_CODE;
                message.obj = result;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("==================TranslationFragment Destroy===============");
    }

}

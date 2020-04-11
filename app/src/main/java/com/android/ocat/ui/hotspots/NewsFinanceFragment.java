package com.android.ocat.ui.hotspots;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocat.MainActivity;
import com.android.ocat.R;
import com.android.ocat.global.entity.NewItem;
import com.android.ocat.global.utils.MyCallBack;
import com.android.ocat.global.utils.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class NewsFinanceFragment extends Fragment {
    private List<NewItem> newItems=new ArrayList<NewItem>();
    private RecyclerView recyclerView_finance;
    private  NewsAdapter adapter;

    public NewsFinanceFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_finance, container, false);
        recyclerView_finance = (RecyclerView) view.findViewById(R.id.newsFinanceRecycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView_finance.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(newItems);
        recyclerView_finance.setAdapter(adapter);
        GetNews();
        return view;
    }

    public void GetNews(){
//        if(!MainActivity.progressDialog.isShowing()){
//            MainActivity.progressDialog.show();
//        }
        OkHttpUtil.get("https://3g.163.com/touch/reconstruct/article/list/BA8EE5GMwangning/0-20.html", new MyCallBack(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("成功！", "12121212");
                String text = response.body().string();
                Log.d("response", text);
                char test[] = text.toCharArray();
                for (int i = 0; i < 9; i++) {
                    test[i]=' ';
                }
                test[test.length-1]=' ';
                Log.d("text",String.valueOf(test));
                text=String.valueOf(test);
                parseJSONWithJSONObject(text);            }
        });
    }
    private  void  parseJSONWithJSONObject(String jsonData)
    {
        try{
            Log.d("hello","hello");
            JSONObject jsonObject=new JSONObject(jsonData);

            Log.d("testtest",jsonObject.toString());
            final JSONArray array=jsonObject.getJSONArray("BA8EE5GMwangning");
            for(int i=1;i<array.length();i++)
            {
                NewItem one=new NewItem();
                JSONObject object=array.getJSONObject(i);

                one.setPictureAddress(object.getString("imgsrc"));
                one.setTitle(object.getString("title"));
                one.setContentAddress(object.getString("url"));
                Log.d("contentadress",one.getContentAddress());
                if(one.getContentAddress().toCharArray()[0]=='0')//对无用的内容地址object进筛选
                {
                    Log.d("goodnull","truetrue!+");
                    continue;

                }
                Log.d("title12",one.getTitle());
                Log.d("pic12",one.getPictureAddress());
                boolean check=false;
                for(NewItem c:newItems){
                    if(c.getTitle().equals(one.getTitle())){
                        check=true;
                        break;}
                }
                if(!check)
                    newItems.add(one);
            }

            Log.d("listsize","1234"+" "+newItems.size());
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if(NewsFragment.progressDialog.isShowing())
//                        NewsFragment.progressDialog.dismiss();
//                    adapter.notifyDataSetChanged();
//                }
//            });
        }catch (Exception e)
        {
            e.printStackTrace();

        }
    }
}

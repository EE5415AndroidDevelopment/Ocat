package com.android.ocat.ui.hotspots;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocat.R;
import com.android.ocat.global.Constant;
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

public class NewsFilmFragment extends Fragment {
    private List<NewItem> newItems=new ArrayList<NewItem>();
    private RecyclerView recyclerView_film;
    private NewsAdapter adapter;
    private View view;
    private static final int MESSAGE_CODE = 2;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_CODE:
                    List<NewItem> list = (List<NewItem>)msg.obj;
                    adapter = new NewsAdapter(list);
                    recyclerView_film.setAdapter(adapter);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news_film, container, false);
        recyclerView_film=(RecyclerView) view.findViewById(R.id.newsFilmRecycleView) ;
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView_film.setLayoutManager(layoutManager);
        GetNews();
        return view;
    }

    public void GetNews(){
//        if(!MainActivity.progressDialog.isShowing()){
//            MainActivity.progressDialog.show();
//        }

        OkHttpUtil.get(Constant.URL_NEWS_FILM, new MyCallBack(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("成功！", "12121212");
                String text = response.body().string();
//                Log.d("response", text);
                char test[] = text.toCharArray();
                for (int i = 0; i < 9; i++) {
                    test[i]=' ';
                }
                test[test.length-1]=' ';
//                Log.d("text",String.valueOf(test));
                text=String.valueOf(test);
                parseJSONWithJSONObject(text);
                Message message = new Message();
                message.what = MESSAGE_CODE;
                message.obj = newItems;
                handler.sendMessage(message);
            }
        });
    }
    private  void  parseJSONWithJSONObject(String jsonData) {
        try{
//            Log.d("hello","hello");
            JSONObject jsonObject=new JSONObject(jsonData);

//            Log.d("testtest",jsonObject.toString());
            final JSONArray array=jsonObject.getJSONArray(Constant.KEY_NEWS_FILM);
            for(int i=1;i<array.length();i++)
            {
                NewItem one=new NewItem();
                JSONObject object=array.getJSONObject(i);

                one.setPictureAddress(object.getString("imgsrc"));
                one.setTitle(object.getString("title"));
                one.setContentAddress(object.getString("url"));
//                Log.d("contentadress",one.getContentAddress());
                if(one.getContentAddress().toCharArray()[0]=='0')
                {
//                    Log.d("goodnull","truetrue!+");
                    continue;

                }
//                Log.d("title12",one.getTitle());
//                Log.d("pic12",one.getPictureAddress());
                boolean check=false;
                for(NewItem c:newItems){
                    if(c.getTitle().equals(one.getTitle())){
                        check=true;
                        break;}
                }
                if(!check)
                    newItems.add(one);
            }

//            Log.d("listsize","1234"+" "+newItems.size());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("==================NewsFilmFragment Destroy===============");
    }

}

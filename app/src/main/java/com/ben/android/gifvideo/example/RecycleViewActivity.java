package com.ben.android.gifvideo.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.ben.android.gifvideo.VideoView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> paths = new ArrayList<String>(){
            {
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
                add(getCacheDir() + "/VID_20200611_191022_263.mp4");
            }
        };
        SimpleAdapter adapter = new SimpleAdapter(paths);
        recyclerView.setAdapter(adapter);

    }



    static class SimpleAdapter extends BaseQuickAdapter<String, BaseViewHolder>{
        public SimpleAdapter(List<String> data) {
            super(R.layout.item_video_layout, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, String path) {
            VideoView videoView = holder.getView(R.id.videoView);
            videoView.setPath(path);
        }
    }
}
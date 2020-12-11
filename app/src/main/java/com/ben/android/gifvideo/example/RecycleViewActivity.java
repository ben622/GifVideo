package com.ben.android.gifvideo.example;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.ben.android.gifvideo.VideoView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.media.MediaMetadataRetriever.OPTION_PREVIOUS_SYNC;

public class RecycleViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                || !(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,permissions, 0x00);
        }else {
            initLayout();
        }

    }

    private void initLayout() {
        String path = Utils.copyAssetsFileToSdcard(this, "VID_20200611_191022_263.mp4");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        File file = new File(path);
        retriever.setDataSource(file.getAbsolutePath());
        Bitmap thumbnail = retriever.getFrameAtTime();
        retriever.release();

        List<Media> medias = new ArrayList<Media>(){
            {
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
                add(new Media(file,thumbnail));
            }
        };
        SimpleAdapter adapter = new SimpleAdapter(medias);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0x00 && grantResults.length == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            initLayout();
        }
    }


    static class SimpleAdapter extends BaseQuickAdapter<Media, BaseViewHolder>{
        public SimpleAdapter(List<Media> data) {
            super(R.layout.item_video_layout, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, Media media) {
            VideoView videoView = holder.getView(R.id.videoView);
            videoView.setPath(media.file.getAbsolutePath());
        }
    }

    static class Media{
         File file;
         Bitmap thumbnail;

        public Media(File file, Bitmap thumbnail) {
            this.file = file;
            this.thumbnail = thumbnail;
        }
    }
}
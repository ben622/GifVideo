package com.ben.android.gifvideo.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ben.android.gifvideo.AndroidUtilities;
import com.ben.android.gifvideo.AnimatedDrawable;
import com.ben.android.gifvideo.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private VideoView imageView1;
    private VideoView imageView2;
    private VideoView imageView3;
    private VideoView imageView4;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //file = new File(getCacheDir()+"/VID_20200611_191022_263.mp4");
        AndroidUtilities.initiate(getApplicationContext());

        /*imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);

        imageView1.setPath(file.getAbsolutePath());
        imageView2.setPath(file.getAbsolutePath());
        imageView3.setPath(file.getAbsolutePath());
        imageView4.setPath(file.getAbsolutePath());*/
    }

    public void recycler(View view) {
        startActivity(new Intent(this, RecycleViewActivity.class));
    }
}
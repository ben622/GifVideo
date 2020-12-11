package com.ben.android.gifvideo.example;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @program: GifVideo
 * @description:
 * @author: black.cube
 * @create: 2020-12-11 14:33
 **/
public class Utils {
    public static String  copyAssetsFileToSdcard(Context context, String fileName){
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + fileName);
            if(!file.exists() || file.length()==0) {
                FileOutputStream fos =new FileOutputStream(file);
                int len=-1;
                byte[] buffer = new byte[1024];
                while ((len=inputStream.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                }
                fos.flush();//刷新缓存区
                inputStream.close();
                fos.close();
                return file.getAbsolutePath();
            }
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

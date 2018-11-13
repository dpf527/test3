package com.scoa.fei.gifdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    GifDrawable gifFromStream;
    GifDrawable gifFromBytes;
    ImageView gifImageView;
    ImageView gifImageView2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gifImageView = (ImageView) this.findViewById(R.id.gif_iv);
        gifImageView2 = (ImageView) this.findViewById(R.id.iv2);
        button = (Button) this.findViewById(R.id.btn);
        initImageLoader(this.getApplicationContext());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504074146342&di=5d6fb8a5ab37987a946eaae1b68dda72&imgtype=0&src=http%3A%2F%2Feasyread.ph.126.net%2F1dKaCBGr4A1fxtVSR8pPyQ%3D%3D%2F7916986196802883705.gif";

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadDrawble(str1);
                            loadRmoteImage(str1);
                        }catch (IOException e){

                        }

                    }
                });
                thread.start();
            }
        });


    }

    private void loadDrawble(String imgUrl) throws IOException {
        URL fileURL = new URL(imgUrl);
        HttpURLConnection conn = (HttpURLConnection) fileURL
                .openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        gifFromStream = new GifDrawable(bis);
        mHandler.sendEmptyMessage(0);
        is.close();

    }

    private void loadRmoteImage(String imgUrl) throws IOException {
        URL fileURL = new URL(imgUrl);
        HttpURLConnection conn = (HttpURLConnection) fileURL
                .openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        int length = (int) conn.getContentLength();
        if (length != -1) {
            byte[] imgData = new byte[length];
            byte[] buffer = new byte[512];
            int readLen = 0;
            int destPos = 0;
            while ((readLen = is.read(buffer)) > 0) {
                System.arraycopy(buffer, 0, imgData, destPos, readLen);
                destPos += readLen;

            }
             gifFromBytes = new GifDrawable( imgData );
            is.close();
            mHandler.sendEmptyMessage(1);
        }

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    gifImageView.setImageDrawable(gifFromStream);
                }
                break;
                case 1: {
                    gifImageView2.setImageDrawable(gifFromBytes);
                    ;
                }
                break;
                default: {

                }
                break;
            }


        }
    };

    static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context);
        builder.denyCacheImageMultipleSizesInMemory().memoryCacheSizePercentage(20);
        builder.tasksProcessingOrder(QueueProcessingType.LIFO);
        builder.defaultDisplayImageOptions(new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build());
        ImageLoader.getInstance().init(builder.build());
    }


}

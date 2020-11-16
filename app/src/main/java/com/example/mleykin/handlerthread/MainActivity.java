package com.example.mleykin.handlerthread;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Handler mUiHandler = new Handler();
    private MyWorkerThread mWorkerThread;
    private ImageView mImageView;
    private TextView mTextView;
    private Bitmap currentBitmap = null;
    int count;
    ArrayList<String> imagesPath;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView3);
        mTextView = (TextView) findViewById(R.id.TextView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
        else {
            String[] projection = new String[]{
                    MediaStore.Images.Media.DATA,
            };

            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cur = managedQuery(images,
                    projection,
                    "",
                    null,
                    ""
            );

            imagesPath = new ArrayList<String>();
            if (cur.moveToFirst()) {
                int dataColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.DATA);
                do {
                    imagesPath.add(cur.getString(dataColumn));
                } while (cur.moveToNext());
            }
            cur.close();

            count = imagesPath.size();
        }

        mWorkerThread = new MyWorkerThread("myWorkerThread");
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                mUiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        random = new Random();

                        int number = random.nextInt(count);
                        String path = imagesPath.get(number);

                        currentBitmap = BitmapFactory.decodeFile(path);
                        mImageView.setImageBitmap(currentBitmap);
                    }
                }, 2000);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                mUiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                "Background task2 is completed",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }, 2000);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        mWorkerThread.start();
        mWorkerThread.prepareHandler();

        for (int i=0; i<10; i++) {
            mWorkerThread.postTask(task1);
            mWorkerThread.postTask(task2);
        }
    }

    @Override
    protected void onDestroy() {
        mWorkerThread.quit();
        super.onDestroy();
    }
}

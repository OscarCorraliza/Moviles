package com.example.picaso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    String scotts = "https://image-cdn.hypb.st/https%3A%2F%2Fhypebeast.com%2Fimage%2F2020%2F05%2Fkid-cudi-travis-scott-the-scotts-no-1-billboard-hot-100-debut-2.jpg?quality=95&w=1170&cbr=1&q=90&fit=max";
    String highest = "https://images.genius.com/a17ac4e073be8c1b49b446e17fa296bc.1000x1000x1.png";
    String yosemite = "https://a10.gaanacdn.com/images/albums/3/2203403/crop_480x480_2203403.jpg";

    String ytScotts = "https://www.youtube.com/watch?v=sw4r0k8WWqU";
    String ytHighest = "https://www.youtube.com/watch?v=tfSS1e3kYeo";
    String ytYosemite = "https://www.youtube.com/watch?v=ykMHDKB0-1o";

    ImageView imageView = null;
    Button btnDownload = null;
    Button btnPermissions = null;
    Button btnSave = null;
    String cargaImagen = null;
    String cargaVideo = null;

    Activity activity = this;
    TimerTask mTimerTask = null;
    Timer mTimer = null;
    TextView mtextView = null;
    Button boton = null;
    int currentTime = 0;
    boolean save = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnPermissions = (Button) findViewById(R.id.btnPermissions);
        btnSave = (Button) findViewById(R.id.btnSave);





        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cronometro();
            }


        });

        btnPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                } else {
                    Toast.makeText(getApplicationContext(), "Ya los tienes aceptado perro.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cargaImagen != null) {
                    saveImage(cargaImagen);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cargaVideo));
                    startActivity(browserIntent);
            }
        });
    }



    public void imageLoad (String image, ImageView imageView){
        Picasso.get().load(image).into(imageView);
    }

    public void saveImage(String image){
        Log.d("Entrada", "si ha entrado");
        Picasso.get().load(image).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
                    if(!directory.exists()){
                        directory.mkdir();
                        Log.d("creado", "creado");
                    }
                    Log.d("Entrada sigue", "sigue ha entrado");
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(directory, new Date().toString().concat(String.valueOf(1))/*String.valueOf(System.currentTimeMillis())*/.concat(".jpg")));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                    Log.d("Entrada sigue 2 ", "sigue ha entrado");
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Log.d("Entrada 3", "fin");
                    Toast.makeText(getApplicationContext(),"save", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    public void cronometro() {
        cargaImagen = scotts;
        cargaVideo = ytScotts;
        Picasso.get().load(cargaImagen).into(imageView);
        mTimerTask = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentTime++;
                        Picasso.get().load(cargaImagen).into(imageView);
                        if (currentTime == 5) {
                            cargaImagen = yosemite;
                            cargaVideo = ytYosemite;
                            Picasso.get().load(cargaImagen).into(imageView);
                        } else if (currentTime == 10) {
                            cargaImagen = highest;
                            cargaVideo = ytHighest;
                            Picasso.get().load(cargaImagen).into(imageView);
                        } else if (currentTime == 15){
                            currentTime = 0;
                            cargaImagen = scotts;
                            cargaVideo = ytScotts;
                            Picasso.get().load(cargaImagen).into(imageView);
                        }
                    }
                });
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 1, 1000);
        Log.d("valor", String.valueOf(currentTime));
    }

}
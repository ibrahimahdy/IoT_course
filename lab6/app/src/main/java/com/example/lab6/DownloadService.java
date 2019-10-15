package com.example.lab6;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.os.Process;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.xml.transform.TransformerException;


public class DownloadService extends Service {


    private Looper serviceLooper;
    private ServiceHandler serviceHandler;
    private static boolean isMulti;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            if(isMulti){
                Log.i("thread",""+Thread.currentThread().getId());
                download();
            }
            else{
                for (int i=0; i<msg.arg2; i++){
                    download();
                    Log.i("thread_"+ i,""+Thread.currentThread().getId());
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }


    private void download(){
        Intent result;
        result =  new Intent();
        byte[] img = getBitmapFromURL("https://cataas.com/cat?width=400&height=400");
        if(img.length != 0){
            Log.i("service", "downloaded: " + img.length);
            result.putExtra("result", img);
            result.setAction("downloadedFiles");
            sendBroadcast(result);
        }
        else{
            Log.i("service", "NUll download");
        }
    }
//    @Override
//    public void onCreate() {
//        // Start up the thread running the service. Note that we create a
//        // separate thread because the service normally runs in the process's
//        // main thread, which we don't want to block. We also make it
//        // background priority so CPU-intensive work doesn't disrupt our UI.
//        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
//        thread.start();
//
//        // Get the HandlerThread's Looper and use it for our Handler
//        serviceLooper = thread.getLooper();
//        serviceHandler = new ServiceHandler(serviceLooper);
//    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("service", "service started");

        int imgCount = intent.getIntExtra("imgCount", -1);
        isMulti = intent.getBooleanExtra("multi", false);
        if(isMulti){
            for(int i=0; i<imgCount; i++){
                createTheread(startId, imgCount);
            }
        }else{
            createTheread(startId, imgCount);
        }
        // If we get killed, after returning from here, restart
        return START_REDELIVER_INTENT;
    }


    private void createTheread(int startId, int imgCount){

        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.arg2 = imgCount;

        serviceHandler.sendMessage(msg);
    }



    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }


    public byte[] getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            myBitmap = getResizedBitmap(myBitmap, 100,100);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            return stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }


}

package com.ranzan.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class FileOperationService extends Service {
    private String name;

    public FileOperationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showNotificationAndStartForeGround();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            name = intent.getStringExtra("name");
        }
        launchBackGroundThread();
        return super.onStartCommand(intent, flags, startId);
    }

    private void launchBackGroundThread() {
        Thread thread = new Thread(task);
        thread.start();

    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            saveToFile();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void saveToFile() {
        try {
            File directory = new File(getFilesDir() + File.separator + "NameFolder");
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(directory, "name.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            writer.append(name + "\n");
            writer.close();
            Intent intent = new Intent("com.xyz.service");
            intent.putExtra("data", "Write Done");
            sendBroadcast(intent);
        } catch (Exception e) {

        }

    }

    private void showNotificationAndStartForeGround() {
        createChannel();

        NotificationCompat.Builder notificationBuilder = null;
        notificationBuilder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle("Yahoo! Service is running")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Notification notification = null;
        notification = notificationBuilder.build();
        startForeground(120, notification);
    }

    public void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Call Notifications");
            Objects.requireNonNull(this.getSystemService(NotificationManager.class)).createNotificationChannel(channel);
        }
    }
}
package com.deskconn.dmr;

import static android.os.FileObserver.ALL_EVENTS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Environment;
import android.os.FileObserver;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.deskconn.dmr.database.InstagramData;
import com.deskconn.dmr.database.TelegramData;
import com.deskconn.dmr.database.WhatsappData;
import com.deskconn.dmr.database.RoomDB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        RoomDB database = RoomDB.getInstance(this);
        System.out.println("package " + sbn.getPackageName());
        if (sbn.getPackageName().equals("com.whatsapp")) {

            WhatsappData data = new WhatsappData();

            String title = sbn.getNotification().extras.getString("android.title");
            String text = sbn.getNotification().extras.getString("android.text");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Icon icon = sbn.getNotification().getLargeIcon();
                try {
                    Drawable drawable = icon.loadDrawable(getApplicationContext());
                    Bitmap bitmap = convertToBitmap(drawable, 120, 120);
                    saveToInternalStorage(bitmap, title,
                            Environment.getExternalStorageDirectory() + "/DMR/WhatsApp/Profile Images");
                    if (title != null && text != null) {
                        if (text.equals(getString(R.string.this_message_was_deleted))) {
                            showNotification(getApplicationContext(), title + getString(R.string.deleted_a_message),
                                    "DMR");
                        }
                        if (title.contains(":")){
                            String[] stringTitle = title.split(":");
                            title = stringTitle[0].trim();
                            if (title.contains("(")){
                                String[] strTitle = title.split("\\(");
                                title = strTitle[0].trim();
                            }
                            text = stringTitle[1] + " : " + text;

                        }

                        data.setName(title);
                        DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
                        System.out.println("getting  " + dateFormat.format(Calendar.getInstance().getTime()));
                        if (database.mainDao().getAllMessages(title) == null) {
                            data.setMessages(text);
                            data.setTime(dateFormat.format(Calendar.getInstance().getTime()));
                        } else {
                            data.setMessages(database.mainDao().getAllMessages(title) + "," + text);
                            data.setTime(database.mainDao().getAllTime(title) + "," +
                                    dateFormat.format(Calendar.getInstance().getTime()));
                        }
                        data.setImage(saveToInternalStorage(bitmap, title.trim(),
                                Environment.getExternalStorageDirectory() + "/DMR/WhatsApp/Profile Images"));
                        database.mainDao().insert(data);
                    }
                } catch (Exception e) {
                    System.out.println("lmnb inside catch");
                }

            }
            observeImage(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images",
                    Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Images");
        }

        if (sbn.getPackageName().equals("org.telegram.messenger")) {
            TelegramData telegramData = new TelegramData();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    if (!sbn.getNotification().extras.getString("android.title").trim().equals("Telegram")) {
                        if (!sbn.getNotification().extras.getString("android.title").trim().equals("Reminder")) {
                            if (sbn.getNotification().extras.getString("android.title") != null
                                    && sbn.getNotification().extras.getString("android.text") != null) {
                                telegramData.setName(sbn.getNotification().extras.getString("android.title"));
                                DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
                                if (database.mainDao().getAllMessagesTelegram(sbn.getNotification()
                                        .extras.getString("android.title")) == null) {
                                    telegramData.setMessages(sbn.getNotification().extras.getString("android.text"));
                                    telegramData.setTime(dateFormat.format(Calendar.getInstance().getTime()));
                                } else {
                                    telegramData.setMessages(database.mainDao().getAllMessagesTelegram(sbn.getNotification().extras.getString("android.title")) + "," + sbn.getNotification().extras.getString("android.text"));
                                    telegramData.setTime(database.mainDao().getAllTimeTelegram(sbn.getNotification().extras.getString("android.title")) + "," + dateFormat.format(Calendar.getInstance().getTime()));
                                }
                                database.mainDao().insert(telegramData);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("lmnb inside catch");
                }


            }
            observeImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Telegram/Telegram Images",
                    Environment.getExternalStorageDirectory() + "/DMR/Telegram/Telegram Images");
        }
        if (sbn.getPackageName().equals("com.instagram.android")) {

            InstagramData instagramData = new InstagramData();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    if (!sbn.getNotification().extras.getString("android.title").trim().equals("Instagram")) {
                        if (sbn.getNotification().extras.getString("android.title") != null
                                && sbn.getNotification().extras.getString("android.text") != null) {
                            instagramData.setName(sbn.getNotification().extras.getString("android.title"));
                            DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
                            if (database.mainDao().getAllMessagesInstagram(sbn.getNotification().extras.getString("android.title")) == null) {
                                instagramData.setMessages(sbn.getNotification().extras.getString("android.text"));
                                instagramData.setTime(dateFormat.format(Calendar.getInstance().getTime()));
                            } else {
                                instagramData.setMessages(database.mainDao().getAllMessagesInstagram(sbn.getNotification().extras.getString("android.title")) + "," + sbn.getNotification().extras.getString("android.text"));
                                instagramData.setTime(database.mainDao().getAllTimeInstagram(sbn.getNotification().extras.getString("android.title")) + "," + dateFormat.format(Calendar.getInstance().getTime()));
                            }
                            database.mainDao().insert(instagramData);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("lmnb inside catch");
                }
            }
        }

    }

    private void showNotification(Context ctx, String message, String title) {
        NotificationManager notificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "dmr_channel";
            CharSequence name = "Dmr";
            String Description = "The DMR channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, "dmr_channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message);
        Intent resultIntent = new Intent(ctx, FirstScreen.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(FirstScreen.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);

        notificationManager.notify(12, builder.build());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    private Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap bitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);
        return bitmap;
    }


    private String saveToInternalStorage(Bitmap bitmapImage, String name, String path) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // Create imageDir
        File mypath = new File(directory, name.replace("", "") + ".jpg");

        FileOutputStream fos = null;
        if (!mypath.exists()) {
            try {
                Log.i("TAg", " " + mypath);
                mypath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath() + "/" + name + ".jpg";
    }

    public void observeImage(String path, String savingPath) {
        File whatsappMediaDirectoryName = new File(path);
        String pathToWatch = whatsappMediaDirectoryName.toString();

        FileObserver observer = new FileObserver(pathToWatch, ALL_EVENTS) { // set up a file observer to watch this directory on sd card
            @Override
            public void onEvent(int event, String file) {
                if (event == MOVED_TO) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }

                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString());
                }
            }
        };
        observer.startWatching(); //START OBSERVING
    }

    public static void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

}

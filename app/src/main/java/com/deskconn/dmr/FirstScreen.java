package com.deskconn.dmr;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.deskconn.dmr.databinding.ActivityFirstScreenBinding;
import com.deskconn.dmr.ui.main.SectionsPagerAdapter;
import com.deskconn.dmr.ui.main.SectionsPagerAdapterInstagram;
import com.deskconn.dmr.ui.main.SectionsPagerAdapterTelegram;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class FirstScreen extends AppCompatActivity {

    private ActivityFirstScreenBinding binding;
    DrawerLayout drawerLayout;
    SectionsPagerAdapter sectionsPagerAdapter;
    SectionsPagerAdapterTelegram sectionsPagerAdapterTelegram;
    SectionsPagerAdapterInstagram sectionsPagerAdapterInstagram;
    ViewPager viewPager;
    TabLayout tabs;
    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 1);
        }

        File directory = new File(Environment.getExternalStorageDirectory() + "/DMR/WhatsApp/.Profile Images");
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("prepare " + directory.mkdirs());
        }


        String path = Environment.getExternalStorageDirectory().toString() + "/DMR/WhatsApp/Whatsapp Images";
        File directory1 = new File(path);
        if (!directory1.exists()) {
            directory1.mkdirs();
        }

        String pathTelegram = Environment.getExternalStorageDirectory().toString() + "/DMR/Telegram/Telegram Images";
        File directoryTelegram = new File(pathTelegram);
        if (!directoryTelegram.exists()) {
            directoryTelegram.mkdirs();
        }

        binding = ActivityFirstScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        sectionsPagerAdapterTelegram = new SectionsPagerAdapterTelegram(this, getSupportFragmentManager());
        sectionsPagerAdapterInstagram = new SectionsPagerAdapterInstagram(this, getSupportFragmentManager());
        viewPager = binding.viewPager;
        titleTextView = binding.title;
        titleTextView.setText("Whatsapp");
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        drawerLayout = findViewById(R.id.drawerLayout);

        if (!isNotificationServiceEnabled()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(R.string.allow_me);
            alertDialog.setMessage(R.string.read_message);
            alertDialog.setPositiveButton(R.string.sure, (dialog, which) -> startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)));
            alertDialog.show();
        } else if (!isNLServiceRunning()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(R.string.notification_listener);
            alertDialog.setMessage(R.string.notification_listener_message);
            alertDialog.setPositiveButton(R.string.sure, (dialog, which) -> startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)));
            alertDialog.show();
        }

    }

    private boolean isNLServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationListener.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    public void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickWhatsapp(View view) {
        viewPager.setAdapter(null);
        viewPager.removeAllViews();
        tabs.removeAllTabs();
        viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        titleTextView.setText("Whatsapp");
        closeDrawer(drawerLayout);
    }

    public void ClickTelegram(View view) {
        viewPager.setAdapter(null);
        viewPager.removeAllViews();
        tabs.removeAllTabs();
        viewPager = binding.viewPagerTelegram;
        viewPager.setAdapter(sectionsPagerAdapterTelegram);
        tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        titleTextView.setText("Telegram");
        closeDrawer(drawerLayout);
    }

    public void ClickInstagram(View view) {
        viewPager.setAdapter(null);
        viewPager.removeAllViews();
        tabs.removeAllTabs();
        viewPager = binding.viewPagerInstagram;
        viewPager.setAdapter(sectionsPagerAdapterInstagram);
        tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        titleTextView.setText("Instagram");
        closeDrawer(drawerLayout);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                File directory = new File(Environment.getExternalStorageDirectory() + "/DMR/WhatsApp/.Profile Images");
                if (!directory.exists()) {
                    directory.mkdirs();
                    System.out.println("prepare " + directory.mkdirs());
                }


                String path = Environment.getExternalStorageDirectory().toString() + "/DMR/WhatsApp/Whatsapp Images";
                File directory1 = new File(path);
                if (!directory1.exists()) {
                    directory1.mkdirs();
                }

                String pathTelegram = Environment.getExternalStorageDirectory().toString() + "/DMR/Telegram/Telegram Images";
                File directoryTelegram = new File(pathTelegram);
                if (!directoryTelegram.exists()) {
                    directoryTelegram.mkdirs();
                }
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
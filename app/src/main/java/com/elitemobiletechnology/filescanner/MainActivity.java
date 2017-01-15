package com.elitemobiletechnology.filescanner;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.elitemobiletechnology.filescanner.model.ExtStorageInfo;
import com.elitemobiletechnology.filescanner.model.ExtensionFrequency;
import com.elitemobiletechnology.filescanner.model.FileSizeInfo;
import com.elitemobiletechnology.filescanner.presenter.MainViewPresenter;
import com.elitemobiletechnology.filescanner.presenter.MainViewPresenterImpl;
import com.elitemobiletechnology.filescanner.view.MainView;

/**
 * @author SteveYang
 */
public class MainActivity extends AppCompatActivity implements MainView {
    private ShareActionProvider mShareActionProvider;
    private MenuItem shareMenuItem;
    private Button btScan;
    private ProgressBar progressBar;
    private TextView tvFilePath;
    private LinearLayout resultContainer;
    private ExtStorageInfo info;
    private MainViewPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btScan = (Button) findViewById(R.id.scan);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        resultContainer = (LinearLayout) findViewById(R.id.result);
        tvFilePath = (TextView) findViewById(R.id.tvFilePath);
        presenter = new MainViewPresenterImpl(this);
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Button) v).getText().equals(getString(R.string.start))) {
                    startScan(v);
                } else {
                    stopScan();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        stopScan();
    }

    @Override
    public void onProgressUpdate(int progress, String filePath) {
        tvFilePath.setText(getString(R.string.scanning) + filePath);
        progressBar.setProgress(progress);
    }

    @Override
    public void onScanComplete(ExtStorageInfo info) {
        btScan.setText(getString(R.string.start));
        if (info != null) {
            this.info = info;
            resultContainer.removeAllViewsInLayout();
            shareMenuItem.setEnabled(true);
            tvFilePath.setText(getString(R.string.scan_complete));
            TextView avgSize = new TextView(this);
            avgSize.setText(getString(R.string.average_file_size) + info.getAvgFileSize() / 1024 + " KB");
            avgSize.setTypeface(null, Typeface.BOLD);
            avgSize.setGravity(Gravity.CENTER_HORIZONTAL);
            resultContainer.addView(avgSize);
            TextView biggestFileTitle = new TextView(this);
            biggestFileTitle.setGravity(Gravity.CENTER_HORIZONTAL);
            biggestFileTitle.setText(getString(R.string.biggest_file_title));
            biggestFileTitle.setTypeface(null, Typeface.BOLD);
            resultContainer.addView(biggestFileTitle);
            for (int i = 0; i < info.getBiggestFiles().size() && i < 10; i++) {
                FileSizeInfo fileSizeInfo = info.getBiggestFiles().get(i);
                TextView tv = new TextView(this);
                tv.setText(fileSizeInfo.getName());
                tv.setGravity(Gravity.LEFT);
                resultContainer.addView(tv);
                tv = new TextView(this);
                tv.setText(fileSizeInfo.getSize() / 1024 + " KB");
                tv.setGravity(Gravity.RIGHT);
                resultContainer.addView(tv);
            }
            TextView mostFrequentExtensions = new TextView(this);
            mostFrequentExtensions.setGravity(Gravity.CENTER_HORIZONTAL);
            mostFrequentExtensions.setText(getString(R.string.most_frequent_ext));
            mostFrequentExtensions.setTypeface(null, Typeface.BOLD);
            resultContainer.addView(mostFrequentExtensions);
            for (int i = 0; i < info.getMostFrequentFileExts().size() && i < 5; i++) {
                ExtensionFrequency extensionFrequency = info.getMostFrequentFileExts().get(i);
                TextView tv = new TextView(this);
                tv.setText(extensionFrequency.getExtensionName());
                tv.setGravity(Gravity.LEFT);
                resultContainer.addView(tv);
                tv = new TextView(this);
                tv.setGravity(Gravity.RIGHT);
                tv.setText(getString(R.string.occured)+" " + extensionFrequency.getFrequency() + " "+getString(R.string.times));
                resultContainer.addView(tv);
            }
        } else {
            tvFilePath.setText(getString(R.string.scan_incomplete));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.shareMenuItem = menu.findItem(R.id.menu_item_share);
        shareMenuItem.setEnabled(false);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
        return true;
    }


    private void showStartNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.file_scanner))
                        .setContentText(getString(R.string.scan_started))
                        .setTicker(getString(R.string.file_scan_started))
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void share(){
        if(info!=null) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            StringBuffer shareBody = new StringBuffer(getString(R.string.average_file_size)+info.getAvgFileSize()/1024+" KB\n"+
                    getString(R.string.biggest_file_title)+"\n");

            for (int i = 0; i < info.getBiggestFiles().size() && i < 10; i++) {
                FileSizeInfo fileSizeInfo = info.getBiggestFiles().get(i);
                shareBody.append(fileSizeInfo.getName()+" ( "+fileSizeInfo.getSize()/1024+" KB )\n");
            }
            shareBody.append(getString(R.string.most_frequent_ext)+":\n");
            for (int i = 0; i < info.getMostFrequentFileExts().size() && i < 5; i++) {
                ExtensionFrequency extensionFrequency = info.getMostFrequentFileExts().get(i);
                shareBody.append(extensionFrequency.getExtensionName()+" "+getString(R.string.occured)+" "+extensionFrequency.getFrequency()+" "+getString(R.string.times)+"\n");
            }
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.my_phone_file_stat));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody.toString());
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    }

    private void startScan(View v) {
        btScan.setText(getString(R.string.stop));
        resultContainer.removeAllViewsInLayout();
        showStartNotification();
        presenter.startScan();
    }

    private void stopScan() {
        btScan.setText(getString(R.string.start));
        presenter.stopScan();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        presenter.destroy();
    }

}

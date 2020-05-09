package com.example.demomusicplayerver101;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.squareup.picasso.Downloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadSongTask extends AsyncTask<String, Integer, String> {
    Context context;
    String songFileName;

    ProgressDialog progressDialog;

    public DownloadSongTask(Context context, String songFileName) {
        this.context = context;
        this.songFileName = songFileName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Download song...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String urlString = strings[0];
        int fileLength = 0;
        String result = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            fileLength = httpURLConnection.getContentLength();
            //File filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            File filePath = new File(context.getFilesDir().getAbsolutePath());

            File inputFile = new File(filePath, this.songFileName + ".mp3");
            // mo cai luu luong input de minh doc vao
            InputStream inputStream = new BufferedInputStream(url.openStream(), 7000);
            byte[] data = new byte[1024];
            int count = 0;
            int totalSize = 0;
            FileOutputStream fileOutputStream = new FileOutputStream(inputFile);
            while ((count = inputStream.read(data)) != -1) {
                totalSize += count;
                fileOutputStream.write(data, 0, count);
                int progress = totalSize * 100 / fileLength;
                publishProgress(progress);
            }

            fileOutputStream.close();
            inputStream.close();
            result = "Download completed..."; // nó chưa tới đây vlin
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // file not found
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        // Cap nhat UI
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.hide();
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }

    public String getSongFileName() {
        return songFileName;
    }

    public void setSongFileName(String songFileName) {
        this.songFileName = songFileName;
    }
}

package com.example.demomusicplayerver101.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demomusicplayerver101.DownloadSongTask;
import com.example.demomusicplayerver101.MusicPlayerActivity;
import com.example.demomusicplayerver101.R;
import com.example.demomusicplayerver101.model.Song;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {
    Context context;
    int resource;
    List<Song> objects;

    File fileDir;
    File[] files;
    boolean isDownloaded = false;
    boolean isDeleted = true;

    public SongAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowItem = layoutInflater.inflate(resource, parent, false);

        TextView songNameTextView = rowItem.findViewById(R.id.songNameTextView);
        TextView artistTextView = rowItem.findViewById(R.id.artistTextView);
        ImageView songImageView = rowItem.findViewById(R.id.songImageView);
        final ImageButton downloadImageButton = rowItem.findViewById(R.id.downloadImageButton);
        final ImageButton deleteImageButton = rowItem.findViewById(R.id.deleteImageButton);

        songNameTextView.setText(objects.get(position).getSongName());
        artistTextView.setText(objects.get(position).getArtist());
        Picasso.get().load(objects.get(position).getImage()).into(songImageView);
        rowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("Song", objects.get(position));
                context.startActivity(intent);
            }
        });

        //isExternalStorageState();
        //Toast.makeText(context, isExternalStorageState() + "", Toast.LENGTH_SHORT).show();
        fileDir = new File(context.getFilesDir().getAbsolutePath());
        files = fileDir.listFiles();

        for (File file : files) {
            if (file.getName().equals(objects.get(position).getSongName() + ".mp3")) {
                Log.d("haha", file + "");
                downloadImageButton.setEnabled(false);
                deleteImageButton.setEnabled(true);
                isDownloaded = true;
                isDeleted = false;
            }
        }
//        if (isDownloaded == true) {
//            downloadImageButton.setEnabled(false);
//            deleteImageButton.setEnabled(true);
//        }
//        if (isDeleted == false){
//            downloadImageButton.setEnabled(true);
//            deleteImageButton.setEnabled(false);
//        }

        downloadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = objects.get(position).getSongPath();
                DownloadSongTask downloadSongTask = new DownloadSongTask(context, objects.get(position).getSongName());
                downloadSongTask.execute(urlString);
                Log.d("lala", urlString);

                downloadImageButton.setEnabled(false);
                deleteImageButton.setEnabled(true);
                //Toast.makeText(context, "lala", Toast.LENGTH_SHORT).show();
            }
        });

        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (File file : files) {
                    if (file.getName().equals(objects.get(position).getSongName() + ".mp3")) {
                        file.delete();
                        deleteImageButton.setEnabled(false);
                        downloadImageButton.setEnabled(true);
                    }
                }
                Toast.makeText(context, "lala", Toast.LENGTH_SHORT).show();
            }
        });

        return rowItem;
    }

    private boolean isExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}

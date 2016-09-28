package com.lapstr.lapstr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private Button mSelectImage;
    private StorageReference mSrorage;
    private ProgressDialog mProgressDialog;
    private Button buttonStart;

    private static final int SELECT_VIDEO = 3;

    private String path = "https://firebasestorage.googleapis.com/v0/b/lapstr-25ae6.appspot.com/o/Videos%2Fvideo%3A4672?alt=media&token=f3ec7f43-7423-4c96-9abc-a5f277650f95";
    private VideoView videoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSrorage = FirebaseStorage.getInstance().getReference();
        mSelectImage = (Button) findViewById(R.id.select_image);
        mProgressDialog = new ProgressDialog(this);
        /////////////
        videoview = (VideoView)findViewById(R.id.surface_view);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        videoview.setVideoPath(path);
        buttonStart = (Button) findViewById(R.id.start_btn);
        ////////

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                buttonStart.setOnClickListener(this);
                if (view == buttonStart) {
                    startVideo();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_VIDEO && resultCode == RESULT_OK);

        mProgressDialog.setMessage("Uploading ...");
        mProgressDialog.show();

        Uri uri = data.getData();
        StorageReference filepath = mSrorage.child("Videos").child(uri.getLastPathSegment());
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(MainActivity.this, "Upload Done", Toast.LENGTH_LONG).show();
                mProgressDialog.dismiss();

            }
        });
    }

    private void startVideo() {
        videoview.setVideoURI(Uri.parse(path));
        videoview.setMediaController(new MediaController(this));
        videoview.requestFocus();
        videoview.start();
    }
}

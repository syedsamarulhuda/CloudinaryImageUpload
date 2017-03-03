package com.example.samar.imageuploadcloudinary;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //a constant to track the file chooser intent
    private static final int SELECT_PICTURE_ACTIVITY_REQUEST_CODE = 0;

    //Buttons
    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonCamera;
    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    private String filePath;
    Cloudinary cloudinary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map config = new HashMap();
        config.put("cloud_name", "dvi1cncba");
        config.put("api_key", "969349186491898");
        config.put("api_secret", "DJ4tjfNA_4mDYzx2P3Jb27m66zY");
        cloudinary = new Cloudinary(config);

        //getting views from layout
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonCamera = (Button) findViewById(R.id.buttonCamera);
        imageView = (ImageView) findViewById(R.id.imageView);


        //attaching listener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonCamera.setOnClickListener(this);
    }



    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PICTURE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        filePath = cursor.getString(columnIndex);

                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                        imageView.setImageBitmap(bitmap);
                    }
                    cursor.close();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        //if the clicked button is choose
        if (view == buttonChoose) {
            showFileChooser();
        }
        //if the clicked button is upload
        else if (view == buttonUpload) {

            uploadFile();
        }

    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PICTURE_ACTIVITY_REQUEST_CODE);
    }




    private void uploadFile() {
        //if there is a file to upload
        Log.d("##FILE-PATH",filePath);
/*
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();*/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (filePath != null) {

            //displaying percentage in progress dialog
           // progressDialog.setMessage("Uploading...");
            Map<String ,String> uploadParams=new HashMap<>();
            uploadParams.put("resource_type", "auto");
            try {
                String response=cloudinary.uploader().uploadLarge(filePath, ObjectUtils.asMap("resource_type", "auto", "chunk_size", 6000000)).toString();
               /* JSONObject reader = new JSONObject(response);

                if(reader!=null)
                {
                    progressDialog.dismiss();
                }*/

                Log.d("##REPONSE",response);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {
            //you can display an error toast
        }
    }

}

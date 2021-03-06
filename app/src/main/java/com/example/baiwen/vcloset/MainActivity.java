package com.example.baiwen.vcloset;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public Button button2;
    public Button button3;
    String mCurrentPhotoPath;
    public int topflag = 0;
    public int botflag = 0;
    String imageFileName = "";
    public void init(){
        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choice = new Intent(MainActivity.this, OutfitChoice.class);
                startActivity(choice);
            }
        });

        button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent cate = new Intent(MainActivity.this, CategorizingScreen.class);
                startActivity(cate);

            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



        final Intent pickPhoto= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ViewPager viewPagerMain = (ViewPager) findViewById(R.id.pagerMain);
        viewPagerMain.setAdapter(new MainPagerAdapter(this));



        FloatingActionButton takePhotoButton = (FloatingActionButton) findViewById(R.id.floatingActionButton6);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dispatchTakePictureIntent();
            }
        });

        FloatingActionButton importPhotoButton = (FloatingActionButton) findViewById(R.id.floatingActionButton5);
        importPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GalleryPhoto = new Intent(MainActivity.this, PhotoViewer.class);

                startActivity(GalleryPhoto);

                startActivity(pickPhoto);
            }
        });

        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private File createImageFile() throws IOException {
        // Create an image file name


        ///remember to set flag to 0 after done. Top/Bottom.
        String timeStamp = new SimpleDateFormat("MMddHHmmss").format(new Date());
        if(topflag == 1){
             imageFileName = "Top" + timeStamp + "_";
        }
        else if(botflag ==1){
             imageFileName = "Bot" + timeStamp + "_";
        }
        else{
            imageFileName = "NoCatego" + timeStamp + "_";
        }

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        botflag = 0;
        topflag = 0;

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent intent = getIntent();
        topflag = intent.getIntExtra("topflag", 0);
        botflag = intent.getIntExtra("botflag", 0);
       
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                // ...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.baiwen.vcloset",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile);

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:

                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    //Intent imageCrop = new Intent(this, ImageCropActivity.class);
                    Intent imageCrop = new Intent(this, PhotoViewer.class);
                    imageCrop.putExtra("bitmapimage", photo);
                    startActivity(imageCrop);
                    Intent photoView = new Intent(this, PhotoViewer.class);
                    photoView.putExtra("bitmapimage", photo);
                    startActivity(photoView);
                    // Setting image image icon on the imageview


                    break;

                default:
                    Toast.makeText(this, "Something went wrong...",
                            Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }
}

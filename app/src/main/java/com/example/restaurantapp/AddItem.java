package com.example.restaurantapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantapp.controller.MyDBHandler;
import com.example.restaurantapp.controller.TextValidator;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddItem extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    MyDBHandler dbHandler;
    EditText itemName, itemPrice;

    Button addItem, selectPic;
    ImageView imageView;
    byte[] image;

    boolean checkName=false;
    boolean checkPrice=false;


    public boolean GALLERY_INTENT_CALLED = false;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> activityResultLauncherString;


    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        itemName = findViewById(R.id.item_name_txb);
        itemPrice = findViewById(R.id.item_price_txb);

        imageView = findViewById(R.id.select_imageView);
        addItem = findViewById(R.id.add_item_btn);
        selectPic = findViewById(R.id.select_image_btn);
        dbHandler = new MyDBHandler(this, null);

        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        itemName.addTextChangedListener(new TextValidator(itemName) {
            @Override public void validate(TextView textView, String text) {


                if(text.isEmpty()){
                    itemName.setError("write an item name");
                    checkName=false;
                }
//
                else
                    checkName=true;
            }
        });
        itemPrice.addTextChangedListener(new TextValidator(itemPrice) {
            @Override public void validate(TextView textView, String text) {


                if(text.isEmpty()){
                    itemPrice.setError("write an item price");
                    checkPrice=false;

                }
//
                else
                    checkPrice=true;
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(image!=null) {

//                if(checkName && checkPrice){
                int price;
                if(!checkPrice)
                    price=250;
                else
                    price=Integer.parseInt(itemPrice.getText().toString());

                if(!checkName)
                    itemName.setText("item name");


                long result = dbHandler.addItemHandler((itemName.getText().toString()), price, image);
                startActivity(new Intent(AddItem.this, MainActivity.class));
//                }
//                else Toast.makeText(getApplicationContext(),"enter the info please",Toast.LENGTH_LONG);
//                    image="[B@afdfe14".getBytes();
//                    long result = dbHandler.addItemHandler((itemName.getText().toString()), Integer.parseInt(itemPrice.getText().toString()), image);
//                    startActivity(new Intent(AddItem.this, MainActivity.class));
//                }


            }
        });


    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddItem.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(AddItem.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    //select from Gallery
    void selectImage() {
        requestPermission();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent,1);

//
        }


    }

//

    public String getImgPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        return filePath;
    }
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {

//            final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
//            ContentResolver resolver = getActivity().getContentResolver();
//            for (Uri uri : images) {
//                resolver.takePersistableUriPermission(uri, takeFlags);
//            }
//            Uri selectedImage = data.getData();
//            picUrl.setText(selectedImage.toString());
//            Uri imageUri=data.getData();
//            imageView.setImageURI(imageUri);

//            try {
//                final Uri imageUri = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                imageView.setImageBitmap(selectedImage);
//
//
//
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//
//            }

            Uri selectedImage = data.getData();


            Bitmap bitmap= null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                Picasso.get().load(selectedImage).into(imageView);
//               image=getBytesFromBitmap(bitmap);
//
//
//
//            } catch (FileNotFoundException e) {
//
//                e.printStackTrace();
//            } catch (IOException e) {
//
//                e.printStackTrace();
//            }

            try {
                bitmap=decodeUri(this,selectedImage,500);
                Picasso.get().load(selectedImage).into(imageView);
                image=getBytesFromBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }



           // imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }



    }


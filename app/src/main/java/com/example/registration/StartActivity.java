package com.example.registration;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.WHITE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StartActivity extends AppCompatActivity {


    private ImageView qrCODE_image, download_QRCODE;
    private OutputStream outputStream;
    private ImageView imageViewBitmap;


    @Override

    //OPEN ME
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Declare FindViewId para ma display or retrieve ang value sa interface
        TextView name = findViewById(R.id.name_here);
        TextView contact = findViewById(R.id.contact_here);
        TextView address = findViewById(R.id.address_here);
        TextView city = findViewById(R.id.city_here);
        TextView gender = findViewById(R.id.gender_here);
        TextView age = findViewById(R.id.age_here);
        TextView birthdate = findViewById(R.id.bday_here);
        TextView studId = findViewById(R.id.sid_here);
        TextView course = findViewById(R.id.course_here);
        TextView year = findViewById(R.id.year_here);
        TextView vaccine = findViewById(R.id.vaccine_here);
        TextView dose = findViewById(R.id.dose_here);
        TextView welcome = findViewById(R.id.welcomeText);
        TextView qrCODE_code = findViewById(R.id.qrcode_code);
        qrCODE_image = findViewById(R.id.user_QRCODE);
        download_QRCODE = findViewById(R.id.download_QRCODE);


        /*
                PROGRAM 2 - DATABASE

                - Dari mahitabo ang pag store sa information or value nga makuha sa interface.
                - Dari mahitabo ang pag retrieve sa information or value nga makuha sa database.
        */

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = Objects.requireNonNull(user).getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://amillionyearslater-7935e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String userId = snapshot.child("userId").getValue(String.class);
                String firstNameU = snapshot.child("firstName").getValue(String.class);
                String lastNameU = snapshot.child("lastName").getValue(String.class);
                String phoneNumberU = snapshot.child("phoneNumber").getValue(String.class);
                String ageU = snapshot.child("age").getValue(String.class);
                String addressU = snapshot.child("address").getValue(String.class);
                String cityU = snapshot.child("city").getValue(String.class);
                String genderU = snapshot.child("gender").getValue(String.class);
                String emailU = snapshot.child("email").getValue(String.class);
                String courseU = snapshot.child("course").getValue(String.class);
                String schoolIdU = snapshot.child("schoolId").getValue(String.class);
                String vaccineU = snapshot.child("vaccine").getValue(String.class);
                String vaccineDosageU = snapshot.child("vaccineDosage").getValue(String.class);
                String birthDateU = snapshot.child("birthDate").getValue(String.class);
                String yearLevelU = snapshot.child("yearLevel").getValue(String.class);


                String fullName = firstNameU + " " + lastNameU;
                String fullContact = phoneNumberU + " | " + emailU;
                String welcomeU = "Welcome " + firstNameU + "!";
                name.setText(fullName);
                contact.setText(fullContact);
                address.setText(addressU);
                city.setText(cityU);
                gender.setText(genderU);
                age.setText(ageU);
                birthdate.setText(birthDateU);
                studId.setText(schoolIdU);
                course.setText(courseU);
                year.setText(yearLevelU);
                vaccine.setText(vaccineU);
                dose.setText(vaccineDosageU);
                welcome.setText(welcomeU);
                qrCODE_code.setText(userId);

                try {
                    String fullQrCode = userId + schoolIdU + firstNameU.toUpperCase() + lastNameU.toUpperCase();
                    Bitmap qrCode = displayBitmap(fullQrCode);
                    Bitmap qrCode2 = downloadBitmap(fullQrCode);
                    qrCODE_image.setImageBitmap(qrCode);
                    download_QRCODE.setImageBitmap(qrCode2);
                } catch (Exception ex) {
                    Log.e("QrGenerate", ex.getMessage());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        Button save_QRCODE = (Button) findViewById(R.id.save_QRCODE);
        save_QRCODE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(StartActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {

                    saveToGallery();
                } else {

                    askPermission();
                }

            }
        });


    }


    //QR TO SCREEN DISPLAY
    private Bitmap downloadBitmap(String fullQrCode) {
        BitMatrix result = null;
        try {
            result = new MultiFormatWriter().encode(fullQrCode, BarcodeFormat.QR_CODE, 1000,1000,null);
        } catch (WriterException e){
            e.printStackTrace();
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int x=0; x<height; x++){
            int offset= x * width;
            for (int k=0; k<width; k++){
                pixels[offset + k] = result.get(k, x) ? BLACK : WHITE ;
            }
        }
        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        myBitmap.setPixels(pixels,0, width,0,0, width, height);
        //setting bitmap to image view
        Bitmap overlay = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        return mergeBitmaps(overlay, myBitmap);

    }
    private Bitmap mergeBitmaps(Bitmap overlay, Bitmap myBitmap){

        int height = myBitmap.getHeight();
        int width = myBitmap.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, myBitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(myBitmap, new Matrix(), null);

        int centreX = (canvasWidth - overlay.getWidth()) / 2;
        int centreY = (canvasHeight - overlay.getHeight()) / 2;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;
    }


    //QR TO DOWNLOADABLE IMAGE
    private Bitmap displayBitmap(String fullQrCode) {
        BitMatrix result = null;
        try {
            result = new MultiFormatWriter().encode(fullQrCode, BarcodeFormat.QR_CODE, 1000,1000,null);
        } catch (WriterException e){
            e.printStackTrace();
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int x=0; x<height; x++){
            int offset= x * width;
            for (int k=0; k<width; k++){
                pixels[offset + k] = result.get(k, x) ? BLACK : TRANSPARENT ;
            }
        }
        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        myBitmap.setPixels(pixels,0, width,0,0, width, height);

        return myBitmap;

    }


    //PERMISSION
    private void askPermission() {
        ActivityCompat.requestPermissions(StartActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveToGallery();
            }else {
                Toast.makeText(StartActivity.this,"Please provide the required permissions",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //DOWNLOAD QRCODE
    private void saveToGallery(){

        File dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if(!dir.exists()){
            dir.mkdirs();
        }

        BitmapDrawable drawable = (BitmapDrawable) download_QRCODE.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        File file = new File(dir, System.currentTimeMillis()+".png");
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        Toast.makeText(StartActivity.this, "QR code saved!", Toast.LENGTH_SHORT).show();

        try{
            outputStream.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }






       /* BitmapDrawable bitmapDrawable = (BitmapDrawable) qrCODE_image.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/MyPics");
        dir.exists();

        String filename = String.format("%d.png",System.currentTimeMillis());
        File outFile = new File(dir,filename);
        try{
            outputStream = new FileOutputStream(outFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        try{
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            outputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }*/
    }
}




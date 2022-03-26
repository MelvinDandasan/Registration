package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class RegistrationUser extends AppCompatActivity {

    private TextView banner;
    private EditText editFNAME, editLNAME, editEMAIL, editPHONE, editPASSWORD, editAGE, editADDRESS, editSCHOOLID;
    private EditText editBIRTHDATE;
    private ProgressBar progressLoading;
    private DatabaseReference databaseReference;
    private DatePickerDialog picker;
    private FirebaseDatabase database;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_user);

        mAuth = FirebaseAuth.getInstance();



        //municipality selection
        Spinner spinnerCities = (Spinner) findViewById(R.id.spin_city);
        ArrayAdapter<CharSequence> adapterCities = ArrayAdapter.createFromResource(this, R.array.municipality, android.R.layout.simple_spinner_item);
        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCities.setAdapter(adapterCities);

        //course selection
        Spinner spinnerCourse = (Spinner) findViewById(R.id.spin_course);
        ArrayAdapter<CharSequence> adapterCourse = ArrayAdapter.createFromResource(this, R.array.course, android.R.layout.simple_spinner_item);
        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourse.setAdapter(adapterCourse);

        //year level selection
        Spinner spinnerYearLevel = (Spinner) findViewById(R.id.spin_yearLevel);
        ArrayAdapter<CharSequence> adapterYearLevel = ArrayAdapter.createFromResource(this, R.array.yearLevel, android.R.layout.simple_spinner_item);
        adapterYearLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYearLevel.setAdapter(adapterYearLevel);

        //vaccine selection
        Spinner spinnerVaccines = (Spinner) findViewById(R.id.spin_vaccine);
        ArrayAdapter<CharSequence> adapterVaccines = ArrayAdapter.createFromResource(this, R.array.vaccines, android.R.layout.simple_spinner_item);
        adapterVaccines.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVaccines.setAdapter(adapterVaccines);


        TextView banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationUser.this, MainActivity.class));

            }
        });


        //ASSIGN ID to VARIABLES.
        editFNAME = (EditText) findViewById(R.id.et_fName);
        editLNAME = (EditText) findViewById(R.id.et_lName);
        editEMAIL = (EditText) findViewById(R.id.et_email);
        editPHONE = (EditText) findViewById(R.id.et_phone);
        editPASSWORD = (EditText) findViewById(R.id.et_password);
        editAGE = (EditText) findViewById(R.id.et_age);
        editADDRESS = (EditText) findViewById(R.id.et_address);
        editSCHOOLID = (EditText) findViewById(R.id.et_schoolID);
        progressLoading = (ProgressBar) findViewById(R.id.loading);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_gender);
        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radio_dose);
        Button buttonSUBMIT = (Button) findViewById(R.id.btn_submit);
        Button buttonBIRTHDATE = (Button) findViewById(R.id.btn_bDate);

        editBIRTHDATE = (EditText) findViewById(R.id.bday);
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        buttonBIRTHDATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker = new DatePickerDialog(RegistrationUser.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String birthdate = month+"/"+dayOfMonth+"/"+year;
                        buttonBIRTHDATE.setHint(birthdate);
                        editBIRTHDATE.setText(birthdate);
                    }
                },year , month, day);
                picker.show();
            }
        });

        buttonSUBMIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fName = editFNAME.getText().toString().trim();
                final String lName = editLNAME.getText().toString().trim();
                final String email = editEMAIL.getText().toString().trim();
                final String phone = editPHONE.getText().toString().trim();
                final String password = editPASSWORD.getText().toString().trim();
                final String age = editAGE.getText().toString().trim();
                final String address = editADDRESS.getText().toString().trim();
                final String schoolId = editSCHOOLID.getText().toString().trim();
                int checkedGENDER = radioGroup.getCheckedRadioButtonId();
                int checkedDOSE = radioGroup1.getCheckedRadioButtonId();
                RadioButton selectedGENDER = radioGroup.findViewById(checkedGENDER);
                RadioButton selectedDOSE = radioGroup1.findViewById(checkedDOSE);
                String city = spinnerCities.getSelectedItem().toString();
                String course = spinnerCourse.getSelectedItem().toString();
                String year_level = spinnerYearLevel.getSelectedItem().toString();
                String vaccine = spinnerVaccines.getSelectedItem().toString();
                final String birthdate = editBIRTHDATE.getText().toString();
                if (selectedGENDER == null) {
                    Toast.makeText(RegistrationUser.this, "Please select a gender", Toast.LENGTH_LONG).show();
                } else if (selectedDOSE == null) {
                    Toast.makeText(RegistrationUser.this, "Please select a vaccine", Toast.LENGTH_LONG).show();
                } else {
                    final String gender = selectedGENDER.getText().toString();
                    final String dose = selectedDOSE.getText().toString();
                    if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(email) ||
                            TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(age) ||
                            TextUtils.isEmpty(address) || TextUtils.isEmpty(schoolId) || city.equals("SELECT CITY") ||
                            course.equals("SELECT COURSE") || year_level.equals("SELECT YEAR") || vaccine.equals("SELECT VACCINE") ||
                            birthdate.equals("SELECT DATE")) {
                        Toast.makeText(RegistrationUser.this, "All fields are required! My goodness!", Toast.LENGTH_LONG).show();
                    } else {
                        registerStudent(fName, lName, email, phone, password, age, address, schoolId, gender, dose, city, course,
                                year_level, vaccine, birthdate);
                    }
                }


            }
        });

    }

    private void registerStudent(String fName, String lName, String email, String phone, String password, String age, String address,
                                 String schoolId, String gender, String dose, String city, String course, String year_level, String vaccine,
                                 String birthdate) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser rUser = mAuth.getCurrentUser();
                    assert rUser != null;
                    String userId = rUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance("https://amillionyearslater-7935e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(userId);
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("userId",userId);
                    hashMap.put("firstName",fName);
                    hashMap.put("lastName",lName);
                    hashMap.put("phoneNumber",phone);
                    hashMap.put("age",age);
                    hashMap.put("address",address);
                    hashMap.put("city",city);
                    hashMap.put("gender",gender);
                    hashMap.put("email",email);
                    hashMap.put("password",password);
                    hashMap.put("course",course);
                    hashMap.put("yearLevel",year_level);
                    hashMap.put("schoolId",schoolId);
                    hashMap.put("vaccine",vaccine);
                    hashMap.put("vaccineDosage",dose);
                    hashMap.put("birthDate", birthdate);
                    hashMap.put("imageURL","default");
                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegistrationUser.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegistrationUser.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    progressLoading.setVisibility(View.GONE);
                    Toast.makeText(RegistrationUser.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}


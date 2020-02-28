package com.example.eztoll;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout rlayout;
    private Animation animation;
    DatabaseHelper mydb;
    EditText editName,editEmail,editPassword,editMobile_Number;
    Button btnSignup;

    Button btnDatePicker;
    EditText txtDate;
    private int mYear, mMonth, mDay;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";


    /*
        public void toDashboardActivity(View view){
            Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
            startActivity(intent);
        }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        radioGroup = (RadioGroup) findViewById(R.id.choose_sex);
        mydb = new DatabaseHelper(this);
        //mydb.openDataBase();

        btnDatePicker=(Button)findViewById(R.id.datepicker);
        txtDate=(EditText)findViewById(R.id.dob);

        btnDatePicker.setOnClickListener(this);

        editName = (EditText)findViewById(R.id.editText_name);
        editEmail = (EditText)findViewById(R.id.editText_email);
        editPassword = (EditText)findViewById(R.id.editText_password);
        //editGender = (EditText)findViewById(R.id.editText_vehicle_number);
        editMobile_Number = (EditText)findViewById(R.id.editText_mobile_number);
        //editDate_of_birth = (EditText)findViewById(R.id.editText_driving_license_number);
        btnSignup = (Button)findViewById(R.id.signup);
        AddData();

        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);
    }

    private boolean validateemail() {
        String email = editEmail.getEditableText().toString().trim();
        if (email.isEmpty()) {
            editEmail.setError("Field cant be empty");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please enter valid email address");
            return false;
        } else {
            editEmail.setError(null);
            return true;
        }
    }

    private boolean validateuser() {
        String user = editName.getEditableText().toString().trim();
        if (user.isEmpty()) {
            editName.setError("Field cant be empty");
            return false;
        }
        else if(!user.matches(USERNAME_PATTERN)){
            editName.setError("Please enter valid user name");
            return false;
        }
        else {
            editName.setError(null);
            return true;
        }
    }

    private boolean validatepass() {
        String password = editPassword.getEditableText().toString().trim();
        if (password.isEmpty()) {
            editPassword.setError("Field cant be empty");
            return false;
        }
//        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(password).matches()){
//            editPassword.setError("Password is too weak");
//            return false;
//        }
        else {
            editPassword.setError(null);
            return true;
        }
    }

    private boolean validateDob() {
        String date = txtDate.getEditableText().toString().trim().substring(5);
        int date1 = Integer.parseInt(date);
        if (date.isEmpty() ) {
            txtDate.setError("Field cant be empty");
            return false;
        }
        else if (date1 >= 2001){
            txtDate.setError("Age should be greater than or equal to 18");
            return false;
        }
        else{
            txtDate.setError(null);
            return true;
        }
    }

    private  boolean validatemobile(){
        String mobile = editMobile_Number.getEditableText().toString().trim();
        String MobilePattern = "[0-9]{10}";
        if(mobile.isEmpty()){
            editMobile_Number.setError("Field cant be empty");
            return false;
        }
        else if(!mobile.matches(MobilePattern)){
            editMobile_Number.setError("Please enter valid mobile number");
            return false;
        }
        else{
            editMobile_Number.setError(null);
            return true;
        }
    }

    public void AddData(){
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                //Toast.makeText(RegisterActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();

                if(!validateemail() | !validatepass() | !validateuser() | !validatemobile() | !validateDob()){
                    return;
                }

                boolean isInserted = mydb.insertData(editName.getText().toString(), editEmail.getText().toString()
                        , editPassword.getText().toString(), radioButton.getText().toString()
                        , editMobile_Number.getText().toString(), txtDate.getText().toString());

                if (isInserted = true) {
                    Toast.makeText(RegisterActivity.this, "Sign-up Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }

                else {
                    Toast.makeText(RegisterActivity.this,"Data not Inserted",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }
}

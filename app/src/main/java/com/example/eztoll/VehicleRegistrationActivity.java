package com.example.eztoll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class VehicleRegistrationActivity extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;
    DatabaseHelper mydb;
    EditText editmodel,edittype,editenginenumber,editvehiclenumber,editfueltype,editdrivinglicencenumber;
    Button btnupdatedetails;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_registration);
        mydb = new DatabaseHelper(this);
        radioGroup = (RadioGroup) findViewById(R.id.license_status);
        btnupdatedetails = (Button) findViewById(R.id.updatebtn);

        editmodel = (EditText) findViewById(R.id.editmodel);
        edittype = (EditText) findViewById(R.id.editText_type);
        editenginenumber = (EditText) findViewById(R.id.editengine_number);
        editvehiclenumber = (EditText) findViewById(R.id.editText_vehicle_number);
        editfueltype = (EditText) findViewById(R.id.editText_fueltype);
        editdrivinglicencenumber = (EditText) findViewById(R.id.editText_driving_license_number);
        AddVData();

        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);
    }

    private boolean validateVmodel() {
        String model = editmodel.getEditableText().toString().trim();
        if (model.isEmpty()) {
            editmodel.setError("Field cant be empty");
            return false;
        }
        else {
            editmodel.setError(null);
            return true;
        }
    }

    private boolean validateVtype() {
        String type = edittype.getEditableText().toString().trim();
        if (type.isEmpty()) {
            edittype.setError("Field cant be empty");
            return false;
        }
        else {
            edittype.setError(null);
            return true;
        }
    }

    private boolean validateEngineNumber() {
        String En = editenginenumber.getEditableText().toString().trim();
        if (En.isEmpty()) {
            editenginenumber.setError("Field cant be empty");
            return false;
        }
        else {
            editenginenumber.setError(null);
            return true;
        }
    }

    private boolean validateVehicleNumber() {
        String Vn = editvehiclenumber.getEditableText().toString().trim();
        if (Vn.isEmpty()) {
            editvehiclenumber.setError("Field cant be empty");
            return false;
        }
        else {
            editvehiclenumber.setError(null);
            return true;
        }
    }

    private boolean validateVfuel() {
        String fuel = editfueltype.getEditableText().toString().trim();
        if (fuel.isEmpty()) {
            editfueltype.setError("Field cant be empty");
            return false;
        }
        else {
            editfueltype.setError(null);
            return true;
        }
    }

    private boolean validateLicense() {
        String license = editdrivinglicencenumber.getEditableText().toString().trim();
        if (license.isEmpty()) {
            editdrivinglicencenumber.setError("Field cant be empty");
            return false;
        }
        else {
            editdrivinglicencenumber.setError(null);
            return true;
        }
    }

    public void AddVData(){
        btnupdatedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                //Toast.makeText(RegisterActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();

                SharedPreferences prefs = getSharedPreferences("ank", MODE_PRIVATE);
                String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.

                if(!validateVmodel() | !validateVehicleNumber() | !validateVfuel() | !validateVtype() | !validateEngineNumber() | !validateLicense()){
                    return;
                }


                boolean isInserted = mydb.insertVData(name,editmodel.getText().toString(), edittype.getText().toString()
                        , editenginenumber.getText().toString(), editvehiclenumber.getText().toString(),
                        editfueltype.getText().toString(),editdrivinglicencenumber.getText().toString(),radioButton.getText().toString());

                if (isInserted = true) {
                    Toast.makeText(VehicleRegistrationActivity.this, "Details Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(VehicleRegistrationActivity.this, Main2Activity.class));
                }

                else {
                    Toast.makeText(VehicleRegistrationActivity.this,"Data not Inserted",Toast.LENGTH_SHORT).show();
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


}

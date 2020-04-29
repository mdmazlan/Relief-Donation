package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DonateInfoActivity extends AppCompatActivity implements View.OnClickListener {


    // variables
    EditText edPhn, edAmount,edDate;
    Spinner spinnerEffort, spinnerAmount;
    Button btn_submit;
    DatabaseReference databaseReference;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    int y,m,d;
    String donateDate;

    // variables pick location part

    private TextView txt_latlong_v;
    private TextView txt_address_v;
    private static final int ADDRESS_PICKER_REQUEST = 1020;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_info);

        MapUtility.apiKey = getResources().getString(R.string.google_place_api);
        findViewById(R.id.btn_location_pick).setOnClickListener(this);

        txt_latlong_v = findViewById(R.id.tv_pick_latlong_id);
        txt_address_v = findViewById(R.id.tv_pick_address_id);



        //hooks
        edPhn = findViewById(R.id.edPhn);
        edAmount = findViewById(R.id.edAmount);
        edDate = findViewById(R.id.edDate);
        spinnerEffort = findViewById(R.id.spinnerEffort);
        spinnerAmount = findViewById(R.id.spinnerAmount);
        btn_submit = findViewById(R.id.btn_submit);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Demo");

        edDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                y = calendar.get(Calendar.YEAR);
                m = calendar.get(Calendar.MONTH);
                d = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DonateInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        donateDate = dayOfMonth +"/" + (month+1) +"/" + year;
                        edDate.setText(donateDate);
                    }
                },y,m,d);
                datePickerDialog.show();
            }
        });



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDonateInfo();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_location_pick:
                Intent intent = new Intent(DonateInfoActivity.this, LocationPickerActivity.class);
                startActivityForResult(intent, ADDRESS_PICKER_REQUEST);
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    txt_latlong_v.setText("Lat:"+currentLatitude+"  Long:"+currentLongitude);
                    txt_address_v.setText("Address: "+address);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }








    private void addDonateInfo(){
        String phone = edPhn.getText().toString().trim();
        String effort = spinnerEffort.getSelectedItem().toString();
        String sAmount = spinnerAmount.getSelectedItem().toString();
        String amount = edAmount.getText().toString().trim();
        String inputDate= simpleDateFormat.format(calendar.getTime());
        //String ddate = edDate.getText().toString();

        // check Phn filed is not empty
        if(!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(amount)){

            String  id = databaseReference.push().getKey();
            DonateAdapter donateAdapter = new DonateAdapter(id,phone,effort,amount+sAmount,inputDate,donateDate);
            databaseReference.child(id).setValue(donateAdapter);

            Toast.makeText(this,"Info Added",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"You should enter a Phn number",Toast.LENGTH_SHORT).show();
        }
    }
}

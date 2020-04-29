package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

public class DonateInfoActivity extends AppCompatActivity {


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

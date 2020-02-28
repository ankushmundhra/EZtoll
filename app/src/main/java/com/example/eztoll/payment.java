package com.example.eztoll;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eztoll.models.Checksum;
import com.example.eztoll.models.Paytm;
import com.example.eztoll.utils.WebServiceCaller;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class payment extends AppCompatActivity {

    Button paytmButton;
    EditText amountText;
    DatabaseHelper mydb;
    static String customer_id;
    static String order_id;
    static String amt_recharged;
    static String currentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mydb = new DatabaseHelper(this);
        init();
    }

    private void init(){
        paytmButton=findViewById(R.id.paytmButton);
        amountText=findViewById(R.id.amountText);
        paytmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //runtime permission
                if (ContextCompat.checkSelfPermission(payment.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(payment.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                }
                processPaytm();
            }
        });
    }

    private void processPaytm(){

        String custID=generateString();
        String orderID=generateString();
        String callBackurl="https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1"+orderID;

        final Paytm paytm=new Paytm(

                "kHUCsy87770468813291",
                "WAP",
                amountText.getText().toString().trim(),
                "WEBSTAGING",
                callBackurl,
                "Retail",
                orderID,
                custID
        );

        WebServiceCaller.getClient().getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl(),
                paytm.getIndustryTypeId()).enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {
                if(response.isSuccessful()){
                    processToPay(response.body().getChecksumHash(),paytm);

                }
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Payment not Successful",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void processToPay(String checksumHash, com.example.eztoll.models.Paytm paytm) {
        PaytmPGService Service = PaytmPGService.getStagingService();

        HashMap<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , paytm.getmId());
        // Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , paytm.getOrderId());
        paramMap.put( "CUST_ID" , paytm.getCustId());
        paramMap.put( "CHANNEL_ID" , paytm.getChannelId());
        paramMap.put( "TXN_AMOUNT" , paytm.getTxnAmount());
        paramMap.put( "WEBSITE" , paytm.getWebsite());
        // This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , paytm.getIndustryTypeId());
        // This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put( "CHECKSUMHASH" , checksumHash);
        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);

        // Database
        customer_id = paytm.getCustId();
        order_id = paytm.getOrderId();
        amt_recharged = paytm.getTxnAmount();
        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SharedPreferences prefs = getSharedPreferences("ank", MODE_PRIVATE);
        String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
        //Toast.makeText(payment.this, name, Toast.LENGTH_LONG).show();

        boolean isInserted = mydb.insertTData(currentDate, name, order_id, amt_recharged);
        if (isInserted = true)
            Toast.makeText(payment.this, "Data Inserted", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(NfcActivity.this, MainActivity.class));
        else
            Toast.makeText(payment.this,"Data not Inserted",Toast.LENGTH_SHORT).show();

        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {}
            public void onTransactionResponse(Bundle inResponse) {
                //Toast.makeText(payment.this,inResponse.toString(),Toast.LENGTH_SHORT).show();

                Toast.makeText(payment.this,"Payment Successful",Toast.LENGTH_SHORT).show();

            }
            public void networkNotAvailable() {}
            public void clientAuthenticationFailed(String inErrorMessage) {}
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {}
            public void onBackPressedCancelTransaction() {}
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {}
        });
    }


    private String generateString(){
        String uuid= UUID.randomUUID().toString();
        return uuid.replaceAll("-","");
    }
}

package com.example.eztoll;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button button;
    CardView nfc_tap;
    CardView recharge_account;
    CardView checkbal;
    CardView chatbot;
    static FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth.AuthStateListener authListener;
    SQLiteDatabase mDatabase;
    public static final String DATABASE_NAME = "Registration.db";
    public static final String TABLE_NAME = "transaction_detail_table";
    double balance = 0.00;
    TextView textView2;

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        DatabaseHelper mydb = new DatabaseHelper(this);


        button = (Button) findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();

        mDatabase = openOrCreateDatabase(Main2Activity.DATABASE_NAME, MODE_PRIVATE, null);

//        SharedPreferences prefs = getSharedPreferences("main_bal", MODE_PRIVATE);
//        int name7 = prefs.getInt("name7", 0);
//        textView2 = (TextView) findViewById(R.id.textView2);
//        textView2.setText("₹" + name7 + ".00");

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    //startActivity(new Intent(Main2Activity.this,MainActivity.class));
                }
            }
        };


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // ...
                    case R.id.logout:
                        sendToLogin();
                        break;
                    // ...
                }
            }
        });

        nfc_tap = (CardView)findViewById(R.id.nfc);
        recharge_account = (CardView)findViewById(R.id.recharge);
        checkbal = (CardView)findViewById(R.id.wallet);
        chatbot = (CardView) findViewById(R.id.chatbot);

//        SharedPreferences prefs = getSharedPreferences("ank", MODE_PRIVATE);
//        String name = prefs.getString("name", "No name defined");
//

        checkbal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this,WalletManagementActivity.class));
            }
        });

        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this,Chatbot.class));
            }
        });

//        checkbal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Cursor res = mydb.getamt();
//                if (res.getCount() == 0) {
//                    //showMessage("Error", "Nothing Found");
//                    return;
//                }
//
//                String[] array_names = new String[res.getCount()]; //db names
//                String[] array_recharged_amt = new String[res.getCount()]; //db amt
//                int i = 0;
//
//                SharedPreferences prefs = getSharedPreferences("ank", MODE_PRIVATE);
//                String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
//                //Toast.makeText(payment.this, name, Toast.LENGTH_LONG).show();
//
//                while(res.moveToNext()){
//                    String uname = res.getString(res.getColumnIndex("NAME"));
//                    String ramt = res.getString(res.getColumnIndex("AMT_RECHARGED"));
//                    array_names[i] = uname;
//                    array_recharged_amt[i] = ramt;
//                    i++;
//                }
//
//                for(i = 0; i < array_names.length; i++){
//                    if (name.equals(array_names[i])){
//                        balance = balance + Double.parseDouble(array_recharged_amt[i]);
//                    }
//                }
//
//                String temp_bal = String.valueOf(balance);
//                SharedPreferences.Editor editor = getSharedPreferences("bal", MODE_PRIVATE).edit();
//                editor.putString("bal_key", temp_bal);
//                editor.apply();
//
//                SharedPreferences last_prefs = getSharedPreferences("bal_nfc_activity", MODE_PRIVATE);
//                String mbal_nfcActivity = last_prefs.getString("mbal_nfcActivity", "No name defined");
//                int flag = last_prefs.getInt("flag",0);
//
//                if (flag == 1){
//                    Toast.makeText(getApplicationContext(), "Main balance:" + mbal_nfcActivity, Toast.LENGTH_LONG).show();
//                    textView2 = (TextView) findViewById(R.id.textView2);
//                    textView2.setText("₹" + mbal_nfcActivity + "0");
//                    balance = 0.0;
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "Main balance:" + balance, Toast.LENGTH_LONG).show();
//                    textView2 = (TextView) findViewById(R.id.textView2);
//                    textView2.setText("₹" + temp_bal + "0");
//                    balance = 0.0;
//                }
//            }
//        });

        nfc_tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Main2Activity.this, "Logout Successfull", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), NfcActivity.class);
                startActivity(intent);
            }
        });

        recharge_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Main2Activity.this, "Logout Successfull", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), payment.class);
                startActivity(intent);
            }
        });


        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_vr) {
            startActivity(new Intent(Main2Activity.this,VehicleRegistrationActivity.class));
        } else if (id == R.id.nav_rt) {
            startActivity(new Intent(Main2Activity.this,RechargeTransactionsActivity.class));
        } else if (id == R.id.nav_tt) {
            startActivity(new Intent(Main2Activity.this,NfcTransactionsActivity.class));
        } else if (id == R.id.nav_tl) {
            startActivity(new Intent(Main2Activity.this,MapsActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void sendToLogin() {
        GoogleSignInClient mGoogleSignInClient ;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getBaseContext(), gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(Main2Activity.this,
                new OnCompleteListener<Void>() {  //signout Google
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut(); //signout firebase
                        Intent setupIntent = new Intent(getBaseContext(),MainActivity.class);
                        Toast.makeText(getBaseContext(), "Logout Successfull", Toast.LENGTH_LONG).show(); //if u want to show some text
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                        finish();
                    }
                });
    }
}

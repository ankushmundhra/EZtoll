package com.example.eztoll;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;

/*
in the first Activity:
Intent i=new Intent(getApplicationContext,secondActivity.class);
i.putExtra("key",value);
startActivity(i);

and in the SecondActivity:
String value=getIntent.getStringExtra("Key");
*/

public class MainActivity extends AppCompatActivity  {

    public static String personName;
    public static String personGivenName;
    public static String personFamilyName;
    public static String personEmail;
    public static String personId;
    public static Uri personPhoto;

    FirebaseAuth auth;
    TextView tvsignup;
    EditText email,password;
    Button loginbtn;
    Button btnViewAll;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    FirebaseAuth.AuthStateListener mAuthListener;
    //private CallbackManager mCallbackManager;
    private static final String TAG = "Facelog";
    static int flag = 0;

    DatabaseHelper mydb;
    SQLiteDatabase mDatabase;
    public static final String DATABASE_NAME = "Registration.db";
    public static final String TABLE_NAME = "registration_table";

//    private static String DB_PATH = "/data/data/com.example.eztoll/databases/";
//    private static String DB_NAME = "Registration";
//    SQLiteDatabase sampleDB =  null;
//    public String TABLE_NAME = "registration_table";

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    // plus sign.
    public void toRegisterActivity(View view){
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
    }


/*
    public void Admin(){
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                    Toast.makeText(MainActivity.this, "admin working!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,AndroidDatabaseManager.class));
                } else
                    Toast.makeText(MainActivity.this, "Welcome" + email, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,DashboardActivity.class));
            }
        });
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper mydb = new DatabaseHelper(this);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        loginbtn = (Button) findViewById(R.id.login);

        //opening the database
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        tvsignup = findViewById(R.id.tvsignup);

        tvsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Cursor res = mydb.getAllData();
                if (res.getCount() == 0) {
                    //showMessage("Error", "Nothing Found");
                    return;
                }

                String[] array = new String[res.getCount()]; //db user
                String[] array_pass = new String[res.getCount()]; //db pass
                String[] array_email = new String[res.getCount()]; //db email

                String usr_given_userName = email.getText().toString().trim();
                String user_given_passWord = password.getText().toString().trim();

//                String selectquery = "CREATE view myview1 AS SELECT transaction_detail_table.DATE,AMT_RECHARGED,AMT_DEBITED " +
//                        "FROM transaction_detail_table join nfc_transaction_table on transaction_detail_table.NAME = nfc_transaction_table.NAME " +
//                        "and nfc_transaction_table.NAME ="+"\""+usr_given_userName+"\"";
//
//                Cursor res1 = mDatabase.rawQuery(selectquery,null);

                int i = 0;

                while(res.moveToNext()){
                    String uname = res.getString(res.getColumnIndex("NAME"));
                    String pass = res.getString(res.getColumnIndex("PASSWORD"));
                    String mail = res.getString(res.getColumnIndex("EMAIL"));
                    array[i] = uname;
                    array_pass[i] = pass;
                    array_email[i] = mail;
                    i++;
                }

                if (usr_given_userName.equals("admin") && user_given_passWord.equals("admin")){
                    startActivity(new Intent(MainActivity.this,AndroidDatabaseManager.class));
                    Toast.makeText(getApplicationContext(),"Admin Panel",Toast.LENGTH_SHORT).show();
                }



                //static int flag = 0;

                for(i = 0; i < array.length; i++)
                {
                    String userName = array[i];
                    String passWord = array_pass[i];

                    if (usr_given_userName.equals(array[i]) || usr_given_userName.equals(array_email[i])){
                        if (user_given_passWord.equals(array_pass[i])){

                            SharedPreferences.Editor editor = getSharedPreferences("ank", MODE_PRIVATE).edit();
                            editor.putString("name", usr_given_userName);
                            editor.apply();

//                            String selectQuery = "CREATE view myview AS SELECT transaction_detail_table.DATE,AMT_RECHARGED,AMT_DEBITED " +
//                                    "FROM transaction_detail_table join nfc_transaction_table on transaction_detail_table.NAME = nfc_transaction_table.NAME " +
//                                    "and nfc_transaction_table.NAME ="+"\""+usr_given_userName+"\"";
//
//                            mDatabase.rawQuery(selectQuery,null);

                            startActivity(new Intent(MainActivity.this,Main2Activity.class));
                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Wrong Username or Password!",Toast.LENGTH_SHORT).show();
                        //continue;
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Wrong Username or Password!",Toast.LENGTH_SHORT).show();

                }

                /*
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    //buffer.append("ID: " + res.getString(0)+"\n");
                    buffer.append("LineType: " + res.getString(1)+"\n");
                    buffer.append("PackageType: " + res.getString(2)+"\n");
                    buffer.append("Quantity: " + res.getString(3)+"\n");
                    buffer.append("Duration: " + res.getString(4)+"\n");
                    buffer.append("StartTime: " + res.getString(5)+"\n");
                    buffer.append("EndTime: " + res.getString(6)+"\n\n");
                }
*/
            }
        });

// ...


        auth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(MainActivity.this,Main2Activity.class));
                }
            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        signInButton = (SignInButton) findViewById(R.id.mygooglebutton);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mygooglebutton:
                        signIn();
                        break;
                    // ...
                }
            }
        });

    }

/*
    public void loginBtn(View view){

        if (email.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
            startActivity(new Intent(MainActivity.this,AndroidDatabaseManager.class));
        } else {
            //gotoDashboard();
        }
    }
*/

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(MainActivity.this, "Something went wrong Yaikes!", Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();

                            Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),"Login Successfull",Toast.LENGTH_SHORT).show();

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);

                            Toast.makeText(getApplicationContext(),"Login Unsuccessfull!",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }



}

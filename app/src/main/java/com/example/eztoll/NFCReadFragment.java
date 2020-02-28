package com.example.eztoll;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class NFCReadFragment extends DialogFragment {
    static String temp;
    public static final String TAG = NFCReadFragment.class.getSimpleName();

    public static NFCReadFragment newInstance() {
        return new NFCReadFragment();
    }

    private TextView mTvMessage;
    private Listener mListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (NfcActivity)context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }

    public void onNfcDetected(Ndef ndef){
        readFromNFC(ndef);
    }

    private void readFromNFC(Ndef ndef) {

        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            Log.d(TAG, "readFromNFC: "+message);

            /**/
//            double bk = Double.parseDouble(bal_key);
//            double toll_amt = Double.parseDouble(messageToWrite1);
//            bk = bk - toll_amt;
//            String bk1 = String.valueOf(bk);
//            System.out.println("lablablablablabalbalbblalbal");
//            System.out.println(bk1);
            /**/

            temp = message;
            mTvMessage.setText(message);

            ndef.close();

        } catch (IOException | FormatException e) {
            e.printStackTrace();

        }
    }
}

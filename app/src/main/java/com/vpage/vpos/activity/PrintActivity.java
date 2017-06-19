package com.vpage.vpos.activity;

import android.app.Activity;
import android.os.Bundle;
import java.io.IOException;
import java.io.OutputStream;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.vpage.vpos.R;
import com.vpage.vpos.tools.utils.LogFlag;

public class PrintActivity extends Activity {

    private static final String TAG = PrintActivity.class.getName();

    EditText message;
    Button printbtn;

    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        message = (EditText)findViewById(R.id.message);
        printbtn = (Button)findViewById(R.id.printButton);

        printbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                connect();
            }
        });
    }

    protected void connect() {
        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), BTDeviceList.class);
            this.startActivityForResult(BTIntent, BTDeviceList.REQUEST_CONNECT_BT);
        }
        else{

            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
            }
            btoutputstream = opstream;
            print_bt();

        }

    }
    private void print_bt() {
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
            }

            btoutputstream = btsocket.getOutputStream();

            byte[] printformat = { 0x1B, 0x21, FONT_TYPE };
            btoutputstream.write(printformat);
            String msg = message.getText().toString();
            btoutputstream.write(msg.getBytes());
            btoutputstream.write(0x0D);
            btoutputstream.write(0x0D);
            btoutputstream.write(0x0D);
            btoutputstream.flush();
        } catch (IOException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(btsocket!= null){
                btoutputstream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            btsocket = BTDeviceList.getSocket();
            if(btsocket != null){
                print_bt();
            }

        } catch (Exception e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }
    }
}
package xyz.starfuture.testmessengerclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    // same as the server
    public static final int CODE_MESSAGE_TRAN = 1000;
    // same as the server
    public static final String KEY_MESSAGE_TRAN = "what_a_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bind();
            }
        });


    }

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // get messenger
            Messenger messenger = new Messenger(iBinder);
            // get message
            Message message = Message.obtain(null, CODE_MESSAGE_TRAN);
            // wrap data
            Bundle bundle = new Bundle();
            bundle.putString(KEY_MESSAGE_TRAN, "dear server,i m client.");
            // put data in the message
            message.setData(bundle);
            // send message
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void bind() {
        Intent i = new Intent();
        i.setPackage("xyz.starfuture.testmessagerserver");
        i.setAction("server");
        bindService(i, sc, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(sc);
        super.onDestroy();
    }
}

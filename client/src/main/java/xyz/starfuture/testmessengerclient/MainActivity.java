package xyz.starfuture.testmessengerclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private static final String TAG = "MessengerClient";

    /**
     * same as the server
     */
    public static final int CODE_MESSAGE_Client2Server = 1000;
    /**
     * same as the server
     */
    public static final int CODE_MESSAGE_Server2Client = 1001;
    /**
     * same as the server
     * message from client to server
     */
    public static final String KEY_MESSAGE_Client2Server = "client_to_server";
    /**
     * same as the server
     */
    public static final String KEY_MESSAGE_Server2Client = "server_to_client";

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
            Message message = Message.obtain(null, CODE_MESSAGE_Client2Server);
            // wrap data
            Bundle bundle = new Bundle();
            bundle.putString(KEY_MESSAGE_Client2Server, "dear server,i m client.");
            // put data in the message
            message.setData(bundle);
            // for receiving reply from server.
            message.replyTo = messengerReceiver;
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

    private Messenger messengerReceiver = new Messenger(new MessengerHandler());

    /**
     * once get message from server, do something.
     */
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_MESSAGE_Server2Client:
                    Log.d(TAG, "handleMessage: " + msg.getData().getString(KEY_MESSAGE_Server2Client));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

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

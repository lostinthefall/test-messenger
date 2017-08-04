package xyz.starfuture.testmessagerserver;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    /**
     * same as the client
     */
    public static final int CODE_MESSAGE_Client2Server = 1000;
    /**
     * same as the client
     */
    public static final int CODE_MESSAGE_Server2Client = 1001;
    /**
     * same as the client
     * message from client to server
     */
    public static final String KEY_MESSAGE_Client2Server = "client_to_server";
    /**
     * same as the client
     */
    public static final String KEY_MESSAGE_Server2Client = "server_to_client";

    public MessengerService() {
    }

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_MESSAGE_Client2Server:
                    // once receive message from client, do something.
                    Log.d(TAG, "handleMessage: " + msg.getData().getString(KEY_MESSAGE_Client2Server));
                    // reply client.
                    // get messenger
                    Messenger messenger = msg.replyTo;
                    // get message
                    Message message = Message.obtain(null, CODE_MESSAGE_Server2Client);
                    // wrap data
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_MESSAGE_Server2Client, "dear client, i just get your message, blablabla...");
                    // put data in the message
                    message.setData(bundle);
                    // send message
                    try {
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger messenger = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}

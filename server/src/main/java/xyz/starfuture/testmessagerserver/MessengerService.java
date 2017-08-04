package xyz.starfuture.testmessagerserver;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    // same as the client
    public static final int CODE_MESSAGE_TRAN = 1000;
    // same as the client
    public static final String KEY_MESSAGE_TRAN = "what_a_key";

    public MessengerService() {
    }

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_MESSAGE_TRAN:
                    // do something
                    Log.d(TAG, "handleMessage: " + msg.getData().getString(KEY_MESSAGE_TRAN));
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

package cn.suiseiseki.www.criminalintent;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * Created by Administrator on 2016/3/9.
 */
public class MyService extends Service {

    private class AudioPlayer {

        private MediaPlayer mMediaPlayer;
        private boolean mIsPaused;
        public void stop()
        {
            if(mMediaPlayer!= null)
                mMediaPlayer.release();
            mMediaPlayer = null;
            mIsPaused = false;
        }
        public void play(Context c)
        {
            if(!mIsPaused) {
                stop();
                mMediaPlayer = MediaPlayer.create(c, R.raw.street);
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stop();
                    }
                });
                mMediaPlayer.start();
            }
            else
                mMediaPlayer.start();
        }
        public void pause()
        {
            mMediaPlayer.pause();
            mIsPaused = true;
        }
        public String tester()
        {
            return "Service Message Sended";
        }
    }

        public class MyBinder extends Binder {
        public String message;

        @Override
        public boolean pingBinder() {
            return false;
        }

        @Override
        public boolean isBinderAlive() {
            return false;
        }

        @Override
        public IInterface queryLocalInterface(String descriptor) {
            return null;
        }


        @Override
        public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
            return false;
        }
    };

    private AudioPlayer audioPlayer;
    private static final String TAG = "MyService";
    @Override
    public IBinder onBind(Intent intent)
    {
        MyBinder myBinder = new MyBinder();
        Log.i(TAG,"onBind");
        audioPlayer = new AudioPlayer();
        audioPlayer.play(MyService.this);
        myBinder.message = audioPlayer.tester();
        return myBinder;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i(TAG,"At your Service on Create()");
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startID)
    {
        super.onStartCommand(intent, flags, startID);
        audioPlayer = new AudioPlayer();
        audioPlayer.play(MyService.this);
        Log.i(TAG,"onstartCommand");
        return 0;
    }
    @Override
    public void onDestroy()
    {
        audioPlayer.stop();
        super.onDestroy();
        Log.i(TAG,"onDestroy()");
    }
    @Override
    public boolean onUnbind(Intent intent)
    {
        audioPlayer.stop();
        Log.i(TAG,"Unbind");
        return         super.onUnbind(intent);
    }

}

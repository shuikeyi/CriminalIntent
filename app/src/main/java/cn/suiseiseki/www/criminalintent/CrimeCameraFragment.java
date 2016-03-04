package cn.suiseiseki.www.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/3/3.
 */
public class CrimeCameraFragment extends Fragment {
    private final static String TAG = "CrimeCameraFragment";
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private View mProgressContainer;
    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback(){
        public void onShutter()
        {
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };
    public static final String EXTRA_PHOTO_FILENAME = "cn.suiseiseki.www.crimeIntent.Camera";

    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback(){
        public void onPictureTaken(byte[] data,Camera camera)
        {
            String filename = UUID.randomUUID().toString() + ".jpg";
            FileOutputStream output = null;
            boolean success = true;
            try{
                output = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                output.write(data);
            }
            catch (Exception e)
            {
                Log.e(TAG,"Error writing to file "+filename,e );
                success = false;
            }
            finally
            {
                try{
                    if(output !=null)
                        output.close();
                }
                catch (Exception e)
                {
                    Log.e(TAG,"Error closing file "+filename,e);
                    success = false;
                }
            }
            if(success)
            {
                Log.d(TAG,"JPEG saved at "+filename);
                Intent i = new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME,filename);
                getActivity().setResult(Activity.RESULT_OK,i);
            }
            else
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().finish();
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle saveInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_crime_camera,parent,false);
        Button takePictureButton = (Button)v.findViewById(R.id.crime_camara_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(mShutterCallback,null,mJpegCallback);
            }
        });
        mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);
        mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camara_surfaceview);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (mCamera!=null) {
                        mCamera.setPreviewDisplay(holder);
                    }
                }
                catch (Exception e)
                    {
                        Log.e(TAG,"Error setting up preview display",e);

                    }
                }


            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if(mCamera == null )
                    return;
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(),width,height);
                parameters.setPreviewSize(s.width, s.height);
                s = getBestSupportedSize(parameters.getSupportedPictureSizes(),width,height);
                parameters.setPictureSize(s.width,s.height);
                mCamera.setParameters(parameters);
                try
                {
                    mCamera.startPreview();
                }
                catch(Exception e)
                {
                    Log.e(TAG,"could not start preview",e);
                    mCamera.release();
                    mCamera = null;
                }

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if(mCamera!=null)
                    mCamera.stopPreview();

            }
        });
        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mCamera = Camera.open();

    }
    @Override
    public void onPause()
    {
        super.onPause();
        if(mCamera!=null)
        {
            mCamera.release();
            mCamera = null;
        }
    }
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes,int width,int height)
    {
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for(Camera.Size s:sizes)
        {
            int area = s.width * s.height;
            if(area > largestArea ){
                bestSize = s;
                largestArea = area;
            }

        }
        return bestSize;
    }

}

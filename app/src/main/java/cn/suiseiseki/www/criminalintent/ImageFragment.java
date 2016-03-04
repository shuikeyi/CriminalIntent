package cn.suiseiseki.www.criminalintent;

/**
 * Created by Administrator on 2016/3/4.
 */

import android.app.DialogFragment;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends DialogFragment {
    public static final String TAG = "cn.suiseiseki.www.CrimeIntent.ImageFragment";
    public static final String EXTRA_IMAGE_PATH = "cn.suiseiseki.www.CrimeIntent.imagepath";
    public static ImageFragment newInstance(String imagepath)
    {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH, imagepath);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
        return fragment;
    }

    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        imageView = new ImageView(getActivity());
        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable bitmap = PictureUtils.getScaledDrawable(getActivity(),path);
        imageView.setImageDrawable(bitmap);
        return  imageView;

    }
    @Override
    public void onDestroyView()
    {
        PictureUtils.cleanImageView(imageView);
        super.onDestroyView();
    }

}

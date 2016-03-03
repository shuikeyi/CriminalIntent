package cn.suiseiseki.www.criminalintent;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/3/3.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {

    public Fragment createFragment()
    {
        return new CrimeCameraFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

    }
}

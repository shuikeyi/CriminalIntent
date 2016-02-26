package cn.suiseiseki.www.criminalintent;

import android.app.Fragment;

/**
 * Created by Administrator on 2016/2/26.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment()
    {
        return new CrimeListFragment();
    }
}

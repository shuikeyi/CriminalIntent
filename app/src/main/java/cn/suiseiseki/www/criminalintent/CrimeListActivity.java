package cn.suiseiseki.www.criminalintent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

/**
 * Created by Administrator on 2016/2/26.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks,CrimeFragment.Callbacks {
    @Override
    protected Fragment createFragment()
    {
        return new CrimeListFragment();
    }
    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_masterdetail;
    }
    @Override
    public void onCrimeUpdated(Crime crime)
    {
        FragmentManager fm = getFragmentManager();
        CrimeListFragment crimeListFragment =(CrimeListFragment) fm.findFragmentById(R.id.fragmentContainer);
        crimeListFragment.updateUI();
    }

    @Override
    public void onCrimeSelected(Crime crime)
    {
        if(findViewById(R.id.detailFragmentContainer) == null)
        {
            Intent i = new Intent(this,CrimePagerActivity.class);
            i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getmId());
            startActivity(i);
        }
        else
        {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction =fm.beginTransaction();
            Fragment olddetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newdetail = CrimeFragment.newInstance(crime.getmId());
            if(olddetail!=null)
                transaction.remove(olddetail);
            transaction.add(R.id.detailFragmentContainer,newdetail);
            transaction.commit();

        }

    }
}

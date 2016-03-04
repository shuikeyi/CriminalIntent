package cn.suiseiseki.www.criminalintent;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;

import java.util.ArrayList;
import java.util.UUID;

import addon.FragmentStatePagerAdapter;


/**
 * Created by Administrator on 2016/2/27.
 */
public class CrimePagerActivity extends Activity implements CrimeFragment.Callbacks{


    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;

    public void onCrimeUpdated(Crime crime)
    {

    }



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);
        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fm=getFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

            public Fragment getItem(int i) {
                Crime crime = mCrimes.get(i);
                return CrimeFragment.newInstance(crime.getmId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        UUID crimeID = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for(int i = 0;i<mCrimes.size();i++)
        {
            if(mCrimes.get(i).getmId().equals(crimeID))
            {mViewPager.setCurrentItem(i);
                setTitle(mCrimes.get(i).getmTitle());
            break;}
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Crime crime = mCrimes.get(i);
                if (crime.getmTitle() != null)
                    setTitle(crime.getmTitle());

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

}

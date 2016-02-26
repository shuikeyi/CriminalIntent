package cn.suiseiseki.www.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Administrator on 2016/2/26.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private ArrayList<Crime> mCrimes;

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID ID)
    {
        for(Crime crime: mCrimes)
        {
            if(crime.getmId().equals(ID))
                return crime;
        }
        return null;
    }




    private CrimeLab(Context AppContext)
    {
        this.mAppContext = AppContext;
        mCrimes = new ArrayList<>();
        for(int i=0;i<100;i++)
        {
            Crime c = new Crime();
            c.setmTitle("Crime #"+i);
            c.setmSolved(i % 2 == 0);
            mCrimes.add(c);
        }
    }
    public static CrimeLab get(Context c){
        if(sCrimeLab == null)
        {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;

    }
}

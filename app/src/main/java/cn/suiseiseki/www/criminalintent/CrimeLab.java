package cn.suiseiseki.www.criminalintent;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Administrator on 2016/2/26.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private ArrayList<Crime> mCrimes;
    private final static String TAG = "CrimeLab";
    private final static String FILENAME = "crimes.json";
    private CriminalIntentJSONSerializer mSerializer;
//    private static CrimeLab sCrimeLab;
//    private Context mAppContext;

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

    public boolean saveCrimes()
    {
        try{
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG,"crimes saved to file");
            return true;
        }
        catch(Exception e)
        {
            Log.e(TAG,"Error saving Crimes: ",e);
            return false;
        }
    }




    private CrimeLab(Context AppContext)
    {
        this.mAppContext = AppContext;
         mSerializer  = new CriminalIntentJSONSerializer(AppContext,FILENAME);
        try {
            mCrimes = mSerializer.loadCrimes();
        }
        catch (Exception e)
        {
            mCrimes = new ArrayList<>(mCrimes);
            Log.e("TAG","Error loading Crimes");
        }
//        mCrimes = new ArrayList<>();
//        for(int i=0;i<100;i++)
//        {
//            Crime c = new Crime();
//            c.setmTitle("Crime #"+i);
//            c.setmSolved(i % 2 == 0);
//            mCrimes.add(c);
//        }
    }
    public static CrimeLab get(Context c){
        if(sCrimeLab == null)
        {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;

    }
    public void addCrime(Crime c)
    {
        mCrimes.add(c);
    }
    public void deleteCrime(Crime c)
    {
        mCrimes.remove(c);
    }

}

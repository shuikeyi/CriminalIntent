package cn.suiseiseki.www.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2016/2/25.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private boolean mSolved;
    private Date mDate;

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }


    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }


    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Crime()
    {
        // make an unique ID
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    @Override
    public String toString()
    {
        return getmTitle();
    }
}

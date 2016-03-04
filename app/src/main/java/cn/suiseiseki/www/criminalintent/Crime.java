package cn.suiseiseki.www.criminalintent;

import android.provider.ContactsContract;

import org.json.JSONException;
import org.json.JSONObject;

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

    public String getmNumber() {
        return mNumber;
    }

    public void setmNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    private String mNumber;

    public String getmSuspect() {
        return mSuspect;
    }

    public void setmSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }

    private String mSuspect;
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "Photo";
    private static final String JSON_SUSPECT = "Suspect";
    private static final String JSON_NUMBER = "Number";
    public Photo getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(Photo mPhoto) {
        this.mPhoto = mPhoto;
    }

    private Photo mPhoto;


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

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID,mId.toString());
        json.put(JSON_TITLE,mTitle);
        json.put(JSON_SOLVED,mSolved);
        json.put(JSON_DATE,mDate.getTime());
        if(mSuspect!=null)
            json.put(JSON_SUSPECT,mSuspect);
        if(mPhoto!=null)
            json.put(JSON_PHOTO,getmPhoto().toJSON());
        if(JSON_NUMBER!=null)
            json.put(JSON_NUMBER,getmNumber());
        return json;
    }

    public Crime(JSONObject json) throws JSONException{
        mId = UUID.fromString(json.getString(JSON_ID));
        if(json.has(JSON_TITLE))
        {
            mTitle =json.getString(JSON_TITLE);
        }
        mSolved = json.getBoolean(JSON_SOLVED);
        mDate = new Date(json.getLong(JSON_DATE));
        if(json.has(JSON_PHOTO))
        {
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }
        if(json.has(JSON_SUSPECT))
        {
            mSuspect = json.getString(JSON_SUSPECT);
        }
        if(json.has(JSON_NUMBER))
        {
            mNumber = json.getString(JSON_NUMBER);
        }
    }

    @Override
    public String toString()
    {
        return getmTitle();
    }
}

package cn.suiseiseki.www.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/3.
 */
public class Photo {

    private final static String JSON_FILENAME = "filename";
    private String mFilename;

    public Photo(String mFilename)
    {
        this.mFilename = mFilename;
    }
    public Photo(JSONObject json) throws JSONException {
        this.mFilename = json.getString(JSON_FILENAME);
    }
    public JSONObject toJSON() throws JSONException
    {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put(JSON_FILENAME,mFilename);
        return jsonobject;
    }
    public String getFilename(){
        return this.mFilename;
    }

}

package cn.suiseiseki.www.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.text.format.DateFormat;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.Serializable;
import java.lang.annotation.Target;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/2/25.
 */
public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mImageView;
    private Button mReportButton;
    private final static String TAG = "CrimeFragment";

    public static final String EXTRA_CRIME_ID = "cn.suiseiseki.www.criminalintent.crime_id";
    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;
    private final static int REQUEST_PHOTO = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_NUMBER = 99;
    private Button mSuspectButton;
    private final static String DIALOG_IMAGE = "image";
    private void showPhoto()
    {
        Photo p = mCrime.getmPhoto();
        BitmapDrawable b = null;
        if(p!=null) {
            String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(),path);
        }
        mImageView.setImageDrawable(b);
    }
    private String getCrimeReport()
    {
        String reportString = null;
        if(mCrime.ismSolved())
            reportString = getString(R.string.crime_report_solved);
        else
            reportString = getString(R.string.crime_report_unsolved);
        String dateFormat = "EEE,MMM dd";
        String dateString = DateFormat.format(dateFormat,mCrime.getmDate()).toString();
        String suspect = mCrime.getmSuspect();
        if(suspect == null)
            suspect = getString(R.string.crime_report_no_suspect);
        else
            suspect = getString(R.string.crime_report_no_suspect,suspect);
        String report = getString(R.string.crime_report,mCrime.getmTitle(),dateString,reportString,suspect);
        return report;

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UUID uuid = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(uuid);
        setHasOptionsMenu(true);
    }
    @Override
    public void onStart()
    {
        super.onStart();
        showPhoto();
    }
    @Override
    public void onStop()
    {
        super.onStart();
        PictureUtils.cleanImageView(mImageView);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
           switch(item.getItemId()){
               case android.R.id.home:
                   if(NavUtils.getParentActivityName(getActivity())!=null){
                       NavUtils.navigateUpFromSameTask(getActivity());
                   }
                   return true;
               default: return super.onOptionsItemSelected(item);
           }
    }
    public static CrimeFragment newInstance(UUID crimeID)
    {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeID);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void updateDate()
    {
        mDateButton.setText(mCrime.getmDate().toString());
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(resultCode != Activity.RESULT_OK)
            return;
        if(requestCode == REQUEST_DATE)
        {
            Date date = (Date)data.getSerializableExtra(DatePickeFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateDate();
        }
        else if(requestCode == REQUEST_PHOTO)
        {
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if(filename != null)
                Log.d(TAG, "photofile received");
            Photo p = new Photo(filename);
            mCrime.setmPhoto(p);
            showPhoto();
        }
        else if(requestCode == REQUEST_CONTACT)
        {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
            if(c.getCount()==0) {
                c.close();
                return;
            }
            c.moveToFirst();
            String suspect = c.getString(0);
            mCrime.setmSuspect(suspect);
            mSuspectButton.setText(suspect);
            c.close();
//            Uri[] myUri = {ContactsContract.CommonDataKinds.Phone.CONTENT_URI};
//            Cursor numberc = getActivity().getContentResolver().query(contactUri,queryFields2,null,null,null);
//            if(numberc.getCount() == 0)
//            {
//                numberc.close();
//                return;
//            }
//            numberc.moveToFirst();
//            mCrime.setmNumber(numberc.getString(0));
//            numberc.close();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mDateButton = (Button)v.findViewById(R.id.crime_date);
        String temp = DateFormat.format("EEEE, MMMM dd, yyyy h:mmaa",mCrime.getmDate()).toString();
        mDateButton.setText(temp);
//        mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                DatePickeFragment dialog = DatePickeFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });
           getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        mReportButton = (Button)v.findViewById(R.id.crime_report_button);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                PackageManager pm = getActivity().getPackageManager();
                List<ResolveInfo> info = pm.queryIntentActivities(i,0);
                if(info.size()>0)
                startActivity(i);
            }
        });
        mSuspectButton = (Button)v.findViewById(R.id.crime_suspect_button);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i,REQUEST_CONTACT);
            }
        });
        if(mCrime.getmSuspect()!=null)
            mSuspectButton.setText(mCrime.getmSuspect());
        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_camaraButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),CrimeCameraActivity.class);
                startActivityForResult(i,REQUEST_PHOTO);
            }
        });
        mImageView = (ImageView)v.findViewById(R.id.crime_imageView);
        mTitleField.setText(mCrime.getmTitle());
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });
        Button callSuspectButton = (Button)v.findViewById(R.id.crime_call_suspect);
        callSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri myUri = Uri.parse("tel:"+mCrime.getmNumber());
                Intent i = new Intent(Intent.ACTION_DIAL,myUri);
                startActivity(i);
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p = mCrime.getmPhoto();
                if(p==null)
                    return;
                FragmentManager fm = getActivity().getFragmentManager();
                String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fm,DIALOG_IMAGE);

            }
        });
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setmTitle(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v ;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

}

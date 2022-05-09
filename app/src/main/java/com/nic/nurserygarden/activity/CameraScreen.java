package com.nic.nurserygarden.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.api.Api;
import com.nic.nurserygarden.api.ServerResponse;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.CameraScreenBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.support.MyLocationListener;
import com.nic.nurserygarden.utils.CameraUtils;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class CameraScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {

    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2500;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static String imageStoragePath;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    LocationManager mlocManager = null;
    LocationListener mlocListener;
    Double offlatTextValue, offlongTextValue;
    private PrefManager prefManager;
    private CameraScreenBinding cameraScreenBinding;





    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private dbData dbData = new dbData(this);
    String pmay_id;


    ///Image With Description
    ImageView imageView, image_view_preview;
    TextView latitude_text, longtitude_text;
    EditText myEditTextView;
    private List<View> viewArrayList = new ArrayList<>();

    String activity_type = "";
    String land_type_name_en = "";
    String land_type_name_ta = "";
    int land_type_id =0;
    int nursery_id =0;
    String land_address="";
    String entry_date="";
    int batch_id=0;
    int batch_primary_id=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraScreenBinding = DataBindingUtil.setContentView(this, R.layout.camera_screen);
        cameraScreenBinding.setActivity(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        activity_type = getIntent().getStringExtra("activity_type");
        intializeUI();

        cameraScreenBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageButtonClick();
            }
        });
    }

    public void intializeUI() {
        if(activity_type.equals("Land")){
            land_type_id = getIntent().getIntExtra("land_type_id",0);
            nursery_id = getIntent().getIntExtra("nursery_id",0);
            land_type_name_en = getIntent().getStringExtra("land_type_name_en");
            land_type_name_ta = getIntent().getStringExtra("land_type_name_ta");
            land_address = getIntent().getStringExtra("land_address");
            cameraScreenBinding.singleCaptureLayout.setVisibility(View.VISIBLE);
            cameraScreenBinding.multiCaptureLayout.setVisibility(View.GONE);
        }
        else if(activity_type.equals("Batch")){
            cameraScreenBinding.singleCaptureLayout.setVisibility(View.GONE);
            cameraScreenBinding.multiCaptureLayout.setVisibility(View.VISIBLE);
        }
        else if(activity_type.equals("GrowthTracking")){
            batch_id = getIntent().getIntExtra("batch_id",0);
            batch_primary_id = getIntent().getIntExtra("batch_primary_id",0);
            entry_date = getIntent().getStringExtra("entry_date");
            cameraScreenBinding.singleCaptureLayout.setVisibility(View.GONE);
            cameraScreenBinding.multiCaptureLayout.setVisibility(View.VISIBLE);
        }
        viewArrayList.clear();
        updateView(CameraScreen.this,cameraScreenBinding.cameraLayout,"","");
        prefManager = new PrefManager(this);

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();

        //pmay_id = getIntent().getStringExtra("lastInsertedID");
        cameraScreenBinding.btnSave.setOnClickListener(this::onClick);
        cameraScreenBinding.btnSaveLocal.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_save:
                if(activity_type.equals("Land")) {
                    saveLandImage();
                }
                break;
            case R.id.btn_save_local:
                if(activity_type.equals("Batch")) {
                    saveImageButtonClick();
                }
                else if(activity_type.equals("GrowthTracking")){
                    saveGrowthTrackImage();
                }
        }
    }


    public void saveActivityImage() {
        dbData.open();

        long id = 0; String whereClause = "";String[] whereArgs = null;
        String type_of_photo = getIntent().getStringExtra(AppConstant.TYPE_OF_PHOTO);
        Log.d("type_of_photo",type_of_photo);


        byte[] imageInByte = new byte[0];
        String image_str = "";
        try {
            Bitmap bitmap = ((BitmapDrawable) cameraScreenBinding.imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imageInByte = baos.toByteArray();
            image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

            ContentValues values = new ContentValues();

            values.put(AppConstant.PMAY_ID, pmay_id);
            values.put(AppConstant.TYPE_OF_PHOTO, getIntent().getStringExtra(AppConstant.TYPE_OF_PHOTO));
            values.put(AppConstant.KEY_LATITUDE, offlatTextValue.toString());
            values.put(AppConstant.KEY_LONGITUDE, offlongTextValue.toString());
            values.put(AppConstant.KEY_IMAGE, image_str.trim());

            if(type_of_photo.equals("2")){
                dbData.open();
                ArrayList<NurserySurvey> imageOffline = dbData.getSavedPMAYImages(pmay_id,"1");

                if (imageOffline.size() > 0){
                    for (int i= 0; i<imageOffline.size(); i++){
                        Double latitude = Double.valueOf(imageOffline.get(i).getLatitude());
                        Double longitude = Double.valueOf(imageOffline.get(i).getLongitude());
                        Log.d("latitude :"+latitude,"longitude :"+longitude);

                        float[] results = new float[1];
                        Location.distanceBetween(latitude, longitude, offlatTextValue, offlongTextValue, results);
                        Log.d("offlatitude :"+offlatTextValue,"offlongitude :"+offlongTextValue);
                        float distanceInMeters = results[0];
                        Log.d("isWithin10m", String.valueOf(distanceInMeters));
                        boolean isWithin10m = distanceInMeters < 15.0;
                        Log.d("isWithin10m", String.valueOf(isWithin10m));
                        distFrom(latitude, longitude, offlatTextValue, offlongTextValue);
//                        if (isWithin10m) {
//                            continue;
//                        }
//                        else {
//                            Utils.showAlert(this, "Capturing must be within 15 metres");
//                            return;
//                        }
                    }
                }
            }

                whereClause = "pmay_id = ? and type_of_photo = ?";
                whereArgs = new String[]{pmay_id,type_of_photo};dbData.open();
                ArrayList<NurserySurvey> imageOffline = dbData.getSavedPMAYImages(pmay_id,type_of_photo);

                if(imageOffline.size() < 1) {
                    id = db.insert(DBHelper.SAVE_PMAY_IMAGES, null, values);
                }
                else {
                    id = db.update(DBHelper.SAVE_PMAY_IMAGES, values, whereClause, whereArgs);
                }


            if (id > 0) {
                Toasty.success(this, "Success!", Toast.LENGTH_SHORT, true).show();
                super.onBackPressed();
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

                if(type_of_photo.equals("2")){
                    Intent intent = new Intent(this,MainPage.class);
                    startActivity(intent);
                }
            }
            Log.d("insIdsavePMAY", String.valueOf(id));

        } catch (Exception e) {
//            Utils.showAlert(CameraScreen.this, "Atleast Capture one Photo");
            //e.printStackTrace();
        }
    }
    public void saveLandImage() {
        dbData.open();

        long id = 0;


        byte[] imageInByte = new byte[0];
        String image_str = "";
        try {
            Bitmap bitmap = ((BitmapDrawable) cameraScreenBinding.imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imageInByte = baos.toByteArray();
            //image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

            ContentValues values = new ContentValues();

            values.put("nursery_land_id", 0);
            values.put("nursery_id",nursery_id);
            values.put("land_type_id", land_type_id);
            values.put("land_address", land_address);
            values.put("land_type_name_en", land_type_name_en);
            values.put("land_type_name_ta", land_type_name_ta);
            values.put(AppConstant.KEY_IMAGE, imageInByte);
            values.put("server_flag", "0");
            values.put("latitude", offlatTextValue.toString());
            values.put("longtitude", offlongTextValue.toString());

            id = db.insert(DBHelper.NURSERY_LAND_SAVE_DETAILS, null, values);

            if (id > 0) {
                Toasty.success(this, "Success!", Toast.LENGTH_SHORT, true).show();
                super.onBackPressed();
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

            }
            Log.d("landInsID", String.valueOf(id));

        } catch (Exception e) {
            Utils.showAlert(CameraScreen.this, "Atleast Capture one Photo");
            e.printStackTrace();
        }
    }

    public static float distFrom(Double lat1, Double lng1, Double lat2, Double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);
        Log.d("DistMeter", "" + dist);
        return dist;
    }
    private void captureImage() {
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        }
        else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
            }

            Uri fileUri = CameraUtils.getOutputMediaFileUri(this, file);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
        if (MyLocationListener.latitude > 0) {
            offlatTextValue = MyLocationListener.latitude;
            offlongTextValue = MyLocationListener.longitude;
        }
    }

    public void getLatLong() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setBearingRequired(false);

        //API level 9 and up
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        Integer gpsFreqInMillis = 1000;
        Integer gpsFreqInDistance = 1;

        // permission was granted, yay! Do the
        // location-related task you need to do.
        if (ContextCompat.checkSelfPermission(CameraScreen.this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            //mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
            mlocManager.requestLocationUpdates(gpsFreqInMillis, gpsFreqInDistance, criteria, mlocListener, null);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(CameraScreen.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CameraScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{CAMERA, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                if (ActivityCompat.checkSelfPermission(CameraScreen.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CameraScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CameraScreen.this, new String[]{ACCESS_FINE_LOCATION}, 1);

                }
            }
            if (MyLocationListener.latitude > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CameraUtils.checkPermissions(CameraScreen.this)) {
                        captureImage();
                    } else {
                        requestCameraPermission(MEDIA_TYPE_IMAGE);
                    }
//                            checkPermissionForCamera();
                } else {
                    captureImage();
                }
            } else {
                Utils.showAlert(CameraScreen.this, "Satellite communication not available to get GPS Co-ordination Please Capture Photo in Open Area..");
            }
        } else {
            Utils.showAlert(CameraScreen.this, "GPS is not turned on...");
        }
    }

    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage();
                            } else {
//                                captureVideo();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(CameraScreen.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public void previewCapturedImage() {
        try {
            // hide video preview
            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            cameraScreenBinding.imageViewPreview.setVisibility(View.GONE);
            cameraScreenBinding.imageView.setVisibility(View.VISIBLE);
            image_view_preview.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(imageStoragePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            cameraScreenBinding.imageView.setImageBitmap(rotatedBitmap);
            imageView.setImageBitmap(rotatedBitmap);
            latitude_text.setText(""+offlatTextValue);
            longtitude_text.setText(""+offlongTextValue);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Bitmap photo= (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(photo);
                    image_view_preview.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    latitude_text.setText(""+offlatTextValue);
                    longtitude_text.setText(""+offlongTextValue);

                    cameraScreenBinding.imageViewPreview.setVisibility(View.GONE);
                    cameraScreenBinding.imageView.setVisibility(View.VISIBLE);
                    cameraScreenBinding.imageView.setImageBitmap(photo);
                }
                else {
                    // Refreshing the gallery
                    CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                    // successfully captured the image
                    // display it in image view
                    previewCapturedImage();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // video successfully recorded
                // preview the recorded video
//                previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }



    @Override
    public void OnMyResponse(ServerResponse serverResponse) {

    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public void homePage() {
        Intent intent = new Intent(this, MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Home", "Home");
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void saveImageButtonClick() {
        long batch_primary_id = 0;
        try {
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String currentDateTimeString = df.format(c);
//            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

            ContentValues values = new ContentValues();

            values.put("batch_id", 0);
            values.put("batch_number", 0);
            values.put("created_date", currentDateTimeString);
            values.put("server_flag", "0");
            values.put("is_batch_closed", "");

            batch_primary_id = db.insert(DBHelper.BATCH_DETAILS, null, values);

        } catch (Exception e) {

        }
        if (batch_primary_id>0){
            JSONArray imageJson = new JSONArray();
            long rowInserted = 0;
            int childCount = cameraScreenBinding.cameraLayout.getChildCount();
            int count = 0;
            if (childCount > 0) {
                for (int i = 0; i < childCount; i++) {
                    JSONArray imageArray = new JSONArray();

                    View vv = cameraScreenBinding.cameraLayout.getChildAt(i);
                    imageView = vv.findViewById(R.id.image_view);
                    myEditTextView = vv.findViewById(R.id.description);
                    latitude_text = vv.findViewById(R.id.latitude);
                    longtitude_text = vv.findViewById(R.id.longtitude);


                    if (imageView.getDrawable() != null) {
                        //if(!myEditTextView.getText().toString().equals("")){
                        count = count + 1;
                        byte[] imageInByte = new byte[0];
                        String image_str = "";
                        String description = "";
                        try {
                            description = myEditTextView.getText().toString();
                            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            imageInByte = baos.toByteArray();


                        } catch (Exception e) {
                            Utils.showAlert(CameraScreen.this, getResources().getString(R.string.at_least_capture_one_photo));
                        }


                        ContentValues imageValue = new ContentValues();
                        imageValue.put("batch_primary_id", batch_primary_id);
                        imageValue.put("batch_id", 0);
                        imageValue.put("server_flag", "0");
                        imageValue.put("image", imageInByte);
                        imageValue.put("lattitude", latitude_text.getText().toString());
                        imageValue.put("longtitude", longtitude_text.getText().toString());


                        rowInserted = db.insert(DBHelper.BATCH_IMAGES_DETAILS, null, imageValue);

                        if (count == childCount) {
                            if (rowInserted > 0) {

                                showToast();
                            }

                        }


                    } else {
                        Utils.showAlert(CameraScreen.this, getResources().getString(R.string.please_capture_image));
                    }
                }
            }
        }
        focusOnView(cameraScreenBinding.scrollView);
    }
    public void addImageButtonClick(){
        if(viewArrayList.size() < 3) {
            if (imageView.getDrawable() != null && viewArrayList.size() > 0) {
                updateView(CameraScreen.this, cameraScreenBinding.cameraLayout, "", "");
            } else {
                Utils.showAlert(CameraScreen.this, getResources().getString(R.string.please_capture_image));
            }
        }
        else {
            Utils.showAlert(CameraScreen.this, getResources().getString(R.string.maximum_three_photos));

        }
    }
    private final void focusOnView(final ScrollView your_scrollview) {
        your_scrollview.post(new Runnable() {
            @Override
            public void run() {
                your_scrollview.fullScroll(View.FOCUS_DOWN);

            }
        });
    }

    //Method for update single view based on email or mobile type
    public View updateView(final Activity activity, final LinearLayout emailOrMobileLayout, final String values, final String type) {
        final View hiddenInfo = activity.getLayoutInflater().inflate(R.layout.image_with_description, emailOrMobileLayout, false);
        final ImageView imageView_close = (ImageView) hiddenInfo.findViewById(R.id.imageView_close);
        final LinearLayout description_layout = (LinearLayout) hiddenInfo.findViewById(R.id.description_layout);
        imageView = (ImageView) hiddenInfo.findViewById(R.id.image_view);
        image_view_preview = (ImageView) hiddenInfo.findViewById(R.id.image_view_preview);
        myEditTextView = (EditText) hiddenInfo.findViewById(R.id.description);
        latitude_text = hiddenInfo.findViewById(R.id.latitude);
        longtitude_text = hiddenInfo.findViewById(R.id.longtitude);
        description_layout.setVisibility(View.GONE);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imageView.setVisibility(View.VISIBLE);
                    if (viewArrayList.size() != 1) {
                        ((LinearLayout) hiddenInfo.getParent()).removeView(hiddenInfo);
                        viewArrayList.remove(hiddenInfo);
                    }

                } catch (IndexOutOfBoundsException a) {
                    a.printStackTrace();
                }
            }
        });
        image_view_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLatLong();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLatLong();
            }
        });
        emailOrMobileLayout.addView(hiddenInfo);

        View vv = emailOrMobileLayout.getChildAt(viewArrayList.size());
        EditText myEditTextView1 = (EditText) vv.findViewById(R.id.description);
        //myEditTextView1.setSelection(myEditTextView1.length());
        myEditTextView1.requestFocus();
        viewArrayList.add(hiddenInfo);
        return hiddenInfo;
    }

    @SuppressLint("CheckResult")
    public void showToast(){
        Toasty.success(CameraScreen.this,getResources().getString(R.string.inserted_success),Toasty.LENGTH_SHORT);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void saveGrowthTrackImage() {
        long batch_growth_tracking_primary_id = 0;
        try {


            ContentValues values = new ContentValues();

            values.put("growth_tracking_id", 0);
            values.put("batch_primary_id", batch_primary_id);
            values.put("batch_id", batch_id);
            values.put("entry_date", entry_date);
            values.put("server_flag", "0");

            batch_growth_tracking_primary_id = db.insert(DBHelper.BATCH_GROWTH_TRACKING_DETAILS, null, values);

        } catch (Exception e) {

        }
        if (batch_growth_tracking_primary_id>0){
            JSONArray imageJson = new JSONArray();
            long rowInserted = 0;
            int childCount = cameraScreenBinding.cameraLayout.getChildCount();
            int count = 0;
            if (childCount > 0) {
                for (int i = 0; i < childCount; i++) {
                    JSONArray imageArray = new JSONArray();

                    View vv = cameraScreenBinding.cameraLayout.getChildAt(i);
                    imageView = vv.findViewById(R.id.image_view);
                    myEditTextView = vv.findViewById(R.id.description);
                    latitude_text = vv.findViewById(R.id.latitude);
                    longtitude_text = vv.findViewById(R.id.longtitude);


                    if (imageView.getDrawable() != null) {
                        //if(!myEditTextView.getText().toString().equals("")){
                        count = count + 1;
                        byte[] imageInByte = new byte[0];
                        String image_str = "";
                        String description = "";
                        try {
                            description = myEditTextView.getText().toString();
                            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            imageInByte = baos.toByteArray();


                        } catch (Exception e) {
                            Utils.showAlert(CameraScreen.this, getResources().getString(R.string.at_least_capture_one_photo));
                        }


                        ContentValues imageValue = new ContentValues();
                        imageValue.put("batch_growth_tracking_primary_id", batch_growth_tracking_primary_id);
                        imageValue.put("batch_primary_id", batch_primary_id);
                        imageValue.put("batch_id", batch_id);
                        imageValue.put("server_flag", "0");
                        imageValue.put("entry_date", entry_date);
                        imageValue.put("image", imageInByte);
                        imageValue.put("lattitude", latitude_text.getText().toString());
                        imageValue.put("longtitude", longtitude_text.getText().toString());


                        rowInserted = db.insert(DBHelper.BATCH_GROWTH_TRACKING_PHOTOS_DETAILS, null, imageValue);

                        if (count == childCount) {
                            if (rowInserted > 0) {

                                showToast();
                            }

                        }


                    } else {
                        Utils.showAlert(CameraScreen.this, getResources().getString(R.string.please_capture_image));
                    }
                }
            }
        }
        focusOnView(cameraScreenBinding.scrollView);
    }

}

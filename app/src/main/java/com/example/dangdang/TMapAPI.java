package com.example.dangdang;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class TMapAPI extends AppCompatActivity {
    private int timer = 0;
    private double sum_dist;

    Intent intent;

    private final String TMAP_API_KEY = "l7xx5b4f58fa51244b8c9e74926a8e77b889";
    private TMapData tmapdata = null;
    private String address;

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;
    String sql;
    Cursor cursor;


    TMapView tmap;
    boolean check = false;
    double latitude;
    double longitude;
    TMapPoint lastLocation;
    TMapPolyLine polyLine = new TMapPolyLine();
    ArrayList<TMapPoint> alTMapPoint = new ArrayList<TMapPoint>();

    private boolean isBtnClickStart = false; // Start클릭이먼저 클릭되었는가? 안그러면 hanlder 오류
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    Handler handler;
    Handler time_handler;
    private TextView tv_timer;
    private TextView tv_distance;

    private FrameLayout container;


    final static int Init = 0;
    final static int Run = 1;
    final static int Pause = 2;

    Intent sharingIntent;
    int distance = 0;
    String r_start = "";
    String r_end = "";

    public static Context gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmap);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //화면 켜집 유지

        gps = this;
        L.i("::::T맵 화면 진입..");
        LinearLayout map = (LinearLayout) findViewById(R.id.layout_main);

        //이전 버튼
        Button prevBtnGps = findViewById(R.id.prevBtnGps);
        prevBtnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TmapIntent = new Intent(TMapAPI.this, SubActivity.class);
                startActivity(TmapIntent);
            }
        });

        sharingIntent = new Intent(this, WriteActivity.class);

        // 버튼 선언
        Button btnCurlocation = (Button) findViewById(R.id.btnCurlocation);
        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        Button btnRoad = (Button) findViewById(R.id.btnRoad);
        tmapdata = new TMapData();

        tvHour = (TextView) findViewById(R.id.hoursTxt);
        tvMinute = (TextView) findViewById(R.id.minutesTxt);
        tvSecond = (TextView) findViewById(R.id.secsTxt);

        tmap = new TMapView(this);

        tmap.setSKTMapApiKey(TMAP_API_KEY);
        tmap.setSightVisible(true);
        tmap.setMapType(TMapView.MAPTYPE_STANDARD);
        tmap.setZoomLevel(17);

        map.addView(tmap);

        polyLine.setLineColor(Color.RED);
        polyLine.setOutLineColor(Color.RED);
        polyLine.setLineWidth(3);

        intent = getIntent();
        final int c_num = intent.getIntExtra("c_num", 1);
        final String c_id = intent.getStringExtra("c_id");

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.marker);
        tmap.setIcon(bitmap);

        tmap.setIconVisibility(true);//현재위치로 표시될 아이콘을 표시할지 여부를 설정합니다.

        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_distance = (TextView) findViewById(R.id.tv_distance);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }
        setGps();


        // 내 위치로 이동
        btnCurlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmap.setTrackingMode(true);
                tmap.setCenterPoint(longitude, latitude);
                ReverseGeocoding();

            }
        });

        // 기록 시작
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStopWatch();
                startDistanceCal();

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
                String formatDate = sdfNow.format(date);
                r_start = formatDate;
                tmap.addTMapPolyLine("Path", polyLine);
                check = true;

            }
        });

        // 기록 종료
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeStopWatch();
                stopDistanceCalculate();
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
                String formatDate = sdfNow.format(date);
                r_end = formatDate;

                if (time_handler != null) time_handler.removeMessages(0);
                isBtnClickStart = false;
                check = false;
                View rootView = getWindow().getDecorView();
            }

        });


        //게시판 기록
        btnRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCaptureScreen();
                tmap.removeTMapPolyLine("Path");
                distance = Integer.parseInt(tv_distance.getText().toString());

                helper = new DatabaseOpenHelper(TMapAPI.this, DatabaseOpenHelper.db, null, version);
                database = helper.getWritableDatabase();

                helper.insertRank(database,distance,r_start,r_end,address,c_num,c_id);





                startActivity(sharingIntent);
            }

        });
    }//onCreate end


    //캡쳐 작업==============
    private void getCaptureScreen() {
        container = (FrameLayout) findViewById(R.id.Main_part);
        String now = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        container.buildDrawingCache();
        container.setDrawingCacheEnabled(true);
        Bitmap captureView = container.getDrawingCache();
        FileOutputStream fos;

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + now + "capture.jpeg"; //저장 경로 (String Type 변수)
        File file = new File(path);

        try {
            fos = new FileOutputStream(file);
            captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        container.setDrawingCacheEnabled(false);

        sharingIntent.setType("image/*");
        Uri uri = Uri.parse(path);
        sharingIntent.putExtra("Img", uri.toString()); //이미지를 uri로 전송
        startActivity(sharingIntent);
    }


    //-------------------이동거리 계산작업--------------------
    private DistanceCalManager mDistanceCalManager;

    private void startDistanceCal() {
        //DistanceCalManager 를 통해 이동거리를 계산한다.
        if (mDistanceCalManager == null) mDistanceCalManager = new DistanceCalManager(this);
        mDistanceCalManager.startDistanceCal();
        mDistanceCalManager.setPTCalCulateListener(new PTCalCulateListener() {
            @Override
            public void onCalculate(float distance) {
                tv_distance.setText(getKmFormat(distance));
                sharingIntent.putExtra("distance", tv_distance.getText().toString());



            }
        });
    }

    private String getKmFormat(float distance) {
        return distance >= 1000 ? (String.format(Locale.KOREA, "%.2fkm", distance / 1000)) : (int) distance + "m";
    }

    private void stopDistanceCalculate() {
        //위치정보를 취소할때 호출되는함수이다.
        if (mDistanceCalManager != null) mDistanceCalManager.stopDistanceCal();
        mDistanceCalManager = null;
    }

    //--------------스탑 워치 작업-------------
    private TextView tvHour;
    private TextView tvMinute;
    private TextView tvSecond;
    private StopWatch mStopWatch;
    private Handler mStopWatachHandler = new Handler();
    private Runnable mStopWatchTimeTask = new Runnable() {
        @Override
        public void run() {
            setTime(mStopWatch);
            mStopWatachHandler.postDelayed(this, 50);
        }
    };

    private void removeStopWatch() {
        mStopWatachHandler.removeCallbacks(mStopWatchTimeTask);
    }

    private void startStopWatch() {
        mStopWatch = getRecord();
        mStopWatch.start();
        setTime(mStopWatch);
        mStopWatachHandler.post(mStopWatchTimeTask);
    }

    public void setTime(StopWatch stopWatch) {
        long milliseconds = stopWatch.getElapsedTime();
        int seconds = (int) ((milliseconds / 1000) % 60);
        int minutes = (int) (((milliseconds / 1000) / 60) % 60);
        int hours = (int) (((milliseconds / 1000) / 3600) % 24);

        tvHour.setText("" + (hours < 10 ? "0" + hours : hours));
        tvMinute.setText("" + (minutes < 10 ? "0" + minutes : minutes));
        tvSecond.setText("" + (seconds < 10 ? "0" + seconds : seconds));
        sharingIntent.putExtra("time", tvHour.getText().toString() + ":" + tvMinute.getText().toString() + ":" + tvSecond.getText().toString());
    }

    public StopWatch getRecord() {
        StopWatch stopwatch = new StopWatch();
        StopWatchitem item = new StopWatchitem();
        item.setStartTime(System.currentTimeMillis());
        item.setStopTime(0);
        return (stopwatch);
    }


    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //현재위치의 좌표를 알수있는 부분
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                tmap.setLocationPoint(longitude, latitude); // 현재위치로 표시될 좌표의 위도, 경도를 설정합니다
                Log.d("TmapTest",""+longitude+"."+latitude);
            }
            if (check == true) {
                lastLocation = new TMapPoint(location.getLatitude(), location.getLongitude());
                alTMapPoint.add(new TMapPoint(location.getLatitude(), location.getLongitude()));

                polyLine.addLinePoint(lastLocation);
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

//    public void drawPath() {
//        //ArrayList<TMapPoint> alTMapPoint = new ArrayList<TMapPoint>();
//        alTMapPoint.add(lastLocation);
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setGps();
                } else {
                    Log.d("locationTest", "동의거부함");
                }
                return;
            }

        }
    }

    public void setGps() {
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
    }

    private void ReverseGeocoding() {

        /*try {
            final String address = new TMapData().convertGpsToAddress(latitude, longitude);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TMapAPI.this, address, Toast.LENGTH_SHORT).show();
                    System.out.println("*****리버스 지오코딩 : " + address);
                    sharingIntent.putExtra("address", address);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(TMapAPI.this, "주소 없음", Toast.LENGTH_SHORT).show();
            System.out.println("*****리버스 지오코딩 : 정보 없음");
        }*/

        tmapdata.convertGpsToAddress(latitude, longitude, new TMapData.ConvertGPSToAddressListenerCallback() {
            @Override
            public void onConvertToGPSToAddress(String strAddress) {
                address = strAddress;
            }
        });
        Toast.makeText(getApplicationContext(), "주소 : " + address, Toast.LENGTH_SHORT).show();
        sharingIntent.putExtra("address", address);
    }

    private void showRoute() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cursor != null)
            cursor.close();
    }
}
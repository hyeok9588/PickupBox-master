package app.project.com.pickupbox.Frag;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import app.project.com.pickupbox.Data.LocationExample;
import app.project.com.pickupbox.R;
import app.project.com.pickupbox.Register_Box.AddBox;
import app.project.com.pickupbox.Register_Box.DialogMap;

public class FirstPageFragment3 extends Fragment implements TimePicker.OnTimeChangedListener{
    ViewGroup viewGroup;

    EditText etBoxName, etPrice;
    Button btnAddBox, btnFindDest;
    TimePicker mTimePicker;

    TextView tvShowTime, tvNotice, tvDestination;
    int nHourDay, nMinute;
    String AmPm, BoxName, BoxSize, PickupTime, BoxPrice;
    RadioButton rbS, rbM, rbL;
    RadioGroup radioGroup;
    Double latitude, longitude;
    private Intent intent;

    private CardView cv1, cv2, cv3, cv4;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<LocationExample> locationList;

    private DatabaseReference mDatabase;
    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //viewGroup = (ViewGroup) inflater.inflate(R.layout.firstpage_fragment3,container,false);
        View v =  inflater.inflate(R.layout.firstpage_fragment3, container, false);
        initFind(v);
        dbConnForLocation();

        // 위치 정보 확인을 위해 정의한 메소드 호출
        startLocationService(); //위치 확인 됨.


        mTimePicker.setIs24HourView(false);
        mTimePicker.setOnTimeChangedListener(this);
        radioGroup.setOnCheckedChangeListener(mRadioCheck);
        locationList = new ArrayList<>();

        intent = new Intent(getContext(),DialogMap.class);

       etBoxName.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               cv1.setVisibility(View.VISIBLE);
           }
       });

       radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup group, int checkedId) {
               cv2.setVisibility(View.VISIBLE);
           }
       });

       mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
           @Override
           public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
               cv3.setVisibility(View.VISIBLE);
           }
       });

       etPrice.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               cv4.setVisibility(View.VISIBLE);
           }
       });







        btnFindDest.setOnClickListener(new View.OnClickListener() { //<지도에서 목적지 찾기> 버튼
            @Override
            public void onClick(View v) {

                String sessBoxName = etBoxName.getText().toString().trim(); //상품명
                String sessBoxSize = BoxSize;
                String sessPickupTime = PickupTime;
                String sessBoxPrice = etPrice.getText().toString().trim(); // 가격

                if (sessBoxName.getBytes().length <=0 || sessBoxSize == null || sessPickupTime ==null
                        || sessBoxPrice.getBytes().length <=0){
                    //Toast.makeText(AddBox.this, sessBoxName+"/"+sessBoxSize+"/"+sessBoxPrice+"/"+sessPickupTime, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "다른 항목을 먼저 선택한 후 선택해주세요!", Toast.LENGTH_SHORT).show();



                }else{ //입력이라도 되어있다면
                    String myLatitude = latitude.toString();
                    String myLongitude = longitude.toString();

                    //intent로 데이터 넘기기
                    intent.putExtra("sessBoxName",sessBoxName);
                    intent.putExtra("sessBoxSize",sessBoxSize);
                    intent.putExtra("sessPickupTime",sessPickupTime);
                    intent.putExtra("sessBoxPrice",sessBoxPrice);
                    intent.putExtra("myLatitude",myLatitude);
                    intent.putExtra("myLongitude",myLongitude);


                    startActivity(intent);

                }

            }
        });



        return v;
    }

    public void initFind(View view){
        etBoxName = view.findViewById(R.id.etBoxName);
        etPrice = view.findViewById(R.id.etPrice);
        btnAddBox = view.findViewById(R.id.btnAddBox);
        tvShowTime = view.findViewById(R.id.tvShowTime);
        tvNotice = view.findViewById(R.id.tvNotice);
        mTimePicker = view.findViewById(R.id.timePicker);

        rbS = view.findViewById(R.id.radioButton5);
        rbM = view.findViewById(R.id.radioButton6);
        rbL = view.findViewById(R.id.radioButton7);

        radioGroup = view.findViewById(R.id.radiogroup);

        btnAddBox = view.findViewById(R.id.btnAddBox);
        btnFindDest = (Button)view.findViewById(R.id.btnFindDest);
        tvDestination = view.findViewById(R.id.tvDestination);

        cv1 = view.findViewById(R.id.cardview1);
        cv2 = view.findViewById(R.id.cardview2);
        cv3 = view.findViewById(R.id.cardview3);
        cv4 = view.findViewById(R.id.cardview4);

        cv1.setVisibility(View.INVISIBLE);
        cv2.setVisibility(View.INVISIBLE);
        cv3.setVisibility(View.INVISIBLE);
        cv4.setVisibility(View.INVISIBLE);

    }

    private void dbConnForLocation(){
        /*여기서부터-------------------------------------------------------------------------------------------*/
        database = FirebaseDatabase.getInstance(); //파이어 베이스 데이터베이스 연동
        databaseReference = database.getReference("location");  //db테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//파이어베이스 데이터 받아오는 곳

                locationList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){ //반복문으로 데이터 가져오는 곳
                    LocationExample location = snapshot.getValue(LocationExample.class); //만들어뒀던 article 객체에 데이터 담기
                    locationList.add(location);

                }

                intent.putParcelableArrayListExtra("LocalList2",(ArrayList<LocationExample>) locationList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생시
                Log.e("Frag1", String.valueOf(databaseError.toException())); //에러문 출력
            }
        });
        /*여기까지 리사이클러 뷰를 보여주기 위한 DB연동--------------------------------------------------------------------------------------*/
    }


    /**
     * 위치 정보 확인을 위해 정의한 메소드
     */
    private void startLocationService() {
        // 위치 관리자 객체 참조
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // 위치 정보를 받을 리스너 생성
        FirstPageFragment3.GPSListener gpsListener = new FirstPageFragment3.GPSListener();
        long minTime = 10000;
        float minDistance = 0;

        try {
            // GPS를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 네트워크를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();



                //Toast.makeText(getApplicationContext(), "Last Known Location : " + "Latitude : " + latitude + "\nLongitude:" + longitude, Toast.LENGTH_LONG).show();
            }
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }

        //Toast.makeText(getApplicationContext(), "위치 확인이 시작되었습니다. 로그를 확인하세요.", Toast.LENGTH_SHORT).show();

    }



    /**
     * 리스너 클래스 정의
     */
    private class GPSListener implements LocationListener {
        /**
         * 위치 정보가 확인될 때 자동 호출되는 메소드
         */
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            String msg = "Latitude : "+ latitude + "\nLongitude:"+ longitude;
            Log.i("GPSListener", msg);

            //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }


    RadioGroup.OnCheckedChangeListener mRadioCheck = new RadioGroup.OnCheckedChangeListener() { //라디오버튼으로 상품 크기 구하기/
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (group.getId() == R.id.radiogroup){
                switch (checkedId){
                    case R.id.radioButton5:
                        BoxSize = "Small";
                        break;
                    case R.id.radioButton6:
                        BoxSize = "Medium";
                        break;
                    case R.id.radioButton7:
                        BoxSize = "Large";
                        break;
                }
            }
        }
    };

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        nHourDay = hourOfDay;
        nMinute = minute;

        if (nHourDay >= 12){
            AmPm = "오후";
            nHourDay = nHourDay - 12;
        }else{
            AmPm = "오전";
        }

        PickupTime = AmPm+" "+nHourDay+" "+nMinute;
        tvShowTime.setText(AmPm+" "+nHourDay+"시 "+nMinute+"분 까지 배송을 원합니다.");
        tvNotice.setText("시간은 교통 상황에 따라 10~15분 정도 오차 범위가 있을 수 있습니다.");
    }



}

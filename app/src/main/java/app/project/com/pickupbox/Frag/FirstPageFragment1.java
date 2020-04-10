package app.project.com.pickupbox.Frag;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.project.com.pickupbox.Adapter.SwipeRecyclerViewAdapter;
import app.project.com.pickupbox.Data.UserBoxInfo;
import app.project.com.pickupbox.Main_Page.BoxLocationMap;
import app.project.com.pickupbox.R;



public class FirstPageFragment1 extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager; //리사이클러뷰 사용을 위한 설정

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    //private List<LocationExample> locationList;
    private List<UserBoxInfo> boxList;

    private FloatingActionButton button1;
    private Button btnOBM, btnOBT, btnOBS;
    private ScrollView scrollView;
    Intent intent;

    SliderLayout sliderLayout;


    ViewGroup viewGroup;
    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //viewGroup = (ViewGroup) inflater.inflate(R.layout.firstpage_fragment1,container,false);
        View v =  inflater.inflate(R.layout.firstpage_fragment1, container, false);

        btnOBM = v.findViewById(R.id.orderByMoney);
        btnOBT = v.findViewById(R.id.orderByTime);
        btnOBS = v.findViewById(R.id.orderBySize);

        sliderLayout=v.findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.SLIDE);
        sliderLayout.setScrollTimeInSec(1);

        setSliderViews();

        final PullRefreshLayout loading;
        loading = (PullRefreshLayout)v.findViewById(R.id.swipeRefreshLayout);


        loading.setRefreshStyle(PullRefreshLayout.STYLE_CIRCLES);



        loading.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.setRefreshing(false);
                        dbConn();
                    }
                },100);
            }
        });




        button1 = v.findViewById(R.id.btnGoMap); //버튼 클릭시 frag2로 이동 후 지도를 보여준다.
        intent = new Intent(getContext(),BoxLocationMap.class);

        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView7);

        recyclerView.setHasFixedSize(true); //리사이클러 뷰 기존성능 강황
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        boxList = new ArrayList<>(); //article 객체를 담을 어레이 리스트 ( 어뎁터 쪽으로)

        dbConn();

        adapter = new SwipeRecyclerViewAdapter(getContext(), (ArrayList<UserBoxInfo>) boxList);  //데이터 가져온 거 리사이클러뷰 보여주기 위해 어댑터 설정
        recyclerView.setAdapter(adapter);  //리사이클러뷰에 어뎁터 연결




        button1.setOnClickListener(new View.OnClickListener() { //버튼 클릭시 지도로 넘어감
            @Override
            public void onClick(View v) {

                startActivity(intent);

            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM Log", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("FCM Log", "FCM 토큰 : " + token);
                        //Toast.makeText(PickupMain.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        btnOBM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbConnPlusOrder("BoxPrice");
            }
        });
        btnOBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbConnPlusOrder("PickupTime");
            }
        });
        btnOBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbConnPlusOrder("BoxSize");
            }
        });


        return v;
    }

    private void setSliderViews() {
        for(int i=0;i<=2;i++){

            DefaultSliderView sliderView=new DefaultSliderView(getContext());

            switch (i){
                case 0:
                    sliderView.setImageDrawable(R.drawable.adone);
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.adtwo);
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.adone);
                    break;
            }
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            final int finalI=i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener(){

                public void onSliderClick(SliderView sliderView){
                    //Toast.makeText(getContext(),"slider"+(finalI+1),Toast.LENGTH_SHORT).show();
                }
            });
            sliderLayout.addSliderView(sliderView);
        }
    }


    public void dbConn(){
        /*여기서부터-------------------------------------------------------------------------------------------*/
        database = FirebaseDatabase.getInstance(); //파이어 베이스 데이터베이스 연동
        databaseReference = database.getReference("BoxList");  //db테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//파이어베이스 데이터 받아오는 곳
                boxList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){ //반복문으로 데이터 가져오는 곳
                    UserBoxInfo boxinfo = snapshot.getValue(UserBoxInfo.class); //만들어뒀던 article 객체에 데이터 담기

                    DatabaseReference pushPostRef =  snapshot.getRef();
                    boxinfo.setKeyValue(pushPostRef.getKey());

                    /*-------------------------고유값을 가져오는 곳----------------------------------*/
                    /*DatabaseReference pushPostRef = databaseReference.getRef().push();
                    boxinfo.setKeyValue(pushPostRef.getKey());*/
                    /*----------------------------------------------------------*/

                    boxList.add(boxinfo);
                }

                intent.putParcelableArrayListExtra("boxList",(ArrayList<UserBoxInfo>) boxList); //지도 보여줄때 리스트로 보낼 것.

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생시
                Log.e("Frag1", String.valueOf(databaseError.toException())); //에러문 출력
            }
        });
        /*여기까지 리사이클러 뷰를 보여주기 위한 DB연동--------------------------------------------------------------------------------------*/

    }

    public void dbConnPlusOrder(String order){
        /*여기서부터-------------------------------------------------------------------------------------------*/
        database = FirebaseDatabase.getInstance(); //파이어 베이스 데이터베이스 연동
        databaseReference = database.getReference("BoxList");  //db테이블 연결
        String orders = order ;

        databaseReference.orderByChild(orders).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boxList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){ //반복문으로 데이터 가져오는 곳
                    UserBoxInfo boxinfo = snapshot.getValue(UserBoxInfo.class); //만들어뒀던 article 객체에 데이터 담기

                    DatabaseReference pushPostRef =  snapshot.getRef();
                    boxinfo.setKeyValue(pushPostRef.getKey());

                    /*-------------------------고유값을 가져오는 곳----------------------------------*/
                    /*DatabaseReference pushPostRef = databaseReference.getRef().push();
                    boxinfo.setKeyValue(pushPostRef.getKey());*/
                    /*----------------------------------------------------------*/

                    boxList.add(boxinfo);
                }

                intent.putParcelableArrayListExtra("boxList",(ArrayList<UserBoxInfo>) boxList); //지도 보여줄때 리스트로 보낼 것.

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*여기까지 리사이클러 뷰를 보여주기 위한 DB연동--------------------------------------------------------------------------------------*/

    }




}

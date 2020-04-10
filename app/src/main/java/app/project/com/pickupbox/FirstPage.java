package app.project.com.pickupbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import app.project.com.pickupbox.Frag.FirstPageFragment1;
import app.project.com.pickupbox.Frag.FirstPageFragment2;
import app.project.com.pickupbox.Frag.FirstPageFragment3;
import app.project.com.pickupbox.Frag.FirstPageFragment4;
import app.project.com.pickupbox.Frag.FirstPageFragment5;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FirstPage extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FirstPageFragment1 fragment1;
    FirstPageFragment2 fragment2;
    FirstPageFragment3 fragment3;
    FirstPageFragment4 fragment4;
    FirstPageFragment5 fragment5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        //fragment
        fragment1 = new FirstPageFragment1();
        fragment2 = new FirstPageFragment2();
        fragment3 = new FirstPageFragment3();
        fragment4 = new FirstPageFragment4();
        fragment5 = new FirstPageFragment5();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //제일 처음 띄워줄 뷰를 세팅해줍니다. commit();까지 해줘야 합니다.
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment1).commitAllowingStateLoss();



        //bottomnavigationview의 아이콘을 선택 했을때 원하는 프래그먼트가 띄워질 수 있도록 리스너를 추가합니다.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab1:{
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,fragment1).commitAllowingStateLoss();
                        return true;
                    }

                    case R.id.tab2:{
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,fragment2).commitAllowingStateLoss();
                        return true;
                    }

                    case R.id.tab3:{
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,fragment3).commitAllowingStateLoss();
                        return true;
                    }

                    case R.id.tab4:{
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,fragment4).commitAllowingStateLoss();
                        return true;
                    }

                    case R.id.tab5:{
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,fragment5).commitAllowingStateLoss();
                        return true;
                    }
                }

                return false;
            }
        });



    }
}

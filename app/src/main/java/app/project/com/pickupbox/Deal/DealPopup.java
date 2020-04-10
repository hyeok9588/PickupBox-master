package app.project.com.pickupbox.Deal;

import androidx.appcompat.app.AppCompatActivity;
import app.project.com.pickupbox.Main_Page.PickupMain;
import app.project.com.pickupbox.Pay.PhoneAuth;
import app.project.com.pickupbox.R;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class DealPopup extends AppCompatActivity {

    Button btnYes, btnNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_deal_popup);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        btnYes = (Button) findViewById(R.id.yes);
        btnNo = (Button)findViewById(R.id.no);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PhoneAuth.class);
                Toast.makeText(DealPopup.this, "Yes!!!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PickupMain.class);
                Toast.makeText(DealPopup.this, "No!!!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });



    }
}

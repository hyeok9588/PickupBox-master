package app.project.com.pickupbox.Frag;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import app.project.com.pickupbox.Delivery_Now.MainFrag;
import app.project.com.pickupbox.R;
import app.project.com.pickupbox.User_Management.AddUser;
import app.project.com.pickupbox.User_Management.FindPW;
import app.project.com.pickupbox.User_Management.LoginActivity;
import app.project.com.pickupbox.User_Management.LoginResult;

public class FirstPageFragment5 extends Fragment {




    ViewGroup viewGroup;
    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //viewGroup = (ViewGroup) inflater.inflate(R.layout.firstpage_fragment5,container,false);
        View v =  inflater.inflate(R.layout.firstpage_fragment5, container, false);

    /*    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_view5, LoginActivity.newInstance()).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
*/

        Intent intent = new Intent(getContext(),LoginActivity.class);
        startActivity(intent);


        return v;
    }






}

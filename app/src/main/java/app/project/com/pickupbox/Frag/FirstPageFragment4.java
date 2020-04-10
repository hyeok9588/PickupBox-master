package app.project.com.pickupbox.Frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import app.project.com.pickupbox.Delivery_Now.MainFrag;
import app.project.com.pickupbox.Delivery_Now.TapActivity;
import app.project.com.pickupbox.R;

public class FirstPageFragment4 extends Fragment {
    ViewGroup viewGroup;
    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //viewGroup = (ViewGroup) inflater.inflate(R.layout.firstpage_fragment4,container,false);
        View v =  inflater.inflate(R.layout.firstpage_fragment4, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_view, TapActivity.newInstance()).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.

        return v;
    }

    public void replaceFragment(Fragment fragment) {

    }


}

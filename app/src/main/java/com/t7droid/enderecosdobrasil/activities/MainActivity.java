package com.t7droid.enderecosdobrasil.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.t7droid.enderecosdobrasil.Fragment.SalvosFragment;
import com.t7droid.enderecosdobrasil.Fragment.EnderecosFragment;
import com.t7droid.enderecosdobrasil.Fragment.PesquisarCEPsFragment;
import com.t7droid.enderecosdobrasil.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmartTabLayout smartTabLayout = findViewById(R.id.viewPagerTab);
        ViewPager viewPager = findViewById(R.id.viewPager);

        //Configurar adapter para abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("CEP", PesquisarCEPsFragment.class)
                        .add("Endereços", EnderecosFragment.class)
                        .add("Salvos", SalvosFragment.class)
                        .create()
        );

        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);

    }

}

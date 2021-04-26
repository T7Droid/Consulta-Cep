package com.t7droid.enderecosdobrasil.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.t7droid.enderecosdobrasil.Fragment.SalvosFragment;
import com.t7droid.enderecosdobrasil.Fragment.EnderecosFragment;
import com.t7droid.enderecosdobrasil.Fragment.PesquisarCEPsFragment;
import com.t7droid.enderecosdobrasil.R;

public class MainActivity extends AppCompatActivity {

    private LinearLayout adContainer;
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;
    InstallStateUpdatedListener installStateUpdatedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setHomeAsUpIndicator(R.drawable.ic_pin_branco);
        ab.setDisplayHomeAsUpEnabled(true);

        adContainer = findViewById(R.id.adContainer);

        getSupportActionBar().setTitle("Pesquise pelo CEP ou Endereço");
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        //Realizar ação se o anúncio estiver carregado
        if (false) {
            getSupportActionBar().hide();
            adContainer.setVisibility(View.VISIBLE);
        }

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

    @Override
    public void onStart() {
        super.onStart();
        installStateUpdatedListener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                if (mAppUpdateManager != null) {
                    mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                }
            } else {

            }
        };

        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        mAppUpdateManager.registerListener(installStateUpdatedListener);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(result -> {
            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            result, AppUpdateType.IMMEDIATE, MainActivity.this, RC_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else if (result.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            }
        });
    }

    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.coordinatorLayout_main),
                        "Há uma nova atualização para este App",
                        Snackbar.LENGTH_INDEFINITE
                );
        snackbar.setAction("Instalar", view -> {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.yellow));
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {

            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }
}
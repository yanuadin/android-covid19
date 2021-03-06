package com.covidata.application.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.covidata.application.contract.LoginContract;
import com.covidata.application.databinding.ActivityLoginBinding;
import com.covidata.application.interactor.LoginInteractor;
import com.covidata.application.presenter.LoginPresenter;
import com.covidata.application.util.UtilProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener {
    private LoginContract.Presenter presenter;
    private ActivityLoginBinding binding;
    private boolean isShowPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new LoginPresenter(this, new LoginInteractor(UtilProvider.getSharedPreferencesUtil()));
        initView();
    }

    private void initView(){
        binding.btLogin.setOnClickListener(this);
        binding.ivEyePassword.setOnClickListener(this);
        binding.tvToRegister.setOnClickListener(this);
    }

    @Override
    public void startLoading() {
        binding.btLogin.setEnabled(false);
        binding.progressBarLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void endLoading() {
        binding.progressBarLogin.setVisibility(View.GONE);
        binding.btLogin.setEnabled(true);
    }

    @Override
    public void loginSuccess(String message) {
        makeToast(message);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailed(String message) {
        makeToast(message);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.btLogin.getId()){
            onButtonLoginClick();
        }else if(v.getId() == binding.tvToRegister.getId()){
            onLinkedDaftarClick();
        }else if(v.getId() == binding.ivEyePassword.getId()){
            onButtonShowPasswordClick();
        }
    }

    private void onButtonShowPasswordClick() {
        if(!isShowPassword){
            binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isShowPassword = true;
        }else{
            binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isShowPassword = false;
        }
    }

    private void onLinkedDaftarClick() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void onButtonLoginClick(){
        if(binding.etEmail.getText().toString().trim().length() == 0 && binding.etPassword.getText().toString().trim().length() == 0)
            Toast.makeText(getApplicationContext(), "email & password cannot be null",
                    Toast.LENGTH_SHORT).show();
        else{
            if(binding.etEmail.getText().toString().trim().length() == 0)
                Toast.makeText(getApplicationContext(), "email cannot be null",
                        Toast.LENGTH_SHORT).show();
            if(binding.etPassword.getText().toString().trim().length() == 0)
                Toast.makeText(getApplicationContext(), "password cannot be null",
                        Toast.LENGTH_SHORT).show();
        }

        if(binding.etPassword.getText().toString().trim().length() != 0 && binding.etEmail.getText().toString().trim().length() != 0)
            presenter.login(binding.etEmail.getText().toString(), binding.etPassword.getText().toString());
    }

    private void makeToast(String message){
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}

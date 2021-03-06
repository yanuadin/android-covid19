package com.covidata.application.presenter;

import android.util.Log;
import android.widget.Toast;

import com.covidata.application.api_response.LoginResponse;
import com.covidata.application.api_response.RegisterResponse;
import com.covidata.application.callback.AuthRequestCallback;
import com.covidata.application.callback.RequestCallback;
import com.covidata.application.contract.RegisterContract;
import com.covidata.application.util.BCrypt;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterPresenter implements RegisterContract.Presenter{
    private RegisterContract.View view;
    private RegisterContract.Interactor interactor;

    public RegisterPresenter(RegisterContract.View view, RegisterContract.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void register(String name, String email, String password, String confirmPassword) {
        view.startLoading();
        if (password.equals(confirmPassword)){
            interactor.requestRegister(name, email, bcryptPassword(password), confirmPassword, new AuthRequestCallback<RegisterResponse>() {
                @Override
                public void requestSuccess(String docId) {
                    view.endLoading();
                    interactor.saveToken(docId);
                    view.registerSuccess("You're successfully registered");
                }

                @Override
                public void requestFailed(String errorMessage) {
                    view.endLoading();
                    view.registerFailed(errorMessage);
                }
            });
        }else{
            view.passwordNotMatch();
        }
    }

    @Override
    public String bcryptPassword(String password) {
        String generatedSecuredPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        return generatedSecuredPassword;
    }

}

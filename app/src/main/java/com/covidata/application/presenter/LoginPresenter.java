package com.covidata.application.presenter;

import com.covidata.application.api_response.LoginResponse;
import com.covidata.application.callback.AuthRequestCallback;
import com.covidata.application.callback.RequestCallback;
import com.covidata.application.contract.LoginContract;
import com.covidata.application.interactor.LoginInteractor;
import com.covidata.application.util.BCrypt;
import com.covidata.application.view.LoginActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private LoginContract.Interactor interactor;

    public LoginPresenter(LoginActivity view, LoginInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void login(String username, String password) {
        view.startLoading();

        interactor.requestLogin(username, password, new AuthRequestCallback<LoginResponse>() {
//            @Override
//            public void requestSuccess(LoginResponse data) {
//                view.endLoading();
//                view.loginSuccess(data.message);
//                interactor.saveToken(data.access_token);
//            }

            @Override
            public void requestSuccess(String docId) {
                view.endLoading();
                view.loginSuccess("You're successfully logged in!");
                interactor.saveToken(docId);

            }

            @Override
            public void requestFailed(String errorMessage) {
                view.endLoading();
                view.loginFailed(errorMessage);
            }
        });
    }

    public String bcryptPassword(String password) {
        String generatedSecuredPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        return generatedSecuredPassword;
    }

}

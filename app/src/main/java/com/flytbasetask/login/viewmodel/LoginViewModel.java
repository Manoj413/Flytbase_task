package com.flytbasetask.login.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.flytbasetask.core.AppState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    Application application;
    private FirebaseAuth mAuth;
    private CompositeDisposable disposable;
    private MutableLiveData<String> repoLoadError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<String> resultLiveData = new MutableLiveData<>();
    private MutableLiveData<String> verificatinLiveData = new MutableLiveData<>();


    @Inject
    public LoginViewModel(Application application) {
        this.application = application;
        mAuth = FirebaseAuth.getInstance();
    }



    public LiveData<String> getError() {
        return repoLoadError;
    }
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> fetchResultResponse( Activity activity, String phone){

        sendVerificationCode(phone,activity);
        return resultLiveData;
    }
    public LiveData<String> fetchVerificatinCode(){
        return verificatinLiveData;
    }

    private void sendVerificationCode(String number,Activity activity) {
        loading.postValue(true);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {

            Log.d("onVerificationCompleted", "onVerificationCompleted:" + credential);
            final String code = credential.getSmsCode();

            resultLiveData.postValue(code);
            loading.postValue(false);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w("onVerificationFailed", "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }
            resultLiveData.postValue(null);
            loading.postValue(false);
            repoLoadError.postValue(e.getMessage());

        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {

            verificatinLiveData.postValue(verificationId);
            loading.postValue(false);

        }
    };

}

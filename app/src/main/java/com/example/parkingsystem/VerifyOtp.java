package com.example.parkingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOtp extends AppCompatActivity {

    String whereToGo,whereFrom;
    PinView pinView;

    String fullName, email, password, phoneNumber,Address,licenseP,model,rate,openhr,closehr,slotsno, cardNumber, cardExpiryDate, cardCVV;

    String codeBySystem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        Intent intent = getIntent();
        whereToGo = intent.getStringExtra("whereToGo");
        whereFrom = intent.getStringExtra("whereFrom");

        pinView = findViewById(R.id.pin_view);

        fullName = intent.getStringExtra("fullName");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        phoneNumber = intent.getStringExtra("phoneNumber");
        Address=intent.getStringExtra("Address");
        if(whereFrom.equals("Parking Garages")){
            rate=intent.getStringExtra("Rate");
            openhr=intent.getStringExtra("Openhr");
            closehr=intent.getStringExtra("Closehr");
            slotsno=intent.getStringExtra("Slotno");
            cardNumber = intent.getStringExtra("CardNumber");
            cardExpiryDate = intent.getStringExtra("Card expiry date");
            cardCVV = intent.getStringExtra("Card CVV");
        } else {
            licenseP=intent.getStringExtra("LicensePlate");
            model=intent.getStringExtra("Model");
        }

        sendVerificationCodeToUser(phoneNumber);

    }

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private void sendVerificationCodeToUser(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d("Auto-complete", "onVerificationCompleted:" + credential);

            String code = credential.getSmsCode();
            if (code != null) {
                pinView.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("WrongCredentials", "onVerificationFailed", e);

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("ManualVerification", "onCodeSent:" + s);

            super.onCodeSent(s, token);
            codeBySystem = s;
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ProcessSucceeded", "signInWithCredential:success");

                            if (whereToGo.equals("toSetNewPassword")) {
                                updateOldUsersData();
                            } else if (whereToGo.equals("toSignUpSuccessfull")) {
                                storeNewUsersData();
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.d("ProcessFailed", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    private void storeNewUsersData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference;
        //Create helperclass reference and store data using firebase
        if(whereFrom.equals("Parking Garages")){
            reference = rootNode.getReference("Parking");
            UserHelperClass addNewUser = new UserHelperClass(fullName, email, phoneNumber, password,Address,rate,openhr,closehr,slotsno, cardNumber, cardExpiryDate, cardCVV);
            reference.child(phoneNumber).setValue(addNewUser);

            reference = rootNode.getReference("City").child(Address).child(fullName);
            reference.setValue(Address);

            reference = rootNode.getReference("Parking Garages").child(fullName);
            reference.child("endTime").setValue(Integer.parseInt(closehr));
            reference.child("startTime").setValue(Integer.parseInt(openhr));
            String a = "aaaaaaaaaaaaaaaaaaaaaaaa";
            for(int i = 1; i <= Integer.parseInt(slotsno); i++) {
                reference.child("slots").child(String.valueOf(i)).child("occupiedState").setValue(a);
            }
            startActivity(new Intent(getApplicationContext(), SignUpSuccessfull.class));
            finish();
        }
        else{
            reference = rootNode.getReference("Users");
            UserHelperClass addNewUser = new UserHelperClass(fullName, email, phoneNumber, password,Address,licenseP,model);
            reference.child(phoneNumber).setValue(addNewUser);
            startActivity(new Intent(getApplicationContext(), SignUpSuccessfull.class));
            finish();
        }


    }

    private void updateOldUsersData() {
        Intent intent = new Intent(getApplicationContext(), SetNewPassword.class);
        intent.putExtra("phoneNo", phoneNumber);
        startActivity(intent);
        finish();
    }

    public void callNextScreenFromOTP(View view) {

        String code = pinView.getText().toString();
        if(!code.isEmpty())
            verifyCode(code);

//        if (whereToGo.equals("toSetNewPassword")) {
//            updateOldUsersData();
//        } else if (whereToGo.equals("toSignUpSuccessfull")) {
//            storeNewUsersData();
//        }

    }

}
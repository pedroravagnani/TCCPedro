package com.pedrofonseca.tcc.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthentication {
    private static FirebaseAuth auth;

    public FirebaseAuth getFirebaseAuthentication() {

        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}

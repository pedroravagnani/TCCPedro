package com.pedrofonseca.tcc.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthentication  {
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public FirebaseAuth FirebaseAuthentication() {
        return this.auth;
    }

    public boolean createUser(User u){
        final boolean[] b = new boolean[1];
        auth.createUserWithEmailAndPassword(u.getName(), u.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.i("firebase", "completou");
                    b[0] = true;
                }else{
                    Log.i("firebase", "falhou");
                    b[0] = false;
                }
            }
        });

        return b[0];
    }
}

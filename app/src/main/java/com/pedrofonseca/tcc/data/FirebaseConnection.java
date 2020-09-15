package com.pedrofonseca.tcc.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConnection  {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public DatabaseReference FirebaseConnection() {
        return this.reference;
    }
}

package com.example.secureonemigrate;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface OnGetDataListener {
    void onSuccess(DataSnapshot dataSnapshot);
    void onStart();
    void onFailure(DatabaseError error);

}

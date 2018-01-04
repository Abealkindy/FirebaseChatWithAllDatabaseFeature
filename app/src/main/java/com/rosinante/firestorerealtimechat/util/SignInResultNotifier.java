package com.rosinante.firestorerealtimechat.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.rosinante.firestorerealtimechat.R;

/**
 * Created by Rosinante24 on 04/01/18.
 */

public class SignInResultNotifier implements OnCompleteListener<AuthResult> {
    private Context mContext;

    public SignInResultNotifier(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Toast.makeText(mContext, "Signed In", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Auth Failed", Toast.LENGTH_LONG).show();
        }
    }
}

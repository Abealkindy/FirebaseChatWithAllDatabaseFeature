package com.rosinante.firestorerealtimechat.util

import android.content.Context
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult


/**
 * Created by Rosinante24 on 04/01/18.
 */

class SignInResultNotifier(context: Context) : OnCompleteListener<AuthResult> {
    private val mContext: Context = context.applicationContext

    override fun onComplete(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            Toast.makeText(mContext, "Signed In", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(mContext, "Auth Failed", Toast.LENGTH_LONG).show()
        }
    }
}

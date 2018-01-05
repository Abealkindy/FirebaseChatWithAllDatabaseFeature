package com.rosinante.firestorerealtimechat.holder

import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RotateDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rosinante.firestorerealtimechat.R
import com.rosinante.firestorerealtimechat.chatabstract.AbstractChat

/**
 * Created by Rosinante24 on 22/12/17.
 */

class ChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mNameField: TextView = itemView.findViewById(R.id.text_user)
    private val mTextField: TextView = itemView.findViewById(R.id.text_message)
    private val mLeftArrow: FrameLayout = itemView.findViewById(R.id.left_arrow)
    private val mRightArrow: FrameLayout = itemView.findViewById(R.id.right_arrow)
    private val mMessageContainer: RelativeLayout = itemView.findViewById(R.id.chat_message_container)
    private val mMessage: LinearLayout = itemView.findViewById(R.id.message_buble)
    private val mGreen300: Int = ContextCompat.getColor(itemView.context, R.color.material_green_300)
    private val mGray300: Int = ContextCompat.getColor(itemView.context, R.color.material_gray_300)

    fun bind(chat: AbstractChat) {
        setName(chat.name)
        setText(chat.message)

        val currentUser = FirebaseAuth.getInstance().currentUser
        setIsSender(currentUser != null && chat.uid == currentUser.uid)
    }

    private fun setName(name: String) {
        mNameField.text = name
    }

    private fun setText(text: String) {
        mTextField.text = text
    }

    private fun setIsSender(isSender: Boolean) {
        val color: Int
        if (isSender) {
            color = mGreen300
            mLeftArrow.visibility = View.GONE
            mRightArrow.visibility = View.VISIBLE
            mMessageContainer.gravity = Gravity.END
        } else {
            color = mGray300
            mLeftArrow.visibility = View.VISIBLE
            mRightArrow.visibility = View.GONE
            mMessageContainer.gravity = Gravity.START
        }

        (mMessage.background as GradientDrawable).setColor(color)
        (mLeftArrow.background as RotateDrawable).drawable!!
                .setColorFilter(color, PorterDuff.Mode.SRC)
        (mRightArrow.background as RotateDrawable).drawable!!
                .setColorFilter(color, PorterDuff.Mode.SRC)
    }
}

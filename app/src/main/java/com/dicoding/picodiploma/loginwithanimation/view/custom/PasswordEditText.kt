package com.dicoding.picodiploma.loginwithanimation.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.picodiploma.loginwithanimation.R

class PasswordEditText
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
    ) : AppCompatEditText(context, attrs) {
        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int,
        ) {
            if (s.toString().length < 8) {
                setError(R.string.password_length_error.toString(), null)
            } else {
                error = null
            }
        }
    }

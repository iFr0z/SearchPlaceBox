package ru.ifr0z.searchplacebox.example.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.text.Editable
import android.text.TextWatcher
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView

fun EditText.onTextChanges(imageView: ImageView) {
    addTextChangedListener(
        object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(
                sequence: CharSequence?, start: Int, before: Int, count: Int
            ) {
                if (sequence!!.isNotEmpty()) {
                    imageView.animate().alpha(1.0f).setListener(
                        object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                imageView.visibility = VISIBLE
                            }
                        }
                    )
                } else {
                    imageView.animate().alpha(0.0f).setListener(
                        object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                imageView.visibility = INVISIBLE
                            }
                        }
                    )
                }
            }
        }
    )
}
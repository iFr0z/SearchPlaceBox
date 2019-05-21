package ru.ifr0z.searchplacebox.example.extension

import android.widget.EditText
import android.widget.Toast

fun EditText.onEditorAction(
    action: Int,
    regex: String,
    requestStart: String,
    requestEnd: String,
    runAction: (List<String>?) -> Unit
) {
    this.setOnEditorActionListener { view, actionId, _ ->
        return@setOnEditorActionListener when (actionId) {
            action -> {
                if (view.text.matches(regex.toRegex())) {
                    runAction.invoke(view.text.split(",".toRegex()))
                } else {
                    Toast.makeText(
                        context, "$requestStart '${view.text}' $requestEnd", Toast.LENGTH_SHORT
                    ).show()
                }

                true
            }
            else -> false
        }
    }
}
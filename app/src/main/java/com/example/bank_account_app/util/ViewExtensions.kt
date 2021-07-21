package com.example.bank_account_app.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import java.text.NumberFormat

fun EditText.onChange(et: EditText){
    this.addTextChangedListener((object: TextWatcher{
        var current = ""
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString().isNotBlank()) {
                et.removeTextChangedListener(this)
                val cleanString = s?.replace("""[$,.]""".toRegex(), "") ?: s.toString()
                val parsed = cleanString.filter { it.isDigit() }.toDouble()
                val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
                current = formatted
                et.setText(formatted)
                et.setSelection(formatted.length)
                et.addTextChangedListener(this)
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }))

}

fun <T> NavController.observeSavedState(key: String, lifecycleOwner: LifecycleOwner, onObserve: (T) -> Unit) {
    currentBackStackEntry?.savedStateHandle?.apply {
        getLiveData<T>(key).observe(lifecycleOwner) {
            remove<T>(key)
            onObserve.invoke(it)
        }
    }
}

fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_LONG) {
    context?.let {
        Toast.makeText(it, text, duration).show()
    }
}

fun Fragment.popBackStack(stackName: String? = null, flags: Int = 0) {
    activity?.supportFragmentManager?.popBackStackImmediate(stackName, flags)
}

fun FragmentActivity.replaceFragment(
    fragment: Fragment,
    containerId: Int,
    tag: String = "",
//    animation: FragmentAnimations = FragmentAnimations.NONE,
    addToStack: Boolean = true,
    stackName: String? = null
) {
    supportFragmentManager.commit {
//        setupAnimation(animation)
        replace(containerId, fragment, tag)
        if (addToStack) {
            supportFragmentManager.findFragmentById(containerId)?.let {
                addToBackStack(stackName)
            }
        }
    }
}

fun Fragment.replaceFragment(
    fragment: Fragment,
    containerId: Int,
    tag: String = "",
//    animation: FragmentAnimations = FragmentAnimations.NONE,
    addToStack: Boolean = true,
    stackName: String? = null
) {
    activity?.replaceFragment(
        fragment,
        containerId,
        tag,
//        animation,
        addToStack,
        stackName
    )
}
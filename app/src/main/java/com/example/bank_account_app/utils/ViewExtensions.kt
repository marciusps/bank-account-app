package com.example.bank_account_app.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController

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
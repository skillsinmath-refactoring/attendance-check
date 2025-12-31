package com.refactoring_android.math_skill

import android.content.Context
import com.refactoring_android.math_skill.view.ClassSelectView

object ClassSelectViewManager {
    private var instance: ClassSelectView? = null

    fun getInstance(context: Context): ClassSelectView {
        if (instance == null) {
            instance = ClassSelectView(context)
        }
        return instance!!
    }

    fun clear() {
        instance = null
    }
}
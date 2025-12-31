package com.refactoring_android.math_skill.login

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.refactoring_android.math_skill.data.response.UserInfoResponse
import com.refactoring_android.math_skill.domain.UserInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

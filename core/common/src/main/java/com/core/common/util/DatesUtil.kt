package com.core.common.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toLocalDate(): LocalDate? = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
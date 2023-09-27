package com.feature_home.domain.model


data class FullGuestInfo(
    val start_date: Long?=null,
    val end_date: Long?=null,
    val name:String="",
    val phone: String="",
    val comment: String="",
    val for_night:Int=0,
    val for_all_nights:Int=0,
)
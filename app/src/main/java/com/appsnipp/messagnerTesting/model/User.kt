package com.appsnipp.messagnerTesting.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
 data class User(val uid:String,val name:String,val phone:String,val email:String,val photoUrl:String):Parcelable{
    constructor():this("","","","","")
}
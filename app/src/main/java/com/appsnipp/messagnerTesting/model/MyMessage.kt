package com.appsnipp.messagnerTesting.model

data class MyMessage(val id:String, val message:String, val fromid:String, val toid:String, val timeLamp:Long) {
    constructor():this("","","","",-1)
}

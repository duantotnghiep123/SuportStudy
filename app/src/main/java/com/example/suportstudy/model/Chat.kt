package com.example.suportstudy.model

import com.google.gson.annotations.SerializedName

data class Chat(
    @SerializedName("senderUid")
    var senderUid:String,
    @SerializedName("receiverUid")
    var receiverUid:String,
    @SerializedName("timeSend")
    var timeSend:String,
    @SerializedName("messageType")
    var messageType:String,
    @SerializedName("message")
    var message:String)
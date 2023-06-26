package com.example.wassalniDR.util

interface OnItemClickListner {
    fun onSendMessageClick(id: String, position: Int)
}

interface OnDialogSubmitListener {
    fun onDialogSubmit( id:String,message: String)
}
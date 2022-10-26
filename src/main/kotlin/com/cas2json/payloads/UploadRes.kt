package com.cas2json.payloads


class UploadRes (
    private var status: Boolean,
    private var OutputFileName:String?,
    private var downloadURL:String?,
    private var message:String,
    private var OutPutFileType:String?
) {
    override fun toString(): String {
        return "UploadRes(status=$status, OutputFileName=$OutputFileName, downloadURL=$downloadURL, message='$message', OutPutFileType=$OutPutFileType)"
    }
}
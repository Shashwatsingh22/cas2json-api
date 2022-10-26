package com.cas2json.services

import  org.springframework.web.multipart.MultipartFile;

interface IFileServices{
    fun uploadFile(path:String,file:MultipartFile) : String?;
    fun runProcess(pdfFilePath:String, password:String, pyScript:String) : Boolean;
}
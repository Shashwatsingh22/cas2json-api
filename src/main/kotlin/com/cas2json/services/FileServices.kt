package com.cas2json.services

import org.springframework.stereotype.Service
import  org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

@Service
class FileServices{
    fun uploadFile(path: String, file: MultipartFile): String?{
        //Get the FileName
        var fileName : String?=file.originalFilename;

        //Update FileName
        val randomID: String = UUID.randomUUID().toString();

        if (fileName != null) {
            fileName=randomID+fileName.substring(fileName.lastIndexOf("."))
        }
//        //Add complete Path
        val filePath : String = path+ File.separator+fileName

        //Is that folder exist
        val fold: File = File(path)

        if(!fold.exists())
        {
            fold.mkdir();
        }

        //Copy the File
        try {
            Files.copy(file.inputStream, Paths.get(filePath));
        }
        catch (e: IOException)
        {
            e.printStackTrace();
        }


        return fileName;
    }

    fun runProcess(pdfFilePath: String, password: String, pyScript: String): Boolean {
        try
        {
            var builder:ProcessBuilder = ProcessBuilder("python",pyScript,pdfFilePath,password);
            var process:Process = builder.start();

            //For Printing or Storing Output
//            var reader:BufferedReader = BufferedReader(InputStreamReader(process.getInputStream()))
//
//            var lines:String?=null;
//            lines=reader.readLine();
//            while (lines!=null)
//            {
//                println(lines);
//                lines=reader.readLine();
//            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            return false;
        }
        return true;
    }

}
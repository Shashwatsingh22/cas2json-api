package com.cas2json.controller

import com.cas2json.services.FileServices
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.File
import java.io.IOException
import org.springframework.core.io.Resource


@RestController
@RequestMapping("/")
class Main (
    private  val fileService: FileServices,
    
    @Value("\${project.filePath}")
    private val path:String,

    @Value("\${project.scriptPath}")
    private  val scriptPath:String
){

    @GetMapping("/check")
    fun home() : Map<String, Any?>{
        return mapOf("Status" to true, "message" to "Server Started 👂👂🚀");
    }

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("pdfFile") file:MultipartFile, @RequestParam("password") password:String) : Map<String, Any?> {
        var fileName: String= "";
        var downloadURL:String = "";
        var fileNameN:String = "";
        var output:Map<String,Any?>;
            try {
            fileName = this.fileService.uploadFile(path, file).toString()
            fileNameN=if(fileName.indexOf(".")>0) fileName.substring(0,fileName.lastIndexOf(".")) else ""

                try
                {
                    output=this.fileService.runProcess(path+fileName, password, scriptPath)
                }
                catch (e: Exception)
                {
                    e.printStackTrace();
                    return  mapOf("status" to false, "message" to "Not Able to Parse the Data");
                    //UploadRes(false,null,null ,"Problem in Parsing the Data!",null)
                }
        }
        catch (e:IOException)
        {
            e.printStackTrace();
            //If Exception Happened then
            return mapOf("status" to false, "message" to "Not Able to Upload the File");
            //UploadRes(false,null,null ,"Error in Uploading the File",null)
        }

        if(output["status"] ==false) return mapOf("status" to false, "message" to output["message"]);

        downloadURL=ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(fileNameN+".json").toUriString()
        return mapOf("status" to true, "message" to output["message"],"OutPutFileName" to fileNameN+".json","downloadUrl" to downloadURL);
        //UploadRes(true,fileNameN+".json",downloadURL ,"Successfully Parsed the Data!",contentType)
    }

    @GetMapping("/download/{fileName}")
    fun getFile(@PathVariable fileName:String): ResponseEntity<Resource>
    {
        val file:File= File("output/$fileName")
        val array:ByteArray = file.readBytes()

        val resource:ByteArrayResource= ByteArrayResource(array)

        return ResponseEntity.ok()
              .contentType(MediaType.APPLICATION_JSON)
              .contentLength(resource.contentLength())
              .header(HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment()
                    .filename(file.name)
                    .build().toString())
                    .body(resource);
    }
}
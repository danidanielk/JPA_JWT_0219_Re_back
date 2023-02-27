package com.kim.dani.controller;


import com.kim.dani.dto.BoardUploadDto;
import com.kim.dani.dto.HomeDto;
import com.kim.dani.service.BoardService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {

    private final BoardService boardService;


    //local Post Upload
    @PostMapping("/upload")
    public ResponseEntity Upload(@RequestParam("file") MultipartFile file,
                                 @RequestParam("title") String title,
                                 @RequestParam("content") String content,
                                 HttpServletRequest req) throws IOException {
        boolean upload = boardService.Upload(file, title, content,req);
        if (upload){
            return new ResponseEntity("success", HttpStatus.OK);
        }
        return new ResponseEntity("failed", HttpStatus.BAD_REQUEST);
    }


    //local Post Upload Test
    @PostMapping("/uploadtest")
    public ResponseEntity postUploadTest(@RequestParam("file") MultipartFile file) throws IOException {
       String name = file.getOriginalFilename();
       return new ResponseEntity(name, HttpStatus.BAD_REQUEST);
    }


    //Return Post Paht
    @GetMapping("/home")
    public ResponseEntity home(HttpServletRequest req){
        List<HomeDto> homeDtos = boardService.getBoard(req);
//        if (homeDtos != null){
            return new ResponseEntity(homeDtos,HttpStatus.OK);
//        }
//        return new ResponseEntity("어디서난 에러지",HttpStatus.CONFLICT);
    }

    //S3 Photo Upload Test
    @PostMapping("/PhotoUpload")
    public ResponseEntity photoUploadTest (@RequestParam("file") MultipartFile file,
                                           @RequestParam("title") String title,
                                           @RequestParam("content") String content,
                                           HttpServletRequest req){

        String path = boardService.photoUpload(file);
        return  new ResponseEntity(path,HttpStatus.OK);
    }


    //S3 Post Upload
    @PostMapping("/PostUpload")
    public ResponseEntity postUpload (@RequestParam("file") MultipartFile file,
                                      @RequestParam("title") String title,
                                      @RequestParam("content") String content,
                                      HttpServletRequest req){
        boolean post = boardService.postUpload(file,title,content,req);
        if (post){
            return new ResponseEntity("success", HttpStatus.OK);
        }
        return new ResponseEntity("failed", HttpStatus.BAD_REQUEST);
    }





}

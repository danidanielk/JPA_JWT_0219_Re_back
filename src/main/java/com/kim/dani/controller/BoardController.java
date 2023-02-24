package com.kim.dani.controller;


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



    @PostMapping("/upload")
    public ResponseEntity postUpload(@RequestParam("file") MultipartFile file,@RequestParam("title") String title, @RequestParam("content") String content, HttpServletRequest req) throws IOException {
        boolean upload = boardService.postUpload(file, title, content,req);
        if (upload){
            return new ResponseEntity("success", HttpStatus.OK);
        }
        return new ResponseEntity("falied", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/uploadtest")
    public ResponseEntity postUploadTest(@RequestParam("file") MultipartFile file,@RequestParam("title") String title, @RequestParam("content") String content, HttpServletRequest req) throws IOException {
        UUID uuid= UUID.randomUUID();
        String lastPath = "C:/file/"+uuid+file.getOriginalFilename();
        Path path = Paths.get(lastPath);
        file.transferTo(path);
        return new ResponseEntity("falied", HttpStatus.BAD_REQUEST);
    }



    @GetMapping("/home")
    public ResponseEntity home(HttpServletRequest req){
        List<HomeDto> homeDtos = boardService.getBoard(req);
        if (homeDtos != null){
            return new ResponseEntity(homeDtos,HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }



}

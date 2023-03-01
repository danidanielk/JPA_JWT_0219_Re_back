package com.kim.dani.controller;


import com.kim.dani.dto.BoardUploadDto;
import com.kim.dani.dto.GetAllBoardDto;
import com.kim.dani.dto.GetPostDto;
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


    //로컬에 저장 테스트
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


    //로컬에 이미지 저장 테스트
    @PostMapping("/uploadtest")
    public ResponseEntity postUploadTest(@RequestParam("file") MultipartFile file) throws IOException {
       String name = file.getOriginalFilename();
       return new ResponseEntity(name, HttpStatus.BAD_REQUEST);
    }


    //홈 화면 로그인유저
    @GetMapping("/myhome")
    public ResponseEntity Myhome(HttpServletRequest req){
        List<HomeDto> homeDtos = boardService.getMyBoard(req);
        if (homeDtos != null){
            return new ResponseEntity(homeDtos,HttpStatus.OK);
        }
        return new ResponseEntity("어디서난 에러지",HttpStatus.CONFLICT);
    }


    // 홈화면 유저별
    @GetMapping("/home/{userid}")
    public ResponseEntity home(@PathVariable Long userid){
        List<HomeDto> homeDtos = boardService.getBoard(userid);
        if (homeDtos != null){
            return new ResponseEntity(homeDtos,HttpStatus.OK);
        }
        return new ResponseEntity("어디서난 에러지",HttpStatus.CONFLICT);
    }





    //S3 이미지 업로드 테스트
    @PostMapping("/PhotoUpload")
    public ResponseEntity photoUploadTest (@RequestParam("file") MultipartFile file,
                                           @RequestParam("title") String title,
                                           @RequestParam("content") String content,
                                           HttpServletRequest req){

        String path = boardService.photoUpload(file);
        return  new ResponseEntity(path,HttpStatus.OK);
    }


    //S3 포스트 업로드
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


    //검색화면 모든 사진 가져오기
    @PostMapping("/getallboard")
    public ResponseEntity getAllBoard (@RequestParam(value = "search",required = false) String search){
       List<GetAllBoardDto> allBoardDtos = boardService.getAllBoard(search);
       if (allBoardDtos!= null){
           return new ResponseEntity(allBoardDtos, HttpStatus.OK);
       }
        return new ResponseEntity("error~!", HttpStatus.NOT_FOUND);
    }

    //사진 클릭시 포스트화면
    @GetMapping("/getpost")
    public ResponseEntity getPost(@RequestParam("userid") Long userId,
                                  @RequestParam("boardid")Long boardId
                                  ){
       GetPostDto getPostDto =  boardService.getPost(userId,boardId);
       if (getPostDto != null){
           return new ResponseEntity(getPostDto, HttpStatus.OK);
       }
        return new ResponseEntity("error~!", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/comment")
    public ResponseEntity comment(@RequestParam("comment") String comment,
                                  @RequestParam("userid") Long userId,
                                  @RequestParam("boardid") Long boardId){
        boolean getComment = boardService.comment(comment,userId,boardId);
        if (getComment){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


}

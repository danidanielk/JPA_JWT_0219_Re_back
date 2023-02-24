package com.kim.dani.controller;


import com.kim.dani.dto.JoinDto;
import com.kim.dani.dto.LoginDto;
import com.kim.dani.entity.Users;
import com.kim.dani.jwt.JwtTokenProvider;
import com.kim.dani.jwt.JwtTokenValidator;
import com.kim.dani.repository.MemberRepository;
import com.kim.dani.repository.TestRepository;
import com.kim.dani.service.BoardService;
import com.kim.dani.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/main")
@RequiredArgsConstructor
public class UsersController {


    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenValidator jwtTokenValidator;
    private final UsersService usersService;
    private final BoardService boardService;
    private final TestRepository testRepository;
    private final MemberRepository memberRepository;

@Value("${cloud.aws.credentials.secret-key}")
private String key;

    @GetMapping("/validation")
    public String test2(HttpServletRequest req){

        String email = jwtTokenValidator.jwtGetUserEmail(req);
        return email;
    }

    @PostMapping("/join")
    public ResponseEntity join(@Valid @RequestBody JoinDto joinDto) {
        Users user = usersService.join(joinDto);
        if (user != null) {
            return new ResponseEntity(user, HttpStatus.OK);
        }
        return new ResponseEntity("Email 중복 409",HttpStatus.CONFLICT);
//        throw new RuntimeException("Email 중복");
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginDto loginDto,HttpServletResponse res){


        String result = usersService.login(loginDto, res);
        if(result != null){
            return new ResponseEntity(result, HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }




    @GetMapping("/test1")
    public String test1() {
  return key;

    }





}

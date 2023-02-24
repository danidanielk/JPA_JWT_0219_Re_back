package com.kim.dani.service;

import com.kim.dani.dto.JoinDto;
import com.kim.dani.dto.LoginDto;
import com.kim.dani.entity.Users;
import com.kim.dani.jwt.JwtTokenProvider;
import com.kim.dani.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.catalina.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Users join(JoinDto joinDto){

        List<Users> users= usersRepository.findAll();
        for (Users user1 : users) {
            if(user1.getEmail().equals(joinDto.getEmail())){
                return null;
            }
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(joinDto.getPassword());
        Users user = new Users();

        user.setName(joinDto.getName());
        user.setEmail(joinDto.getEmail());
        user.setPhone(joinDto.getPhone());
        user.setPassword(encodePassword);
        usersRepository.save(user);
        return user;
    }


    public String login(LoginDto loginDto, HttpServletResponse res) {

        Users user = usersRepository.findByemail(loginDto.getEmail());
        if (user == null){
            return null;
        }
        String hashedPassword = user.getPassword();
        String plainPassword = loginDto.getPassword();
        String email = loginDto.getEmail();
        Long userid = user.getId();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean decodePassword = passwordEncoder.matches(plainPassword,hashedPassword);
        if(decodePassword){
           return jwtTokenProvider.setTokenCookie(res, email);

        }else{
            return null;
        }
    }
}

package com.kim.dani.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kim.dani.dto.HomeDto;
import com.kim.dani.dto.JoinDto;
import com.kim.dani.dto.LoginDto;
import com.kim.dani.entity.Users;
import com.kim.dani.jwt.JwtTokenProvider;
import com.kim.dani.jwt.JwtTokenValidator;
import com.kim.dani.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersService {



    @Value("${cloud.aws.s3.bucket.name}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;


    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenValidator jwtTokenValidator;
    private final AmazonS3Client amazonS3Client;
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



    public Users profileUpdate(MultipartFile file,HomeDto homeDtos,
                               HttpServletRequest req) throws IOException {


            String userEmail = jwtTokenValidator.jwtGetUserEmail(req);
            if (userEmail !=null){
                String path = file.getOriginalFilename();
                UUID uuid = UUID.randomUUID();
                String key = "uploads/"+path+uuid;
                String allPath = "https://"+bucket +".s3."+ region + ".amazonaws.com/" + key;
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());
                amazonS3Client.putObject(bucket, key, file.getInputStream(), metadata);
                Users users = usersRepository.findByemail(userEmail);
                users.setNickname(homeDtos.getNickname());
                users.setProfile(allPath);
                users.setIntroduce(homeDtos.getIntroduce());
                usersRepository.save(users);
                return users;
            }
            return null;
    }


    public HomeDto getProfile(Long userid){
        if(userid != null){
            Optional<Users> user = usersRepository.findById(userid);
            if(user.isPresent()){
                HomeDto homeDto = new HomeDto();
                homeDto.setNickname(user.get().getNickname());
                homeDto.setProfile(user.get().getProfile());
                homeDto.setIntroduce(user.get().getIntroduce());
                return homeDto;
            }
        }

        return null;

    }


    public HomeDto getMyProfile(HttpServletRequest req){
        String userEmail = jwtTokenValidator.jwtGetUserEmail(req);
        if(userEmail != null){
            Users user2 = usersRepository.findByemail(userEmail);
            if(user2 != null){
                HomeDto homeDto = new HomeDto();
                homeDto.setNickname(user2.getNickname());
                homeDto.setProfile(user2.getProfile());
                homeDto.setIntroduce(user2.getIntroduce());
                return homeDto;
            }
        }

        return null;
    }
}

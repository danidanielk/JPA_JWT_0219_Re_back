package com.kim.dani.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kim.dani.dto.BoardUploadDto;
import com.kim.dani.dto.HomeDto;
import com.kim.dani.entity.Board;
import com.kim.dani.entity.Users;
import com.kim.dani.jwt.JwtTokenValidator;
import com.kim.dani.repository.BoardRepository;
import com.kim.dani.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {


    @Value("${cloud.aws.s3.bucket.name}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;



    private final BoardRepository boardRepository;
    private final JwtTokenValidator jwtTokenValidator;
    private final UsersRepository usersRepository;
    private final AmazonS3Client amazonS3Client;


    public boolean Upload (MultipartFile file, String title, String content, HttpServletRequest req) throws IOException {

        String userEmail = jwtTokenValidator.jwtGetUserEmail(req);
        if(userEmail !=null){
            Users user = usersRepository.findByemail(userEmail);
            UUID uuid= UUID.randomUUID();
            String lastPath = "C:/file/"+uuid+file.getOriginalFilename();
            Path path = Paths.get(lastPath);
            file.transferTo(path);

            Board board = new Board();
            usersRepository.save(user);
            board.setPhoto(lastPath);
            board.setTitle(title);
            board.setContents(content);
            board.setUsers(user);
            boardRepository.save(board);
            return true;
        }
        return false;
    }

    public List<HomeDto> getBoard(HttpServletRequest req){
        String userEmail = jwtTokenValidator.jwtGetUserEmail(req);
        if (userEmail !=null){
            List<Board> boards= boardRepository.findAll();
            List<HomeDto> homeDtos =  new ArrayList<>();
            for (Board board : boards) {
               if(board.getUsers().getEmail().equals(userEmail)){
                  HomeDto homeDto= new HomeDto();
                       homeDto.setPhoto(board.getPhoto());
                       homeDto.setName(board.getUsers().getName());
                       homeDto.setProfile(board.getUsers().getProfile());
                       homeDto.setEmail(board.getUsers().getEmail());
                       homeDto.setNickname(board.getUsers().getNickname());
                       homeDtos.add(homeDto);
               }
            }
            return homeDtos;
        }
        return null;
    }



    public String  photoUpload(MultipartFile file) {

        try {
            //이름 가져와서
            String path = file.getOriginalFilename();
            //이름에 추가할 uuid 만들고
            UUID uuid = UUID.randomUUID();
            //폴더 만들어주고 폭더/이름 의 path 를 만들어준다.
            String key = "uploads/"+path+uuid;
            //S3 에서 나오는 url 과 똑같이 만들어서 반환하려고 만들었다.. 이렇게하는게 맞는지 모르겠다.
            String allPath = "https://"+bucket +".s3."+ region + ".amazonaws.com/" + key;
            //메타데이타 만들어주고 파일 사이즈와 컨텐츠타입을 정의해준다. 컨텐츠타입 안넣으면 url클릭시 download 된다.
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            //이제 잘 섞어주고 업로드 한다.
            amazonS3Client.putObject(bucket, key, file.getInputStream(), metadata);
            return allPath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean postUpload(MultipartFile file,String title,String content,HttpServletRequest req){

        String email = jwtTokenValidator.jwtGetUserEmail(req);
        if (email != null){
            Users user = usersRepository.findByemail(email);
            String path  = photoUpload(file);
            usersRepository.save(user);
            Board board = new Board();
            board.setPhoto(path);
            board.setContents(content);
            board.setTitle(title);
            board.setUsers(user);
            boardRepository.save(board);
            return true;
        }
        return false;
    }


}

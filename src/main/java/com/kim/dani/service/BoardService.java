package com.kim.dani.service;

import com.kim.dani.dto.HomeDto;
import com.kim.dani.entity.Board;
import com.kim.dani.entity.Users;
import com.kim.dani.jwt.JwtTokenValidator;
import com.kim.dani.repository.BoardRepository;
import com.kim.dani.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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




    private final BoardRepository boardRepository;
    private final JwtTokenValidator jwtTokenValidator;
    private final UsersRepository usersRepository;


    public boolean postUpload (MultipartFile file, String title, String content, HttpServletRequest req) throws IOException {

        String userEmail = jwtTokenValidator.jwtGetUserEmail(req);
        if(userEmail !=null){
            Users user = usersRepository.findByemail(userEmail);
            UUID uuid= UUID.randomUUID();
            String lastPath = "C:/file/"+uuid+file.getOriginalFilename();
            Path path = Paths.get(lastPath);
            file.transferTo(path);

            Board board = new Board();
            Users users = new Users();
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

}

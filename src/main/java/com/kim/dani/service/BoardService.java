package com.kim.dani.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kim.dani.dto.GetAllBoardDto;
import com.kim.dani.dto.GetPostDto;
import com.kim.dani.dto.HomeDto;
import com.kim.dani.entity.Board;
import com.kim.dani.entity.Comment;
import com.kim.dani.entity.Users;
import com.kim.dani.jwt.JwtTokenValidator;
import com.kim.dani.repository.BoardRepository;
import com.kim.dani.repository.CommentRepository;
import com.kim.dani.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {


    @Value("${cloud.aws.s3.bucket.name}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;


    private final BoardRepository boardRepository;
    private final JwtTokenValidator jwtTokenValidator;
    private final UsersRepository usersRepository;
    private final AmazonS3Client amazonS3Client;
    private final CommentRepository commentRepository;


    public boolean Upload(MultipartFile file, String title, String content, HttpServletRequest req) throws IOException {

        String userEmail = jwtTokenValidator.jwtGetUserEmail(req);
        if (userEmail != null) {
            Users user = usersRepository.findByemail(userEmail);
            UUID uuid = UUID.randomUUID();
            String lastPath = "C:/file/" + uuid + file.getOriginalFilename();
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


    public List<HomeDto> getMyBoard(HttpServletRequest req) {
        String userEmail = jwtTokenValidator.jwtGetUserEmail(req);
        if (userEmail != null) {
            List<Board> boards = boardRepository.findAll();
            List<HomeDto> homeDtos = new ArrayList<>();
            for (Board board : boards) {
                if (board.getUsers().getEmail().equals(userEmail)) {
                    HomeDto homeDto = new HomeDto();
                    homeDto.setUserId(board.getUsers().getId());
                    homeDto.setBoardId(board.getId());
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



    public List<HomeDto> getBoard(Long userId) {
        Optional<Users> user = usersRepository.findById(userId);
        if(user.isPresent()){
            String userEmail = user.get().getEmail();
        if (userEmail != null) {
            List<Board> boards = boardRepository.findAll();
            List<HomeDto> homeDtos = new ArrayList<>();
            for (Board board : boards) {
                if (board.getUsers().getEmail().equals(userEmail)) {
                    HomeDto homeDto = new HomeDto();
                    homeDto.setUserId(board.getUsers().getId());
                    homeDto.setBoardId(board.getId());
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
        }
        return null;
    }





    public String photoUpload(MultipartFile file) {

        try {
            //이름 가져와서
            String path = file.getOriginalFilename();
            //이름에 추가할 uuid 만들고
            UUID uuid = UUID.randomUUID();
            //폴더 만들어주고 폭더/이름 의 path 를 만들어준다.
            String key = "uploads/" + path + uuid;
            //S3 에서 나오는 url 과 똑같이 만들어서 반환하려고 만들었다.. 이렇게하는게 맞는지 모르겠다.
            String allPath = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
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


    public boolean postUpload(MultipartFile file, String title, String content, HttpServletRequest req) {

        String email = jwtTokenValidator.jwtGetUserEmail(req);
        System.out.println(email+"33333333333333333333333333333333333333333333333");
        if (email != null) {
            Users user = usersRepository.findByemail(email);
            String path = photoUpload(file);
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


    public List<GetAllBoardDto> getAllBoard(String search) {
        if(search != null){
            Users user = usersRepository.findByemail(search);
            List<Board> board = user.getBoard();
            if(board != null){
                List<GetAllBoardDto> allBoardDtos = new ArrayList<>();
                for (Board board1 : board) {
                    GetAllBoardDto getAllBoardDto = new GetAllBoardDto();
                    getAllBoardDto.setPhoto(board1.getPhoto());
                    getAllBoardDto.setBoardId(board1.getId());
                    getAllBoardDto.setUserId(user.getId());
                    allBoardDtos.add(getAllBoardDto);
                }
                return allBoardDtos;
            }
        }
            List<Board> board = boardRepository.findAll();
            if (board != null) {
                List<GetAllBoardDto> getAllBoardDtos = new ArrayList<>();
                for (Board boards : board) {
                    GetAllBoardDto getAllBoardDto = new GetAllBoardDto();
                    getAllBoardDto.setPhoto(boards.getPhoto());
                    getAllBoardDto.setBoardId(boards.getId());
                    getAllBoardDto.setUserId(boards.getUsers().getId());
                    getAllBoardDtos.add(getAllBoardDto);
                }
                return getAllBoardDtos;

        }
        return null;
    }


public boolean comment(String comment,Long userId,Long boardId,HttpServletRequest req) {


    String email = jwtTokenValidator.jwtGetUserEmail(req);
    if (comment != null) {
        String userEmail = jwtTokenValidator.jwtGetUserEmail(req);
        Optional<Users> user = usersRepository.findById(userId);
        Optional<Board> board = boardRepository.findById(boardId);
        if (user.isPresent() && board.isPresent()) {

            Users getUser = usersRepository.findByemail(userEmail);
//            Users getUser = user.get();
            Board getBoard = board.get();
            Comment getComment = new Comment();
            getComment.setComment(comment);
            getComment.setUsers(getUser);
            getComment.setBoard(getBoard);
            usersRepository.save(getUser);
            boardRepository.save(getBoard);
            commentRepository.save(getComment);
            return true;
        }
    }
    return false;

}

    public GetPostDto getPost (Long userId, Long boardId,HttpServletRequest req) {
//        Optional<Board> board3 = boardRepository.findById(boardId);
//        List<Comment> comment1 = board.get().getComment();
        Optional<Users> user = usersRepository.findById(userId);
        String userEmail = user.get().getEmail();
        if (user.isPresent()){
            List<Board> boards = user.get().getBoard();
            for (Board board : boards) {
                if (board.getId() == boardId) {
                    GetPostDto getPostDto = new GetPostDto();
                    getPostDto.setPhoto(board.getPhoto());
                    getPostDto.setContents(board.getContents());
                    getPostDto.setComments(board.getComment());
                    getPostDto.setEmail(userEmail);
                    List<String> commentList= new ArrayList<>();
                    getPostDto.setCommentList(commentList);
                    List<Comment> com = board.getComment();
                    for (Comment comment : com) {
                        String getComment = comment.getComment();
                        String getUserEmail = comment.getUsers().getEmail();
                        String emailPlusComment = getUserEmail + " : " + getComment;
                        commentList.add(emailPlusComment);
                    }
        return getPostDto;
                }
          }
        }
                return null;

    }
}
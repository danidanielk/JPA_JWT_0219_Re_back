package com.kim.dani.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HomeDto {



    private String email;
    private String name;
    private String nickname;
    private String profile;
    private String introduce;
    private String photo;
    private Long boardId;
    private Long userId;

}

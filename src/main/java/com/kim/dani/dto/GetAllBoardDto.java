package com.kim.dani.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetAllBoardDto {

    private String photo;
    private Long userId;
    private Long boardId;

}

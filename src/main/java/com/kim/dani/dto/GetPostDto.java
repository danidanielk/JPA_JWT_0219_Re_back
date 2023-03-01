package com.kim.dani.dto;

import com.kim.dani.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetPostDto {


   private List<Comment> comment;
   private String photo;
   private String contents;
   private String email;
}

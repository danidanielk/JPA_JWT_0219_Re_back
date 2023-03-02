package com.kim.dani.dto;

import com.kim.dani.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class GetPostDto {


   private List<Comment> comments;
   private String photo;
   private String contents;
   private String email;
//   private Map<String,String> commentMap;
//   private List<Map> commentMap;
   private List<String> commentList;

}

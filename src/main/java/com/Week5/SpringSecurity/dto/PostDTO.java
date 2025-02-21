package com.Week5.SpringSecurity.dto;

import com.Week5.SpringSecurity.entities.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    private Long id;
    private String title;
    private String description;
    private User author;
}

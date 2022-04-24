package com.example.demo.payload.model;

import com.example.demo.payload.dto.AuthorDTO;
import lombok.Data;

import java.util.Set;

@Data
public class CreateBookResponseModel {

    private String id;
    private String title;
    private Long publisherId;
    private String numPages;
    private String isbn;
    private Integer year;
    private String description;
    private Set<AuthorDTO> authors;
}

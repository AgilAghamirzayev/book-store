package com.example.demo.payload.dto;

import lombok.Data;

import java.util.Set;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private Long publisherId;
    private String numPages;
    private String isbn;
    private Integer year;
    private String description;
    private Set<AuthorDTO> authors;
}

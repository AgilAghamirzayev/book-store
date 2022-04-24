package com.example.demo.payload.model;

import com.example.demo.payload.dto.AuthorDTO;
import lombok.Data;

import java.util.Set;

@Data
public class CreateBookRequestModel {
    private String title;
    private String numPages;
    private String isbn;
    private Integer year;
    private String description;
    private Set<AuthorDTO> authors;
}

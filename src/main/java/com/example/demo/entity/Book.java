package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Book")
@Table(name = "books")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {
    @Id
    @Positive(message = "Id must be positive value")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Long publisherId;
    private String numPages;
    private String isbn;
    private Integer year;
    private String description;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    )
    private Set<Author> authors = new HashSet<>();
}

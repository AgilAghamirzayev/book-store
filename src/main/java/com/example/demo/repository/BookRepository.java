package com.example.demo.repository;

import com.example.demo.entity.Book;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    PageImpl<Book> findByTitle(String title, Pageable pageable);
    PageImpl<Book> findByPublisherId(Long id, Pageable page);
}

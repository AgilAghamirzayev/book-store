package com.example.demo.service;

import com.example.demo.payload.dto.BookDTO;
import org.springframework.data.domain.Page;

public interface BookService {
    Page<BookDTO> getAllBooks(Integer pageNum, Integer pageSize, String sortBy, String sortDir);

    Page<BookDTO> getBookByTitle(Integer pageNum, Integer pageSize, String sortBy, String sortDir, String title);

    void createBook(BookDTO book);

    BookDTO updateBookInfo(BookDTO book, Long id);

    Page<BookDTO> findByPublishedId(Integer pageNum, Integer pageSize, String sortBy, String sortDir, Long id);
}

package com.example.demo.service.impl;

import com.example.demo.entity.Book;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.mapper.ObjectMapperUtils;
import com.example.demo.payload.dto.BookDTO;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final String CALL_LOG = "Called {} method from BookServiceImpl";
    private static final String END_LOG = "Ended {} method from BookServiceImpl";

    private final BookRepository bookRepository;


    @Override
    public Page<BookDTO> getAllBooks(Integer pageNum, Integer pageSize, String sortBy, String sortDir) {
        log.info(CALL_LOG, "getAllBooks");

        Pageable paging = PageRequest.of(pageNum, pageSize, ascOrDesc(sortDir, sortBy));
        Page<Book> filteredValue = bookRepository.findAll(paging);

        Page<BookDTO> returnValue = filteredValue.map(book ->
                ObjectMapperUtils.map(book, BookDTO.class));

        log.info(END_LOG, "getAllBooks");
        return returnValue;
    }

    @Override
    public Page<BookDTO> getBookByTitle(Integer pageNum, Integer pageSize, String sortBy, String sortDir, String title) {
        log.info(CALL_LOG, "getBookByTitle");
        Pageable paging = PageRequest.of(pageNum, pageSize, ascOrDesc(sortDir, sortBy));
        Page<Book> bookByTitle = bookRepository.findByTitle(title, paging);
        Page<BookDTO> mappedValue = bookByTitle.map(book ->
                ObjectMapperUtils.map(book, BookDTO.class));
        log.info(END_LOG, "getBookByTitle");
        return mappedValue;
    }

    @Override
    public void createBook(BookDTO bookDTO) {
        log.info(CALL_LOG, "createBook");
        Book book = ObjectMapperUtils.map(bookDTO, Book.class);
        Long idFromToken = getIdFromToken();
        book.setPublisherId(idFromToken);
        Book savedBook = bookRepository.save(book);
        log.info("Book created title with {}", savedBook.getTitle());
        log.info(END_LOG, "createBook");
    }

    @Override
    @Transactional
    public BookDTO updateBookInfo(BookDTO bookDTO, Long id) {
        log.info(CALL_LOG, "updateBookInfo");

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        Long jwtId = getIdFromToken();
        if (jwtId.equals(book.getPublisherId())) {
            book.setTitle(bookDTO.getTitle());
            book.setDescription(bookDTO.getDescription());
            book.setIsbn(bookDTO.getIsbn());
            book.setYear(bookDTO.getYear());
            book.setNumPages(bookDTO.getNumPages());

            if (bookDTO.getAuthors() != null) {
                book.getAuthors().forEach(author -> {
                    bookDTO.getAuthors().forEach(newAuthor -> {
                        if (newAuthor.getId().equals(author.getId())) {
                            author.setName(author.getName());
                            author.setLastName(author.getLastName());
                        }
                    });
                });
            }

        } else {
            throw new RuntimeException("Id is not match");
        }

        BookDTO mappedBook = ObjectMapperUtils.map(book, BookDTO.class);
        log.info(END_LOG, "updateBookInfo");
        return mappedBook;
    }

    @Override
    public Page<BookDTO> findByPublishedId(Integer pageNum, Integer pageSize, String sortBy, String sortDir, Long id) {
        log.info(CALL_LOG, "findByPublishedId");
        Pageable paging = PageRequest.of(pageNum, pageSize, ascOrDesc(sortDir, sortBy));

        Page<Book> filteredValue = bookRepository.findByPublisherId(id, paging);

        Page<BookDTO> returnValue = filteredValue.map(book ->
                ObjectMapperUtils.map(book, BookDTO.class));
        log.info(END_LOG, "findByPublishedId");
        return returnValue;
    }

    private Sort ascOrDesc(String sortDir, String sortBy) {
        // if sortDir is null return unsorted
        // if sortDir is asc return ascending
        // if sortDir is desc/other return descending

        return sortDir.equals("asc") ? sortBy == null ? Sort.unsorted() : Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    }


    private Long getIdFromToken() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal().toString());
    }

}

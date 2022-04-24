package com.example.demo.controller;

import com.example.demo.mapper.ObjectMapperUtils;
import com.example.demo.payload.dto.BookDTO;
import com.example.demo.payload.model.CreateBookRequestModel;
import com.example.demo.payload.model.CreateBookResponseModel;
import com.example.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('PUBLISHER')")
    ResponseEntity<Page<CreateBookResponseModel>> getBooksByTitle(@RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 @RequestParam(name = "sortBy", required = false) String sortBy,
                                                                 @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                                                                 @RequestParam(name = "title") String title) {

        Page<BookDTO> books = bookService.getBookByTitle(pageNum, pageSize, sortBy, sortDir,title);

        Page<CreateBookResponseModel> returnValue = books.map(book ->
                ObjectMapperUtils.map(book, CreateBookResponseModel.class));

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @PostMapping
    @PreAuthorize("hasRole('PUBLISHER')")
    ResponseEntity<Void> createBooks(@RequestBody @Valid CreateBookRequestModel bookDetails) {
        BookDTO savingValue = ObjectMapperUtils.map(bookDetails, BookDTO.class);
        bookService.createBook(savingValue);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/id")
    @PreAuthorize("hasRole('PUBLISHER')")
    ResponseEntity<Void> updateBooks(@RequestBody @Valid CreateBookRequestModel bookDetails, @PathVariable("id") Long id) {
        BookDTO savingValue = ObjectMapperUtils.map(bookDetails, BookDTO.class);
        bookService.updateBookInfo(savingValue, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/list")
    @PreAuthorize("hasRole('USER') or hasRole('PUBLISHER')")
    ResponseEntity<Page<CreateBookResponseModel>> getAllBooksPage(@RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
                                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                      @RequestParam(name = "sortBy", required = false) String sortBy,
                                                                      @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir

    ) {

        Page<BookDTO> bookDTOPage = bookService.getAllBooks(pageNum, pageSize, sortBy, sortDir);


        Page<CreateBookResponseModel> returnValue = bookDTOPage
                .map(book -> ObjectMapperUtils.map(book, CreateBookResponseModel.class));

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @GetMapping("/list/{id}")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Page<CreateBookResponseModel>> findByPublishedId(@RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
                                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(name = "sortBy", required = false) String sortBy,
                                                                    @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                                                                    @RequestParam(name = "id") Long id) {

        Page<BookDTO> bookDTOPage =
                bookService.findByPublishedId(pageNum, pageSize, sortBy, sortDir, id);

        Page<CreateBookResponseModel> returnValue = bookDTOPage
                .map(book -> ObjectMapperUtils.map(book, CreateBookResponseModel.class));

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}

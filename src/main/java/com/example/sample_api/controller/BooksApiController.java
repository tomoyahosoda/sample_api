package com.example.sample_api.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.sample_api.domain.Book;
import com.example.sample_api.exception.ValidationException;
import com.example.sample_api.service.BookService;


@RestController
@RequestMapping("/books")
public class BooksApiController {
    private final BookService service;

    public BooksApiController(BookService service) {
        this.service = service;
    }

    @GetMapping("{bookId}")
    public Book getBook(@PathVariable String bookId) {
        Book book = service.find(bookId);
        return book;
    }

    @PostMapping("")
    public ResponseEntity<Void> createBook(
            @Validated @RequestBody Book book, UriComponentsBuilder uriBuilder) {

        List<String> errors = new ArrayList<>();
        if (book.getName() == null || book.getName().isBlank()) {
            errors.add("名前の入力は必須です");
        }
        if (book.getPublishedDate() == null) {
            errors.add("出版日の入力は必須です");
        }
        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Book newBook = new Book();
        newBook.setName(book.getName());
        newBook.setPublishedDate(book.getPublishedDate());

        Book createdBook = service.create(newBook);

        URI resourceUri = uriBuilder.path("books/{bookId}").buildAndExpand(createdBook.getId()).encode().toUri();

        return ResponseEntity.created(resourceUri).build();
    }

    @PutMapping("{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(@PathVariable String bookId,
            @Validated @RequestBody Book requestBook) {
        service.update(bookId, requestBook);
    }

    @DeleteMapping("{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String bookId) {
        service.delete(bookId);
    }

    @GetMapping("")
    public List<Book> searchBooks(@Validated Book requestBook) {
        List<Book> books = service.findAllByCriteria(requestBook);

        return books.stream().map(book -> {
            Book resource = new Book();
            resource.setId(book.getId());
            resource.setName(book.getName());
            resource.setPublishedDate(book.getPublishedDate());
            return resource;
        }).collect(Collectors.toList());
    }

}

package com.example.sample_api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.sample_api.domain.Book;
import com.example.sample_api.exception.BookNotFoundException;
import com.example.sample_api.repository.BookRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public Book find(String bookId) {
        Optional<Book> book = repository.findById(Long.valueOf(bookId));
        if (book.isEmpty()) {
            throw new BookNotFoundException(bookId);
        }
        return book.get();
    }
    
    public Book create(Book book) {
        return repository.save(book);
    }

    public void update(String id, Book responseBook) {
        Book book = repository.findById(Long.valueOf(id)).get();
        book.setName(responseBook.getName());
        book.setPublishedDate(responseBook.getPublishedDate());
        repository.save(book);
    }

    public void delete(String id) {
        repository.deleteById(Long.valueOf(id));
    }

    public List<Book> findAllByCriteria(Book criteria) {
        return repository.findAll().stream()
            .filter(book ->
            (criteria.getName() == null
             || book.getName().contains(criteria.getName())) &&
            (criteria.getPublishedDate() == null
             || book.getPublishedDate().equals(criteria.getPublishedDate())))
             .sorted((o1, o2) ->
              o1.getPublishedDate().compareTo(o2.getPublishedDate()))
              .collect(Collectors.toList());
    }
}

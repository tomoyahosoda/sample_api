package com.example.sample_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sample_api.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    
}

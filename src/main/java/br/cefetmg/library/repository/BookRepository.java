package br.cefetmg.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetmg.library.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    
}

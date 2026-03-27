package br.cefetmg.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetmg.library.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    
}

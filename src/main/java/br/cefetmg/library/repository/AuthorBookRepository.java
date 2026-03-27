package br.cefetmg.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetmg.library.model.AuthorBook;
import br.cefetmg.library.model.AuthorBookId;

public interface AuthorBookRepository extends JpaRepository<AuthorBook, AuthorBookId> {
    
}

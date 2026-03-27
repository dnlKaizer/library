package br.cefetmg.library.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import br.cefetmg.library.model.Author;
import br.cefetmg.library.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {
    
    private final AuthorRepository authorRepository;

    public Author findById(Long id) {
        Objects.requireNonNull(id, "O id do autor não pode ser nulo.");
        return authorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado com id: " + id));
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Author insert(Author author) {
        Objects.requireNonNull(author, "O autor não pode ser nulo.");
        author.setId(null);
        return authorRepository.save(author);
    }

    public Author update(Long id, Author author) {
        Objects.requireNonNull(author, "O autor não pode ser nulo.");
        
        if (!authorRepository.existsById(id))
            throw new EntityNotFoundException("Autor não encontrado com id: " + id);

        author.setId(id);
        return authorRepository.save(author);
    }

    public void deleteById(Long id) {
        Objects.requireNonNull(id, "O id do autor não pode ser nulo.");
        authorRepository.deleteById(id);
    }
    
}

package br.cefetmg.library.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.cefetmg.library.model.Author;
import br.cefetmg.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {
    
    private final AuthorRepository authorRepository;

    public Author findById(Long id) {
        Objects.requireNonNull(id, "O id do autor não pode ser nulo.");
        return authorRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado com id: " + id));
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
        
        Author existingAuthor = this.findById(id);

        existingAuthor.setName(Objects.requireNonNullElse(author.getName(), existingAuthor.getName()));
        existingAuthor.setNationality(Objects.requireNonNullElse(author.getNationality(), existingAuthor.getNationality()));
        existingAuthor.setDescription(Objects.requireNonNullElse(author.getDescription(), existingAuthor.getDescription()));

        return authorRepository.save(existingAuthor);
    }

    public void deleteById(Long id) {
        Objects.requireNonNull(id, "O id do autor não pode ser nulo.");
        if (!authorRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado com id: " + id);
        authorRepository.deleteById(id);
    }
    
}

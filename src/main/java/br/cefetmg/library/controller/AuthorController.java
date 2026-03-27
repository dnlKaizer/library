package br.cefetmg.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.cefetmg.library.model.Author;
import br.cefetmg.library.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authors")
public class AuthorController {
    
    private final AuthorService authorService;

    @GetMapping("/{id}")
    public ResponseEntity<Author> findById(@PathVariable Long id) {
        Author author = authorService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(author);
    }

    @GetMapping("")
    public ResponseEntity<List<Author>> findAll() {
        List<Author> authors = authorService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(authors);
    }

    @PostMapping("")
    public ResponseEntity<Author> insert(@RequestBody @Valid Author author) {
        Author createdAuthor = authorService.insert(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@PathVariable Long id, @RequestBody Author author) {
        Author updatedAuthor = authorService.update(id, author);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        authorService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

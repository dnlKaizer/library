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

import br.cefetmg.library.model.AuthorBook;
import br.cefetmg.library.model.Book;
import br.cefetmg.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {
    
    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @GetMapping("")
    public ResponseEntity<List<Book>> findAll() {
        List<Book> books = bookService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @PostMapping("")
    public ResponseEntity<Book> insert(@RequestBody @Valid Book book) {
        Book createdBook = bookService.insert(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody @Valid Book book) {
        Book updatedBook = bookService.update(id, book);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{bookId}/authors/{authorId}")
    public ResponseEntity<AuthorBook> addAuthorToBook(@PathVariable Long bookId, @PathVariable Long authorId) {
        AuthorBook authorBook = bookService.addAuthorToBook(bookId, authorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorBook);
    }

    @DeleteMapping("/{bookId}/authors/{authorId}")
    public ResponseEntity<Void> removeAuthorFromBook(@PathVariable Long bookId, @PathVariable Long authorId) {
        bookService.removeAuthorFromBook(bookId, authorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

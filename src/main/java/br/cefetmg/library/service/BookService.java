package br.cefetmg.library.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import br.cefetmg.library.model.Book;
import br.cefetmg.library.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
    
    private final BookRepository bookRepository;

    public Book findById(Long id) {
        Objects.requireNonNull(id, "O id do livro não pode ser nulo.");
        return bookRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado com id: " + id));
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book insert(Book book) {
        Objects.requireNonNull(book, "O livro não pode ser nulo.");
        book.setId(null);
        return bookRepository.save(book);
    }

    public Book update(Long id, Book book) {
        Objects.requireNonNull(book, "O livro não pode ser nulo.");

        if (!bookRepository.existsById(id))
            throw new EntityNotFoundException("Livro não encontrado com id: " + id);

        book.setId(id);
        return bookRepository.save(book);
    }

    public void deleteById(Long id) {
        Objects.requireNonNull(id, "O id do livro não pode ser nulo.");
        bookRepository.deleteById(id);
    }

}

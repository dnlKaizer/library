package br.cefetmg.library.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import br.cefetmg.library.model.Author;
import br.cefetmg.library.model.AuthorBook;
import br.cefetmg.library.model.AuthorBookId;
import br.cefetmg.library.model.Book;
import br.cefetmg.library.repository.AuthorBookRepository;
import br.cefetmg.library.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
    
    private final BookRepository bookRepository;
    private final AuthorBookRepository authorBookRepository;
    private final AuthorService authorService;

    public Book findById(Long id) {
        Objects.requireNonNull(id, "O id do livro não pode ser nulo.");
        return bookRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado com id: " + id));
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book insert(Book book) {
        Objects.requireNonNull(book, "O livro não pode ser nulo.");
        
        List<AuthorBook> relationsFromRequest = book.getAuthorBooks();
        
        book.setId(null);
        book.setAuthorBooks(null); 

        Book savedBook = bookRepository.save(book);

        if (relationsFromRequest == null || relationsFromRequest.isEmpty()) return savedBook;
        
        savedBook.setAuthorBooks(new ArrayList<>());

        relationsFromRequest.forEach(relations -> {
            Long authorId = relations.getId().getAuthorId();
            Author author = authorService.findById(authorId);
            
            AuthorBookId relationId = new AuthorBookId(authorId, savedBook.getId());
            AuthorBook authorBook = new AuthorBook(relationId, author, savedBook);
            
            AuthorBook savedAuthorBook = authorBookRepository.save(authorBook);

            savedBook.getAuthorBooks().add(savedAuthorBook);
        });
        
        return savedBook;
    }

    public Book update(Long id, Book book) {
        Objects.requireNonNull(book, "O livro não pode ser nulo.");

        Book existingBook = this.findById(id);

        existingBook.setTitle(Objects.requireNonNullElse(book.getTitle(), existingBook.getTitle()));
        existingBook.setIsbn(Objects.requireNonNullElse(book.getIsbn(), existingBook.getIsbn()));
        existingBook.setPublicationDate(Objects.requireNonNullElse(book.getPublicationDate(), existingBook.getPublicationDate()));
        existingBook.setDescription(Objects.requireNonNullElse(book.getDescription(), existingBook.getDescription()));

        return bookRepository.save(existingBook);
    }

    public void deleteById(Long id) {
        Objects.requireNonNull(id, "O id do livro não pode ser nulo.");
        bookRepository.deleteById(id);
    }

    @Transactional
    public AuthorBook addAuthorToBook(Long bookId, Long authorId) {
        Book book = this.findById(bookId);
        Author author = authorService.findById(authorId);

        AuthorBookId relationId = new AuthorBookId(authorId, bookId);
        if (authorBookRepository.existsById(relationId)) {
            throw new IllegalStateException("A relação entre o autor e o livro já existe.");
        }

        AuthorBook authorBook = new AuthorBook(relationId, author, book);
        return authorBookRepository.save(authorBook);
    }

    @Transactional
    public void removeAuthorFromBook(Long bookId, Long authorId) {
        this.findById(bookId);
        authorService.findById(authorId);

        AuthorBookId relationId = new AuthorBookId(authorId, bookId);
        if (!authorBookRepository.existsById(relationId)) {
            throw new EntityNotFoundException("A relação entre o autor e o livro não existe.");
        }
        authorBookRepository.deleteById(relationId);
    }

}

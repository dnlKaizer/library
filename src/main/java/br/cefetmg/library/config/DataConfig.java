package br.cefetmg.library.config;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import br.cefetmg.library.model.Author;
import br.cefetmg.library.model.Book;
import br.cefetmg.library.repository.AuthorRepository;
import br.cefetmg.library.repository.BookRepository;
import br.cefetmg.library.service.BookService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataConfig implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    @Override
    public void run(String... args) throws Exception {
        if (authorRepository.count() == 0 && bookRepository.count() == 0) {
            
            Author tolkien = Author.builder()
                .name("J.R.R. Tolkien")
                .nationality("Britânico")
                .description("Criador de O Senhor dos Anéis")
                .build();
            
            Author martin = Author.builder()
                .name("George R. R. Martin")
                .nationality("Americano")
                .description("Criador de As Crônicas de Gelo e Fogo")
                .build();

            authorRepository.saveAll(List.of(tolkien, martin));

            Book senhorDosAneis = Book.builder()
                .title("O Senhor dos Anéis: A Sociedade do Anel")
                .isbn("9788533613379")
                .publicationDate(Date.valueOf(LocalDate.of(1954, 7, 29)))
                .description("Um hobbit recebe a missão de destruir um anel mágico.")
                .build();

            Book savedBook = bookService.insert(senhorDosAneis);
            bookService.addAuthorToBook(savedBook.getId(), tolkien.getId());
            
            System.out.println("✅ Dados de teste inseridos com sucesso!");
        }
    }
}
package br.cefetmg.library.config;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.cefetmg.library.model.Author;
import br.cefetmg.library.model.Book;
import br.cefetmg.library.model.security.Role;
import br.cefetmg.library.model.security.User;
import br.cefetmg.library.repository.AuthorRepository;
import br.cefetmg.library.repository.BookRepository;
import br.cefetmg.library.repository.UserRepository;
import br.cefetmg.library.service.BookService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataConfig implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            insertUsers(userRepository);
        }

        if (authorRepository.count() == 0 && bookRepository.count() == 0) {
            insertAuthorsAndBooks(authorRepository, bookRepository);
        }
    }

    private void insertUsers(UserRepository userRepository) {
        User admin = User.builder()
                .name("Administrador")
                .login("admin")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build();

        User viewer = User.builder()
                .name("Leitor")
                .login("viewer")
                .password(passwordEncoder.encode("viewer123"))
                .role(Role.VIEWER)
                .build();

        User librarian = User.builder()
                .name("Bibliotecário")
                .login("librarian")
                .password(passwordEncoder.encode("librarian123"))
                .role(Role.LIBRARIAN)
                .build();

        userRepository.saveAll(List.of(admin, viewer, librarian));
        System.out.println("✅ Usuários de segurança inseridos com sucesso!");
    }

    private void insertAuthorsAndBooks(AuthorRepository authorRepository, BookRepository bookRepository) {
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
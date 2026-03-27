package br.cefetmg.library.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "O título do livro é obrigatório.")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "isbn", length = 13)
    private String isbn;

    @Column(name = "publication_date")
    private Date publicationDate;

    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AuthorBook> authorBooks = new ArrayList<>();

    public List<Author> getAuthors() {
        return authorBooks.stream()
            .map(AuthorBook::getAuthor)
            .toList();
    }

}

package br.cefetmg.library.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "O nome do autor é obrigatório.")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<AuthorBook> authorBooks = new ArrayList<>();

    @JsonIgnoreProperties("authors")
    public List<Book> getBooks() {
        if (authorBooks == null) return List.of();

        return authorBooks.stream()
            .map(AuthorBook::getBook)
            .toList();
    }

}

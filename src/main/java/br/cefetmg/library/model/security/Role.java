package br.cefetmg.library.model.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    ADMIN(List.of(
        "AUTHOR_READ",
        "AUTHOR_WRITE",
        "BOOK_READ",
        "BOOK_WRITE"
    )),
    LIBRARIAN(List.of(
        "AUTHOR_READ",
        "AUTHOR_WRITE",
        "BOOK_READ",
        "BOOK_WRITE"
    )),
    VIEWER(List.of(
        "AUTHOR_READ",
        "BOOK_READ"
    ));

    private final List<String> authorities;

    Role(List<String> authorities) {
        this.authorities = authorities;
    }

    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();
    }
}
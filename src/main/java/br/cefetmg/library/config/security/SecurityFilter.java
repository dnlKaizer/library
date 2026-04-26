package br.cefetmg.library.config.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.cefetmg.library.model.security.User;
import br.cefetmg.library.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenProvider tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Optional<String> token = recoverToken(request);
        if (token.isPresent()) {
            Optional<String> login = tokenService.validateToken(token.get());

            if (login.isEmpty()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalido.");
                return;
            }

            User user = userRepository.findByLogin(login.get()).orElse(null);
            if (user == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario do token nao encontrado.");
                return;
            }

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }

        return Optional.of(authHeader.replace("Bearer ", ""));
    }
}
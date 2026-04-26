package br.cefetmg.library.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.cefetmg.library.config.security.TokenProvider;
import br.cefetmg.library.controller.dto.LoginRequest;
import br.cefetmg.library.controller.dto.LoginResponse;
import br.cefetmg.library.model.security.User;
import br.cefetmg.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenProvider tokenService;

    public LoginResponse login(LoginRequest loginRequest, AuthenticationManager authenticationManager) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);

        User user = (User) authentication.getPrincipal();
        TokenProvider.GeneratedToken generatedToken = tokenService.generateToken(user);

        return new LoginResponse(
            generatedToken.token(),
            generatedToken.expiresAt(),
            user.getLogin(),
            user.getRole().name());
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado para o login: " + login));
    }
}
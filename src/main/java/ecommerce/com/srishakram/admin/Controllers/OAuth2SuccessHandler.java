package ecommerce.com.srishakram.admin.Controllers;

import ecommerce.com.srishakram.admin.Repository.UsersRepository;
import ecommerce.com.srishakram.models.Users;
import ecommerce.com.srishakram.utils.CustomerIdGenerator;
import ecommerce.com.srishakram.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;


import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.oauth2.redirect-success-url:http://localhost:5173/oauth-success}")
    private String redirectSuccessUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, jakarta.servlet.ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        Users user = usersRepository.findByEmail(email)
                .orElseGet(() -> {
                    Users newUser = Users.builder()
                            .email(email)
                            .password(passwordEncoder.encode("GOOGLE_USER"))
                            .role("ROLE_USER")
                            .customerId(CustomerIdGenerator.generate())
                            .build();
                    return usersRepository.save(newUser);
                });

        // Generate JWT including user ID
        String token = jwtUtil.generateTokenWithId(user.getId().toString(), user.getEmail(), user.getRole());

        // Redirect with token + ID
        String redirectUrl = UriComponentsBuilder
                .fromUriString(redirectSuccessUrl)
                .queryParam("token", token)
                .queryParam("customerId", user.getId())
                .queryParam("role", user.getRole())
                .queryParam("email", user.getEmail())
                .build()
                .encode()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}

package backend.apiscart.controller;

import backend.apiscart.configuration.security.JwtTokenProvider;
import backend.apiscart.dao.IUsersDao;
import backend.apiscart.dto.base.AuthRequestDto;
import backend.apiscart.dto.base.AuthResponseDto;
import backend.apiscart.dto.base.RenewTokenRequestDto;
import backend.apiscart.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private IUsersDao usersRepository;

    @PostMapping("/getToken")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequestDto authRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(authRequest.getUsername());

            String token = tokenProvider.generateToken(
                    userDetails.getUsername(),
                    authorities
            );

            return ResponseEntity.ok(new AuthResponseDto(token));

        } catch (Exception e) {
            // Captura cualquier excepción de autenticación
            return ResponseEntity.status(401).body(
                    new ErrorResponse(401, "Usuario o contraseña incorrecto")
            );
        }
    }

    @PostMapping("/renewToken")
    public ResponseEntity<?> renewToken(@RequestBody RenewTokenRequestDto renewTokenRequest) {
        String token = renewTokenRequest.getToken();
        if (tokenProvider.validateToken(token)) {
            String renewedToken = tokenProvider.renewToken(token);
            return ResponseEntity.ok(new AuthResponseDto(renewedToken));
        } else {
            return ResponseEntity.badRequest().body("Token inválido");
        }
    }

    public record ErrorResponse(int status, String message) {}
}

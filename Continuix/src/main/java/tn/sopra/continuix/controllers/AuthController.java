package tn.sopra.continuix.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.sopra.continuix.config.JwtUtil;
import tn.sopra.continuix.entities.Users;
import tn.sopra.continuix.repositories.UserRepository;
import tn.sopra.continuix.request.LoginRequest;
import tn.sopra.continuix.request.ResetPasswordRequest;
import tn.sopra.continuix.response.LoginResponse;
import tn.sopra.continuix.services.BlacklistService;
import tn.sopra.continuix.services.UserDetailsImpl;

import java.time.LocalDateTime;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BlacklistService blacklistService;


    @CrossOrigin("*")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);
        System.out.println("Generated JWT for user: email=" + userDetails.getEmail() + ", token: " + jwt);
        return ResponseEntity.ok(new LoginResponse(jwt, userDetails.getId(), userDetails.getRole().name()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        String token = request.getToken();

        // Vérification que le token n'est pas null ou vide
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token invalide");
        }

        // Recherche de l'utilisateur par le token
        Users users = userRepository.findByResetPasswordToken(token);
        if (users == null) {
            return ResponseEntity.badRequest().body("Token invalide");
        }

        // Vérification de l'expiration du token
        if (users.getTokenExpirationDate() != null && users.getTokenExpirationDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token expiré");
        }

        // Mise à jour du mot de passe
        users.setPassword(passwordEncoder.encode(request.getNewPassword()));
        users.setResetPasswordToken(null);
        users.setTokenExpirationDate(null);
        userRepository.save(users);

        return ResponseEntity.ok("Mot de passe mis à jour avec succès");
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            blacklistService.revokeToken(token); // 🟢 Ajoute bien cet appel ici
            System.out.println("🛑 Token ajouté à la blacklist : " + token);
        }


        return ResponseEntity.ok("Logged out successfully");
    }


}

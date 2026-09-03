package in.dementiaCare.auth.service;

import in.dementiaCare.auth.dto.AuthResponseDTO;
import in.dementiaCare.auth.dto.LoginRequestDTO;
import in.dementiaCare.auth.dto.RegisterRequestDTO;
import in.dementiaCare.auth.entity.User;
import in.dementiaCare.auth.repository.UserRepository;
import in.dementiaCare.auth.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponseDTO register(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // hash before saving
                .trustedPersonName(request.getTrustedPersonName())
                .patientName(request.getPatientName())
                .build();

        User saved = userRepository.save(user);

        String token = jwtUtil.generateToken(saved.getId(), saved.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(saved.getId())
                .patientName(saved.getPatientName())
                .trustedPersonName(saved.getTrustedPersonName())
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getId())
                .patientName(user.getPatientName())
                .trustedPersonName(user.getTrustedPersonName())
                .build();
    }
}

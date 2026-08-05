package com.ecommerce.auth.serviceImpl;

import com.ecommerce.auth.dto.request.LoginRequest;
import com.ecommerce.auth.dto.request.RegisterRequest;
import com.ecommerce.auth.dto.response.AuthResponse;
import com.ecommerce.auth.dto.response.UserResponse;
import com.ecommerce.auth.entity.Role;
import com.ecommerce.auth.entity.User;
import com.ecommerce.auth.enums.RoleType;
import com.ecommerce.auth.exception.ResourceAlreadyExistsException;
import com.ecommerce.auth.mapper.UserMapper;
import com.ecommerce.auth.repository.RoleRepository;
import com.ecommerce.auth.repository.UserRepository;
import com.ecommerce.auth.security.CustomUserDetails;
import com.ecommerce.auth.security.JwtService;
import com.ecommerce.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())){
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        Role customerRole = roleRepository.findByName(RoleType.CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = userMapper.toEntity(request);
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setRole(customerRole);
                user.setEnabled(true);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .build();
    }
}

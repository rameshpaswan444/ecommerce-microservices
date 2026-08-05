package com.ecommerce.auth.config;

import com.ecommerce.auth.entity.Role;
import com.ecommerce.auth.enums.RoleType;
import com.ecommerce.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

        if (!roleRepository.existsByName(RoleType.ADMIN)){
            roleRepository.save(
                    Role.builder()
                            .name(RoleType.ADMIN)
                            .build()
            );
        }

        if (!roleRepository.existsByName(RoleType.CUSTOMER)){
            roleRepository.save(
                    Role.builder()
                            .name(RoleType.CUSTOMER)
                            .build()
            );
        }

    }
}

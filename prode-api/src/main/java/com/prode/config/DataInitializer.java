package com.prode.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.prode.entity.Rol;
import com.prode.enums.RolNombre;
import com.prode.repository.RolRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;

    public DataInitializer(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public void run(String... args) {

        if (rolRepository.findByNombre(RolNombre.ROLE_ADMIN).isEmpty()) {

            Rol admin = new Rol();
            admin.setNombre(RolNombre.ROLE_ADMIN);

            rolRepository.save(admin);
        }

        if (rolRepository.findByNombre(RolNombre.ROLE_USER).isEmpty()) {

            Rol user = new Rol();
            user.setNombre(RolNombre.ROLE_USER);

            rolRepository.save(user);
        }
    }
}

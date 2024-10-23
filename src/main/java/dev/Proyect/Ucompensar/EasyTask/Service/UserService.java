package dev.Proyect.Ucompensar.EasyTask.Service;

import dev.Proyect.Ucompensar.EasyTask.Model.User;
import dev.Proyect.Ucompensar.EasyTask.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // Aquí puedes cifrar la contraseña antes de guardar
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> login(String phoneNumber, String password) {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent() && new BCryptPasswordEncoder().matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }
}

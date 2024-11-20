package dev.Proyect.Ucompensar.EasyTask.Service;

import dev.Proyect.Ucompensar.EasyTask.Model.User;
import dev.Proyect.Ucompensar.EasyTask.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    // Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Obtener todos los usuarios
    }

    // Buscar un usuario por ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id); // Buscar por ID
    }

    // Eliminar un usuario por ID
    public void deleteById(Long id) {
        userRepository.deleteById(id); // Eliminar un usuario por ID
    }

    // Actualizar detalles de un usuario
    public User updateUser(Long id, User userDetails) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Actualizar los campos del usuario
            user.setName(userDetails.getName());
            user.setSecondName(userDetails.getSecondName());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setEmail(userDetails.getEmail());
            user.setImg_Profile(userDetails.getImg_Profile());
            return userRepository.save(user); // Guardar el usuario actualizado
        }
        return null; // Si no se encuentra, retornar null
    }
}

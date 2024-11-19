package dev.Proyect.Ucompensar.EasyTask.Controller;

import dev.Proyect.Ucompensar.EasyTask.Model.User;
import dev.Proyect.Ucompensar.EasyTask.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class userController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User newUser = userService.registerUser(user);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar el usuario: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String phoneNumber = credentials.get("phoneNumber");
        String password = credentials.get("password");

        Optional<User> user = userService.login(phoneNumber, password);
        if (user.isPresent()) {
            // Devuelve un mensaje de éxito en formato JSON
            return ResponseEntity.ok(Map.of("message", "Inicio de sesión exitoso"));
        } else {
            // Devuelve un mensaje de error en formato JSON
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Número de teléfono o contraseña incorrectos."));
        }
    }

}

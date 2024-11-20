package dev.Proyect.Ucompensar.EasyTask.Controller;

import dev.Proyect.Ucompensar.EasyTask.Model.Group;
import dev.Proyect.Ucompensar.EasyTask.Model.Note;
import dev.Proyect.Ucompensar.EasyTask.Model.Role;
import dev.Proyect.Ucompensar.EasyTask.Model.User;
import dev.Proyect.Ucompensar.EasyTask.Repository.GroupRepository;
import dev.Proyect.Ucompensar.EasyTask.Repository.RoleRepository;
import dev.Proyect.Ucompensar.EasyTask.Repository.UserRepository;
import dev.Proyect.Ucompensar.EasyTask.Service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class groupController {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private GroupService groupService;

    // Crear un nuevo grupo
    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestBody Group group, @RequestParam long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (group.getUsers() == null) {
            group.setUsers(new ArrayList<>());
        }

        group.getUsers().add(user); // Añadir el usuario al grupo

        // Sincronizar en el lado del usuario
        if (user.getGroups() == null) {
            user.setGroups(new ArrayList<>());
        }
        user.getGroups().add(group);

        Group savedGroup = groupRepository.save(group);
        return ResponseEntity.ok(savedGroup);
    }

    // Unirse a un grupo por ID
    @PostMapping("/{groupId}/join")
    public ResponseEntity<String> joinGroup(@PathVariable long groupId, @RequestParam long userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!group.getUsers().contains(user)) {
            group.getUsers().add(user);
            user.getGroups().add(group); // Añadir el grupo al usuario para sincronizar la relación bidireccional
            groupRepository.save(group);
            userRepository.save(user); // Asegúrate de guardar también el usuario
            return ResponseEntity.ok("Usuario añadido al grupo.");
        } else {
            return ResponseEntity.badRequest().body("El usuario ya pertenece al grupo.");
        }
    }

    // Asignar un rol a un usuario dentro de un grupo
    @PostMapping("/{groupId}/assign-role")
    public ResponseEntity<String> assignRole(
            @PathVariable long groupId,
            @RequestParam long userId,
            @RequestParam Role.Permission permission) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!group.getUsers().contains(user)) {
            return ResponseEntity.badRequest().body("El usuario no pertenece al grupo.");
        }

        Role role = new Role();
        role.setPermission(permission);
        Role savedRole = roleRepository.save(role);

        group.setRole(savedRole);
        groupRepository.save(group);
        return ResponseEntity.ok("Rol asignado correctamente.");
    }


    @GetMapping("/{groupId}/notes")
    public ResponseEntity<List<Note>> getNotes(@PathVariable long groupId, @RequestParam long userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return ResponseEntity.ok(group.getNotes());
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<User>> getMembers(@PathVariable Long groupId) {
        List<User> users = groupService.getMembersByGroupId(groupId); // Llamamos al servicio para obtener los usuarios
        if (users != null && !users.isEmpty()) {
            return ResponseEntity.ok(users); // Devolver los usuarios si existen
        } else {
            return ResponseEntity.notFound().build(); // Si no se encuentran usuarios, devolver 404
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        // Verificar si el grupo existe
        Group group = groupService.findById(id);
        if (group == null) {
            return ResponseEntity.status(404).body("Grupo no encontrado");
        }

        // Eliminar el grupo
        groupService.deleteById(id);
        return ResponseEntity.ok("Grupo eliminado exitosamente");
    }
}


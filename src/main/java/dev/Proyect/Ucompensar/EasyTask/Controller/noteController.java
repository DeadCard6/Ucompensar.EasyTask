package dev.Proyect.Ucompensar.EasyTask.Controller;

import dev.Proyect.Ucompensar.EasyTask.Model.*;
import dev.Proyect.Ucompensar.EasyTask.Repository.GroupRepository;
import dev.Proyect.Ucompensar.EasyTask.Repository.LocationRepository;
import dev.Proyect.Ucompensar.EasyTask.Repository.NoteRepository;
import dev.Proyect.Ucompensar.EasyTask.Repository.UserRepository;
import dev.Proyect.Ucompensar.EasyTask.Service.GroupService;
import dev.Proyect.Ucompensar.EasyTask.Service.NoteService;
import dev.Proyect.Ucompensar.EasyTask.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class noteController {
    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;


    @PostMapping("/user/{userId}")
    public ResponseEntity<Note> createNote(@PathVariable Long userId, @RequestBody Note note) {
        // Verificar si el usuario existe
        var user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        note.setUser(user.get());

        // Manejar Location
        if (note.getLocation() != null) {
            Location location = note.getLocation();

            if (location.getId() == null) {
                // Nueva ubicación
                location = locationRepository.save(location);
            } else {
                // Ubicación existente
                Optional<Location> existingLocation = locationRepository.findById(location.getId());
                if (existingLocation.isPresent()) {
                    location = existingLocation.get();
                } else {
                    location = locationRepository.save(location);
                }
            }


            note.setLocation(location);
        }

        // Guardar la nota
        Note savedNote = noteService.saveNote(note);

        return ResponseEntity.ok(savedNote);
    }



    // Obtener todas las notas o filtrarlas por usuario o grupo
    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long groupId
    ) {
        List<Note> notes;


        if (userId != null) {
            notes = noteService.findByUserId(userId);
        }

        else if (groupId != null) {
            notes = noteService.findByGroupId(groupId);
        }

        else {
            notes = noteService.findAll();
        }

        return ResponseEntity.ok(notes);
    }

    // Obtener una nota por su ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Note>> getNotesByUser(@PathVariable Long userId) {
        try {
            List<Note> notes = noteService.findByUserId(userId);

            if (notes == null || notes.isEmpty()) {
                return ResponseEntity.noContent().build();  // 204 No Content si no hay notas
            }

            return ResponseEntity.ok(notes);  // 200 OK con las notas del usuario
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);  // 500 Internal Server Error en caso de excepción
        }
    }


    // Actualizar una nota existente
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note updatedNote) {
        Note existingNote = noteService.findById(id);
        if (existingNote == null) {
            return ResponseEntity.notFound().build();
        }


        existingNote.setTitle(updatedNote.getTitle());
        existingNote.setDescription(updatedNote.getDescription());
        existingNote.setDate(updatedNote.getDate());
        existingNote.setHour(updatedNote.getHour());
        existingNote.setImage(updatedNote.getImage());


        if (updatedNote.getUser() != null) {
            User user = userService.findById(updatedNote.getUser().getId()).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body(null);
            }
            existingNote.setUser(user);
        }


        if (updatedNote.getGroup() != null) {
            Group group = groupService.findById(updatedNote.getGroup().getId());
            if (group == null) {
                return ResponseEntity.badRequest().body(null);
            }
            existingNote.setGroup(group);
        }

        Note savedNote = noteService.saveNote(existingNote);
        return ResponseEntity.ok(savedNote);
    }

    // Eliminar una nota por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNoteById(id);
        return ResponseEntity.noContent().build();
    }


    //NOTAS PARA GRUPOOOOOOOOO
    @PostMapping("/{groupId}/notes")
    public ResponseEntity<?> createNote(
            @PathVariable Long groupId,
            @RequestBody Note note,
            @RequestParam Long userId
    ) {
        // Validar existencia del grupo
        Group group = groupService.findById(groupId);
        if (group == null) {
            return ResponseEntity.badRequest().body("El grupo especificado no existe.");
        }

        // Validar existencia del usuario
        User user = userService.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("El usuario especificado no existe.");
        }

        // Validar que el usuario pertenece al grupo
        if (group.getUsers().stream().noneMatch(u -> u.getId() == userId)) {
            return ResponseEntity.status(403).body("El usuario no pertenece al grupo especificado.");
        }

        // Validar que el usuario tiene permiso para crear una nota
        if (group.getRole() == null || group.getRole().getPermission() != Role.Permission.CAN_EDIT) {
            return ResponseEntity.status(403).body("El usuario no tiene permisos para crear una nota en este grupo.");
        }

        // Asociar el grupo y usuario a la nota
        note.setGroup(group);

        // Guardar la nota
        Note savedNote = noteService.saveNote(note);

        return ResponseEntity.ok(savedNote);
    }
}

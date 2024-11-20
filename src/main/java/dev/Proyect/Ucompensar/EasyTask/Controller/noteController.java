package dev.Proyect.Ucompensar.EasyTask.Controller;

import dev.Proyect.Ucompensar.EasyTask.Model.Group;
import dev.Proyect.Ucompensar.EasyTask.Model.Note;
import dev.Proyect.Ucompensar.EasyTask.Model.Role;
import dev.Proyect.Ucompensar.EasyTask.Model.User;
import dev.Proyect.Ucompensar.EasyTask.Repository.GroupRepository;
import dev.Proyect.Ucompensar.EasyTask.Repository.NoteRepository;
import dev.Proyect.Ucompensar.EasyTask.Repository.UserRepository;
import dev.Proyect.Ucompensar.EasyTask.Service.GroupService;
import dev.Proyect.Ucompensar.EasyTask.Service.NoteService;
import dev.Proyect.Ucompensar.EasyTask.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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


    // Crear una nueva nota
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {

        if (note.getUser() == null || userService.findById(note.getUser().getId()).isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Si el usuario no es válido
        }


        if (note.getGroup() != null && groupService.findById(note.getGroup().getId()) == null) {
            return ResponseEntity.badRequest().body(null);
        }

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
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Note note = noteService.findById(id);
        if (note == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(note);
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

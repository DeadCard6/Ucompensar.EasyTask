package dev.Proyect.Ucompensar.EasyTask.Service;

import dev.Proyect.Ucompensar.EasyTask.Model.Location;
import dev.Proyect.Ucompensar.EasyTask.Model.Note;
import dev.Proyect.Ucompensar.EasyTask.Repository.LocationRepository;
import dev.Proyect.Ucompensar.EasyTask.Repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository, LocationRepository locationRepository) {
        this.noteRepository = noteRepository;
        this.locationRepository = locationRepository;
    }

    public List<Note> findByUserId(Long userId) {
        return noteRepository.findByUserId(userId);
    }

    public List<Note> findByGroupId(Long groupId) {
        return noteRepository.findByGroupId(groupId);
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public Note findById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    public Note saveNote(Note note) {
        Location location = note.getLocation();
        if (location != null && location.getId() == null) {
            location = locationRepository.save(location); // Guarda primero la ubicación si no está en la BD.
            note.setLocation(location);
        }

        return noteRepository.save(note); // Luego guarda la nota con la ubicación asociada.
    }

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }


}

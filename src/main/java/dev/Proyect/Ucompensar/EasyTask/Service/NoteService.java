package dev.Proyect.Ucompensar.EasyTask.Service;

import dev.Proyect.Ucompensar.EasyTask.Model.Note;
import dev.Proyect.Ucompensar.EasyTask.Repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

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
        return noteRepository.save(note);
    }

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }

}

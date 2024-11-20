package dev.Proyect.Ucompensar.EasyTask.Repository;

import dev.Proyect.Ucompensar.EasyTask.Model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserId(Long userId); // Buscar notas por ID de usuario

    List<Note> findByGroupId(Long groupId);
}
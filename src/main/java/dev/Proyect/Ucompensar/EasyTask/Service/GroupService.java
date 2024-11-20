package dev.Proyect.Ucompensar.EasyTask.Service;

import dev.Proyect.Ucompensar.EasyTask.Model.Group;
import dev.Proyect.Ucompensar.EasyTask.Model.User;
import dev.Proyect.Ucompensar.EasyTask.Repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository; // Repositorio para manejar la persistencia de los grupos

    // Crear un nuevo grupo
    public Group createGroup(Group group) {
        return groupRepository.save(group); // Guardar el grupo en la base de datos
    }

    // Obtener todos los grupos
    public List<Group> getAllGroups() {
        return groupRepository.findAll(); // Obtener todos los grupos de la base de datos
    }

    // Obtener un grupo por su ID
    public Group findById(Long id) {
        Optional<Group> group = groupRepository.findById(id); // Buscar un grupo por ID
        return group.orElse(null); // Si no se encuentra, devolver null
    }

    // Eliminar un grupo por su ID
    public void deleteById(Long id) {
        groupRepository.deleteById(id); // Eliminar un grupo por ID
    }

    // Actualizar un grupo
    public Group updateGroup(Long id, Group groupDetails) {
        Group group = findById(id);
        if (group != null) {
            group.setName(groupDetails.getName()); // Actualizar nombre del grupo
            // Aquí podrías actualizar más campos si es necesario
            return groupRepository.save(group); // Guardar el grupo actualizado
        }
        return null; // Si el grupo no existe, devolver null
    }
    public List<User> getMembersByGroupId(Long groupId) {
        return groupRepository.findUsersByGroupId(groupId); // Utiliza el repositorio para obtener los miembros
    }

}

package dev.Proyect.Ucompensar.EasyTask.Repository;

import dev.Proyect.Ucompensar.EasyTask.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}

package dev.Proyect.Ucompensar.EasyTask.Repository;

import dev.Proyect.Ucompensar.EasyTask.Model.Group;
import dev.Proyect.Ucompensar.EasyTask.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT g.users FROM Group g WHERE g.id = :groupId")
    List<User> findUsersByGroupId(@Param("groupId") long groupId);

}

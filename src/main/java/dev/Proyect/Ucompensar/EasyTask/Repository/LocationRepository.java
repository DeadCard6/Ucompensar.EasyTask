package dev.Proyect.Ucompensar.EasyTask.Repository;

import dev.Proyect.Ucompensar.EasyTask.Model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}

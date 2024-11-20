package dev.Proyect.Ucompensar.EasyTask.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Roles")

public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Enumerated(EnumType.STRING)
    private Permission permission;

    public enum Permission {
        CAN_EDIT,
        ONLY_VIEW
    }
}

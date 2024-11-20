package dev.Proyect.Ucompensar.EasyTask.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String name;
    private String secondName;
    private String password;
    private String phoneNumber;
    private String email;
    private String img_Profile;

    // Relación muchos a muchos con Group
    @ManyToMany
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_group")
    )
    private List<Group> groups = new ArrayList<>();

    // Usamos @JsonManagedReference aquí para manejar la serialización correctamente
    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Note> notes;
}

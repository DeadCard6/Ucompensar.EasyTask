package dev.Proyect.Ucompensar.EasyTask.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "Groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String name;

    // Relación muchos a muchos con la tabla User a través de UserGroup
    @ManyToMany(mappedBy = "groups", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<User> users = new ArrayList<>();  // Esta es la relación correcta

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "group")
    private List<Note> notes;
}

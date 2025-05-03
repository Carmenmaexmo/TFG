package   com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "foro")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Foro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = true)
    private String descripcion;

    @OneToMany(mappedBy = "foro")
    private List<TemaForo> temas;

    @OneToMany(mappedBy = "foro")
    private List<BloqueoForo> bloqueos;

}

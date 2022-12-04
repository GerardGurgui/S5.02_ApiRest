package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter

@Entity
@Table(name = "jugadores")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;
    private String email;
    private int puntuacion;
    private int victoria;

    @Column(name = "porcentaje_acierto")
    private int acierto;
    private String password;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;


    //Lazy para las peticiones que le pedimos y no todo lo relacionado
    @OneToMany(cascade = CascadeType.ALL,
               fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jugador", referencedColumnName = "id")
    private Set<Launch> launches;


    public Player(){

    }

    public Player(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public void addTirada(Launch launch){

        if (launches == null){
            launches = new HashSet<>();
        }

        launches.add(launch);


    }

}

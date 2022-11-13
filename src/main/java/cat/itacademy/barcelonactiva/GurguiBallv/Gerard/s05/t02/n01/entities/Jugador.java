package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jugadores")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;
    private String email; //supuestamente recibe calificaciones en email, será DTO
    private int puntuacion;
    private int victoria;

    @Column(name = "porcentaje_acierto")
    private int acierto;
    private String password; // PER ENCRIPTAR SI DONA TEMPS

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;


    //Lazy para las peticiones que le pedimos y no todo lo relacionado
    @OneToMany(cascade = CascadeType.ALL,
               fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jugador", referencedColumnName = "id")
    private Set<Tirada> tiradas;


    public Jugador() {
    }

    public Jugador(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    ////

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Set<Tirada> getTiradas() {
        return tiradas;
    }

    public void setTiradas(Set<Tirada> tiradas) {
        this.tiradas = tiradas;
    }

    public int getVictoria() {
        return victoria;
    }

    public int getAcierto() {
        return acierto;
    }

    public void setAcierto(int acierto) {
        this.acierto = acierto;
    }

    public void setVictoria(int victoria) {
        this.victoria = victoria;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    ////
    public void addTirada(Tirada tirada){

        if (tiradas == null){
            tiradas = new HashSet<>();
        }

        tiradas.add(tirada);

//        tirada.setJugador(this);

    }


}

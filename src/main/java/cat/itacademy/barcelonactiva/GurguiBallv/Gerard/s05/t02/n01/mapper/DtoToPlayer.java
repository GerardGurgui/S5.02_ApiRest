package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.mapper;

import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.DTO.JugadorDTO;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Jugador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/*
* Convertir de jugadorDto a JugadorEntidad
* implementamos la interfaz Imapper, recibe un jugadorDto (el que introduce el usuario)
* nos devuelve el jugador que guardaremos en BDD
* @component para poder inyectar en las clases donde necesitamos
* */

@Component
public class DtoToPlayer implements IMapper <JugadorDTO, Jugador>{

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Jugador map(JugadorDTO jugadorDTO) {

        Jugador jugadorEntity = new Jugador();

        if (jugadorDTO.getUsername().isEmpty()){
            jugadorEntity.setUsername("Anónimo");

        } else {
            jugadorEntity.setUsername(jugadorDTO.getUsername());
        }

        jugadorEntity.setEmail(jugadorDTO.getEmail());
        jugadorEntity.setPassword(encoder.encode(jugadorDTO.getPassword()));
        jugadorEntity.setFechaRegistro(LocalDate.now());
        jugadorEntity.setPuntuacion(0);
        jugadorEntity.setVictoria(0);

        return jugadorEntity;
    }




}

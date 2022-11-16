package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.mapper;

import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.DTO.PlayerDTO;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Player;
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
public class DtoToPlayer implements IMapper <PlayerDTO, Player>{

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Player map(PlayerDTO playerDTO) {

        Player playerEntity = new Player();

        if (playerDTO.getUsername().isEmpty()){
            playerEntity.setUsername("Anónimo");

        } else {
            playerEntity.setUsername(playerDTO.getUsername());
        }

        playerEntity.setEmail(playerDTO.getEmail());
        playerEntity.setPassword(encoder.encode(playerDTO.getPassword()));
        playerEntity.setFechaRegistro(LocalDate.now());
        playerEntity.setPuntuacion(0);
        playerEntity.setVictoria(0);

        return playerEntity;
    }




}

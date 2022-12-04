package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.service;

import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.DTO.PlayerDTO;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Player;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Launch;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions.customizedExceptions.ExistentEmailException;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions.customizedExceptions.ExistentUserNameException;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions.customizedExceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.game.GameFunctions;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.mapper.DtoToPlayer;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.repositories.PlayerRepository;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.repositories.LaunchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerService.class);


    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LaunchRepository launchRepository;

    @Autowired
    private DtoToPlayer dtoToPlayer;


    ////CRUD
    //--> CREATE
    public Player createPlayer(PlayerDTO playerDtoNew) {

        //MAPEAR DE DTO A ENTIDAD (COMPROBAR NOMBRE VACÍO)
        Player playerEntity = dtoToPlayer.map(playerDtoNew);

        //COMPROBAR SI YA EXISTE ESE JUGADOR O NO
        boolean existe = playerRepository.existsByUsername(playerEntity.getUsername());

        if (existe && !playerEntity.getUsername().equalsIgnoreCase("Anónimo")){

            throw new ExistentUserNameException(HttpStatus.FOUND,"This user name is already taken");

        }

        log.info("Jugador creado correctamente");

        return playerRepository.save(playerEntity);

    }

    //--> READ
    public List<Player> findAllPlayers() {

        return playerRepository.findAll();

    }

    public Player getOne(Long id) {

        Optional<Player> jugadorOpt = playerRepository.findById(id);


        if (jugadorOpt.isEmpty()) {

            throw new PlayerNotFoundException(HttpStatus.NOT_FOUND,"No existe el jugador con id " + id);

        }

        return jugadorOpt.get();
    }


    //--> UPDATE

    public Player update(PlayerDTO playerDTO, Long id) {

        if (playerRepository.existsByUsername(playerDTO.getUsername())){

            throw new ExistentUserNameException(HttpStatus.FOUND,"El nombre del jugador ya existe");
        }

        if (playerRepository.existsByEmail(playerDTO.getEmail())){

            throw new ExistentEmailException(HttpStatus.FOUND,"El email ya existe");
        }

        if (!playerRepository.existsById(id)) {

            throw new PlayerNotFoundException(HttpStatus.NOT_FOUND,"El jugador no existe");
        }

        Optional<Player> jugadorOpt = playerRepository.findById(id);

        //ACTUALIZAMOS LOS ATRIBUTOS QUE SE PUEDEN INTRODUCIR EL USUARIO
        jugadorOpt.get().setUsername(playerDTO.getUsername());
//        //EN PRINCIPI NOMES NOM
//        jugadorOpt.get().setEmail(jugadorDTO.getEmail());


        return playerRepository.save(jugadorOpt.get());

    }


    //FALTAAAAA
    //--> DELETE TIRADAS 1 JUGADOR
    public void deleteTiradas(Long id) {

        Optional<Player> jugadorOpt = playerRepository.findById(id);

        if (jugadorOpt.isEmpty()) {

            throw new PlayerNotFoundException(HttpStatus.NOT_FOUND,"No existe el jugador con id " + id);

        }

        Set<Launch> launches = jugadorOpt.get().getLaunches();

        for (Launch launch : launches) {

            launchRepository.delete(launch);

        }

        jugadorOpt.get().getLaunches().clear();

        playerRepository.save(jugadorOpt.get());


        if (jugadorOpt.get().getLaunches().isEmpty()) {
            log.warn("Se han eliminado correctamente las tiradas del jugador");
        }

    }


    ////FUNCIONALIDADES JUEGO

    //INICIO JUEGO

    //REVISAR TIRAR DADOS FUERA DEL SERIVICO

    //TIRAR DADOS - REGISTRO TIRADAS - PORCENTAJE
    public Player realizarTirada(Long id) {

        Optional<Player> jugadorOpt = playerRepository.findById(id);

        //CAMBIAR POR EXCEPTION
        if (jugadorOpt.isEmpty()) {

            throw new PlayerNotFoundException(HttpStatus.NOT_FOUND,"El jugador no existe");
        }

        Player player = jugadorOpt.get();

        //TIRA DADOS
        Launch launch = GameFunctions.tirarDados();

        //COMPROBACIONES PUNTUACION
        comprobarTirada(player, launch);

        //ASIGNAR TIRADA Y PORCENTAJES
        asignarTirada(player, launch);

        return player;
    }

    public void comprobarTirada(Player player, Launch launch) {


        //COMPRUEBA SUMA DE LA TIRADA
        boolean ganadorRonda = GameFunctions.comprobarTirada(launch.getResultadoTirada());


        //COMPRUEBA TOTAL RONDAS GANADAS
        if (ganadorRonda) {

            boolean ganadorPartida = GameFunctions.sumarPuntuacionRonda(player);
            player.setAcierto(100);

            if (ganadorPartida) {
                player.setVictoria(1);
            }

        } else {
            player.setAcierto(0);
        }

    }

    public void asignarTirada(Player player, Launch launch) {

        player.addTirada(launch);

        int porcentaje = GameFunctions.calcularPorcentajeJugador(player);
        player.setAcierto(porcentaje);

        launchRepository.save(launch);

    }


    //// PORCENTAJES
    public Map<String, Integer> porcentajeJugadores() {

        List<Player> jugadores = findAllPlayers();

        return GameFunctions.calcularPorcentajeJugadores(jugadores);

    }


    public int porcentajeMediaTotal() {

        //TOTAL TIRADAS TOTS ELS JUGADORS * 100 / NUM JUGADORS
        List<Player> jugadores = findAllPlayers();

        return GameFunctions.calcularPorcentajeMedio(jugadores);

    }


    public Map<String, Integer> porcentajeJugadorLoser() {

        List<Player> jugadores = findAllPlayers();

        return GameFunctions.calcularPorcentajeLoser(jugadores);

    }

    public Map<String, Integer> porcentajeJugadorWinner() {

        List<Player> jugadores = findAllPlayers();

        return GameFunctions.calcularPorcentajeWinner(jugadores);

    }

}

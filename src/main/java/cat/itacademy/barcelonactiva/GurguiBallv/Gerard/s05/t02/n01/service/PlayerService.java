package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.service;

import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.DTO.PlayerDTO;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Player;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Launch;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions.ExistentEmailException;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions.ExistentUserNameException;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions.IdPlayerException;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.game.GameFunctions;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.mapper.DtoToPlayer;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.repositories.PlayerRepository;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.repositories.LaunchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerService.class);


    //INYECCIONES POR CONSTRUCTOR, FINAL POR INMUTABLE??
    private final PlayerRepository playerRepository;

    private final LaunchRepository launchRepository;

    private final DtoToPlayer dtoToPlayer;


    public PlayerService(PlayerRepository playerRepository, LaunchRepository launchRepository,
                         DtoToPlayer dtoToPlayer) {
        this.playerRepository = playerRepository;
        this.launchRepository = launchRepository;
        this.dtoToPlayer = dtoToPlayer;
    }


    ////CRUD
    //--> CREATE
    public Player createPlayer(PlayerDTO playerDtoNew) {

        //MAPEAR DE DTO A ENTIDAD (COMPROBAR NOMBRE VACÍO)
        Player playerEntity = dtoToPlayer.map(playerDtoNew);

        //COMPROBAR SI YA EXISTE ESE JUGADOR O NO
        boolean existe = playerRepository.existsByUsername(playerEntity.getUsername());

        if (existe && !playerEntity.getUsername().equalsIgnoreCase("Anónimo")){

            throw new ExistentUserNameException("El nombre del jugador ya existe");

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

            throw new PlayerNotFoundException("No existe el jugador con id " + id);

        }

        return jugadorOpt.get();
    }


    //--> UPDATE

    public Player update(PlayerDTO playerDTO, Long id) {

        if (playerRepository.existsByUsername(playerDTO.getUsername())){

            throw new ExistentUserNameException("El nombre del jugador ya existe");
        }

        if (playerRepository.existsByEmail(playerDTO.getEmail())){

            throw new ExistentEmailException("El email ya existe");
        }

        if (id == null) {

            throw new IdPlayerException("El id del jugador a actualizar no puede ser nulo");
        }

        if (!playerRepository.existsById(id)) {

            throw new PlayerNotFoundException("El jugador no existe");
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

            throw new PlayerNotFoundException("No existe el jugador con id " + id);

        }

        //---- PENDIENTE
        //PENDENT BORRAR TIRADAS-- comprobar primero si teine tiradas
        //exception si no tiene

        Set<Launch> launches = jugadorOpt.get().getTiradas();

        for (Launch launch : launches) {

            launchRepository.delete(launch);

        }


        //TAMPOCO...

//        Long ids;
//
//        for (Tirada tirada : jugadorOpt.get().getTiradas()) {
//
//            ids = tirada.getId();
//            tirada = tiradaRepository.getById(ids);
//
//            tiradaRepository.delete(tirada);
//
//        }


        if (jugadorOpt.get().getTiradas().isEmpty()) {
            log.warn("Se han eliminado correctamente las tiradas del jugador");
        }

    }


    ////FUNCIONALIDADES JUEGO

    //INICIO JUEGO


    //TIRAR DADOS - REGISTRO TIRADAS - PORCENTAJE
    public Player realizarTirada(Long id) {

        Optional<Player> jugadorOpt = playerRepository.findById(id);

        //CAMBIAR POR EXCEPTION
        if (jugadorOpt.isEmpty()) {
            log.warn("no existe el jugador");
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
                //ALGO QUE ACABE EL JUEGO
            }

        } else {
            player.setAcierto(0);
        }

    }

    public void asignarTirada(Player player, Launch launch) {

        player.addTirada(launch);

        int porcentaje = GameFunctions.calcularPorcentajeJugador(player);
        player.setAcierto(porcentaje);

        //HACE FALTA?? ES RARO, CON JUGADOR DESDE EL MAIN YA SE GUARDA POR LA RELACION
        launchRepository.save(launch);

    }


    //// PORCENTAJES
    public Map<String, Integer> porcentajeJugadores() {

        //EXCEPTION DE SI TIENE TIRADAS O NO???

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

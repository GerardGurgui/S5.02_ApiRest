package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.service;

import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.DTO.JugadorDTO;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Jugador;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Tirada;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions.ExistentEmailException;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions.ExistentUserNameException;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions.IdPlayerException;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.game.GameFunctions;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.mapper.DtoToPlayer;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.repositories.JugadorRepository;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.repositories.TiradaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JugadorService {

    private final Logger log = LoggerFactory.getLogger(JugadorService.class);


    //INYECCIONES POR CONSTRUCTOR, FINAL POR INMUTABLE??
    private final JugadorRepository jugadorRepository;

    private final TiradaRepository tiradaRepository;

    private final DtoToPlayer dtoToPlayer;


    public JugadorService(JugadorRepository jugadorRepository, TiradaRepository tiradaRepository,
                          DtoToPlayer dtoToPlayer) {
        this.jugadorRepository = jugadorRepository;
        this.tiradaRepository = tiradaRepository;
        this.dtoToPlayer = dtoToPlayer;
    }


    ////CRUD
    //--> CREATE
    public Jugador createPlayer(JugadorDTO jugadorDtoNew) {

        Jugador jugadorEntity = dtoToPlayer.map(jugadorDtoNew);

        boolean existe = jugadorRepository.existsByUsername(jugadorEntity.getUsername());

        if (existe && !jugadorEntity.getUsername().equalsIgnoreCase("Anónimo")){

            throw new ExistentUserNameException("El nombre del jugador ya existe");

        }

        log.info("Jugador creado correctamente");

        return jugadorRepository.save(jugadorEntity);

    }


    //--> READ
    public List<Jugador> findAllPlayers() {

        return jugadorRepository.findAll();

    }

    public Jugador getOne(Long id) {

        Optional<Jugador> jugadorOpt = jugadorRepository.findById(id);


        if (jugadorOpt.isEmpty()) {

            throw new PlayerNotFoundException("No existe el jugador con id " + id);

        }

        return jugadorOpt.get();
    }


    //--> UPDATE

    public Jugador update(JugadorDTO jugadorDTO, Long id) {

        if (jugadorRepository.existsByUsername(jugadorDTO.getUsername())){

            throw new ExistentUserNameException("El nombre del jugador ya existe");
        }

        if (jugadorRepository.existsByEmail(jugadorDTO.getEmail())){

            throw new ExistentEmailException("El email ya existe");
        }

        if (id == null) {

            throw new IdPlayerException("El id del jugador a actualizar no puede ser nulo");
        }

        if (!jugadorRepository.existsById(id)) {

            throw new PlayerNotFoundException("El jugador no existe");
        }

        Optional<Jugador> jugadorOpt = jugadorRepository.findById(id);

        //ACTUALIZAMOS LOS ATRIBUTOS QUE SE PUEDEN INTRODUCIR EL USUARIO
        jugadorOpt.get().setUsername(jugadorDTO.getUsername());
        //EN PRINCIPI NOMES NOM
        jugadorOpt.get().setEmail(jugadorDTO.getEmail());


        return jugadorRepository.save(jugadorOpt.get());

    }


    //FALTAAAAA
    //--> DELETE TIRADAS 1 JUGADOR
    public void deleteTiradas(Long id) {

        Optional<Jugador> jugadorOpt = jugadorRepository.findById(id);

        if (jugadorOpt.isEmpty()) {

            throw new PlayerNotFoundException("No existe el jugador con id " + id);

        }

        //---- PENDIENTE
        //PENDENT BORRAR TIRADAS-- comprobar primero si teine tiradas
        //exception si no tiene

        Set<Tirada> tiradas = jugadorOpt.get().getTiradas();

        for (Tirada tirada : tiradas) {

            tiradaRepository.delete(tirada);

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
    public Jugador realizarTirada(Long id) {

        Optional<Jugador> jugadorOpt = jugadorRepository.findById(id);

        //CAMBIAR POR EXCEPTION
        if (jugadorOpt.isEmpty()) {
            log.warn("no existe el jugador");
        }

        Jugador jugador = jugadorOpt.get();

        //TIRA DADOS
        Tirada tirada = GameFunctions.tirarDados();

        //COMPROBACIONES PUNTUACION
        comprobarTirada(jugador, tirada);

        //ASIGNAR TIRADA Y PORCENTAJES
        asignarTirada(jugador, tirada);

        return jugador;

    }

    public void comprobarTirada(Jugador jugador, Tirada tirada) {


        //COMPRUEBA SUMA DE LA TIRADA
        boolean ganadorRonda = GameFunctions.comprobarTirada(tirada.getResultadoTirada());


        //COMPRUEBA TOTAL RONDAS GANADAS
        if (ganadorRonda) {

            boolean ganadorPartida = GameFunctions.sumarPuntuacionRonda(jugador);
            jugador.setAcierto(100);

            if (ganadorPartida) {
                jugador.setVictoria(1);
                //ALGO QUE ACABE EL JUEGO
            }

        } else {
            jugador.setAcierto(0);
        }

    }

    public void asignarTirada(Jugador jugador, Tirada tirada) {

        jugador.addTirada(tirada);

        int porcentaje = GameFunctions.calcularPorcentajeJugador(jugador);
        jugador.setAcierto(porcentaje);

        //HACE FALTA?? ES RARO, CON JUGADOR DESDE EL MAIN YA SE GUARDA POR LA RELACION
        tiradaRepository.save(tirada);

    }


    //// PORCENTAJES
    public Map<String, Integer> porcentajeJugadores() {

        //EXCEPTION DE SI TIENE TIRADAS O NO???

        List<Jugador> jugadores = findAllPlayers();

        return GameFunctions.calcularPorcentajeJugadores(jugadores);

    }


    public int porcentajeMediaTotal() {

        //TOTAL TIRADAS TOTS ELS JUGADORS * 100 / NUM JUGADORS
        List<Jugador> jugadores = findAllPlayers();

        return GameFunctions.calcularPorcentajeMedio(jugadores);

    }


    public Map<String, Integer> porcentajeJugadorLoser() {

        List<Jugador> jugadores = findAllPlayers();

        return GameFunctions.calcularPorcentajeLoser(jugadores);

    }

    public Map<String, Integer> porcentajeJugadorWinner() {

        List<Jugador> jugadores = findAllPlayers();

        return GameFunctions.calcularPorcentajeWinner(jugadores);

    }

}

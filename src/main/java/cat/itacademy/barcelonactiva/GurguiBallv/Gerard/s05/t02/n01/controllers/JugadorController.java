package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.controllers;

import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.DTO.JugadorDTO;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Jugador;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.service.JugadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/players")
public class JugadorController {


    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService){
        this.jugadorService = jugadorService;
    }

    ////----> CRUD

        //--> CREATE
    //SUSTITUIR POR REGISTER DE AUTHCONTROLLER??

//    @PostMapping("/add")
//    public ResponseEntity<Jugador> addPlayer(@RequestBody JugadorDTO jugadorDTO){
//
//        return new ResponseEntity<>(jugadorService.createPlayer(jugadorDTO), HttpStatus.CREATED);
//
//    }


        //--> READ
    @GetMapping("/get/findAll")
    public List<Jugador> getAllPlayers(){

        return jugadorService.findAllPlayers();

    }


    @GetMapping("/get/findOne/{id}")
    public ResponseEntity<Jugador> getOnePlayer(@PathVariable Long id){

        return ResponseEntity.ok(jugadorService.getOne(id));

    }

        //--> UPDATE

    @PutMapping("/updatePlayer/{id}")
    public ResponseEntity<Jugador> updatePlayer(@RequestBody JugadorDTO jugadorDTO,
                                                @PathVariable Long id){

        return new ResponseEntity<>(jugadorService.update(jugadorDTO, id), HttpStatus.OK);

    }

        //--> DELETE

    @DeleteMapping("/deleteTiradas/{id}")
    public void deletePlayer(@PathVariable Long id){

        jugadorService.deleteTiradas(id);
    }


    ////FUNCIONALIDADES JUEGO

        //INICIO JUEGO





        //JUGADOR REALIZA TIRADA

    @PostMapping("/game/tirada/{id}")
    public ResponseEntity<Jugador> realizaTirada(@PathVariable Long id){

        return ResponseEntity.ok(jugadorService.realizarTirada(id));

    }


        //LISTA DE PORCENTAJE DE CADA JUGADOR
    @GetMapping("/get/porcentajes/jugadores")
    public Map<String,Integer> mostrarPorcentajes(){

        return jugadorService.porcentajeJugadores();
    }

    //EL PORCENTAJE MEDIO TOTAL DE LOS JUGADORES
    @GetMapping("/get/ranking")
    public int mostrarPorcentajeMediaTotal(){

       return jugadorService.porcentajeMediaTotal();

    }

    @GetMapping("/get/ranking/loser")
    public Map<String, Integer> mostrarLoser(){

        return jugadorService.porcentajeJugadorLoser();

    }

    @GetMapping("/get/ranking/winner")
    public Map<String, Integer> mostrarWinner(){

        return jugadorService.porcentajeJugadorWinner();

    }


}

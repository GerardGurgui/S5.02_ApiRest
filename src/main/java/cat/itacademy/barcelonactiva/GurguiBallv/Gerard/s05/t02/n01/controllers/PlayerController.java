package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.controllers;

import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.DTO.PlayerDTO;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Player;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/players")
public class PlayerController {


    private final PlayerService playerService;

    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
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
    public List<Player> getAllPlayers(){

        return playerService.findAllPlayers();

    }


    @GetMapping("/get/findOne/{id}")
    public ResponseEntity<Player> getOnePlayer(@PathVariable Long id){

        return ResponseEntity.ok(playerService.getOne(id));

    }

        //--> UPDATE

    @PutMapping("/updatePlayer/{id}")
    public ResponseEntity<Player> updatePlayer(@RequestBody PlayerDTO playerDTO,
                                               @PathVariable Long id){

        return new ResponseEntity<>(playerService.update(playerDTO, id), HttpStatus.OK);

    }

        //--> DELETE

    @DeleteMapping("/deleteThrows/{id}")
    public void deletePlayer(@PathVariable Long id){

        playerService.deleteTiradas(id);
    }


    ////FUNCIONALIDADES JUEGO


        //JUGADOR REALIZA TIRADA

    @PostMapping("/game/throws/{id}")
    public ResponseEntity<Player> realizaTirada(@PathVariable Long id){

        return ResponseEntity.ok(playerService.realizarTirada(id));

    }


        //LISTA DE PORCENTAJE DE CADA JUGADOR
    @GetMapping("/percentage/players")
    public Map<String,Integer> mostrarPorcentajes(){

        return playerService.porcentajeJugadores();
    }

    //EL PORCENTAJE MEDIO TOTAL DE LOS JUGADORES
    @GetMapping("/ranking")
    public int mostrarPorcentajeMediaTotal(){

       return playerService.porcentajeMediaTotal();

    }

    @GetMapping("/ranking/loser")
    public Map<String, Integer> mostrarLoser(){

        return playerService.porcentajeJugadorLoser();

    }

    @GetMapping("/ranking/winner")
    public Map<String, Integer> mostrarWinner(){

        return playerService.porcentajeJugadorWinner();

    }


}

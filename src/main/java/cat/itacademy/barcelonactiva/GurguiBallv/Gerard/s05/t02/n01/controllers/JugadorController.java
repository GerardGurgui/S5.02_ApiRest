package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.controllers;

import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.DTO.JugadorDTO;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Jugador;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.service.JugadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class JugadorController {


    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService){
        this.jugadorService = jugadorService;
    }

    ////----> CRUD

        //--> CREATE
    @PostMapping("/add")
    public ResponseEntity<Jugador> addPlayer(@RequestBody JugadorDTO jugadorDTO){

        return new ResponseEntity<>(jugadorService.createPlayer(jugadorDTO), HttpStatus.CREATED);

    }


        //--> READ
    @GetMapping("/findAll")
    public List<Jugador> getAllPlayers(){

        return jugadorService.findAllPlayers();

    }


    @GetMapping("/findOne/{id}")
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

        //JUGADOR REALIZA TIRADA

    @PostMapping("/game/tirada/{id}")
    public ResponseEntity<Jugador> realizaTirada(@PathVariable Long id){

        return ResponseEntity.ok(jugadorService.realizarTirada(id));

    }

        //LISTA DE PORCENTAJE DE CADA JUGADR


}

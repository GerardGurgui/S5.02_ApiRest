package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.game;

import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Player;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Launch;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.security.payload.MessageResponse;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class GameFunctions {

    private static final Logger log = LoggerFactory.getLogger(PlayerService.class);

    public static Launch tirarDados(){

        Launch launch = new Launch();

        int dado1 = numRandom();
        launch.setDado1(dado1);

        int dado2 = numRandom();
        launch.setDado2(dado2);

        int resultado = dado1 + dado2;

        launch.setResultadoTirada(resultado);

        return launch;

    }


    public static int numRandom(){

        int numero = (int) ((Math.random() * ((6 - 1) + 1)) + 1);

        return numero;
    }


    public static boolean comprobarTirada(int resultado){

        boolean ganadorRonda = false;

        if (resultado == 7){

            ganadorRonda = true;

        }

        return ganadorRonda;
    }

    public static boolean sumarPuntuacionRonda(Player player){

        boolean ganadorPartida = false;

        int puntuacionTotal = player.getPuntuacion();
        puntuacionTotal++;
        player.setPuntuacion(puntuacionTotal);

        if (player.getPuntuacion() == 3){
            ganadorPartida = true;
        }

        return ganadorPartida;

    }


    ////PORCENTAJES-ESTADISTICAS

    //(TOTAL TIRADAS * LAS QUE SE HAN GANADO / JUGADORES)*100
    public static int calcularPorcentajeJugador(Player player){

        //calcular porcentaje
            //necesito la puntuación (cuantos 7 ha sacado)
            //necesito el total de tiradas que ha realizado
        //recorrer lista tiradas??

        //comparar total puntuacion con total tiradas??
        int puntuacion = player.getPuntuacion();

        int tiradasRealizadas = player.getTiradas().size();

        return (puntuacion * 100)/ tiradasRealizadas;

    }


    public static Map<String,Integer> calcularPorcentajeJugadores(List<Player> jugadores){

        Map<String,Integer> jugadoresAcierto = new HashMap<>();

        int acierto;

        for (Player player : jugadores) {

            acierto = calcularPorcentajeJugador(player);

            jugadoresAcierto.put(player.getUsername(),acierto);

        }

        return jugadoresAcierto;

    }

    public static int calcularPorcentajeMedio(List<Player> jugadores){

        //MAL, NO FUNCA BIEN

        Set<Launch> totalLaunches;

        int total = 0;

        for (Player jugadorsIter : jugadores) {

            totalLaunches = jugadorsIter.getTiradas();
            total += totalLaunches.size();

        }

        return (total * 100) / jugadores.size();

    }


    public static Map<String, Integer> calcularPorcentajeLoser(List<Player> jugadores){


        Map<String,Integer> jugadoresAciertos = calcularPorcentajeJugadores(jugadores);

        //OTRO HASHMAP PARA GUARDAR SOLO EL JUGADOR GANADOR
        Map<String,Integer> jugadorAcierto = new HashMap<>();

        int porcentajeMinimo = 100;
        String nombreGanador = " ";


        //CALCULO EL PORCENTAJE MAYOR CON EL NOMBRE CORRESPONDIENTE, MACHACA Y GUARDO EN NUEVO HASHMAP

        for (Map.Entry<String, Integer> jugadoresAciertosIter : jugadoresAciertos.entrySet()) {

            if (jugadoresAciertosIter.getValue() < porcentajeMinimo){

                nombreGanador = jugadoresAciertosIter.getKey();
                porcentajeMinimo = jugadoresAciertosIter.getValue();

            }
        }

        jugadorAcierto.put(nombreGanador,porcentajeMinimo);

        log.info("Jugador porcentaje maximo: " +nombreGanador+ " " +porcentajeMinimo);

        return jugadorAcierto;


    }

    public static Map<String,Integer> calcularPorcentajeWinner(List<Player> jugadores){

        //OBTENER LA LISTA DE JUGADORES CON SUS PORCENTAJES LLAMANDO AL METODO QUE LOS CALCULA
        Map<String,Integer> jugadoresAciertos = calcularPorcentajeJugadores(jugadores);

        //OTRO HASHMAP PARA GUARDAR SOLO EL JUGADOR GANADOR
        Map<String,Integer> jugadorAcierto = new HashMap<>();

        int porcentajeMaximo = 0;
        String nombreGanador = " ";


        //CALCULO EL PORCENTAJE MAYOR CON EL NOMBRE CORRESPONDIENTE, MACHACA Y GUARDO EN NUEVO HASHMAP

        for (Map.Entry<String, Integer> jugadoresAciertosIter : jugadoresAciertos.entrySet()) {

            if (jugadoresAciertosIter.getValue() > porcentajeMaximo){

                nombreGanador = jugadoresAciertosIter.getKey();
                porcentajeMaximo = jugadoresAciertosIter.getValue();

            }
        }

        jugadorAcierto.put(nombreGanador,porcentajeMaximo);

        log.info("Jugador porcentaje maximo: " +nombreGanador+ " " +porcentajeMaximo);

        return jugadorAcierto;
    }



}

package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.game;

import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Jugador;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Tirada;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions.ExistentUserNameException;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.service.JugadorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//CONTROLLER OR COMPONENT??
@Controller
public class GameFunctions {

    private static final Logger log = LoggerFactory.getLogger(JugadorService.class);



    public static void validarNombre(String nombre, List<Jugador> jugadores){

        int i = 0;

        while (i < jugadores.size()){

            if (jugadores.get(i).getNombre().equals(nombre)){

                throw new ExistentUserNameException("El nombre de usuario ya existe");
            }

            i++;
        }
    }


    public static Tirada tirarDados(){

        Tirada tirada = new Tirada();

        int dado1 = numRandom();
        tirada.setDado1(dado1);

        int dado2 = numRandom();
        tirada.setDado2(dado2);

        int resultado = dado1 + dado2;

        tirada.setResultadoTirada(resultado);

        return tirada;

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

    public static boolean sumarPuntuacionRonda(Jugador jugador){

        boolean ganadorPartida = false;

        int puntuacionTotal = jugador.getPuntuacion();
        puntuacionTotal++;
        jugador.setPuntuacion(puntuacionTotal);

        if (jugador.getPuntuacion() == 3){
            ganadorPartida = true;
        }

        return ganadorPartida;

    }


    ////PORCENTAJES-ESTADISTICAS

    //(TOTAL TIRADAS * LAS QUE SE HAN GANADO / JUGADORES)*100
    public static int calcularPorcentajeJugador(Jugador jugador){

        //calcular porcentaje
            //necesito la puntuación (cuantos 7 ha sacado)
            //necesito el total de tiradas que ha realizado
        //recorrer lista tiradas??

        //comparar total puntuacion con total tiradas??
        int puntuacion = jugador.getPuntuacion();

        int tiradasRealizadas = jugador.getTiradas().size();

        return (puntuacion * 100)/ tiradasRealizadas;

    }


    public static Map<String,Integer> calcularPorcentajeJugadores(List<Jugador> jugadores){

        Map<String,Integer> jugadoresAcierto = new HashMap<>();

        int acierto;

        for (Jugador jugador : jugadores) {

            acierto = calcularPorcentajeJugador(jugador);

            jugadoresAcierto.put(jugador.getNombre(),acierto);

        }

        return jugadoresAcierto;

    }

    public static int calcularPorcentajeMedio(List<Jugador> jugadores){

        //MAL, NO FUNCA BIEN

        Set<Tirada> totalTiradas;

        int total = 0;

        for (Jugador jugadorsIter : jugadores) {

            totalTiradas = jugadorsIter.getTiradas();
            total += totalTiradas.size();

        }

        return (total * 100) / jugadores.size();

    }


    public static Map<String, Integer> calcularPorcentajeLoser(List<Jugador> jugadores){


        Map<String,Integer> jugadoresAciertos = calcularPorcentajeJugadores(jugadores);

        //OTRO HASHMAP PARA GUARDAR SOLO EL JUGADOR GANADOR (NO PUEDO 2 RETURNS EN CONTROLLER)
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

    public static Map<String,Integer> calcularPorcentajeWinner(List<Jugador> jugadores){

        //OBTENER LA LISTA DE JUGADORES CON SUS PORCENTAJES LLAMANDO AL METODO QUE LOS CALCULA

        Map<String,Integer> jugadoresAciertos = calcularPorcentajeJugadores(jugadores);

        //OTRO HASHMAP PARA GUARDAR SOLO EL JUGADOR GANADOR (NO PUEDO 2 RETURNS EN CONTROLLER)
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

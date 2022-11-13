package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.rest;

import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.DTO.JugadorDTO;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities.Jugador;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.repositories.JugadorRepository;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.security.jwt.JwtTokenUtil;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.security.payload.JwtResponse;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.security.payload.LoginRequest;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.security.payload.MessageResponse;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.security.payload.RegisterRequest;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.service.JugadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para llevar a cabo la autenticación utilizando JWT
 *
 * Se utiliza AuthenticationManager para autenticar las credenciales que son el
 * usuario y password que llegan por POST en el cuerpo de la petición
 *
 * Si las credenciales son válidas se genera un token JWT como respuesta
 *
 * ADAPTAR REGISTRO Y LOGIN A LA ENTIDAD DE JUGADORES, SE REGISTRAN Y LOGIN PARA PODER JUGAR CON LA COOKIE GENERADA
 */

// @CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JugadorRepository jugadorRepository;
    private final PasswordEncoder encoder;
    private final JugadorService jugadorService;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(AuthenticationManager authManager,
                          JugadorRepository jugadorRepository,
                          PasswordEncoder encoder,
                          JugadorService jugadorService, JwtTokenUtil jwtTokenUtil){
        this.authManager = authManager;
        this.jugadorRepository = jugadorRepository;
        this.encoder = encoder;
        this.jugadorService = jugadorService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest){

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);

        // UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody JugadorDTO jugadorDto) {

        // Check 1: username
        if (jugadorRepository.existsByUsername(jugadorDto.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Check 2: email
        if (jugadorRepository.existsByEmail(jugadorDto.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account -- ADAPTAR A MI ENTIDAD JUGADOR

        jugadorService.createPlayer(jugadorDto);

        return ResponseEntity.ok(new MessageResponse("Player registered successfully!"));
    }
}

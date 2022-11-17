package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01;

import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.repositories.PlayerRepository;
import cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.repositories.LaunchRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

//--> SWAGGER LOCALHOST http://localhost:8080/swagger-ui.html#/

/*LINKS DE AYUDA
*
* Registrar en dos tablas relacionadas --> https://www.youtube.com/watch?v=rKj1NhXuHuk
* open bootcamp
* java guides
* Json ignore --> passwords https://de-vraag.com/es/54542256
* CRUD completo --> DTOS CORRECTES?? https://www.youtube.com/watch?v=z_dLYcQqSHI
* SERIALIZABLE?? --> https://stackoverflow.com/questions/49003908/why-while-using-spring-boot-i-need-entities-to-be-serializable
* 				--> https://www.baeldung.com/jpa-entities-serializable
*
* JWT --> https://www.youtube.com/watch?v=362F9RjbnPc
* */

@SpringBootApplication
public class S05T02N01GurguiBallveGerardApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(S05T02N01GurguiBallveGerardApplication.class, args);
		PlayerRepository playerRepository = context.getBean(PlayerRepository.class);
		LaunchRepository launchRepository = context.getBean(LaunchRepository.class);

	}

}

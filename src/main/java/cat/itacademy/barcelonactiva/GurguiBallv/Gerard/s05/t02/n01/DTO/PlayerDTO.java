package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PlayerDTO {


    //ATRIBUTOS QUE INTRODUCIRÁ EL USUARIO
    private String username;
    private String email;
    private String password;

    public PlayerDTO() {

    }

    public PlayerDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


}

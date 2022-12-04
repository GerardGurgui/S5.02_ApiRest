package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter

@Entity
@Table(name = "registro_tiradas")
public class Launch {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int dado1;
    private int dado2;
    private int resultadoTirada;


    public Launch() {
    }



}

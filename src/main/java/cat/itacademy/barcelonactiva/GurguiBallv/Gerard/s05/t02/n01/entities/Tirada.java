package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.entities;


import javax.persistence.*;


@Entity
@Table(name = "registro_tiradas")
public class Tirada {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int dado1;
    private int dado2;
    private int resultadoTirada;



    public Tirada() {
    }

    public int getDado1() {
        return dado1;
    }

    public void setDado1(int dado1) {
        this.dado1 = dado1;
    }

    public int getDado2() {
        return dado2;
    }

    public void setDado2(int dado2) {
        this.dado2 = dado2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getResultadoTirada() {
        return resultadoTirada;
    }

    public void setResultadoTirada(int resultadoTirada) {
        this.resultadoTirada = resultadoTirada;
    }



}

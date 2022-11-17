package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.mapper;


/*
* Interfaz generica para mapear las entidades a DTO y viceversa
* Input Output
* */

public interface IMapper <I, O> {
    O map(I input);

}

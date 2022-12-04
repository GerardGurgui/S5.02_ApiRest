package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor

public class ErrorDetails {

    private Date timestamp;
    private String message;
    private String details;
}

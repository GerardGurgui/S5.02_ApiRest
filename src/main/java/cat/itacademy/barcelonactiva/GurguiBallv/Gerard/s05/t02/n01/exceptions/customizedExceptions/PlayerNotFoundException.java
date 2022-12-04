package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.exceptions.customizedExceptions;


import org.springframework.http.HttpStatus;

public class PlayerNotFoundException extends RuntimeException{

    private HttpStatus httpStatus;
    private String message;

    public PlayerNotFoundException(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public PlayerNotFoundException(String message, HttpStatus httpStatus, String message2){
        super(message);
        this.httpStatus = httpStatus;
        this.message = message2;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

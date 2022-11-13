package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.security.payload;

public class MessageResponse {

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

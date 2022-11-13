package cat.itacademy.barcelonactiva.GurguiBallv.Gerard.s05.t02.n01.security.payload;

public class JwtResponse {

    private String token;

    public JwtResponse() {
    }
    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}


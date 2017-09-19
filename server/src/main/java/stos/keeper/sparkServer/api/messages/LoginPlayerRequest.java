package stos.keeper.sparkServer.api.messages;

public class LoginPlayerRequest {
    private String username;
    private String password;

    public LoginPlayerRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

package stos.keeper.sparkServer.api.messages;

public class PlayerIdAndTokenReply {
    private final int id;
    private final String username;
    private final String token;


    public PlayerIdAndTokenReply(int id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

}

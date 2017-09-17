package stos.keeper.sparkServer.api.messages;

public class PlayerIdAndTokenReply {
    private final int id;
    private final String userName;
    private final String token;


    public PlayerIdAndTokenReply(int id, String userName, String token) {
        this.id = id;
        this.userName = userName;
        this.token = token;
    }

}

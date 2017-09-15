package stos.keeper.model.player;

public class PlayerIdAndToken {
    private final int id;
    private final String userName;
    private final String token;


    public PlayerIdAndToken(int id, String userName, String token) {
        this.id = id;
        this.userName = userName;
        this.token = token;
    }

}

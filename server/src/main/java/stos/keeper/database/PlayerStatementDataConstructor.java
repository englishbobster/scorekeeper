package stos.keeper.database;

import stos.keeper.model.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class PlayerStatementDataConstructor {
    private static final String PLAYERS_TABLE_NAME = "players";
    private static final String INSERT_USER = "INSERT INTO " + PLAYERS_TABLE_NAME + " (username, password, salt, email, paid, created)"
            + " VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_USER = "SELECT * FROM " + PLAYERS_TABLE_NAME
            + " WHERE username= ?";
    private static final String DELETE_USER = "DELETE FROM " + PLAYERS_TABLE_NAME
            + " WHERE username= ?";


    static StatementDataObject getStatementDataFor(String transactionName, Optional<Player> playerOptional) {
        if(transactionName.equals("getPlayer")){
            return new StatementDataObject(GET_USER, Collections.emptyList());
        }
        if(transactionName.equals("deletePlayer")){
            return new StatementDataObject(DELETE_USER, Collections.emptyList());
        }
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            if (transactionName.equals("addPlayer")) {
                List<Object> statementParameters = new ArrayList<>();
                statementParameters.add(player.getUserName());
                statementParameters.add(player.getPassword());
                statementParameters.add(player.getSalt());
                statementParameters.add(player.getEmail());
                statementParameters.add(player.hasPaid());
                statementParameters.add(ConversionUtils.sqlTimeStampFrom(player.getCreated()));
                return new StatementDataObject(INSERT_USER, statementParameters);
            }
        }
        return new StatementDataObject("", Collections.emptyList());
    }
}

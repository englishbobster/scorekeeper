package stos.keeper.database;

import java.util.List;

class StatementDataObject {
    private String sqlStatement;
    private List<Object> parameters;

    StatementDataObject(String sqlStatement, List<Object> parameters) {
        this.sqlStatement = sqlStatement;
        this.parameters = parameters;
    }

    String getSqlStatement() {
        return sqlStatement;
    }

    List<Object> getParameters() {
        return parameters;
    }
}

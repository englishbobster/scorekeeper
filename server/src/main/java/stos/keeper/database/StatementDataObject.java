package stos.keeper.database;

import java.util.List;

public class StatementDataObject {
    private String sqlStatement;
    private List<Object> parameters;

    public StatementDataObject(String sqlStatement, List<Object> parameters) {
        this.sqlStatement = sqlStatement;
        this.parameters = parameters;
    }

    public String getSqlStatement() {
        return sqlStatement;
    }

    public List<Object> getParameters() {
        return parameters;
    }
}

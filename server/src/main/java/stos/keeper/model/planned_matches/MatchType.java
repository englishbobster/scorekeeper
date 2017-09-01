package stos.keeper.model.planned_matches;

public enum MatchType {
    A("Group A"),
    B("Group B"),
    C("Group C"),
    D("Group D"),
    E("Group E"),
    F("Group F"),
    G("Group G"),
    ROUND16("Round of 16"),
    QUARTERS("Quarter-Finals"),
    SEMIS("Semi-Finals"),
    THIRDPLACE("Third Place Match"),
    FINAL("Final");

    private final String printableName;

    MatchType(String printableName) {
        this.printableName = printableName;
    }

    @Override
    public String toString() {
        return printableName;
    }
}
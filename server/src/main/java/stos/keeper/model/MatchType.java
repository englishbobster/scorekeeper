package stos.keeper.model;

public enum MatchType {
    A("Group A"),
    B("Group B"),
    C("Group C"),
    D("Group D"),
    E("Group E"),
    F("Group F"),
    G("Group G"),
    ROUND16("Round of 16"),
    QUARTERS("Quarter-finals"),
    SEMIS("Semi-finals"),
    THIRDPLACE("Third place match"),
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
package pt.ulusofona.lp2.theWalkingDEISIGame;

public class InvalidTWDInitialFileException extends Exception {
    public boolean validNrOfCreatures() {
        return new TWDGameManager().totalCriaturas >= 2;
    }

    public boolean validCreatureDefinition() {
        return true;
    }

    public String getErroneousLine() {
        return "";
    }
}

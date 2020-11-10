package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TWDGameManager {

    public TWDGameManager () {
    }

    public boolean startGame(File ficheiroInicial) {
        return true;
    }

    public int[] getWorldSize() {
        return new int[0];
    }

    public int getInitialTeam() {
        return 0;
    }

    public List<Humano> getHumans() {
        return new ArrayList<>();
    }

    public List<Zombie> getZombies() {
        return new ArrayList<>();
    }

    public boolean move(int xO, int yO, int xD, int yD) {
        return true;
    }

    public boolean gameIsOver() {
        return true;
    }

    public List<String> getAuthors() {
        return new ArrayList<>();
    }

    public int getCurrentTeamId() {
        return 0;
    }

    public int getElementId(int x, int y) {
        return 0;
    }

    public List<String> getSurvivors() {
        return new ArrayList<>();
    }

    public boolean isDay() {
        return true;
    }

    public boolean hasEquipment(int creatureId, int equipmentTypeId) {
        return true;
    }

}

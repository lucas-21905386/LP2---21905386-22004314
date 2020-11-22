package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class TWDGameManager {
    static List<Humano> humanos = new ArrayList<>();
    static List<Zombie> zombies = new ArrayList<>();
    static List<Equipamentos> equipamentos = new ArrayList<>();
    static int[] grid;
    static int first, totalCriaturas, totalEquipamentos,nrTurnos=0;

    public TWDGameManager () {
    }

    public boolean startGame(File ficheiroInicial) {
        int count = 1;
        try {
            Scanner ficheiro = new Scanner(ficheiroInicial);
            String linha = ficheiro.nextLine();
            String[] linhaInfo = linha.split(" ");
            grid = new int[]{Integer.parseInt(linhaInfo[0]), Integer.parseInt(linhaInfo[1])};
            while (ficheiro.hasNextLine()) {
                linha = ficheiro.nextLine();
                count++;
                linhaInfo = linha.split(" : ");
                switch (linhaInfo.length) {
                    case 1: {
                        if (count == 2) {
                            first = Integer.parseInt(linhaInfo[0]);
                        } else if (count == 3) {
                            totalCriaturas = Integer.parseInt(linhaInfo[0]);
                        } else {
                            totalEquipamentos = Integer.parseInt(linhaInfo[0]);
                        }
                    }
                    break;
                    case 4: {
                        equipamentos.add(new Equipamentos(Integer.parseInt(linhaInfo[0]), Integer.parseInt(linhaInfo[1]),
                                Integer.parseInt(linhaInfo[2]), Integer.parseInt(linhaInfo[3])));
                    }
                    break;
                    case 5: {
                        if (Integer.parseInt(linhaInfo[1]) == 0) {
                            zombies.add(new Zombie(Integer.parseInt(linhaInfo[0]), Integer.parseInt(linhaInfo[1]),
                                    linhaInfo[2], Integer.parseInt(linhaInfo[3]), Integer.parseInt(linhaInfo[4])));
                        }
                        if (Integer.parseInt(linhaInfo[1]) == 1) {
                            humanos.add(new Humano(Integer.parseInt(linhaInfo[0]), Integer.parseInt(linhaInfo[1]),
                                    linhaInfo[2], Integer.parseInt(linhaInfo[3]), Integer.parseInt(linhaInfo[4])));
                        }
                    }
                    break;
                    default:
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Zombie> getZombies() {
        return zombies;
    }

    public int[] getWorldSize() {

        return grid;
        }

    public int getInitialTeam()
    {
        return first;
    }

    public List<Humano> getHumans() {

        return humanos;
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
        if ((nrTurnos % 4) == 1 || (nrTurnos % 4) == 2) {
            return true;
        }
        return false;
    }


    public boolean hasEquipment(int creatureId, int equipmentTypeId) {
        AtomicBoolean confirma = new AtomicBoolean();
        confirma.set(false);
        humanos.forEach(k -> {
            if (k.iD == creatureId && k.equip != null) {
                if (k.equip.tipo == equipmentTypeId) {
                    confirma.set(true);
                }
            }
        });
        return confirma.get();
    }

}

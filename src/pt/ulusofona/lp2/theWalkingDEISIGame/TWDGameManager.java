package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import java.util.Scanner;

public class TWDGameManager {
    static List<Humano> humanos = new ArrayList<>();
    static List<Zombie> zombies = new ArrayList<>();
    static List<Equipamentos> equipamentos = new ArrayList<>();
    static int[] grid;
    static int first, totalCriaturas, totalEquipamentos;


    public TWDGameManager () {
    }

    public boolean startGame(File ficheiroInicial) throws IOException {
        int count = 1;
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
        ficheiro.close();
        if (count == 4 + totalCriaturas + totalEquipamentos) {
            return true;
        } else {
            return false;
        }
    }

    public int[] getWorldSize() {
        return grid;
    }

    public int getInitialTeam() {
        return first;
    }

    public List<Humano> getHumans() {
        return humanos;
    }

    public List<Zombie> getZombies() {
        return zombies;
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

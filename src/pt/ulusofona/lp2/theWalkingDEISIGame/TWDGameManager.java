package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.io.*;
import java.util.*;
import java.lang.String;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TWDGameManager {
    HashMap<Integer, Equipamentos> equipamentos = new HashMap<>();
    List<Creature> criaturas = new ArrayList<>();
    List<SafeHeaven> safeHeavens = new ArrayList<>();
    List<Creature> safeHeavenHumanos = new ArrayList<>();
    List<Creature> foraDeJogo = new ArrayList<>();
    int[] grid;
    int first, totalCriaturas, totalEquipamentos, totalSafeHeavens, nrTurnos = 0, semMortes = 0, currentTeamId;

    public TWDGameManager () {
    }

    void reset () {
        criaturas.clear();
        safeHeavens.clear();
        safeHeavenHumanos.clear();
        foraDeJogo.clear();
        equipamentos.clear();
        nrTurnos = 0;
    }


    public boolean startGame(File ficheiroInicial) {
        reset();
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
                            currentTeamId = Integer.parseInt(linhaInfo[0]);
                        } else if (count == 3) {
                            totalCriaturas = Integer.parseInt(linhaInfo[0]);
                        } else if (count == count + 1 + totalCriaturas) {
                            totalEquipamentos = Integer.parseInt(linhaInfo[0]);
                        } else {
                            totalSafeHeavens = Integer.parseInt(linhaInfo[0]);
                        }
                    }
                    break;
                    case 2: {
                        safeHeavens.add(new SafeHeaven(Integer.parseInt(linhaInfo[0]), Integer.parseInt(linhaInfo[1])));
                    }
                    break;
                    case 4: {
                        equipamentos.put(Integer.parseInt(linhaInfo[0]),
                                new Equipamentos(Integer.parseInt(linhaInfo[0]), Integer.parseInt(linhaInfo[1]),
                                        Integer.parseInt(linhaInfo[2]), Integer.parseInt(linhaInfo[3])));
                    }
                    break;
                    case 5: {
                        List<Integer> ids = new ArrayList<>();
                        criaturas.forEach(k -> ids.add(k.getId()));
                        if (Integer.parseInt(linhaInfo[1]) > 4) {
                            if (!ids.contains(Integer.parseInt(linhaInfo[0]))) {
                                criaturas.add(new Humano(Integer.parseInt(linhaInfo[0]), Integer.parseInt(linhaInfo[1]),
                                        linhaInfo[2], Integer.parseInt(linhaInfo[3]), Integer.parseInt(linhaInfo[4])));
                            }
                        } else if (Integer.parseInt(linhaInfo[1]) < 5) {
                            if (!ids.contains(Integer.parseInt(linhaInfo[0]))) {
                                criaturas.add(new Zombie(Integer.parseInt(linhaInfo[0]), Integer.parseInt(linhaInfo[1]),
                                        linhaInfo[2], Integer.parseInt(linhaInfo[3]), Integer.parseInt(linhaInfo[4])));
                            }
                        }
                    }
                    break;
                    default:
                }
            }
            ficheiro.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
        AtomicBoolean confirm = new AtomicBoolean();
        confirm.set(false);
        if (xD != xO && yD != yO || xD - xO > 1 || xD - xO < -1 || yD - yO > 1 || yD - yO < -1) {
            return false;
        }
        if (currentTeamId == 0) {
            humanos.forEach(k -> {
                if (xO == k.getX() && yO == k.getY()) {
                    if (getElementId(xD, yD) < 0) {
                        if (k.getEquip() != null) {
                            equipamentos.put(k.getEquip().getId(),
                                    new Equipamentos(k.getEquip().getId(), k.getEquip().getTipo(), xO, yO));
                        }
                        k.setEquip(new Equipamentos(equipamentos.get(getElementId(xD, yD)).getId(),
                                equipamentos.get(getElementId(xD, yD)).getTipo(), equipamentos.get(getElementId(xD, yD)).getX(),
                                equipamentos.get(getElementId(xD, yD)).getY()));
                        k.setX(xD);
                        k.setY(yD);
                        nrTurnos++;
                        k.setEquipApanhados();
                        currentTeamId = 1;
                        confirm.set(true);
                        equipamentos.remove(getElementId(xD, yD));
                    } else if (getElementId(xD, yD) == 0) {
                        k.setX(xD);
                        k.setY(yD);
                        nrTurnos++;
                        currentTeamId = 1;
                        confirm.set(true);
                    } else if (getElementId(xD, yD) > 0) {
                        confirm.set(false);
                    }
                }
            });
        } else {
            zombies.forEach(k -> {
                if (xO == k.getX() && yO == k.getY()) {
                    if (getElementId(xD, yD) < 0) {
                        equipamentos.remove(getElementId(xD, yD));
                        k.setX(xD);
                        k.setY(yD);
                        nrTurnos++;
                        k.setEquipDestruidos();
                        currentTeamId = 0;
                        confirm.set(true);
                    } else if (getElementId(xD, yD) == 0) {
                        k.setX(xD);
                        k.setY(yD);
                        nrTurnos++;
                        currentTeamId = 0;
                        confirm.set(true);
                    } else if (getElementId(xD, yD) > 0) {
                        confirm.set(false);
                    }
                }
            });
        }
        return confirm.get();
    }

    public boolean gameIsOver() {
        return nrTurnos >= 12;
    }

    public List<String> getAuthors() {
        List<String> autores = new ArrayList<>();
        autores.add("Lucas Souza");
        autores.add("Luís Miguel Galo");
        return autores;
    }

    public int getCurrentTeamId() {
        return currentTeamId;
    }

    public int getElementId(int x, int y) {
        AtomicInteger id = new AtomicInteger();
        criaturas.forEach(k -> {
            if (x == k.getX() && y == k.getY()) {
                id.set(k.getId());
            }
        });
        equipamentos.forEach((k, v) -> {
            if (x == v.getX() && y == v.getY()) {
                id.set(v.getId());
            }
        });
        return id.intValue();
    }

    public List<String> getSurvivors() {
        List<String> sobreviventes = new ArrayList<>();
        sobreviventes.add("Nr. de turnos terminados:");
        sobreviventes.add(String.valueOf(nrTurnos));
        sobreviventes.add("\n");
        sobreviventes.add("OS VIVOS");
        humanos.forEach(k -> sobreviventes.add(k.getId() + " " + k.getNome()));
        sobreviventes.add("\n");
        sobreviventes.add("OS OUTROS");
        zombies.forEach(k -> sobreviventes.add(k.getId() + " " + k.getNome()));
        return sobreviventes;
    }


    public boolean isDay() {
        return (nrTurnos/2) % 2 == 0;
    }


    public boolean hasEquipment(int creatureId, int equipmentTypeId) {
        AtomicBoolean confirma = new AtomicBoolean();
        confirma.set(false);
        humanos.forEach(k -> {
            if (k.getId() == creatureId && k.getEquip() != null) {
                if (k.getEquip().getTipo() == equipmentTypeId) {
                    confirma.set(true);
                }
            }
        });
        return confirma.get();
    }

}

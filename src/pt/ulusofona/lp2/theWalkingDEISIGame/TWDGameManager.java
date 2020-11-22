package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.io.*;
import java.util.*;
import java.lang.String;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TWDGameManager {
    static List<Humano> humanos = new ArrayList<>();
    static List<Zombie> zombies = new ArrayList<>();
    static HashMap<Integer, Equipamentos> equipamentos = new HashMap<>();
    static int[] grid;
    static int first, totalCriaturas, totalEquipamentos, nrTurnos = 0, currentTeamId;

    public TWDGameManager () {
    }

    void reset () {
        humanos.clear();
        zombies.clear();
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
                        } else {
                            totalEquipamentos = Integer.parseInt(linhaInfo[0]);
                        }
                    }
                    break;
                    case 4: {
                            equipamentos.put(Integer.parseInt(linhaInfo[0]),
                                    new Equipamentos(Integer.parseInt(linhaInfo[0]), Integer.parseInt(linhaInfo[1]),
                                            Integer.parseInt(linhaInfo[2]), Integer.parseInt(linhaInfo[3])));
                    }
                    break;
                    case 5: {
                        if (Integer.parseInt(linhaInfo[1]) == 0) {
                            List<Integer> ids = new ArrayList<>();
                            zombies.forEach(k -> ids.add(k.iD));
                            if (!ids.contains(Integer.parseInt(linhaInfo[0]))) {
                                zombies.add(new Zombie(Integer.parseInt(linhaInfo[0]), Integer.parseInt(linhaInfo[1]),
                                        linhaInfo[2], Integer.parseInt(linhaInfo[3]), Integer.parseInt(linhaInfo[4])));
                            }
                        }
                        if (Integer.parseInt(linhaInfo[1]) == 1) {
                            List<Integer> ids = new ArrayList<>();
                            humanos.forEach(k -> ids.add(k.iD));
                            if (!ids.contains(Integer.parseInt(linhaInfo[0]))) {
                                humanos.add(new Humano(Integer.parseInt(linhaInfo[0]), Integer.parseInt(linhaInfo[1]),
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
                if (xO == k.x && yO == k.y) {
                    if (getElementId(xD, yD) < 0) {
                        if (k.equip != null) {
                            equipamentos.put(k.equip.id,
                                    new Equipamentos(k.equip.id, k.equip.tipo, xO, yO));
                        }
                        k.equip = new Equipamentos(equipamentos.get(getElementId(xD, yD)).id,
                                equipamentos.get(getElementId(xD, yD)).tipo, equipamentos.get(getElementId(xD, yD)).x,
                                equipamentos.get(getElementId(xD, yD)).y);
                        k.x = xD;
                        k.y = yD;
                        nrTurnos++;
                        k.equipApanhados++;
                        currentTeamId = 1;
                        confirm.set(true);
                        equipamentos.remove(getElementId(xD, yD));
                    } else if (getElementId(xD, yD) == 0) {
                        k.x = xD;
                        k.y = yD;
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
                if (xO == k.x && yO == k.y) {
                    if (getElementId(xD, yD) < 0) {
                        equipamentos.remove(getElementId(xD, yD));
                        k.x = xD;
                        k.y = yD;
                        nrTurnos++;
                        k.equipDestruidos++;
                        currentTeamId = 0;
                        confirm.set(true);
                    } else if (getElementId(xD, yD) == 0) {
                        k.x = xD;
                        k.y = yD;
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
        humanos.forEach(k -> {
            if (x == k.x && y == k.y) {
                id.set(k.iD);
            }
        });
        zombies.forEach(k -> {
            if (x == k.x && y == k.y) {
                id.set(k.iD);
            }
        });
        equipamentos.forEach((k, v) -> {
            if (x == v.x && y == v.y) {
                id.set(v.id);
            }
        });
        return id.intValue();
    }

    public List<String> getSurvivors() {
        List<String> sobreviventes = new ArrayList<>();
        sobreviventes.add("Nr. de turnos terminados:");
        sobreviventes.add(String.valueOf(nrTurnos - 1));
        sobreviventes.add("\n");
        sobreviventes.add("OS VIVOS");
        humanos.forEach(k -> sobreviventes.add(k.iD + " " + k.nome));
        sobreviventes.add("\n");
        sobreviventes.add("OS OUTROS");
        zombies.forEach(k -> sobreviventes.add(k.iD + " " + k.nome));
        return sobreviventes;
    }


    public boolean isDay() {
        return (nrTurnos/2) % 2 == 0;
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

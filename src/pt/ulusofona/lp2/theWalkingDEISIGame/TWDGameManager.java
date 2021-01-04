package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.io.*;
import java.util.*;
import java.lang.String;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

    public List<Creature> getCreatures() {
        return criaturas;
    }

    public boolean sobreposicao(int xO, int yO, int xD, int yD) {
        int distanceX = xO - xD;
        int distanceY = yO - yD;
        int i = xO;
        int j = yO;

        if (xO != xD && yO != yD) {
            while (i != xD && j != yD) {
                if (getElementId(i, j) > 0 && getElementId(i, j) < 0) {
                    return false;
                }
                if (distanceX < 0) {
                    i++;
                } else if (distanceX > 0) {
                    i--;
                }
                if (distanceY < 0) {
                    j++;
                } else if (distanceY > 0) {
                    j--;
                }
            }
        } else if (xO == xD && yO != yD) {
            while (j != yD) {
                if (getElementId(i, j) > 0 && getElementId(i, j) < 0) {
                    return false;
                }
                if (distanceY < 0) {
                    j++;
                } else if (distanceY > 0) {
                    j--;
                }
            }
        } else if (xO != xD && yO == yD) {
            while (i != xD) {
                if (getElementId(i, j) > 0 && getElementId(i, j) < 0) {
                    return false;
                }
                if (distanceX < 0) {
                    i++;
                } else if (distanceX > 0) {
                    i--;
                }
            }
        }
        return true;
    }

    public boolean move(int xO, int yO, int xD, int yD) {
        if (xD < 0 || xD > grid[0] || yD < 0 || yD > grid[0] ) {
            return false;
        }
        AtomicBoolean confirm = new AtomicBoolean();
        AtomicReference<Creature> temp = new AtomicReference<>();
        confirm.set(true);
        if (!sobreposicao(xO, yO, xD, yD)) {
            return false;
        }
        criaturas.forEach(k -> {
            if (xO == k.getX() && yO == k.getY()) {
                switch (k.getiDTipo()) {
                    case 5:
                    case 0: {
                        if (new VerificarMov().crianca(xO, yO, xD, yD)) {
                            break;
                        } else {
                            confirm.set(false);
                        }
                    }
                    break;
                    case 6:
                    case 1: {
                        if (new VerificarMov().adulto(xO, yO, xD, yD)) {
                            break;
                        } else {
                            confirm.set(false);
                        }
                    }
                    break;
                    case 7:
                    case 2: {
                        if (new VerificarMov().militar(xO, yO, xD, yD)) {
                            break;
                        } else {
                            confirm.set(false);
                        }
                    }
                    break;
                    case 3:
                    case 8: {
                        if (new VerificarMov().idoso(xO, yO, xD, yD, k.getiDTipo(), isDay())) {
                            break;
                        } else {
                            confirm.set(false);
                        }
                    }
                    break;
                    case 4: {
                        if (new VerificarMov().vampiro(xO, yO, xD, yD, isDay())) {
                            break;
                        } else {
                            confirm.set(false);
                        }
                    }
                    break;
                    case 9: {
                        if (new VerificarMov().cao(xO, yO, xD, yD)) {
                            break;
                        } else {
                            confirm.set(false);
                        }
                    }
                }
            }
        });
        if (!confirm.get()) {
            return confirm.get();
        }
        confirm.set(false);

        if (currentTeamId == 10) {
            criaturas.forEach(k -> {
                if (k instanceof Humano && xO == k.getX() && yO == k.getY()) {
                    if(isDoorToSafeHaven(xD, yD)) {
                        ((Humano) k).setSafeHeaven(true);
                        safeHeavenHumanos.add(k);
                        k.setX(grid[0]);
                        k.setY(grid[1]);
                        currentTeamId = 20;
                        nrTurnos++;
                        confirm.set(true);
                        return;
                    }
                    if (getElementId(xD, yD) < 0) {
                        if (((Humano) k).getEquip() != null) {
                            equipamentos.put(((Humano) k).getEquip().getId(),
                                    new Equipamentos(((Humano) k).getEquip().getId(),
                                            ((Humano) k).getEquip().getTipo(), xO, yO));
                        }
                        if (k.getiDTipo() == 7 && equipamentos.get(getElementId(xD, yD)).getTipo() == 0) {
                            if (equipamentos.get(getElementId(xD, yD)).getUsado() == 0) {
                                equipamentos.get(getElementId(xD, yD)).setUsado();
                                equipamentos.get(getElementId(xD, yD)).incrementUsosDisponiveis();
                                ((Humano) k).setEquip(new Equipamentos(equipamentos.get(getElementId(xD, yD)).getId(),
                                        equipamentos.get(getElementId(xD, yD)).getTipo(),
                                        equipamentos.get(getElementId(xD, yD)).getX(),
                                        equipamentos.get(getElementId(xD, yD)).getY()));
                            } else {
                                ((Humano) k).setEquip(new Equipamentos(equipamentos.get(getElementId(xD, yD)).getId(),
                                        equipamentos.get(getElementId(xD, yD)).getTipo(),
                                        equipamentos.get(getElementId(xD, yD)).getX(),
                                        equipamentos.get(getElementId(xD, yD)).getY()));
                            }
                        } else {
                            if (equipamentos.get(getElementId(xD, yD)).getTipo() == 8 && equipamentos.get(getElementId(xD, yD)).getUsosDisponiveis() > 0) {
                                equipamentos.get(getElementId(xD, yD)).setUsosDisponiveis();
                                ((Humano) k).setEnvenenado(true);
                            } else if (equipamentos.get(getElementId(xD, yD)).getTipo() == 9 && !((Humano) k).getEnvenenado()) {
                                confirm.set(false);
                                return;
                            } else if (equipamentos.get(getElementId(xD, yD)).getTipo() == 9 && ((Humano) k).getEnvenenado()) {
                                ((Humano) k).setEnvenenado(false);
                                ((Humano) k).resetTurnosEnvenenado();
                            }
                            ((Humano) k).setEquip(new Equipamentos(equipamentos.get(getElementId(xD, yD)).getId(),
                                    equipamentos.get(getElementId(xD, yD)).getTipo(),
                                    equipamentos.get(getElementId(xD, yD)).getX(),
                                    equipamentos.get(getElementId(xD, yD)).getY()));
                        }
                        k.setContarEquip();
                        equipamentos.remove(getElementId(xD, yD));
                        k.setX(xD);
                        k.setY(yD);
                        nrTurnos++;
                        currentTeamId = 20;
                        confirm.set(true);
                    } else if (getElementId(xD, yD) == 0) {
                        if (k.getiDTipo() == 8 && ((Humano) k).getEquip() != null) {
                            equipamentos.put(((Humano) k).getEquip().getId(),
                                    new Equipamentos(((Humano) k).getEquip().getId(),
                                            ((Humano) k).getEquip().getTipo(), xO, yO));
                            ((Humano) k).setEquip(null);
                        }
                        k.setX(xD);
                        k.setY(yD);
                        nrTurnos++;
                        currentTeamId = 20;
                        confirm.set(true);
                    } else if (getElementId(xD, yD) > 0) {
                        criaturas.forEach(v -> {
                            if (v.getId() == getElementId(xD, yD) && v instanceof Humano) {
                                confirm.set(false);
                                return;
                            }
                        });
                        if (((Humano) k).getEquip() != null) {
                            if (((Humano) k).getEquip().getAcao() > 0) {
                                switch (k.getiDTipo()) {
                                    case 5: {
                                        if (((Humano) k).getEquip().getTipo() == 1) {
                                            criaturas.forEach(v -> {
                                                if (v.getId() == getElementId(xD, yD) && v instanceof Zombie) {
                                                    if (v.getiDTipo() == 0) {
                                                        foraDeJogo.add(v);
                                                        v.setDead(true);
                                                        v.setX(grid[0]);
                                                        v.setY(grid[1]);
                                                        k.setX(xD);
                                                        k.setY(yD);
                                                        currentTeamId = 20;
                                                        nrTurnos++;
                                                        confirm.set(true);
                                                        return;
                                                    } else {
                                                        confirm.set(false);
                                                        return;
                                                    }
                                                }
                                            });
                                        } else if (((Humano) k).getEquip().getTipo() == 2) {
                                            criaturas.forEach(v -> {
                                                if (v.getId() == getElementId(xD, yD) && v instanceof Zombie) {
                                                    if (v.getiDTipo() == 4) {
                                                        confirm.set(false);
                                                        return;
                                                    } else {
                                                        if (((Humano) k).getEquip().getUsosDisponiveis() != 0) {
                                                            ((Humano) k).getEquip().setUsosDisponiveis();
                                                            foraDeJogo.add(v);
                                                            v.setDead(true);
                                                            v.setX(grid[0]);
                                                            v.setY(grid[1]);
                                                            k.setX(xD);
                                                            k.setY(yD);
                                                            currentTeamId = 20;
                                                            nrTurnos++;
                                                            confirm.set(true);
                                                            return;
                                                        } else {
                                                            confirm.set(false);
                                                            return;
                                                        }
                                                    }
                                                }
                                            });
                                        } else {
                                            if (((Humano) k).getEquip().getUsosDisponiveis() < 4 &&
                                                    ((Humano) k).getEquip().getUsosDisponiveis() > 0) {
                                                criaturas.forEach(v -> {
                                                    if (v.getId() == getElementId(xD, yD) && v instanceof Zombie) {
                                                        ((Humano) k).getEquip().setUsosDisponiveis();
                                                        foraDeJogo.add(v);
                                                        v.setDead(true);
                                                        v.setX(grid[0]);
                                                        v.setY(grid[1]);
                                                        k.setX(xD);
                                                        k.setY(yD);
                                                        currentTeamId = 20;
                                                        nrTurnos++;
                                                        confirm.set(true);
                                                        return;
                                                    }
                                                });
                                            } else if (((Humano) k).getEquip().getUsosDisponiveis() > 4){
                                                criaturas.forEach(v -> {
                                                    if (v.getId() == getElementId(xD, yD) && v instanceof Zombie) {
                                                        foraDeJogo.add(v);
                                                        v.setDead(true);
                                                        v.setX(grid[0]);
                                                        v.setY(grid[1]);
                                                        k.setX(xD);
                                                        k.setY(yD);
                                                        currentTeamId = 20;
                                                        nrTurnos++;
                                                        confirm.set(true);
                                                        return;
                                                    }
                                                });
                                            } else {
                                                confirm.set(false);
                                                return;
                                            }
                                        }
                                    }
                                    break;
                                    default: {
                                        criaturas.forEach(v -> {
                                            if (v.getId() == getElementId(xD, yD) && v instanceof Zombie) {
                                                foraDeJogo.add(v);
                                                v.setDead(true);
                                                v.setX(grid[0]);
                                                v.setY(grid[1]);
                                                k.setX(xD);
                                                k.setY(yD);
                                                currentTeamId = 20;
                                                nrTurnos++;
                                                confirm.set(true);
                                                return;
                                            }
                                        });
                                    }
                                }
                            }
                        } else {
                            confirm.set(false);
                            return;
                        }
                    }
                }
            });
        } else if (currentTeamId == 20) {
            if(isDoorToSafeHaven(xD, yD)) {
                return false;
            }
            criaturas.forEach(k -> {
                if (k instanceof Zombie && xO == k.getX() && yO == k.getY()) {
                    if (getElementId(xD, yD) < 0) {
                        if (equipamentos.get(getElementId(xD, yD)).getTipo() == 8) {
                            confirm.set(false);
                            return;
                        }
                        if (k.getiDTipo() == 4 && equipamentos.get(getElementId(xD, yD)).getTipo() == 5) {
                            confirm.set(false);
                            return;
                        } else {
                            equipamentos.remove(getElementId(xD, yD));
                        }
                        k.setX(xD);
                        k.setY(yD);
                        k.setContarEquip();
                        currentTeamId = 10;
                        nrTurnos++;
                        confirm.set(true);
                    } else if (getElementId(xD, yD) == 0) {
                        k.setX(xD);
                        k.setY(yD);
                        currentTeamId = 10;
                        nrTurnos++;
                        confirm.set(true);
                    } else if (getElementId(xD, yD) > 0) {
                        criaturas.forEach(v -> {
                            if (v.getId() == getElementId(xD, yD) && v instanceof Zombie) {
                                confirm.set(false);
                                return;
                            }
                        });
                        criaturas.forEach(v -> {
                            if (v instanceof Humano && v.getId() == getElementId(xD, yD) && ((Humano) v).getEquip() != null) {
                                if (((Humano) v).getEnvenenado()) {
                                    confirm.set(true);
                                    nrTurnos++;
                                    currentTeamId = 10;
                                    return;
                                }
                                if (((Humano) v).getEquip().getTipo() == 5 && k.getiDTipo() == 4) {
                                    currentTeamId = 10;
                                    confirm.set(true);
                                    nrTurnos++;
                                    return;
                                } else if (((Humano) v).getEquip().getAcao() > 0) {
                                    if (((Humano) v).getEquip().getUsosDisponiveis() > 0) {
                                        if (v.getiDTipo() == 9) {
                                            confirm.set(false);
                                            return;
                                        } else {
                                            k.setDead(true);
                                            foraDeJogo.add(k);
                                            k.setX(grid[0]);
                                            k.setY(grid[1]);
                                            ((Humano) v).getEquip().setUsosDisponiveis();
                                            currentTeamId = 10;
                                            nrTurnos++;
                                            confirm.set(true);
                                            return;
                                        }
                                    } else {
                                        if (v.getiDTipo() == 9) {
                                            confirm.set(false);
                                            return;
                                        } else {
                                            temp.set(v);
                                            currentTeamId = 10;
                                            nrTurnos++;
                                            confirm.set(true);
                                            return;
                                        }
                                    }
                                } else if (((Humano) v).getEquip().getAcao() == 0) {
                                    if (((Humano) v).getEquip().getUsosDisponiveis() > 0) {
                                        if (v.getiDTipo() == 9) {
                                            confirm.set(false);
                                            return;
                                        } else {
                                            if (((Humano) v).getEquip().getTipo() == 5 && k.getiDTipo() != 4 ||
                                                    ((Humano) v).getEquip().getTipo() == 4 && k.getiDTipo() != 3) {
                                                temp.set(v);
                                            }
                                            ((Humano) v).getEquip().setUsosDisponiveis();
                                            currentTeamId = 10;
                                            nrTurnos++;
                                            confirm.set(true);
                                            return;
                                        }
                                    } else {
                                        if (v.getiDTipo() == 9) {
                                            confirm.set(false);
                                            return;
                                        } else {
                                            temp.set(v);
                                            currentTeamId = 10;
                                            nrTurnos++;
                                            confirm.set(true);
                                            return;
                                        }
                                    }
                                }
                            } else if (v instanceof Humano && v.getId() == getElementId(xD, yD) && ((Humano) v).getEquip() == null) {
                                if (((Humano) v).getEnvenenado()) {
                                    confirm.set(true);
                                    nrTurnos++;
                                    currentTeamId = 10;
                                    return;
                                }
                                if (v.getiDTipo() == 9) {
                                    confirm.set(false);
                                    return;
                                } else {
                                    temp.set(v);
                                    currentTeamId = 10;
                                    nrTurnos++;
                                    confirm.set(true);
                                    return;
                                }
                            }
                        });
                    }
                }
            });
        }
        if (temp.get() != null) {
            if (temp.get().getId() != 0) {
                criaturas.remove(temp.get());
                criaturas.add(new Zombie(temp.get().getId(), temp.get().getiDTipo() - 5, temp.get().getNome(),
                        temp.get().getX(), temp.get().getY()));
                semMortes = 0;
            } else {
                semMortes++;
            }
        } else {
            semMortes++;
        }
        criaturas.forEach(k -> {
            if (k instanceof Humano && ((Humano) k).getEnvenenado() && ((Humano) k).getTurnosEnvenenado() > 2) {
                k.setDead(true);
                foraDeJogo.add(k);
                k.setX(grid[0]);
                k.setY(grid[1]);
            }
        });
        if (confirm.get()) {
            criaturas.forEach(k -> {
                if (k instanceof Humano && ((Humano) k).getEnvenenado()) {
                    ((Humano) k).setTurnosEnvenenado();
                }
            });
        }
        return confirm.get();
    }

    public boolean gameIsOver() {
        int vivos = 0;
        for (Creature criatura : criaturas) {
            if (criatura.getX() < grid[0] && criatura.getY() < grid[1]) {
                vivos++;
            }
        }
        if (semMortes >= 12) {
            return true;
        } else if (vivos == 0) {
            return true;
        }
        return false;
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

    public List<String> getGameResults() {
        List<String> resultados = new ArrayList<>();
        resultados.add("Nr. de turnos terminados:");
        resultados.add(String.valueOf(nrTurnos));
        resultados.add("\n");
        resultados.add("Ainda pelo bairro:");
        resultados.add("\n");
        resultados.add("OS VIVOS");
        criaturas.forEach(k -> {
            if (k.getiDTipo() > 4) {
                resultados.add(k.getId() + " " + k.getNome());
            }
        });
        resultados.add("\n");
        resultados.add("OS OUTROS");
        criaturas.forEach(k -> {
            if (k.getiDTipo() < 5) {
                resultados.add(k.getId() + " (antigamente conhecido como " + k.getNome() + ")");
            }
        });
        resultados.add("\n");
        resultados.add("Num safe heaven:");
        resultados.add("\n");
        resultados.add("OS VIVOS");
        safeHeavenHumanos.forEach(k -> resultados.add(k.getId() + " " + k.getNome() + ")"));
        resultados.add("\n");
        resultados.add("Envenenados / Destruidos");
        resultados.add("\n");
        resultados.add("OS VIVOS");
        foraDeJogo.forEach(k -> {
            if (k.getiDTipo() > 4) {
                resultados.add(k.getId() + " " + k.getNome() + ")");
            }
        });
        resultados.add("\n");
        resultados.add("OS OUTROS");
        foraDeJogo.forEach(k -> {
            if (k.getiDTipo() < 5) {
                resultados.add(k.getId() + " (antigamente conhecido como " + k.getNome() + ")");
            }
        });
        return resultados;
    }

    public boolean isDay() {
        return (nrTurnos/2) % 2 == 0;
    }

    public int getEquipmentId(int creatureId) {
        AtomicInteger iD = new AtomicInteger();
        criaturas.forEach(k -> {
            if (k instanceof Humano && k.getId() == creatureId && ((Humano) k).getEquip() != null) {
                iD.set(((Humano) k).getEquip().getId());
            }
        });
        return iD.get();
    }

    public List<Integer> getIdsInSafeHaven() {
        List<Integer> iDs = new ArrayList<>();
        safeHeavenHumanos.forEach(k -> iDs.add(k.getId()));
        return iDs;
    }

    public boolean isDoorToSafeHaven(int x, int y) {
        AtomicBoolean isDoor = new AtomicBoolean();
        isDoor.set(false);
        safeHeavens.forEach(k -> {
            if (k.getX() == x && k.getY() == y) {
                isDoor.set(true);
            }
        });
        return isDoor.get();
    }

    public int getEquipmentTypeId(int equipmentId)  {
        if (equipamentos.get(equipmentId) != null) {
            return equipamentos.get(equipmentId).getTipo();
        }
        for (Creature k : criaturas) {
            if (k instanceof Humano) {
                if (((Humano) k).getEquip() != null) {
                    if (((Humano) k).getEquip().getId() == equipmentId) {
                        return ((Humano) k).getEquip().getTipo();
                    }
                }
            }
        }
        return 99;
    }

    public String getEquipmentInfo(int equipmentId) {
        if (equipamentos.get(equipmentId) != null) {
            if (equipamentos.get(equipmentId).getUsosDisponiveis() < 4) {
                return equipamentos.get(equipmentId).nomeById() + " | " +
                        equipamentos.get(equipmentId).getUsosDisponiveis();
            } else {
                return equipamentos.get(equipmentId).nomeById();
            }
        }
        for (Creature k : criaturas) {
            if (k instanceof Humano) {
                if (((Humano) k).getEquip() != null) {
                    if (((Humano) k).getEquip().getId() == equipmentId) {
                        if (((Humano) k).getEquip().getUsosDisponiveis() < 4) {
                            return ((Humano) k).getEquip().nomeById() + " | " +
                                    ((Humano) k).getEquip().getUsosDisponiveis();
                        } else {
                            return ((Humano) k).getEquip().nomeById();
                        }
                    }
                }
            }
        }
        return "";
    }

    public boolean saveGame(File fich) {
        try {
            File ficheiroCriado = fich;
            ficheiroCriado.createNewFile();
            FileWriter salvar = new FileWriter(ficheiroCriado.getName());
            salvar.write(grid[0] + " : " + grid[1] + "\n" + currentTeamId + "\n" + totalCriaturas);
            for (Creature k : criaturas) {
                salvar.write(k.getId() + " : " + k.getiDTipo() + " : " + k.getNome() + " : "
                        + k.getX() + " : " + k.getY() + "\n");
            }
            salvar.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean loadGame(File fich) {
        try {
            Scanner ficheiro = new Scanner(fich);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String[] popCultureExtravaganza() {
        String[] pop = new String[14];
        pop[0] = "Resident Evil";
        pop[1] = "Evil Dead";
        pop[2] = "I Am Legend";
        pop[3] = "I Am Legend";
        pop[4] = "Dragon Ball";
        pop[5] = "World War Z";
        pop[6] = "Mandalorians";
        pop[7] = "1972";
        pop[8] = "Kill Bill";
        pop[9] = "1978";
        pop[10] = "Bond. James Bond.";
        pop[11] = "Lost";
        pop[12] = "Chocho";
        pop[13] = "Farrokh Bulsara";
        return pop;
    }

}

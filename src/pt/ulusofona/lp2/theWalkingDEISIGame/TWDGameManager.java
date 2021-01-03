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

    public boolean move(int xO, int yO, int xD, int yD) {
        AtomicBoolean confirm = new AtomicBoolean();
        AtomicReference<Creature> temp = new AtomicReference<>();
        confirm.set(true);
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
                        safeHeavenHumanos.add(k);
                        k.setX(grid[0]);
                        k.setY(grid[1]);
                        currentTeamId = 20;
                        confirm.set(true);
                        return;
                    }
                    if (getElementId(xD, yD) < 0) {
                        if (((Humano) k).getEquip() != null) {
                            equipamentos.put(((Humano) k).getEquip().getId(),
                                    new Equipamentos(((Humano) k).getEquip().getId(),
                                            ((Humano) k).getEquip().getTipo(), xO, yO));
                        }
                        ((Humano) k).setEquip(new Equipamentos(equipamentos.get(getElementId(xD, yD)).getId(),
                                equipamentos.get(getElementId(xD, yD)).getTipo(),
                                equipamentos.get(getElementId(xD, yD)).getX(),
                                equipamentos.get(getElementId(xD, yD)).getY()));
                        k.setContarEquip();
                        equipamentos.remove(getElementId(xD, yD));
                        k.setX(xD);
                        k.setY(yD);
                        nrTurnos++;
                        semMortes++;
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
                        semMortes++;
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
                                                        temp.set(v);
                                                        currentTeamId = 20;
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
                                                            temp.set(v);
                                                            currentTeamId = 20;
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
                                                        temp.set(v);
                                                        currentTeamId = 20;
                                                        confirm.set(true);
                                                        return;
                                                    }
                                                });
                                            } else if (((Humano) k).getEquip().getUsosDisponiveis() > 4){
                                                criaturas.forEach(v -> {
                                                    if (v.getId() == getElementId(xD, yD) && v instanceof Zombie) {
                                                        foraDeJogo.add(v);
                                                        temp.set(v);
                                                        currentTeamId = 20;
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
                                    case 0: {

                                    }
                                    break;
                                    default: {

                                    }
                                    //fim switch
                                }
                            }
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
                        equipamentos.remove(getElementId(xD, yD));
                        k.setX(xD);
                        k.setY(yD);
                        nrTurnos++;
                        k.setContarEquip();
                        currentTeamId = 10;
                        confirm.set(true);
                    } else if (getElementId(xD, yD) == 0) {
                        k.setX(xD);
                        k.setY(yD);
                        nrTurnos++;
                        currentTeamId = 10;
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
        if (nrTurnos >= 12) {
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

    public int getEquipmentTypeId(int equipmentId) {
        if (equipamentos.get(equipmentId) != null) {
            return equipamentos.get(equipmentId).getTipo();
        }
        return 0;
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
        return "";
    }

    public boolean saveGame(File fich) {
        try {
            fich.createNewFile();
            FileWriter salvar = new FileWriter(fich.getName());
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
        return new String[]{};
    }



}

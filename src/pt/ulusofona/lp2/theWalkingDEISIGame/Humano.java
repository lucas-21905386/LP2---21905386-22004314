package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Humano extends Creature {
    private Equipamentos equip;
    private boolean safeHeaven, envenenado;
    private int turnosEnvenenado;

    Humano (int iD, int iDTipo, String nome, int x, int y) {
        super(iD, iDTipo, x, y, nome);
    }

    public Equipamentos getEquip() {
        return equip;
    }

    public void setEquip(Equipamentos equip) {
        this.equip = equip;
    }

    public void setSafeHeaven(boolean safeHeaven) {
        this.safeHeaven = safeHeaven;
    }

    public boolean getSafeHeaven() {
        return safeHeaven;
    }

    public void setEnvenenado(boolean envenenado) {
        this.envenenado = envenenado;
    }

    public boolean getEnvenenado() {
        return envenenado;
    }

    public void setTurnosEnvenenado() {
        this.turnosEnvenenado++;
    }

    public void resetTurnosEnvenenado() {
        this.turnosEnvenenado = 0;
    }

    public int getTurnosEnvenenado() {
        return turnosEnvenenado;
    }
}

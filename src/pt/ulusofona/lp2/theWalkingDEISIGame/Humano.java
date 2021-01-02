package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Humano extends Creature {
    private Equipamentos equip;

    Humano (int iD, int iDTipo, String nome, int x, int y) {
        super(iD, iDTipo, x, y, nome);
    }

    public Equipamentos getEquip() {
        return equip;
    }

    public void setEquip(Equipamentos equip) {
        this.equip = equip;
    }
}

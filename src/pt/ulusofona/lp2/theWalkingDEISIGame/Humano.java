package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Humano {
    private int iD, iDTipo, equipApanhados, x, y;
    private String nome, nomeEquipa;
    private Equipamentos equip;

    Humano (int iD, int iDTipo, String nome, int x, int y) {
        this.iD = iD;
        this.iDTipo = iDTipo;
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.equipApanhados = 0;
        this.nomeEquipa = "Os Vivos";
        this.equip = null;
    }

    public int getId() {
        return this.iD;
    }

    public String getImagePNG() {
        return "human.png";
    }

    public String toString() {
        return iD + " | Humano | " + nomeEquipa + " | " + nome +
                " " + equipApanhados + " @ (" + x + ", " + y +")";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getNome() {
        return nome;
    }

    public Equipamentos getEquip() {
        return equip;
    }

    public void setEquip(Equipamentos equip) {
        this.equip = equip;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setEquipApanhados() {
        this.equipApanhados++;
    }
}

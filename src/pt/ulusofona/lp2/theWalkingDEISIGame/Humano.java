package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Humano {
    int iD, iDTipo, equipApanhados, x, y;
    String nome, nomeEquipa;
    Equipamentos equip;

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
        return iD + " | " + iDTipo + " | " + nomeEquipa + " | " + nome +
                " " + equipApanhados + " @ (" + x + ", " + y +")";
    }
}

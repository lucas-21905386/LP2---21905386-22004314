package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Humano {
    int iD, iDTipo, equipApanhados, x, y;
    String nome, nomeEquipa;

    Humano (int iD, int iDTipo, String nome, int x, int y) {
        this.iD = iD;
        this.iDTipo = iDTipo;
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.equipApanhados = 0;
        this.nomeEquipa = "Os Vivos";
    }

    public int getId() {
        return 0;
    }

    public String getImagePNG() {
        return null;
    }

    public String toString() {
        return iD + " | " + iDTipo + " | " + nomeEquipa + " | " + nome +
                " " + equipApanhados + " @ (" + x + ", " + y +")";
    }
}

package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Zombie {
    int iD, iDTipo, equipDestruidos, x, y;
    String nome, nomeEquipa;

    Zombie (int iD, int iDTipo, String nome, int x, int y) {
        this.iD = iD;
        this.iDTipo = iDTipo;
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.equipDestruidos = 0;
        this.nomeEquipa = "Os Vivos";
    }

    public int getId() {
        return this.iD;
    }

    public String getImagePNG() {
        return "zombie.png";
    }

    public String toString() {
        return iD + " | " + iDTipo + " | " + nomeEquipa + " | " + nome +
                " " + equipDestruidos + " @ (" + x + ", " + y +")";
    }
}

package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Zombie {
    private int iD, iDTipo, equipDestruidos, x, y;
    private String nome, nomeEquipa;

    Zombie (int iD, int iDTipo, String nome, int x, int y) {
        this.iD = iD;
        this.iDTipo = iDTipo;
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.equipDestruidos = 0;
        this.nomeEquipa = "Os Outros";
    }

    public int getId() {
        return this.iD;
    }

    public String getImagePNG() {
        return "zombie.png";
    }

    public String toString() {
        return iD + " | Zombie | " + nomeEquipa + " | " + nome +
                " " + equipDestruidos + " @ (" + x + ", " + y +")";
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setEquipDestruidos() {
        this.equipDestruidos++;
    }
}

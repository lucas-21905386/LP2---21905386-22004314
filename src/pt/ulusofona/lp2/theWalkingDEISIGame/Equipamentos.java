package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Equipamentos {
    private int id, tipo, x, y;

    Equipamentos() {
    }
    Equipamentos (int id, int tipo, int x, int y){
        this.id = id;
        this.tipo = tipo;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return this.id;
    }

    public String getImagePNG() {
        return "equipment.png";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTipo() {
        return tipo;
    }
}

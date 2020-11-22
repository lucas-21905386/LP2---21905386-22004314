package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Equipamentos {
    int id, tipo, x, y;

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
}

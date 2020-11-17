package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.util.ArrayList;

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
        return 0;
    }

    public String getImagePNG() {
        return null;
    }

    public String toString() {
        return "<ID> | <Tipo> | <Nome Equipa> | <Nome> <Equipamentos> @ (<x>, <y>)";
    }
}

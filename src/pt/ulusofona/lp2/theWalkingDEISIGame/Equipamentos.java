package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Equipamentos {
    private int id, tipo, x, y, acao, usosDisponiveis;

    Equipamentos (int id, int tipo, int x, int y){
        this.id = id;
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        if (tipo == 1 || tipo == 2 || tipo == 6) {
            acao = 1;
        } else if (tipo == 10) {
            acao = 2;
        } else {
            acao = 0;
        }
    }

    public String getDefOf(int acao) {
        if (acao == 0) {
            return "Defensivo";
        } else if (acao == 1) {
            return "Ofensivo";
        } else {
            return "Defensivo/Ofensivo";
        }
    }

    public int getId() {
        return this.id;
    }

    public String getImagePNG() {
        switch (getTipo()) {
            case 0: return "equipment_0.png";
            case 1: return "equipment.png";
            case 2: return "gun.png";
            case 3: return "tactical_shield.png";
            case 4: return "rolled_magazine.png";
            case 5: return "garlic.png";
            case 6: return "steak.png";
            case 7: return "bleach.png";
            case 8: return "poison.png";
            case 9: return "antidote.png";
            case 10: return "beskar_helmet.png";
            default: return "unknown-piece.png";
        }
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

    public int getAcao() {
        return acao;
    }

    public int getUsosDisponiveis() {
        return usosDisponiveis;
    }

    public void setUsosDisponiveis() {
        this.usosDisponiveis--;
    }
}

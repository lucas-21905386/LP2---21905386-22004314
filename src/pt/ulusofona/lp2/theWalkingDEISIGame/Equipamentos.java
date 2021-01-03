package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Equipamentos {
    private int id, tipo, x, y, acao, usosDisponiveis, usado;

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
        createUsosDisponiveis();
    }

    public void createUsosDisponiveis() {
        switch (this.tipo) {
            case 0:
            case 8:
            case 9:
                this.usosDisponiveis = 1;
            break;
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
            case 10:
                this.usosDisponiveis = 99;
            break;
            case 2:
            case 7:
                this.usosDisponiveis = 3;
            break;
            default: this.usosDisponiveis = 0;
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

    public String nomeById() {
        switch (this.tipo) {
            case 0: return "Escudo de Madeira";
            case 1: return "Espada Hattori Hanzo";
            case 2: return "Pistola Walther PPK";
            case 3: return "Escudo Táctico";
            case 4: return "Revista Maria";
            case 5: return "Cabeça de Alho";
            case 6: return "Estaca de Madeira";
            case 7: return "Garrafa de Lixívia (1 litro)";
            case 8: return "Veneno";
            case 9: return "Antídoto";
            case 10: return "Beskar Helmet";
            default: return "Desconhecido";
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
        return this.usosDisponiveis;
    }

    public void setUsosDisponiveis() {
        this.usosDisponiveis--;
    }

    public void incrementUsosDisponiveis() {
        this.usosDisponiveis++;
    }

    public void setUsado() {
        this.usado++;
    }

    public int getUsado() {
        return usado;
    }
}

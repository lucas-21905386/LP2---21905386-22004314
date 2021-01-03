package pt.ulusofona.lp2.theWalkingDEISIGame;

public abstract class Creature {
    private int iD, iDTipo, x, y, contarEquip;
    private String nome, nomeEquipa;

    Creature (int iD, int iDTipo, int x, int y, String nome) {
        this.iD = iD;
        this.iDTipo = iDTipo;
        this.x = x;
        this.y = y;
        this.nome = nome;
        if (iDTipo > 4) {
            setNomeEquipa("Os Vivos");
        } else {
            setNomeEquipa("Os Outros");
        }
    }

    public int getId() {
        return iD;
    }

    public String tipoById(int iDTipo) {
        switch (iDTipo) {
            case 0: return "Criança (Zombie)";
            case 1: return "Adulto (Zombie)";
            case 2: return "Militar (Zombie)";
            case 3: return "Idoso (Zombie)";
            case 4: return "Zombie Vampiro";
            case 5: return "Criança (Vivo)";
            case 6: return "Adulto (Vivo)";
            case 7: return "Militar (Vivo)";
            case 8: return "Idoso (Vivo)";
            case 9: return "Cão";
            default: return "Desconhecido";
        }
    }

    public String getImagePNG() {
        switch (this.iDTipo) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                return "zombie.png";
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return "human.png";
            default: return "unknown-piece.png";
        }
    }

    public String toString(){
        if (iDTipo > 4 && ((Humano) this).getSafeHeaven()) {
            return iD + " | " + tipoById(iDTipo) + " | " + nomeEquipa + " | " + nome + " " + contarEquip +
                        " " + "@" + " " + "A salvo";
        } else {
            return iD + " | " + tipoById(iDTipo) + " | " + nomeEquipa + " | " + nome + " " + contarEquip +
                    " " + "@" + " " + "(" + x + ", " + y + ")";
        }
    }

    public void setiD(int iD) {
        this.iD = iD;
    }

    public int getiDTipo() {
        return iDTipo;
    }

    public void setiDTipo(int iDTipo) {
        this.iDTipo = iDTipo;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getContarEquip() {
        return contarEquip;
    }

    public void setContarEquip() {
        this.contarEquip++;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeEquipa() {
        return nomeEquipa;
    }

    public void setNomeEquipa(String nomeEquipa) {
        this.nomeEquipa = nomeEquipa;
    }


}

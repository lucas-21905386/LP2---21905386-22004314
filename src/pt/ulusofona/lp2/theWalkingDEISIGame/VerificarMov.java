package pt.ulusofona.lp2.theWalkingDEISIGame;

public class VerificarMov {
    public boolean crianca(int xO, int yO, int xD, int yD) {
        if (xD != xO && yD != yO || xD - xO > 1 || xD - xO < -1 || yD - yO > 1 || yD - yO < -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean adulto(int xO, int yO, int xD, int yD) {
        if (xD - xO > 2 || xD - xO < -2 || yD - yO > 2 || yD - yO < -2) {
            return false;
        } else {
            return true;
        }
    }

    public boolean militar(int xO, int yO, int xD, int yD) {
        if (xD - xO > 3 || xD - xO < -3 || yD - yO > 3 || yD - yO < -3) {
            return false;
        } else {
            return true;
        }
    }

    public boolean idoso(int xO, int yO, int xD, int yD, int tipo, boolean dia) {
        if (tipo == 8) {
            if (xD != xO && yD != yO || xD - xO > 1 || xD - xO < -1 || yD - yO > 1 || yD - yO < -1 || !dia) {
                return false;
            } else {
                return true;
            }
        } else {
            return crianca(xO, yO, xD, yD);
        }
    }

    public boolean cao(int xO, int yO, int xD, int yD) {
        if (xD == xO || yD == yO || xD + yD > xO + yO + 4) {
            return false;
        } else {
            return true;
        }
    }

    public boolean vampiro(int xO, int yO, int xD, int yD, boolean dia) {
        if (xD - xO > 2 || xD - xO < -2 || yD - yO > 2 || yD - yO < -2 || dia) {
            return false;
        } else {
            return true;
        }
    }
}

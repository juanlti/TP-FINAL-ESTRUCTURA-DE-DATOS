package trenes;

public class Riel {
    private final int codEstacionOrigen;
    private final int codEstacionDestino;
    private int distanciaKm;

    public Riel(int codEstacionOrigen, int codEstacionDestino, int distanciaKm) {
        this.codEstacionOrigen = codEstacionOrigen;
        this.codEstacionDestino = codEstacionDestino;
        this.distanciaKm = distanciaKm;
    }

    public int getCodEstacionOrigen() {
        return codEstacionOrigen;
    }

    public int getCodEstacionDestino() {
        return codEstacionDestino;
    }

    public int getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(int distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    @Override
    public String toString() {
        return "Riel{" +
                "origen=" + codEstacionOrigen +
                ", destino=" + codEstacionDestino +
                ", distanciaKm=" + distanciaKm +
                '}';
    }
}

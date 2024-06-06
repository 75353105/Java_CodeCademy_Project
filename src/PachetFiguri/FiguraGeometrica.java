package PachetFiguri;

public class FiguraGeometrica {

    private Comandat stareFigura;
    private String culoare;
    private int nrLaturi;

    public FiguraGeometrica(Comandat stareFigura, String culoare, int nrLaturi) {
        this.stareFigura = stareFigura;
        this.culoare = culoare;
        this.nrLaturi = nrLaturi;
    }

    public Comandat getStareFigura() {
        return stareFigura;
    }

    public void setStareFigura(Comandat stareFigura) {
        this.stareFigura = stareFigura;
    }

    public String getCuloare() {
        return culoare;
    }

    public void setCuloare(String culoare) {
        this.culoare = culoare;
    }

    public int getNrLaturi() {
        return nrLaturi;
    }

    public void setNrLaturi(int nrLaturi) {
        this.nrLaturi = nrLaturi;
    }

    @Override
    public String toString() {
        return "FiguraGeometrica{" +
                "stareFigura=" + stareFigura +
                ", culoare='" + culoare + '\'' +
                ", nrLaturi=" + nrLaturi +
                '}';
    }
}

package PachetFiguri;

public class Cerc extends FiguraGeometrica implements Arie,Comparable<Cerc> {
    public double raza;
    public double costPerMetruPatrat;

    public Cerc(Comandat stareFigura, String culoare, int nrLaturi, double raza, double costPerMetruPatrat) {
        super(stareFigura, culoare, nrLaturi);
        this.raza = raza;
        this.costPerMetruPatrat = costPerMetruPatrat;
    }

    public double getRaza() {
        return raza;
    }

    public void setRaza(double raza) {
        this.raza = raza;
    }

    public double getCostPerMetruPatrat() {
        return costPerMetruPatrat;
    }

    public void setCostPerMetruPatrat(double costPerMetruPatrat) {
        this.costPerMetruPatrat = costPerMetruPatrat;
    }

    @Override
    public String toString() {
        return String.format("Stare:%s----Culoare:%s----NrLaturi:%d----Suprafata:%5.2f-----Pret:%5.2f",
                getStareFigura(),getCuloare(),getNrLaturi(),calculSuprafata(),pretCerc());
    }

    @Override
    public double calculSuprafata() {
        return 3.14*3.14*raza;
    }
    public double pretCerc(){
        return calculSuprafata()*costPerMetruPatrat;
    }
    public double LungimeCerc()
    {
        return 2*3.14*raza;
    }


    @Override
    public int compareTo(Cerc o) {
        if(o.calculSuprafata()>this.calculSuprafata())
            return -1;
        else
            if(o.calculSuprafata()<this.calculSuprafata())
                return 1;
            else
                return 0;
    }
}

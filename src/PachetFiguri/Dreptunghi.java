package PachetFiguri;

public class Dreptunghi extends FiguraGeometrica implements Arie,Comparable<Dreptunghi>{
    private double lungime;
    private double latime;
    private double costPeMetruPatrat;

    public Dreptunghi(Comandat stareFigura, String culoare, int nrLaturi, double lungime, double latime, double costPeMetruPatrat) {
        super(stareFigura, culoare, nrLaturi);
        this.lungime = lungime;
        this.latime = latime;
        this.costPeMetruPatrat = costPeMetruPatrat;
    }

    public double getLungime() {
        return lungime;
    }

    public void setLungime(double lungime) {
        this.lungime = lungime;
    }

    public double getLatime() {
        return latime;
    }

    public void setLatime(double latime) {
        this.latime = latime;
    }

    public double getCostPeMetruPatrat() {
        return costPeMetruPatrat;
    }

    public void setCostPeMetruPatrat(double costPeMetruPatrat) {
        this.costPeMetruPatrat = costPeMetruPatrat;
    }

    @Override
    public String toString() {
        return String.format("%s--%s--%d laturi--%5.2f suprafata--%5.2f pret",this.getStareFigura().toString(),this.getCuloare(),this.getNrLaturi(),
                this.calculSuprafata(),this.pretDreptunghi());
    }
    public double pretDreptunghi()
    {
        return calculSuprafata()*costPeMetruPatrat;
    }
    public double Perimetru(){
        return 2*lungime+2*latime;
    }

    @Override
    public  double calculSuprafata() {
        return lungime*latime;
    }

    @Override
    public int compareTo(Dreptunghi d) {
        if(d.calculSuprafata()>this.calculSuprafata())
            return -1;
        else
            if(d.calculSuprafata()<this.calculSuprafata())
                return 1;
            else
                return 0;

    }
}

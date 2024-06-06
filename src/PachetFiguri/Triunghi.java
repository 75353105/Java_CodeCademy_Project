package PachetFiguri;

public class Triunghi extends FiguraGeometrica implements Arie,Comparable<Triunghi>{
    private double latura1;
    private double latura2;
    private double latura3;
    private double costPerMetruPatrat;

    public Triunghi(Comandat stareFigura, String culoare, int nrLaturi, double latura1, double latura2, double latura3, double costPerMetruPatrat) {
        super(stareFigura, culoare, nrLaturi);
        this.latura1 = latura1;
        this.latura2 = latura2;
        this.latura3 = latura3;
        this.costPerMetruPatrat = costPerMetruPatrat;
    }

    public double getLatura1() {
        return latura1;
    }

    public void setLatura1(double latura1) {
        this.latura1 = latura1;
    }

    public double getLatura2() {
        return latura2;
    }

    public void setLatura2(double latura2) {
        this.latura2 = latura2;
    }

    public double getLatura3() {
        return latura3;
    }

    public void setLatura3(double latura3) {
        this.latura3 = latura3;
    }

    public double getCostPerMetruPatrat() {
        return costPerMetruPatrat;
    }

    public void setCostPerMetruPatrat(double costPerMetruPatrat) {
        this.costPerMetruPatrat = costPerMetruPatrat;
    }

    @Override
    public String toString() {

        StringBuilder sb=new StringBuilder();
        sb.append("Starea: ").append(getStareFigura()).append(",");
        sb.append("Culoarea: ").append(getCuloare()).append(",");
        sb.append("Numar Laturi: ").append(getNrLaturi()).append(",");
        sb.append("Perimetrul: ").append(Perimetru()).append("m,");
        sb.append("Aria: ").append(String.format("%5.2f",calculSuprafata())).append("m^2,");
        sb.append("Pret: ").append(String.format("%5.2f",pretTriunghi())).append(" lei");
        return sb.toString();
    }
    public double calculSemiPerimetru(){
        return (latura1+latura2+latura3)/2;
    }

    @Override
    public double calculSuprafata() {
        return Math.sqrt(calculSemiPerimetru()*(calculSemiPerimetru()-latura1)*(calculSemiPerimetru()-latura2)*(calculSemiPerimetru()-latura3));
    }
    public double Perimetru(){
        return latura1+latura2+latura3;
    }
    public double pretTriunghi()
    {
        return calculSuprafata()*costPerMetruPatrat;
    }

    @Override
    public int compareTo(Triunghi o) {
        if(o.calculSuprafata()>this.calculSuprafata())
            return -1;
        else
            if(o.calculSuprafata()<this.calculSuprafata())
                return 1;
            else
                return 0;
    }
}

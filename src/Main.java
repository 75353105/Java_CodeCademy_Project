import PachetFiguri.*;

import java.util.*;
import java.io.*;
import java.sql.*;
import java.util.stream.Collectors;


public class Main {

//preluam date despre dreptunghiuri dintr-un fisier text si le punem intr-o lista
    public static List<Dreptunghi> creareListaDreptunghiuri(String caleFisier)
    throws IOException{
        List<Dreptunghi> listaDreptunghiuri=new ArrayList<>();
        FileReader fr=new FileReader(caleFisier);
        Scanner scanner=new Scanner(fr);
        while(scanner.hasNext()){
            String linie= scanner.nextLine();
            String[] valori=linie.split(",");
            String tip=valori[0];
            Comandat comandat=Comandat.valueOf(tip);
            String culoare=valori[1];
            int nrLaturi=Integer.parseInt(valori[2]);
            double lungime=Double.parseDouble(valori[3]);
            double latime=Double.parseDouble(valori[4]);
            double costPerMetruPatrat=Double.parseDouble(valori[5]);
            Dreptunghi dreptunghi=new Dreptunghi(comandat,culoare,nrLaturi,lungime,latime,costPerMetruPatrat);
            listaDreptunghiuri.add(dreptunghi);
        }
        return listaDreptunghiuri;

    }
    //preluam date despre Triunghuri dintr-un fisier text si le punem intr-un dicionar
    public static Map<String,List<Triunghi>> creeazaDIctionarTriunghiuri(String caleFisier)
    throws IOException{
        Map<String,List<Triunghi>> dictionarTriunghiuri=new HashMap<>();
        try(var fisier=new BufferedReader(new FileReader(caleFisier))){
            dictionarTriunghiuri=fisier.lines().map(linie->new Triunghi(Comandat.valueOf(linie.split(" ")[0]),linie.split(" ")[1],
                    Integer.parseInt(linie.split(" ")[2]),Double.parseDouble(linie.split(" ")[3]),
                    Double.parseDouble(linie.split(" ")[4]),Double.parseDouble(linie.split(" ")[5]),
                    Double.parseDouble(linie.split(" ")[6]))).collect(Collectors.groupingBy(Triunghi::getCuloare));
        }
        catch(Exception ex){
            System.out.println("Verifica codul scris pentru citirea din fisier!");
        }
        return dictionarTriunghiuri;
    }
    //cream o baza de date unde vom avea informatiile necesare despre cercuri
    public static void creazaBazaDeDatePentruCercuri()
    throws SQLException{

        var listaCercuri=List.of(new Cerc(Comandat.COMANDAT,"negru",0,4.5,2),
                new Cerc(Comandat.RAMAS,"alb",0,7.8,1.89),
                new Cerc(Comandat.PRIMIT,"galben",0,6.65,1.27),
                new Cerc(Comandat.COMANDAT,"rosu",0,8.88,2.16),
                new Cerc(Comandat.COMANDAT,"maro",0,9,2.31));
        try(
                var conexiune=DriverManager.getConnection("jdbc:sqlite:cercuri.db");

                )
        {
            try(var comanda=conexiune.createStatement();){
                comanda.executeUpdate("CREATE TABLE IF NOT EXISTS Cercuri ("+
                        "StareFigura TEXT,"+"Culoare TEXT,"+"NumarLatrui INTEGER,"+"Raza REAL,"+"PretPerMediuPatrat REAL"+")");
                comanda.executeUpdate("DELETE FROM Cercuri");
            }

            for(var cerc:listaCercuri){
                String stareFigura=cerc.getStareFigura().toString();
                String culoare=cerc.getCuloare();
                int nrLaturi=cerc.getNrLaturi();
                double raza= cerc.getRaza();
                double pret=cerc.getCostPerMetruPatrat();

                String insertQuery="INSERT INTO Cercuri VALUES(?,?,?,?,?)";
                try(var inserare=conexiune.prepareStatement(insertQuery)){
                    inserare.setString(1,stareFigura);
                    inserare.setString(2,culoare);
                    inserare.setInt(3,nrLaturi);
                    inserare.setDouble(4,raza);
                    inserare.setDouble(5,pret);
                    inserare.executeUpdate();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        catch(Exception ex){
        ex.printStackTrace();
        }

    }

    //Luam informatiile din baza de date si le punem intr-o lista de obiecte de tip cerc
    public static List<Cerc> creareListaCercuri() throws SQLException{
        List<Cerc> listaCercuri=new ArrayList<>();
        try(var conexiune=DriverManager.getConnection("jdbc:sqlite:cercuri.db");
        var comanda=conexiune.createStatement();
        var rezultat=comanda.executeQuery("SELECT * FROM Cercuri");){
            while(rezultat.next()){
                var cerc=new Cerc(Comandat.valueOf(rezultat.getString(1)), rezultat.getString(2), rezultat.getInt(3),
                        rezultat.getDouble(4),rezultat.getDouble(5));
                listaCercuri.add(cerc);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return listaCercuri;
    }


    public static void main(String[] args)
    throws IOException,SQLException{
        //facem o lista de dreptunghiuri, iar elementele acesteia se vor lua dintr-un fisier text
        List<Dreptunghi> listaDreptunghiuri=new ArrayList<>();
        listaDreptunghiuri=creareListaDreptunghiuri("C:\\Users\\Gucci\\Desktop\\Java_exercitiile mele\\CoCo_Project\\fisiere\\dreptunghiuri.txt");


        //Facem dictionarul de triunghiuri cu date din fisier text
        Map<String,List<Triunghi>> dictionarTriunghiuri=creeazaDIctionarTriunghiuri("C:\\Users\\Gucci\\Desktop\\Java_exercitiile mele\\CoCo_Project\\fisiere\\triunghiuri.txt");

        //Cream baza de date in SQLite si apoi punem elementele din tabelul Cerc in o lista
        creazaBazaDeDatePentruCercuri();
        List<Cerc> listaCercuri=creareListaCercuri();


        //Afisam dreptunghiurile care genereaza venituri
        System.out.println("Afisam dreptunghiurile care genereaza venituri companiei");
        listaDreptunghiuri.stream().filter(dreptunghi-> dreptunghi.getStareFigura()==Comandat.COMANDAT).forEach(dreptunghi->System.out.println(dreptunghi));


        //Afisam triunghiurile care genereaza pierderi in ordine descrescatoare pe baza suprafetei
        System.out.println("\nAfisam triunghiurile care genereaza pierderi in ordine descrescatoare pe baza suprafetei");

        dictionarTriunghiuri.entrySet().forEach(entry -> {
            List<Triunghi> triangles = entry.getValue();
            triangles.sort(Comparator.comparingDouble(Triunghi::calculSuprafata).reversed());
            entry.setValue(triangles);
        });

        for(Map.Entry<String,List<Triunghi>> rand:dictionarTriunghiuri.entrySet()){
            List<Triunghi> lista=rand.getValue();
            for(var element:lista){
                if(element.getStareFigura()==Comandat.PRIMIT)
                    System.out.println(element);
            }
        }

        //Afisam cercurile care nu au fost nici vandute, nici cumparate in ordine crescatoare a pretului
        System.out.println("\nAfisam cercurile care nu au fost nici vandute, nici cumparate in ordine crescatoare a pretului");
        listaCercuri.stream().filter(cerc->cerc.getStareFigura()==Comandat.RAMAS).
                sorted(Comparator.comparingDouble(Cerc::pretCerc)).forEach(cerc->System.out.println(cerc));

        //Verificam daca cumva am obtinut profit sau pierderi prin vanzari. Produsele comandate se vor inregistra ca si profit, cele primite ca pierderi
        //si cele ramase cu 0.

        double profit=0;
        for(var dreptunghi:listaDreptunghiuri){
            if(dreptunghi.getStareFigura()==Comandat.COMANDAT)
                profit=profit+dreptunghi.pretDreptunghi();
            else
                if(dreptunghi.getStareFigura()==Comandat.PRIMIT)
                    profit=profit-dreptunghi.pretDreptunghi();
        }
        for(var cerc:listaCercuri){
            if(cerc.getStareFigura()==Comandat.COMANDAT)
                profit=profit+cerc.pretCerc();
            else
                if(cerc.getStareFigura()==Comandat.PRIMIT)
                    profit=profit-cerc.pretCerc();
        }

        for(Map.Entry<String,List<Triunghi>> rand:dictionarTriunghiuri.entrySet()){
            List<Triunghi> lista=rand.getValue();
            for(var triunghi:lista){
                if(triunghi.getStareFigura()==Comandat.COMANDAT)
                    profit=profit+triunghi.pretTriunghi();
                else
                if(triunghi.getStareFigura()==Comandat.PRIMIT)
                    profit=profit-triunghi.pretTriunghi();
            }
        }
        System.out.println("\n");
        if(profit>0)
            System.out.println("Se inregistreaza un profit de: "+profit+"  lei.");
        else
            if(profit<0)
                System.out.println("Se inregistreaza o pierdere de: "+profit+"  lei.");
        else
            System.out.println("Nu avem nici profit, nici pierderi!");
        //Sa se puna intr-un dictionar profitul/pierderea pe baza culorii obiectelor

        class ProfitPerCuloare{
            private String Culoare;
            public double profit;

            public ProfitPerCuloare(String culoare, double profit) {
                Culoare = culoare;
                this.profit = profit;
            }

            public ProfitPerCuloare(String culoare) {
                Culoare = culoare;
            }

            public String getCuloare() {
                return Culoare;
            }

            public double getProfit() {
                return profit;
            }

            @Override
            public String toString() {
                return String.format("Culoare:%s-----Profit/Pierdere:%5.2f",getCuloare(),getProfit());
            }
        }

        Map<String,ProfitPerCuloare> preturiPerCuloare=new HashMap<>();
        for(var dreptunghi:listaDreptunghiuri){
            if(!preturiPerCuloare.containsKey(dreptunghi.getCuloare())){
                preturiPerCuloare.put(dreptunghi.getCuloare(), new ProfitPerCuloare(dreptunghi.getCuloare()));
            }
            if(dreptunghi.getStareFigura()==Comandat.COMANDAT){
                preturiPerCuloare.get(dreptunghi.getCuloare()).profit+=dreptunghi.pretDreptunghi();
            }
            else
            if(dreptunghi.getStareFigura()==Comandat.PRIMIT){
                preturiPerCuloare.get(dreptunghi.getCuloare()).profit-=dreptunghi.pretDreptunghi();
            }
        }
        for(var cerc:listaCercuri){
            if(!preturiPerCuloare.containsKey(cerc.getCuloare())){
                preturiPerCuloare.put(cerc.getCuloare(), new ProfitPerCuloare(cerc.getCuloare()));
            }
            if(cerc.getStareFigura()==Comandat.COMANDAT){
                preturiPerCuloare.get(cerc.getCuloare()).profit+=cerc.pretCerc();
            }
            else
            if(cerc.getStareFigura()==Comandat.PRIMIT){
                preturiPerCuloare.get(cerc.getCuloare()).profit-=cerc.pretCerc();
            }
        }
        for(Map.Entry<String,List<Triunghi>> rand:dictionarTriunghiuri.entrySet()){
            List<Triunghi> lista=rand.getValue();

            for(var triunghi:lista){
                if(!preturiPerCuloare.containsKey(triunghi.getCuloare())){
                    preturiPerCuloare.put(triunghi.getCuloare(), new ProfitPerCuloare(triunghi.getCuloare()));
                }
                if(triunghi.getStareFigura()==Comandat.COMANDAT){
                    preturiPerCuloare.get(triunghi.getCuloare()).profit+=triunghi.pretTriunghi();
                }
                else
                if(triunghi.getStareFigura()==Comandat.PRIMIT){
                    preturiPerCuloare.get(triunghi.getCuloare()).profit-=triunghi.pretTriunghi();
                }
            }
        }
        System.out.println("\nAfisam castigurile/pierderile pe fiecare culoare!");

        preturiPerCuloare.values().stream().forEach(castig->System.out.println(castig.Culoare+"--------  "+String.format("%5.2f",castig.getProfit())+" lei"));
    }

}
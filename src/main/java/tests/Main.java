package tests;

import services.LabService;


public class Main {
    public static void main(String[] args) {

        //DataSource ds1=DataSource.getInstance();
        //System.out.println(ds1);
        //*******************************test***************
        /*Personne p1=new Personne("ghass","ba");
        PersonneService ps=new PersonneService();
        ps.addPST(p1);
        ps.getAll().forEach(System.out::println);*/
        //*******************************addLab***********************************
        //Lab l1=new Lab("Laboratoire","Ariana",27898852,Time.valueOf("08:00:00"),Time.valueOf("20:00:00"));
        LabService labo = new LabService();
        //lab.add(l1);
        //***************************updateLab********************
        /*int id =4; // Par exemple
        Lab l2 = new Lab("Laboratoire", "Ariana", 27000000, Time.valueOf("08:00:00"), Time.valueOf("18:00:00"));
        lab.update(l2, id);*/
        //*********************afficherbyId_lab**************************
        /*Lab laboratoireRecupere = labo.getById(id);
        if (laboratoireRecupere != null) {
            System.out.println("Nom du laboratoire: " + laboratoireRecupere.getNom());
            System.out.println("Adresse du laboratoire: " + laboratoireRecupere.getAdresse());
            System.out.println("Numéro de téléphone du laboratoire: " + laboratoireRecupere.getNtel());
            System.out.println("Heure de début du laboratoire: " + laboratoireRecupere.getHdebut());
            System.out.println("Heure de fin du laboratoire: " + laboratoireRecupere.getHfin());
        } else {
            System.out.println("Aucun laboratoire trouvé avec l'identifiant : " + id);
        }*/
        //*********************afficher_tout_les_laboratoires**************************
        /*List<Lab> allLabs = labo.getAll();

        for (Lab lab : allLabs) {
            System.out.println("ID : " + lab.getId());
            System.out.println("Nom : " + lab.getNom());
            System.out.println("Adresse : " + lab.getAdresse());
            System.out.println("Numéro de téléphone : " + lab.getNtel());
            System.out.println("Heure de début : " + lab.getHdebut());
            System.out.println("Heure de fin : " + lab.getHfin());
            System.out.println("--------------------------------------");
        }*/
        //*********************supprimer_Laboratoire**************************
        /*int id =4;
        labo.delete(id);*/
        //*********************ajouter_analyse**************************
        /*int id =3;
        LabService labService = new LabService();
        Lab laboratoire = labService.getById(id);
        if (laboratoire != null) {
            // Création d'un nouvel objet Analyse
            Analyse a1=new Analyse("analyse","Urinaire","test de sang","il faut faire attention",laboratoire);
            AnalyseService analyse=new AnalyseService();
            analyse.addAnalyse(a1,id);
        } else {
            System.out.println("Aucun laboratoire trouvé avec l'identifiant : " + id);
        }*/
        //*********************update_analyse**************************
        /*int id =13; // Par exemple
        Analyse a1=new Analyse("analyseeee","Urinaire","test de sang","il faut faire attention");
        AnalyseService analyse=new AnalyseService();
        analyse.update(a1, id);*/
        //*********************afficher_par_idanalyse**************************
        /*Analyse an = analyse.getById(id);
        System.out.println("Analyse trouvée : " + an);*/
        //*********************afficher_tout_les_analyses**************************
        /*List<Analyse> analyses = analyse.getAll();
        for (Analyse an : analyses) {
            System.out.println("Analyse : " + an);
        }*/
    }
}

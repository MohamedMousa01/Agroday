package main.java.model;

public class Agricoltore extends Utente {

    private final String citta;
    private final String idAgricoltore;

    public Agricoltore(String idUtente, String nome, String cognome, String username, String email, String password, String citta) {
        super(nome, cognome, username, email, password, idUtente);
        this.citta = citta;
        this.idAgricoltore = idUtente;
    }

    public String getCitta(){
        return citta;
    }

}
package main.java.model;

public class Agricoltore extends Utente {

    private final String citta;

    public Agricoltore(String idUtente, String nome, String cognome, String username, String email, String password, String citta) {
        super(nome, cognome, username, email, password,idUtente);
        this.citta = citta;
    }

    public String getCitta(){
        return citta;
    }

}
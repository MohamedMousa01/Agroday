package it.agrounit.model;

public class Agricoltore extends Utente {

    public Agricoltore(String idUtente, String nome, String cognome, String username, String email, String password) {
        super(nome, cognome, username, email, password, idUtente);

    }
}

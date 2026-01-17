package main.java.engclasses.beans;

import main.java.misc.PersistenceType;
import main.java.misc.TipoUtente;

public class RegistrazioneBean {

        private String nome;
        private String cognome;
        private String username;
        private String email;
        private String password;
        private String confirmPassword;
        private String citta;

        private PersistenceType tipoPersistenza;
        private TipoUtente tipoUtente;

        //_________________________________________________

        public PersistenceType getPersistenceType() {
            return tipoPersistenza;
        }
        public void setPersistenceType(PersistenceType tipoPersistenza){
            this.tipoPersistenza = tipoPersistenza;
        }

        public TipoUtente getTipoUtente(){
            return tipoUtente;
        }
        public void setTipoUtente(TipoUtente tipoUtente){
            this.tipoUtente = tipoUtente;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCognome() {
            return cognome;
        }

        public void setCognome(String cognome) {
            this.cognome = cognome;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

        public String getCitta(){
            return citta;
        }

        public void setCitta(String citta){
            this.citta = citta;
        }

}

package engclasses.beans;

import misc.PersistenceType;

public class LoginBean {

        private String username;
        private String password;
        private PersistenceType persistenzaLogin;

        // Getter per l'username
        public String getUsername() {
            return username;
        }

        // Setter per l'username
        public void setUsername(String username) {
            this.username = username;
        }

        // Getter per la password
        public String getPassword() {
            return password;
        }

        // Setter per la password
        public void setPassword(String password) {
            this.password = password;
        }

        public PersistenceType getPersistenzaLogin(){ return persistenzaLogin;}

        public void setPersistenzaLogin( PersistenceType persistenzaLogin){ this.persistenzaLogin = persistenzaLogin;}

    }


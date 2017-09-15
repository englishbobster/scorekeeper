package stos.keeper.model.player;

import java.time.ZonedDateTime;

public class Player {
    private int id;
    private String userName;
    private String password;
    private byte[] passwordSalt;
    private String email;
    private boolean hasPaid;
    private ZonedDateTime created;


    private Player(int id, String userName, String password, byte [] passwordSalt, String email, boolean hasPaid, ZonedDateTime created) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.passwordSalt = passwordSalt;
        this.email = email;
        this.hasPaid = hasPaid;
        this.created = created;
    }

    public static PlayerBuilder builder() {
        return new PlayerBuilder();
    }

    public int getId() {
        return id;
    }
    public boolean hasPaid() {
        return hasPaid;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getSalt() {
        return passwordSalt;
    }

    public String getEmail() {
        return email;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Player)) {
            return false;
        } else {
            Player that = (Player) obj;
            if (this.userName.equals(that.userName)
                    && this.password.equals(that.password)
                    && this.email.equals(that.email)
                    && this.created.equals(that.created)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + userName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + created.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.userName +
                ":" +
                this.password +
                ":" +
                this.email +
                ":" +
                (hasPaid ? "PAID" : "NOTPAID") +
                ":" +
                created.toString();
    }

    public Player withId(int id) {
        return this.id == id ? this : new Player(id, userName, password, passwordSalt, email, hasPaid, created);
    }

    public Player withHashedPasswordAndSalt(String hashedPassword, byte[] passwordSalt) {
        return this.password.equals(hashedPassword) ? this : new Player(id, userName, hashedPassword, passwordSalt, email, hasPaid, created);
    }

    public static class PlayerBuilder {
        private int id = 0;
        private String userName;
        private String password;
        private byte[] passwordSalt = {0,0,0,0};
        private String email;
        private boolean hasPaid;
        private ZonedDateTime created;

        public PlayerBuilder id(int id) {
            this.id = id;
            return this;
        }

        public PlayerBuilder username(String userName) {
            this.userName = userName;
            return this;
        }

        public PlayerBuilder password(String password) {
            this.password = password;
            return this;
        }

        public PlayerBuilder passwordSalt(byte[] passwordSalt) {
            this.passwordSalt = passwordSalt;
            return this;
        }

        public PlayerBuilder email(String email) {
            this.email = email;
            return this;
        }

        public PlayerBuilder hasPaid(boolean hasPaid) {
            this.hasPaid = hasPaid;
            return this;
        }

        public PlayerBuilder created(ZonedDateTime created) {
            this.created = created;
            return this;
        }

        public Player build() {
            return new Player(id, userName, password, passwordSalt, email, hasPaid, created);
        }
    }
}

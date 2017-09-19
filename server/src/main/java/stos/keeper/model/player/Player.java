package stos.keeper.model.player;

import java.time.ZonedDateTime;

public class Player {
    private int id;
    private String username;
    private String password;
    private String passwordSalt;
    private String email;
    private boolean hasPaid;
    private ZonedDateTime created;


    private Player(int id, String username, String password, String passwordSalt, String email, boolean hasPaid, ZonedDateTime created) {
        this.id = id;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
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
            if (this.username.equals(that.username)
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
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + created.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.username +
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
        return this.id == id ? this : new Player(id, username, password, passwordSalt, email, hasPaid, created);
    }


    public static class PlayerBuilder {
        private int id = 0;
        private String username;
        private String password;
        private String passwordSalt;
        private String email;
        private boolean hasPaid;
        private ZonedDateTime created;

        public PlayerBuilder id(int id) {
            this.id = id;
            return this;
        }

        public PlayerBuilder username(String userName) {
            this.username = userName;
            return this;
        }

        public PlayerBuilder password(String password) {
            this.password = password;
            return this;
        }

        public PlayerBuilder passwordSalt(String passwordSalt) {
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
            return new Player(id, username, password, passwordSalt, email, hasPaid, created);
        }
    }
}

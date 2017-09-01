package stos.keeper.model.player;

import java.time.ZonedDateTime;

public class Player {
    private String userName;
    private String password;
    private String email;
    private boolean hasPaid;
    private ZonedDateTime created;


    private Player(String userName, String password, String email, boolean hasPaid, ZonedDateTime created) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.hasPaid = hasPaid;
        this.created = created;
    }

    public static PlayerBuilder builder() {
        return new PlayerBuilder();
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
        StringBuilder builder = new StringBuilder();
        builder.append(this.userName);
        builder.append(":");
        builder.append(this.password);
        builder.append(":");
        builder.append(this.email);
        builder.append(":");
        builder.append(hasPaid ? "PAID" : "NOTPAID");
        builder.append(":");
        builder.append(created.toString());
        return builder.toString();
    }

    public static class PlayerBuilder {
        private String userName;
        private String password;
        private String email;
        private boolean hasPaid;
        private ZonedDateTime created;

        public PlayerBuilder username(String userName) {
            this.userName = userName;
            return this;
        }

        public PlayerBuilder password(String password) {
            this.password = password;
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
            return new Player(userName, password, email, hasPaid, created);
        }
    }
}

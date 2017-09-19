package stos.keeper.sparkServer.api.messages;

import java.time.ZonedDateTime;

public class RegisterPlayerRequest {
    private String username;
    private String password;
    private String email;
    private ZonedDateTime created;


    public RegisterPlayerRequest(String username, String password, String email, ZonedDateTime created) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.created = created;
    }


    public String getUsername() {
        return username;
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
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof RegisterPlayerRequest)) {
            return false;
        } else {
            RegisterPlayerRequest that = (RegisterPlayerRequest) object;
            if (this.username.equals(that.username) &&
                    this.password.equals(that.password) &&
                    this.email.equals(that.email) &&
                    this.created.equals(that.created)) {
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
}

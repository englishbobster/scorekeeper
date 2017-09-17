package stos.keeper.sparkServer.api.messages;

import java.time.ZonedDateTime;

public class RegisterPlayerRequest {
    private String userName;
    private String password;
    private String email;
    private ZonedDateTime created;


    public RegisterPlayerRequest(String userName, String password, String email, ZonedDateTime created) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.created = created;
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
            if (this.userName.equals(that.userName) &&
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
        result = 31 * result + userName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + created.hashCode();
        return result;
    }
}

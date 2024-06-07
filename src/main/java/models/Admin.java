package models;

public class Admin {
    private int id;
    private String username;
    private String password;
    private String role;
    private String email;
    private boolean is_blocked;

    public Admin(String username, String password, String role, String email, boolean is_blocked) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.is_blocked = is_blocked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(boolean is_blocked) {
        this.is_blocked = is_blocked;
    }
}

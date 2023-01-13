package Client;

public record User(String username, String password) {

    public String getUsername() {
        return username;
    }
}

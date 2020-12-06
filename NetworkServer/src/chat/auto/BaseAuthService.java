package chat.auto;

import chat.User;

import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {

    private static final List<User> clients = List.of(
            new User("user1", "1111", "Алекс"),
            new User("user2", "2222", "Шерлок"),
            new User("user3", "3333", "Алиса")
    );


    public String getUsernameByLoginAndPassword(String login, String password) {
        for (User client : clients) {
            if(client.getLogin().equals(login) & client.getPassword().equals(password) ) {
                return client.getUsername();
            }
        }
        return null;
    }
}

package application.user;

import java.util.HashMap;
import java.util.Map;

public class Users {
    private Map<String, User> map;

    public Users() {
        map = new HashMap<>();
    }

    public void register(User user) {
        User existingUser = findByUserName(user.getUserName());
        if(existingUser != null) {
            throw new UserDuplicateException();
        }
        user.setLoggedIn(false);
        map.put(user.getUserName(), user);
    }

    public User findByUserName(String userName) {
        return map.get(userName);
    }

    public void logIn(String username, String password) {

        User user = findByUserName(username);
        if(user == null){
            throw new LogInErrorException();
        }

        if(password != user.getPassword()){
            throw new LogInErrorException();
        }
        user.setLoggedIn(true);
    }

    public void logOut(String userName) {

        User user = findByUserName(userName);
        if (user == null) {
            throw new LogOutErrorException();
        }

        if (!user.isLoggedIn()) {
            throw new LogOutErrorException();
        }
        user.setLoggedIn(false);
    }
}

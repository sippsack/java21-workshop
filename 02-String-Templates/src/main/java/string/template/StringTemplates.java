package string.template;


import java.sql.*;

/**
 * The template expression STR."My name is \{name}" consists of:
 *
 * A template processor (STR);
 * A dot character (U+002E), as seen in other kinds of expressions; and
 * A template ("My name is \{name}") which contains an embedded expression (\{name}).
 */
public class StringTemplates {

    record Role(String name) {}
    record User(String email, Role role, boolean enabled){
        User(String email, Role role) {
            this(email, role, true);
        }
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        User user = new User("dieter@develop.de", new Role("admin"));
        String info = STR."User '\{user.email()}' hat die Rolle '\{user.role().name()}'";
        System.out.println(info); // User 'dieter@develop.de' hat die Rolle 'admin'

    }
}
package string.template;


import java.sql.*;
import java.time.LocalDate;

import static java.lang.StringTemplate.RAW;
import static java.util.FormatProcessor.FMT;

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

        // STR
        String info = STR."User '\{user.email()}' hat die Rolle '\{user.role().name()}'";
        System.out.println(info); // User 'dieter@develop.de' hat die Rolle 'admin'

        // FMT
        String s = FMT."Zahl: %10.2f\{ 3.00911 }" ;
        System.out.println(s);

        // Text Blocks
        var s2 = STR."""
            Zeile 1: \{ "abc" }
            Zeile 2: \{s}""";
        System.out.println(s2);

        // RAW
        internals();
    }

    private static void internals() {
        System.out.println("\n\nInternals:\n");
        System.out.println(STR."Today is day \{ LocalDate.now().getDayOfYear()} of year \{LocalDate.now().getYear()}.");
        StringTemplate template = StringTemplate.of("");
        System.out.println(template.fragments());
        System.out.println(template.values());

        template = RAW."Today is day \{LocalDate.now().getDayOfYear()} of year \{LocalDate.now().getYear()}.";
        System.out.println(template.fragments());
        System.out.println(template.values());
        String result = template.process(STR);
        System.out.println(result);
    }

}
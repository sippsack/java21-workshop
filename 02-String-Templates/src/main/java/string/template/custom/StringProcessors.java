package string.template.custom;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.StringTemplate.RAW;

public class StringProcessors {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        customJsonProcessor();
        customSQLProcessing();
    }


    record Role(String name) {}
    record User(String email, Role role, boolean enabled){
        User(String email, Role role) {
            this(email, role, true);
        }
    }

    record Point(int x, int y) {}
    record Rectangle(Point p1, Point p2) {};

    private static void customJsonProcessor() {
        // new Template Processor
        var JSON = StringTemplate.Processor.of(
                (StringTemplate template) -> new JSONObject(template.interpolate())
        );

        User user = new User("dieter@develop.de", new Role("admin"));

        JSONObject json = JSON."""
            {
              "user": "\{
                // We only want to use the email
                user.email()
                }",
              "roles": "\{user.role().name()}"
            }
        """;

        // {"roles":"admin","user":"dieter@develop.de"}
        System.out.println(json);


        StringTemplate.Processor<JSONObject, JSONException> JSON2 = template -> {
            String quote = "\"";
            List<Object> newValues = new ArrayList<>();

            for (Object value : template.values()) {
                if (value instanceof String str) {
                    // SANITIZE STRINGS
                    // the many backslashes look weird, but it's the correct regex
                    str = str.replaceAll(quote, "\\\\\"");
                    newValues.add(quote + str + quote);
                }
                else if (value instanceof Number || value instanceof Boolean) {
                    newValues.add(value);
                }
                // TODO: support more types
                else {
                    throw new JSONException("Invalid value type");
                }
            }

            var result = StringTemplate.interpolate(template.fragments(), newValues);
            return new JSONObject(result);
        };

        JSONObject json2 = JSON2."""
            {
              "user": \{
                // We only want to use the email
                user.email()
                },
              "roles": \{user.role().name()}
            }
        """;

        // {"roles":"admin","user":"dieter@develop.de"}
        System.out.println(json2);
    }

    private static void customSQLProcessing() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:~/test-string-templates", "sa","");

        Statement stmt = connection.createStatement();
        String create = """
                DROP TABLE IF EXISTS USERS;
                CREATE TABLE IF NOT EXISTS USERS
                (id INTEGER not NULL,
                email VARCHAR(255), 
                enabled BOOLEAN, 
                PRIMARY KEY ( id ));""";
        stmt.executeUpdate(create);

        String insert = """
                INSERT INTO USERS (id, email, enabled)
                VALUES (1, 'dieter@develop.de', true);
                INSERT INTO USERS (id, email, enabled)
                VALUES (2, 'hugo@boss.de', true);
                INSERT INTO USERS (id, email, enabled)
                VALUES (3, 'theo@tester.de', false);""";
        stmt.executeUpdate(insert);

        User user = new User("dieter@develop.de", new Role("admin"));
        StringTemplate.Processor<ResultSet, SQLException> SQL = template -> {
            String sql = template.fragments().stream().collect(Collectors.joining(" ? "));
            System.out.println(sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int index = 1;
            for (var value : template.values()) {
                switch (value) {
                    case Integer i -> preparedStatement.setInt(index++, i);
                    case Boolean b -> preparedStatement.setBoolean(index++, b);
                    default -> preparedStatement.setString(index++, value.toString());
                }
            }
            return preparedStatement.executeQuery();
        };

        ResultSet result = SQL."select count(*) as total from Users";
        result.next();
        System.out.println(STR."Number of records: \{result.getInt("total")}");
        result = SQL."select id, email, enabled from Users u where u.email = \{user.email()} and u.enabled = \{user.enabled()};";
        result.next();
        System.out.println(STR."Found: \{result.getString("email")}");
    }

}

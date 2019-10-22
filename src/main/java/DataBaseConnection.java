import javax.faces.context.FacesContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

class DataBaseConnection {
    private String db_url;
    private String user;
    private String pass;
    private Connection connection;
    private DBqueries dbq;

    /***
     * Инициализация соединения с БД
     */
    DataBaseConnection() {
        try  {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = classLoader.getResourceAsStream("application.properties");
            Properties property = new Properties();

            property.load(input);

            user = property.getProperty("DB.username");
            pass = property.getProperty("DB.userPassword");

            String address = property.getProperty("DB.address");
            String port = property.getProperty("DB.port");
            String dbname = property.getProperty("DB.name");

            db_url = String.format("jdbc:postgresql://%s:%s/%s", address, port, dbname);

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(db_url, user, pass);

            dbq = new DBqueries();

            System.out.println("Соединение с БД успешно установлено");
        } catch (IOException e) {
            System.err.println("Не удалось загрузить настройки приложения");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC драйвер не найден.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Не удалось устновить соединение с БД.");
            e.printStackTrace();
        }
    }


    /***
     * Метод последовательно загружает всех пользователей из БД в TableBean
     */
    void loadUsers() {
        try {
            ResultSet set = dbq.loadUsers.executeQuery();

            while (set.next()) {
                int id = set.getInt("id");
                String name = set.getString("name");
                String surname = set.getString("surname");
                String address = set.getString("address");
                Date birthDate = new Date(set.getDate("birthDate").getTime()+3600*6*1000);

                TableBean.getInstance().loadUser(new User(id, name, surname, address, birthDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * Добавить нового пользователя в БД
     * @return ID нового пользователя или -1, если не удалось добавить пользователя
     */
    int addUser(User user) {
        try {
            dbq.addUser.setString(1, user.getName());
            dbq.addUser.setString(2, user.getSurname());
            dbq.addUser.setString(3, user.getAddress());
            dbq.addUser.setDate(4, new Date(user.getBirthDate().getTime()));

            dbq.addUser.executeUpdate();

            ResultSet set = dbq.getCurrentId.executeQuery();

            set.next();

            return set.getInt("currval");
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /***
     * Обновить запись существующего пользователя в БД
     * @return true в случае, если удалось обновить информацию о пользователе, иначе - false
     */
    boolean updateUser(User user) {
        try {
            dbq.updateUser.setString(1, user.getName());
            dbq.updateUser.setString(2, user.getSurname());
            dbq.updateUser.setString(3, user.getAddress());
            dbq.updateUser.setDate(4, new Date(user.getBirthDate().getTime()));
            dbq.updateUser.setInt(5, user.getId());

            dbq.updateUser.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /***
     * Удалить запись о пользователе из БД
     * @param id - ID пользователя, инофрмацию о котором надо удалить
     */
    void removeUser(int id) {
        try {
            dbq.removeUser.setInt(1, id);
            dbq.removeUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * Вспомогательный класс, содеражащий в себе подготовленные запросы к БД
     */
    class DBqueries {
        /***
         * Запрос для извлечения таблицы пользователей из БД
         */
        private PreparedStatement loadUsers = connection.prepareStatement
                ("SELECT * FROM users ORDER BY id;");

        /***
         * Запрос для обновления записи о пользователе в БД
         */
        private PreparedStatement updateUser = connection.prepareStatement
                ("UPDATE users SET name=(?), surname=(?), address=(?), birthdate=(?) WHERE id=(?);");

        /***
         * Запрос для добавления записи о пользователе в БД
         */
        private PreparedStatement addUser = connection.prepareStatement
                ("INSERT INTO users(name, surname, address, birthdate) VALUES(?, ?, ?, ?);");

        /***
         * Запрос для получения ID последнего созданного в БД пользователя
         */
        private PreparedStatement getCurrentId = connection.prepareStatement
                ("SELECT currval(pg_get_serial_sequence('users', 'id'));");

        /***
         * Запрос для удаления записи о пользователе из БД по ID
         */
        private PreparedStatement removeUser = connection.prepareStatement
                ("DELETE FROM users WHERE id=(?);");

        DBqueries() throws SQLException { }
    }
}

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.*;

/***
 * Бин, отвечающий за отображение и изменение таблицы пользователей
 */
@ManagedBean(name = "tableBean", eager = true)
@ApplicationScoped
public class TableBean {

    private Map<Integer, User> users;
    private DataBaseConnection DBConnection;

    private static TableBean instance;

    /***
     * Загрузка данных из БД при инициализации бина
     */
    @PostConstruct
    private void init() {
        instance = this;
        users = new HashMap<>();
        DBConnection = new DataBaseConnection();

        DBConnection.loadUsers();
    }

    static TableBean getInstance() {
        return instance;
    }

    /***
     * Изменить запись о пользователе в таблице и БД
     */
    synchronized boolean setUser(int id, User user) {
        if (DBConnection.updateUser(user)) {
            users.put(id, user);
            return true;
        } else
            return false;
    }

    /***
     * Добавление в таблицу пользователя, загруженного из БД
     */
    void loadUser(User user) {
        users.put(user.getId(), user);
    }

    /***
     * Добавить в таблицу и БД нового пользователя
     */
    synchronized void addUser(User user) {
        int id = DBConnection.addUser(user);
        users.put(id, user);
    }

    /***
     * Метод, преобразующий Map в List, необходим для вывода информации о пользователях в таблицу
     */
    public List<Map.Entry<Integer, User>> getUsersList() {
        Set<Map.Entry<Integer, User>> managerSet = users.entrySet();
        List<Map.Entry<Integer, User>> list = new ArrayList<>(managerSet);

        list.sort((entry1, entry2) -> entry1.getKey() > entry2.getKey() ? 1 : -1);

        return list;
    }

    Map<Integer, User> getUsers() {
        return users;
    }

    /***
     * Удалить из таблицы и БД запись о пользователе
     */
    synchronized User deleteUser(Integer id) {
        DBConnection.removeUser(id);
        return users.remove(id);
    }
}

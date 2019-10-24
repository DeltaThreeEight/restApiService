import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/***
 * Класс, отвечающий за получение списка пользователей и его обновление
 */
@RequestScoped
public class UsersListProducer {

    private List<User> users;

    private Query query;

    @Inject
    private EntityManager em;

    @Produces
    @Named
    public List<User> getUsers() {
        return users;
    }

    /***
     * Метод-слушатель, обновляющий список пользователей после операции, модифицировавшей таблицу
     */
    @SuppressWarnings("unchecked")
    public void onMemberListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final User user) {
        users = query.getResultList();
        users.sort((u1, u2) -> u1.getId() > u2.getId() ? 1 : -1);
    }

    /***
     * Загрузка всех пользователей из БД при инициализации
     */
    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        query = em.createQuery("select p from User p");
        users = query.getResultList();
        users.sort((u1, u2) -> u1.getId() > u2.getId() ? 1 : -1);
    }
}

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/***
 * Класс, отвечающий за взаимодействие с БД при помощи ORM
 */
@Stateless
public class UserManagement {
    @Inject
    private EntityManager em;

    @Inject
    private Event<User> userEvent;

    public User find(int id) {
        return em.find(User.class, id);
    }

    public void register(User user) {
        user.setId(0);
        em.persist(user);
        userEvent.fire(user);
    }

    public void edit(User user) {
        em.merge(user);
        userEvent.fire(user);
    }

    public void delete(int id) {
        User user = em.find(User.class, id);
        em.remove(user);
        userEvent.fire(user);
    }

}

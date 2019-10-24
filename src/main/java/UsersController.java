import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.Date;

/***
 * Класс, отвечающий за изменение таблицы при доступе из JSF
 */
@Model
public class UsersController {

    @Produces
    @Named
    private User newUser;

    @Inject
    private UserManagement userManagement;

    @Inject
    private EntityManager em;

    public UsersController() {}

    /***
     * Инициализация полей пользователя в том случае, если мы собираемся редактировать существующего пользователя
     */
    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("usrId");

        if (id != null) {
            newUser = em.find(User.class, Integer.parseInt(id));
        } else {
            newUser = new User();
        }
    }

    public String register() throws Exception {
        try {
            userManagement.register(newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "confirmation";
    }

    public String edit() throws Exception {
        try {
            userManagement.edit(newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "confirmation";
    }

    public String delete(int id) throws Exception {
        userManagement.delete(id);
        return "confirmation";
    }

}

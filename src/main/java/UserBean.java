import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.util.Date;

/***
 * Бин, используемый при редактировании или создании нового пользователя при помощи JSP
 */
@ManagedBean
@RequestScoped
public class UserBean {
    private Integer usrId;
    private String name;
    private String surname;
    private String address;
    private Date birthDate;

    public UserBean() {}

    /***
     * Инициализация полей в бина в случае, если мы собираемся редактировать инормацию о пользователе
     */
    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("usrId");

        if (id != null) {
            User user = TableBean.getInstance().getUsers().get(Integer.parseInt(id));
            usrId = Integer.valueOf(id);
            name = user.getName();
            surname = user.getSurname();
            birthDate = user.getBirthDate();
            address = user.getAddress();
        }
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getUsrId() {
        return usrId;
    }

    public void setUsrId(Integer usrId) {
        this.usrId = usrId;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = new java.sql.Date(birthDate.getTime());
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

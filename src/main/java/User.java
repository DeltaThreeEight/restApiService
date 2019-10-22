import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/***
 * Класс, представляющий собой описание сущности пользователя
 */
@XmlRootElement
public class User {
    private int id;
    private String name;
    private String surname;
    private String address;
    private Date birthDate;

    public User() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public User(int id, String name, String surname, String address, Date birthDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return String.format(" id: %s\n name: %s\n surname: %s\n birthDate: %s\n address: %s\n", id, name, surname, birthDate, address);
    }
}

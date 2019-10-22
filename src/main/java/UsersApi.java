import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@ManagedBean
@RequestScoped
@Path("/users")
public class UsersApi {

    private TableBean tableBean = TableBean.getInstance();

    /***
     * Rest API метод, принимающий http GET запрос в формате "/rest/api/users/$id" для отображения информации о пользователе
     * @param id идентификатор пользователя
     * @return Информацию о пользователе в формате XML, если он существует
     */
    @GET
    @Produces("text/xml")
    @Path("{userId}")
    public User getUser(@PathParam(value="userId") String id) {
        return tableBean.getUsers().get(Integer.parseInt(id));
    }

    /***
     * Rest API метод, принимающий http POST запрос в формате "/rest/api/users/$id" для изменения информации о пользователе
     * @param id идентификатор пользователя
     * @param user информация о пользователе в формате XML
     */
    @POST
    @Consumes("text/xml")
    @Path("{userId}")
    public Response editUser(@PathParam(value="userId") String id, User user) {
        if (tableBean.setUser(Integer.parseInt(id), user))
            return Response.status(200).entity("User successfully" + id + " edited!").build();
        else
            return Response.status(204).entity("User " + id + " doesn't exist.").build();
    }

    /***
     * Rest API метод, принимающий http PUT запрос в формате "/rest/api/users" для добавления нового пользователя
     * @param user информация о пользователе в формате XML
     */
    @PUT
    @Consumes("text/xml")
    public Response addUser(User user) {
        tableBean.addUser(user);
        return Response.status(200).entity("User successfully added...\n" + user).build();
    }

    /***
     * Rest API метод, принимающий http DELETE запрос в формате "/rest/api/users/$id" для удаления информации о пользователе
     * @param id идентификатор пользователя
     */
    @DELETE
    @Path("{userId}")
    public Response removeUser(@PathParam(value="userId") String id) {
        User user = tableBean.deleteUser(Integer.parseInt(id));
        if (user != null)
            return Response.status(200).entity("User " + id + " successfully removed...\n").build();
        else
            return Response.status(204).entity("User " + id + " doesn't exist.").build();
    }

    /***
     * Вспомогательный метод для доступа к REST API с JSP страницы
     */
    public String editUser(UserBean user) {
        editUser(user.getUsrId()+"", new User(user.getUsrId(), user.getName(), user.getSurname(), user.getAddress(), user.getBirthDate()));
        return "confirmation";
    }

    /***
     * Вспомогательный метод для доступа к REST API с JSP страницы
     */
    public String addUser(UserBean user) {
        addUser(new User(0, user.getName(), user.getSurname(), user.getAddress(), user.getBirthDate()));
        return "confirmation";
    }

    /***
     * Вспомогательный метод для доступа к REST API с JSP страницы
     */
    public String removeUser(int id) {
        removeUser(id+"");
        return "confirmation";
    }

}

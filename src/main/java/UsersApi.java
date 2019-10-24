import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/users")
public class UsersApi {

    @Inject
    private UserManagement userManagement;

    /***
     * Rest API метод, принимающий http GET запрос в формате "/rest/api/users/$id" для отображения информации о пользователе
     * @param id идентификатор пользователя
     * @return Информацию о пользователе в формате XML, если он существует
     */
    @GET
    @Produces("text/xml")
    @Path("{userId}")
    public User getUser(@PathParam(value="userId") String id) {
        return userManagement.find(Integer.parseInt(id));
    }

    /***
     * Rest API метод, принимающий http POST запрос в формате "/rest/api/users" для изменения информации о пользователе
     * @param user информация о пользователе в формате XML
     */
    @POST
    @Consumes("text/xml")
    public Response editUser(User user) {
        try {
            if (userManagement.find(user.getId()) == null)
                throw new NullPointerException("user not found");

            userManagement.edit(user);
            return Response.status(200).entity("User successfully edited!").build();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /***
     * Rest API метод, принимающий http PUT запрос в формате "/rest/api/users" для добавления нового пользователя
     * @param user информация о пользователе в формате XML
     */
    @PUT
    @Consumes("text/xml")
    public Response addUser(User user) {
        try {
            userManagement.register(user);
            return Response.status(200).entity("User successfully added...\n" + user).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /***
     * Rest API метод, принимающий http DELETE запрос в формате "/rest/api/users/$id" для удаления информации о пользователе
     * @param id идентификатор пользователя
     */
    @DELETE
    @Path("{userId}")
    public Response removeUser(@PathParam(value="userId") String id) {
        try {
            userManagement.delete(Integer.parseInt(id));
            return Response.status(200).entity("User " + id + " successfully removed...\n").build();
        } catch (Exception e) {
            return Response.status(204).entity("User " + id + " doesn't exist.").build();
        }
    }




}

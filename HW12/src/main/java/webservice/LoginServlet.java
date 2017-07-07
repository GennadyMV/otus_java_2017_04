package webservice;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @author sergey
 *         created on 06.07.17.
 */
public class LoginServlet extends HttpServlet {
    public static final String LOGIN_PARAMETER_NAME = "login";
    private static final String LOGIN_VARIABLE_NAME = "login";

    private String login;
    private static final String ADMIN_NAME = "admin";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestLogin = request.getParameter(LOGIN_PARAMETER_NAME);

        if (requestLogin != null && ADMIN_NAME.equals(requestLogin)) {
            saveToVariable(requestLogin);
            saveToSession(request, requestLogin); //request.getSession().getAttribute("login");
            saveToServlet(request, requestLogin); //request.getAttribute("login");
            saveToCookie(response, requestLogin); //request.getCookies();

            response.sendRedirect("/admin");
            new Utils().setOK(response);
        } else {
            response.sendRedirect("/accessDenied.html");
            new Utils().setFORBIDDEN(response);
        }
    }

    private void saveToCookie(HttpServletResponse response, String requestLogin) {
        response.addCookie(new Cookie("dbServicelogin", requestLogin));
    }

    private void saveToServlet(HttpServletRequest request, String requestLogin) {
        request.getServletContext().setAttribute("login", requestLogin);
    }

    private void saveToSession(HttpServletRequest request, String requestLogin) {
        request.getSession().setAttribute("login", requestLogin);
    }

    private void saveToVariable(String requestLogin) {
        login = requestLogin != null ? requestLogin : login;
    }
}

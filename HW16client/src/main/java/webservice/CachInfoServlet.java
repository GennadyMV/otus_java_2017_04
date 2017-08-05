package webservice;

import cache.CachInfo;
import com.google.gson.Gson;
import messageSystem.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author sergey
 *         created on 07.07.17.
 */

@Component
public class CachInfoServlet extends HttpServlet {
    private Address adress;

    @Autowired
    private CachInfoHelper cachInfoHelper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        adress = new Address("CachInfoServlet");
        System.out.println(new Date() + " address: " + adress);
    }


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        System.out.println(new Date() + " get request");
        final Utils utils = new Utils();
        if (request.getSession().getAttribute("login") != null) {
            System.out.println(new Date() + " Request to messageSystem...");
            final CachInfo cachInfo = cachInfoHelper.get(adress);
            final String data = new Gson().toJson(cachInfo);
            System.out.println(new Date() + " Sending message:" + data);
            response.getWriter().println(data);
            utils.setOK(response);
        } else {
            response.sendRedirect("/warAppl/accessDenied.html");
            utils.setFORBIDDEN(response);
        }
    }
}

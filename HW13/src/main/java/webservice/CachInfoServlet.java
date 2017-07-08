package webservice;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sergey
 *         created on 07.07.17.
 */

@Configurable
public class CachInfoServlet extends HttpServlet {
    @Autowired
    private DbServiceUsage dbServiceUsage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        System.out.println("get request");
        if (request.getSession().getAttribute("login") != null) {

            final CachInfo cachInfo = new CachInfo(dbServiceUsage.getDbService().getHitCount(),
                    dbServiceUsage.getDbService().getMissCount());
            final String data = new Gson().toJson(cachInfo);
            System.out.println("Sending message:" + data);
            response.getWriter().println(data);
            new Utils().setOK(response);
        } else {
            response.sendRedirect("/warAppl/accessDenied.html");
            new Utils().setFORBIDDEN(response);
        }
    }

    class CachInfo {
        private int hitCount;
        private int missCount;

        public CachInfo() {
        }

        public CachInfo(int hitCount, int missCount) {
            this.hitCount = hitCount;
            this.missCount = missCount;
        }

        public int getHitCount() {
            return hitCount;
        }

        public void setHitCount(int hitCount) {
            this.hitCount = hitCount;
        }

        public int getMissCount() {
            return missCount;
        }

        public void setMissCount(int missCount) {
            this.missCount = missCount;
        }
    }

}

package webservice;

import javax.servlet.http.HttpServletResponse;

/**
 * @author sergey
 *         created on 06.07.17.
 */
public class Utils {

    public void setOK(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void setFORBIDDEN(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

}

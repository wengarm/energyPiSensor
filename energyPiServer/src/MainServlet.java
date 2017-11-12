import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MainServlet extends HttpServlet {

    double multiplier = 1.0;
    int interval = 10;
    int globalValue = 0;
    LinkedList<Integer> stackOfBlinks = new LinkedList<>();
    LinkedList<String> stackOfNames = new LinkedList<>();
    LinkedList<String> stackOfValues = new LinkedList<>();
    String login = "admin:admin";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("print") != null && req.getParameter("print").equals("global")) {
            Map<String, String> values = new HashMap<>();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            values.put("value", "" + (globalValue * multiplier));
            values.put("multiplier", "" + multiplier);
            values.put("interval", "" + interval);
            resp.getWriter().write(new Gson().toJson(values));
        } else if (req.getParameter("print") != null && req.getParameter("print").equals("list")) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            resp.getWriter().write(gson.toJson(stackOfValues));
        } else if (req.getParameter("get") != null && req.getParameter("get").equals("interval")) {
            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("" + interval);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        if (req.getParameter("change") != null && req.getParameter("change").equals("multiplier")) {
            String authorization = req.getHeader("Authorization");
            if (authorization == null) {
            }
            if (!authorization.toUpperCase().startsWith("BASIC ")) {
            }
            String credentials = authorization.substring(6);
            sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
            String finalCredentials = new String(dec.decodeBuffer(credentials));

            if (finalCredentials.equals(login)) {
                multiplier = Double.parseDouble(req.getParameter("multiplier"));
                stackOfValues.clear();
                for(int i = 0; i < stackOfBlinks.size(); i++){
                    String temp = stackOfNames.get(i) + "  =  " + stackOfBlinks.get(i)*multiplier;
                    stackOfValues.add(i, temp);
                }
            } else {
                resp.getWriter().write("You should not try that");
            }
        } else if (req.getParameter("change") != null && req.getParameter("change").equals("interval")) {
            int temp = Integer.parseInt(req.getParameter("interval"));
            if (temp > 0)
                interval = temp;
        } else if (req.getParameter("updateGlobal") != null && req.getParameter("updateGlobal").equals("true")) {
            int temp = Integer.parseInt(req.getParameter("globalValue"));

            globalValue = temp;
        } else if (req.getParameter("updateStack") != null && req.getParameter("updateStack").equals("true")) {
            if(req.getParameter("type").equals("blink")){
                stackOfBlinks = new Gson().fromJson(req.getParameter("stack"),
                        new TypeToken<LinkedList<Integer>>(){}.getType());
            }else if(req.getParameter("type").equals("name")){
                stackOfNames = new Gson().fromJson(req.getParameter("stack"),
                        new TypeToken<LinkedList<String>>(){}.getType());
            }else if(req.getParameter("type").equals("build")){
                stackOfValues.clear();
                for(int i = 0; i < stackOfBlinks.size(); i++){
                    String temp = stackOfNames.get(i) + "  =  " + stackOfBlinks.get(i)*multiplier;
                    stackOfValues.add(i, temp);
                }
            }
        }
    }
}

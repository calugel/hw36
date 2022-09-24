package ge.ufc.servlets;


import ge.ufc.conf.ConfManager;
import ge.ufc.figures.Triangle;
import ge.ufc.figures.Triangles;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "JobServlet", urlPatterns = "/jobServlet")
public class JobServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ConfManager conf;
        try {
            conf = ConfManager.getConfiguration();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String username = req.getHeader("username");
        String password = req.getHeader("password");

        if(!username.equals(conf.getUser().getUsername()) || !password.equals(conf.getUser().getPassword())){
            try {
                resp.sendError(402, "authorization failed");
            } catch (IOException e) {
                logger.error("enter valid username/password");
                throw new RuntimeException(e);
            }
            return;
        }

        double p = Double.parseDouble(req.getParameter("p"));
        if(p <= 0){
            try {
                resp.sendError(400,"invalid value for p");
            } catch (IOException e) {
                logger.error("invalid value of p");
                throw new RuntimeException(e);
            }
            return;
        }


        Triangles triangles;
        try {
            triangles = getTriangles();
        } catch (JAXBException | IOException e) {
            throw new RuntimeException(e);
        }

        List<Triangle> triangleList = triangles.getTriangles();

        int i = 0;
        for(Triangle t : triangleList){
            if(p == t.getPerimeter()){
                try {
                    resp.setContentType("text/xml");
                    resp.setCharacterEncoding("UTF-8");
                    JAXBContext context = JAXBContext.newInstance(Triangle.class);
                    Marshaller marshaller = context.createMarshaller();
                    marshaller.marshal(t, resp.getOutputStream());
                    i++;
                } catch (JAXBException | IOException e) {
                    try {
                        resp.sendError(500, "Internal error");
                    } catch (IOException ex) {
                        logger.error(e);
                        throw new RuntimeException(ex);
                    }
                    return;
                }
            }
        }
        if(i == 0){
            try {
                resp.sendError(404, "no match found");
            } catch (IOException e) {
                logger.error("no such triangle");
                throw new RuntimeException(e);
            }
        }


        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
            Thread.sleep(60_000);
            scheduler.shutdown();
        } catch (SchedulerException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static Triangles getTriangles() throws IOException, JAXBException {
        BufferedReader br = new BufferedReader(new FileReader(String.valueOf(ConfManager.getConfiguration().getUser().getPath())));
        JAXBContext context = JAXBContext.newInstance(Triangles.class);
        return (Triangles) context.createUnmarshaller().unmarshal(br);
    }

}

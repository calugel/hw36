package ge.ufc.jobs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import ge.ufc.figures.Triangle;
import ge.ufc.figures.Triangles;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TriangleJob implements Job {

    private static final Logger lgg = LogManager.getLogger(TriangleJob.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        lgg.info("started");
        int count = jobExecutionContext.getJobDetail().getJobDataMap().getInt("X");

        List<Triangle> triangles = new ArrayList<>();

        while (count != 0){
            count--;
            Triangle t = new Triangle(Math.random() * 10 + 1, Math.random() * 10 + 1, Math.random() * 10 + 1);
            triangles.add(t);
        }

        try {
            JAXBContext context = JAXBContext.newInstance(Triangles.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Triangles tr = (Triangles) unmarshaller.unmarshal(new File(jobExecutionContext.getJobDetail().getJobDataMap().getString("path")));
            tr.getTriangles().addAll(triangles);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(tr, new FileWriter(jobExecutionContext.getJobDetail().getJobDataMap().getString("path")));
            lgg.debug("new triangle added");
        } catch (JAXBException | IOException e) {
            lgg.error("exception occurred");
            throw new RuntimeException(e);
        }

    }
}

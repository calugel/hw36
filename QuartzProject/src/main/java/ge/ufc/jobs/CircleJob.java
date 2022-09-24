package ge.ufc.jobs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import ge.ufc.figures.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CircleJob implements Job {

    private static final Logger lgg = LogManager.getLogger(TriangleJob.class);
    private static final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private static final String path = System.getProperty("user.dir") + "\\src\\main\\lib\\circles.json";
    static JsonArray jsonArray = new JsonArray();
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        BufferedWriter br;
        try {
            br = new BufferedWriter(new FileWriter(path, false));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double radius = Math.random() * 10 + 1;
        Circle c = new Circle(radius);

        String jsonString = gson.toJson(c);
        System.out.println(jsonString);
        jsonArray.add(jsonString);

        try {
            br.write(String.valueOf(jsonArray));
            br.flush();
            lgg.info("circle created");
        } catch (IOException e) {
            lgg.error("exception occurred");
            throw new RuntimeException(e);
        }

    }
}


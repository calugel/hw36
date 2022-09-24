package ge.ufc.jobs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import ge.ufc.figures.Triangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class TriangleJob implements Job {

    private static final Logger lgg = LogManager.getLogger(TriangleJob.class);
    private static final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private static final String path = System.getProperty("user.dir") + "\\src\\main\\lib\\triangles.json";
    static JsonArray jsonArray = new JsonArray();


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        BufferedWriter br;
        try {
            br = new BufferedWriter(new FileWriter(path, false));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double a = Math.random() * 10 + 1;
        double b = Math.random() * 10 + 1;
        double c = Math.random() * 10 + 1;


        Triangle t = new Triangle(a,b,c);
        String jsonString = gson.toJson(t, Triangle.class);
        System.out.println(jsonString);
        jsonArray.add(jsonString);
        try {
            br.write(String.valueOf(jsonArray));
            br.flush();
            lgg.info("triangle created");
        } catch (IOException e) {
            lgg.error("exception occurred");
            throw new RuntimeException(e);
        }
    }
}

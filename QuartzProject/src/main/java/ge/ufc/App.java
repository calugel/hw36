package ge.ufc;

import ge.ufc.jobs.CircleJob;
import ge.ufc.jobs.RectangleJob;
import ge.ufc.jobs.TriangleJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class App {
    private static final Logger lgg = LogManager.getLogger();
    static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    public static void main(String[] args) {

        // TASK 1-2
//        try {
//            readFromClasses();
//        } catch (SchedulerException e) {
//            throw new RuntimeException(e);
//        }


        // TASK3
        readFromXML();


    }


    public static void readFromClasses() throws SchedulerException {

        Scheduler scheduler;
        try {
            scheduler = schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

        JobDetail triangleJob = JobBuilder.newJob(TriangleJob.class).withIdentity("TriangleJob", "group1").build();
        Trigger triangleTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("TriangleTrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).withRepeatCount(3))
                .build();

        JobDetail rectangleJob = JobBuilder.newJob(RectangleJob.class).withIdentity("RectangleJob", "group2").build();
        Trigger rectangleTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("RectangleTrigger", "group2")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(6).withRepeatCount(2))
                .build();

        JobDetail circleJob = JobBuilder.newJob(CircleJob.class).withIdentity("CircleJob", "group3").build();
        Trigger circleTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("CircleTrigger", "group3")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(7).withRepeatCount(4))
                .build();

        scheduler.scheduleJob(triangleJob, triangleTrigger);
        scheduler.scheduleJob(rectangleJob, rectangleTrigger);
        scheduler.scheduleJob(circleJob, circleTrigger);
        scheduler.start();

    }

    public static void readFromXML() {

        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();

        } catch (SchedulerException e) {
            lgg.error(e.getMessage(), e);
        }
    }

}

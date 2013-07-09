/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.core.util;

import java.util.concurrent.Future;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for the SingleExecutorServiceClean.
 *
 * @author drs
 */
@RunWith(JUnit4.class)
public class SingleExecutorServiceTest {

    @Test
    public void testSingleExecutorService() throws InterruptedException {
        SingleExecutorService service = new SingleExecutorService();

        for (int i = 0; i < 10; i++) {
            System.out.println("Submit for : " + i);

            System.out.println(String.format(
                    "[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                    service.getPoolSize(),
                    service.getCorePoolSize(),
                    service.getActiveCount(),
                    service.getCompletedTaskCount(),
                    service.getTaskCount(),
                    service.isShutdown(),
                    service.isTerminated()));
            Future<?> submit = service.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Started a task waiting now.");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("Task done.");
                }
            });

            if (i == 0) {
                assertThat(submit, notNullValue());
            } else {
                assertThat(submit, nullValue());
            }
            System.out.println("Future task : " + submit);
            Thread.sleep(10);
        }

    }
}

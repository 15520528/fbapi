
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.CustomAudience;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class Main {
    public static void main(String args[]) throws APIException {
        final Runnable stuffToDo = new Thread() {
            @Override
            public void run() {
                /* Do stuff here. */
                try {
                    sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("first task");
            }
        };
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future future = executor.submit(stuffToDo);
        executor.shutdown();
        try {
            future.get(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException ie) {
            /* Handle the interruption. Or ignore it. */
            System.out.println(ie);
        }
        catch (ExecutionException ee) {
            /* Handle the error. Or ignore it. */
            System.out.println(ee);
        }
        catch (TimeoutException te) {
            /* Handle the timeout. Or ignore it. */
            System.out.println(te);
        }

        System.out.println("second task");
    }
}

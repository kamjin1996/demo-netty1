package com.example.demonetty1filebrowser;

import com.example.demonetty1filebrowser.http.HttpFileServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoNetty1FilebrowserApplication {

    private static final String DEFAULT_URL = "/";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DemoNetty1FilebrowserApplication.class, args);
        httpFileServerRun(args);
    }

    public static void httpFileServerRun(String... args) throws Exception {
        int port = 8891;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        String url = DEFAULT_URL;
        if (args.length > 1) {
            url = args[1];
        }
        new HttpFileServer().run(port, url);
    }
}

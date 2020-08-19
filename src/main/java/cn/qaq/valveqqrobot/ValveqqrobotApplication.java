package cn.qaq.valveqqrobot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Executors;

@SpringBootApplication
public class ValveqqrobotApplication implements CommandLineRunner {

    @Autowired
    private BotStarter botStarter;
    public static void main(String[] args) {
        SpringApplication.run(ValveqqrobotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        botStarter.run();

    }
}

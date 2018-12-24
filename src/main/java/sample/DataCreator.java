package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataCreator {
    public static void main(String[] args) {
        Path path = Paths.get("data2.txt");

        StringBuilder builder  = new StringBuilder();

        for (int i = 0; i < 100; i++) {
            builder.append(i*1000).append(",").append(Math.random()*100).append("\n");
        }

        try {
            Files.write(path, builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

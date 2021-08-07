package dev.azn9.ntviewer;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    public LineChart<String, Integer> ping8;

    @FXML
    public LineChart<String, Integer> ping1;

    @FXML
    public LineChart<String, Integer> pingEx;

    @FXML
    public LineChart<String, Integer> pingTw;

    public void start(HelloApplication application) {
        File file = new File("debug.log");

        if (!file.exists()) {
            try {
                application.stop();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<String> lines = new ArrayList<>();
        boolean skipping = false;

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("|SKIP|")) {
                    skipping = !skipping;
                }

                if (!skipping) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();

        XYChart.Series<String, Integer> ping8S = new XYChart.Series<>();
        XYChart.Series<String, Integer> ping1S = new XYChart.Series<>();
        XYChart.Series<String, Integer> pingExS = new XYChart.Series<>();
        XYChart.Series<String, Integer> pingTwS = new XYChart.Series<>();

        ping8S.setName("Ping");
        ping1S.setName("Ping");
        pingExS.setName("Ping");
        pingTwS.setName("Ping");

        boolean startOfErr = false;
        int errC = 0;

        for (String line : lines) {
            if (!line.contains("RTT")) {
                if (line.contains("at java.lang.Thread")) {
                    errC = 0;
                }

                continue;
            }

            String[] params = line.split(" ");

            if (params.length != 10) {
                continue;
            }

            String hour = params[0] + (startOfErr ? errC++ : "");

            String type = params[5];
            int ping = Integer.parseInt(params[9].replace("ms", ""));

            XYChart.Data<String, Integer> data = new XYChart.Data<>(hour, ping);

            switch (type) {
                case "WEBSITE": {
                    pingExS.getData().add(data);
                    break;
                }
                case "TWITCHAPI": {
                    pingTwS.getData().add(data);
                    break;
                }
                case "8.8.8.8": {
                    ping8S.getData().add(data);
                    break;
                }
                case "1.1.1.1": {
                    ping1S.getData().add(data);
                    break;
                }
            }
        }

        this.ping8.getData().add(ping8S);
        this.ping1.getData().add(ping1S);
        this.pingEx.getData().add(pingExS);
        this.pingTw.getData().add(pingTwS);
    }

}
package com.example;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task3 {
    public static void getWeatherForecast() {
        System.setProperty("webdriver.chrome.driver", "S:/chromedriver-win64/chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();

        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=56&longitude=44" +
                    "&hourly=temperature_2m,rain&current=cloud_cover" +
                    "&timezone=Europe%2FMoscow&forecast_days=1&wind_speed_unit=ms";

            webDriver.get(url);
            WebElement elem = webDriver.findElement(By.tagName("pre"));
            String json_str = elem.getText();

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(json_str);
            JSONObject hourly = (JSONObject) obj.get("hourly");

            JSONArray times = (JSONArray) hourly.get("time");
            JSONArray temperatures = (JSONArray) hourly.get("temperature_2m");
            JSONArray rains = (JSONArray) hourly.get("rain");

            StringBuilder table = new StringBuilder();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            table.append("----------------------------------------------------\n");
            table.append(String.format("| %-1s | %-16s | %-12s | %-9s |%n",
                    "№", "Дата/время", "Температура", "Осадки"));
            table.append("----------------------------------------------------\n");

            for (int i = 0; i < times.size(); i++) {
                String timeStr = (String) times.get(i);
                Date time = inputFormat.parse(timeStr);
                String formattedTime = outputFormat.format(time);

                double temp = (double) temperatures.get(i);
                double rain = (double) rains.get(i);

                table.append(String.format("| %-2d | %-16s | %-12s | %-9s |%n",
                        i+1,
                        formattedTime,
                        String.format("%.1f°C", temp),
                        String.format("%.2f мм", rain)));
            }

            table.append("----------------------------------------------------\n");

            new File("result").mkdirs();

            try (OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream("result/forecast.txt"), StandardCharsets.UTF_8)) {
                writer.write(table.toString());
                System.out.println("Прогноз сохранен в result/forecast.txt");
            } catch (IOException e) {
                System.out.println("Ошибка при записи файла: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        } finally {
            webDriver.quit();
        }
    }
}
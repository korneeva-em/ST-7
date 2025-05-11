package com.mycompany.app;

public class App {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "S:/chromedriver-win64/chromedriver.exe");
        Task2.getIpAddress();
        Task3.getWeatherForecast();
    }
}
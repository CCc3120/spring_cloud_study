package com.bingo.test.script;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @Author h-bingo
 * @Date 2023-09-15 14:35
 * @Version 1.0
 */
public class Script {

    static String DRIVER_PATH = "/Users/h-bingo/Desktop/development/driver/chromedriver";
    // static String DRIVER_PATH = "/Users/h-bingo/Desktop/development/driver/chromedriver_mac64/chromedriver";

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        // ChromeOptions options = new ChromeOptions();
        // options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");

        douban();
    }

    public static void douban() throws InterruptedException {
        String account = "15070873120";
        String password = "hb15070873120";

        WebDriver webDriver = new ChromeDriver();
        webDriver
                // .get("https://www.douban.com")
                .get("https://accounts.douban.com/passport/login_popup?login_source=anony")
        ;
        Thread.sleep(5000);
        System.out.println("===切换登陆方式===");

        // webDriver = webDriver.switchTo().frame(webDriver.findElement(By.name("iframe")));
        WebElement account_tab = webDriver.findElement(By.className("account-body-tabs")).findElement(By.className("account-tab-account"));
        // WebElement account_tab = webDriver.findElement(By.xpath("//*[@id='account']/div[2]/div[2]/div/div[1]/ul[1]/li[2]"));
        account_tab.click();
        Thread.sleep(2000);
        System.out.println("===输入账号===");
        WebElement username = webDriver.findElement(By.id("username"));
        username.sendKeys(account);
        Thread.sleep(2000);
        System.out.println("===输入密码===");
        WebElement password1 = webDriver.findElement(By.id("password"));
        password1.sendKeys(password);
        Thread.sleep(2000);
        System.out.println("===登陆===");
        WebElement login = webDriver.findElement(By.className("account-form-field-submit"));
        login.findElement(By.className("btn")).click();
    }
}

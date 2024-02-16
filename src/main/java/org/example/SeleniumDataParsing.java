/*Example program to show how selenium parse data from websites*/

package org.example;

import java.util.List;
import java.io.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

public class SeleniumDataParsing {

    public static void main(String[] args) {

        // call method to parse data
        // calling to parse ebay
        parsingSearch("iphone", "https://ebay.com", "gh-ac", "gh-btn",
                "s-item__title", "s-item__price");
        // calling to parse target
        parsingSearch("iphone", "https://target.com", "search",
                "//button[@data-test='@web/Search/SearchButton']",
                "//a[@class=\"styles__StyledLink-sc-vpsldm-0 styles__StyledTitleLink-sc-14ktig2-1 cbOry csOImU h-display-block h-text-bold h-text-bs\"][@data-test=\"product-title\"]",
                "//span[@class][@data-test=\"current-price\"]/span");
    }

    public static void parsingSearch(String value, String webURL, String searchBy, String button, String extractTitle, String extractPrice) {

        WebDriver chromeTest = new ChromeDriver();
        chromeTest.get(webURL);
        if(webURL.equals("https://ebay.com")) {

            chromeTest.findElement(By.id(searchBy)).sendKeys(value); // finds search element and put input to search for
            chromeTest.findElement(By.id(button)).submit(); // finds search button and submit the search

            List<WebElement> itemTitles = chromeTest.findElements(By.className(extractTitle)); // extract webelements e.g. title/description of item
            List<WebElement> itemPrices = chromeTest.findElements(By.className(extractPrice)); // extract webelements e.g. the price to the corresponding title/description
            writeFile("parsing_data_ebay.txt", itemTitles, itemPrices); // write the data to an output file
        }
        else if (webURL.equals("https://target.com")){

            chromeTest.findElement(By.id(searchBy)).sendKeys(value);
            chromeTest.findElement(By.xpath(button)).submit();
            try {
                Thread.sleep(5000); // i don't know why, but if i don't put some way to slow the process down on target the read file is empty
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<WebElement> itemTitles = chromeTest.findElements(By.xpath(extractTitle));
            List<WebElement> itemPrices = chromeTest.findElements(By.xpath(extractPrice));
            writeFile("parsing_data_target.txt", itemTitles, itemPrices);
        }

        chromeTest.quit(); // close the connection
    }

    // function to write to an output file
    public static void writeFile(String fileName, List<WebElement> itemTitles, List<WebElement> itemPrices){

        try {

            FileWriter fw = new FileWriter(fileName);
            PrintWriter pw = new PrintWriter(fw);

            for (int i = 0; i < itemTitles.size(); i++) {

                pw.println(itemTitles.get(i).getText() + " " + itemPrices.get(i).getText());
            }

            pw.close();
        }
        catch (IOException e) {

            System.out.println("Error - cannot write to file: " + fileName);
        }
    }
}

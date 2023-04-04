import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scrape {
    public static void main(String[] args) {

        String name = "billiard posters";
     scrapeImages(name, 1000);
     downloadImages(name);

    }

    public static void scrapeImages(String query, int size) {
        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
        Actions actions = new Actions(driver);



        driver.get("https://www.google.com/search?tbm=isch&q=" + query.replace(' ', '+'));
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));

//        List<WebElement> links = driver.findElements(By.cssSelector("a.wXeWr.islib.nfEiy"));
//        System.out.println(links.size());
//        System.out.println(links);

//        WebElement showMore = driver.findElement(By.cssSelector("input[type='button'][value='Show more results']"));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showMore);
//        actions.click(showMore).perform();

//        int count = 0;
//        while (true) {
//
//            List<WebElement> elements = driver.findElements(By.cssSelector("a.wXeWr.islib.nfEiy"));
//            if (count > size) {
//                break;
//            }
////            if (count == elements.size()) {
////                WebElement showMore = driver.findElement(By.cssSelector("input[type='button'][value='Show more results']"));
////                WebElement reachedEnd = driver.findElement(By.xpath("//*[contains(text(), 'Looks like you\'ve reached the end')]"));
////                if (showMore != null && reachedEnd == null) {
////                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showMore);
////                    actions.click(showMore).perform();
////                    sleep(5000);
////                }
////            }
//
//            if (count == elements.size()) {
//                break;
//            }
//
//            count = elements.size();
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elements.get(count - 1));
//            sleep(5000);
//
//        }

        sleep(100000);

        driver.findElements(By.cssSelector("a.wXeWr.islib.nfEiy"))
                .stream()
                .limit(size)
                .forEach(webElement -> {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
                    actions.contextClick(webElement).perform();
                });



        List<String> imageLinks = driver.findElements(By.cssSelector("a.wXeWr.islib.nfEiy"))
                .stream()
                .map(webElement -> webElement.getAttribute("href"))
                .filter(Objects::nonNull)
                .map(s -> {
                    Pattern pattern = Pattern.compile("imgurl=([^&]*)");
                    Matcher matcher = pattern.matcher(s);
                    matcher.find();
                    String imgurl = matcher.group(1);
                    String decodedUrl = URLDecoder.decode(imgurl, Charset.forName("UTF-8"));
                    return decodedUrl;
                })
                .toList();
        System.out.println(imageLinks);

        System.out.println("Images found. size: " + imageLinks.size());
        imageLinks.forEach(System.out::println);

        Util.createFolder(query);

        Util.writeLinksToFile(query, imageLinks);

        driver.quit();
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void downloadImages(String query) {
        List<String> links = Util.readLinksFromFile(query);
        System.out.println("Links found. size:" + links.size());

        Util.downloadUrls(links, query);
    }


}

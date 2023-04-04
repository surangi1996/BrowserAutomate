import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class Util {

    static void writeLinksToFile(String query, List<String> imageLinks) {
        try {
            Files.write(Paths.get("downloads/" + query + "/links.txt"), imageLinks);
            System.out.println("Strings written to file successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void createFolder(String query) {
        Path path = Paths.get("downloads/" + query);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Folder created successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String>readLinksFromFile(String query) {
        try {
            List<String> strings = Files.readAllLines(Paths.get("downloads/" + query + "/links.txt"));
            return strings;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

//    public static void downloadUrls(List<String> urls, String query) {
//        int index = 0;
//        int successCount = 0;
//        int failCount = 0;
//        for (String url : urls) {
//            try {
//                URL website = new URL(url);
//                String fileName = website.getFile().substring(website.getFile().lastIndexOf('/') + 1).split("\\?")[0];
//                Path filePath = Paths.get("downloads", query, fileName);
//                InputStream in = website.openStream();
//                Files.copy(in, filePath);
//                System.out.println("File downloaded successfully: " + filePath);
//                successCount++;
//            } catch (IOException e) {
//                e.printStackTrace();
//                failCount++;
//            }
//        }
//        System.out.println("Download completed: " + successCount + " files downloaded, " + failCount + " files failed");
//    }

    public static void downloadUrl(String url, String query, int index) throws  IOException {
            URL website = new URL(url);
//            String fileName = website.getFile().substring(website.getFile().lastIndexOf('/') + 1).split("\\?")[0];
            String fileName = "" + index + ".jpg";
            Path filePath = Paths.get("downloads", query, fileName);
            InputStream in = website.openStream();
            Files.copy(in, filePath);
            System.out.println("File downloaded successfully: " + filePath);
    }

    public static void downloadUrls(List<String> urls, String query) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        try {
            for (int i: IntStream.range(0, urls.size()).toArray()) {
                Future<?> future = executorService.submit(() -> {
                    try {
                        downloadUrl(urls.get(i), query, i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } finally {
            executorService.shutdown();
        }
    }
}

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileTransferManager {
    private static final String SOURCE_FOLDER = "C:/FileTransfer/Source";
    private static final String DEST_FOLDER = "C:/FileTransfer/Destination";
    private static final String BACKUP_FOLDER = "C:/FileTransfer/Backup";
    private static final String LOG_FOLDER = "C:/FileTransfer/Logs";
    private static final String LOG_FILE = LOG_FOLDER + "/event-log.txt";
    private static final String SUMMARY_FILE = LOG_FOLDER + "/result-summary.txt";

    private static final AtomicBoolean isRunning = new AtomicBoolean(false);

    public static void main(String[] args) {
        new File(SOURCE_FOLDER).mkdirs();
        new File(DEST_FOLDER).mkdirs();
        new File(BACKUP_FOLDER).mkdirs();
        new File(LOG_FOLDER).mkdirs();

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new FileTransferTask(), 0, 60 * 1000); // every 1 minute

        log("File Transfer Manager started. Monitoring every 1 minute...");
        try {
            Thread.sleep(Long.MAX_VALUE); // Keep main thread alive
        } catch (InterruptedException e) {
            log("Main thread interrupted.");
        }
    }

    static class FileTransferTask extends TimerTask {
        @Override
        public void run() {
            if (isRunning.get()) {
                log("Previous process still running. Skipping this cycle.");
                return;
            }

            isRunning.set(true);
            log("Scan started at " + currentTime());

            File folder = new File(SOURCE_FOLDER);
            File[] files = folder.listFiles();

            if (files == null || files.length == 0) {
                log("No files found.");
                isRunning.set(false);
                return;
            }

            StringBuilder summary = new StringBuilder("Summary at " + currentTime() + ":\n");

            for (File file : files) {
                if (file.getName().endsWith(".temp") || file.isDirectory()) {
                    summary.append("Skipped: ").append(file.getName()).append("\n");
                    continue;
                }

                try {
                    String zipFileName = file.getName() + ".zip";
                    File zipFile = new File(DEST_FOLDER, zipFileName);

                    compressFile(file, zipFile);

                    long originalSize = file.length();
                    long zipSize = zipFile.length();

                    if (zipFile.exists() && zipFile.length() > 0) {
                        // Transfer success
                        Files.move(file.toPath(), Paths.get(BACKUP_FOLDER, file.getName()), StandardCopyOption.REPLACE_EXISTING);
                        summary.append("Transferred: ").append(file.getName()).append("\n");
                    } else {
                        // Transfer failed
                        zipFile.delete();
                        summary.append("Failed (Size mismatch): ").append(file.getName()).append("\n");
                    }

                } catch (IOException e) {
                    summary.append("Error: ").append(file.getName()).append(" - ").append(e.getMessage()).append("\n");
                    log("Exception: " + e.getMessage());
                }
            }

            // Write summary
            appendToFile(SUMMARY_FILE, summary.append("\n").toString());

            log("Scan ended at " + currentTime());
            isRunning.set(false);
        }
    }

    private static void compressFile(File sourceFile, File zipFile) throws IOException {
        try (
            FileInputStream fis = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            ZipEntry entry = new ZipEntry(sourceFile.getName());
            zos.putNextEntry(entry);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }

            zos.closeEntry();
        }
    }

    private static void log(String message) {
        String logMessage = "[" + currentTime() + "] " + message + "\n";
        appendToFile(LOG_FILE, logMessage);
        System.out.print(logMessage);
    }

    private static void appendToFile(String path, String content) {
        try (FileWriter writer = new FileWriter(path, true)) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("Error writing to log: " + e.getMessage());
        }
    }

    private static String currentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}

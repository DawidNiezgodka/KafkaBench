package util;

import bench.BenchmarkResult;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvWriter {

    private static final Logger LOGGER = LogManager.getLogger(CsvWriter.class);
    private static final CsvMapper CSV_MAPPER = new CsvMapper();
    private static OutputStream OUT;
    static {
        // get the current directory
        Path currentDir = Paths.get(BenchConsts.RESULTS_FOLDER_RELATIVE);
        String relativeForFile = currentDir.toString().concat(BenchConsts.RESULTS_FILE);
        Path pathToFile = Paths.get(relativeForFile);
        try {
            // create a directory by combining and normalizing the relative path
            // doesn't throw an exception if a dir already exists
            Files.createDirectories(Paths.get(String.valueOf(currentDir.normalize().toAbsolutePath())));
            // Check if the file exists and create it if not
            File outputFile;
            if (!Files.exists(pathToFile.normalize().toAbsolutePath())) {
                outputFile = Files.createFile(pathToFile.normalize().toAbsolutePath()).toFile();
            } else {
                outputFile = Paths.get(pathToFile.normalize().toAbsolutePath().toUri()).toFile();
            }
            OUT = new FileOutputStream(outputFile, true);
        } catch (IOException e) {
            LOGGER.error(e.getStackTrace());
            System.exit(1);
        }

        //CSV_MAPPER.findAndRegisterModules();
        //CSV_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    private static final CsvSchema CSV_SCHEMA = CSV_MAPPER
            .schemaFor(BenchmarkResult.class)
            .withColumnSeparator(';')
            .withHeader();
    private static final ObjectWriter WRITER = CSV_MAPPER.writerFor(BenchmarkResult.class).with(CSV_SCHEMA);

    public static void toCsv(BenchmarkResult benchmarkResult) {
        try {
            LOGGER.info("Writing results for {}", benchmarkResult.getName());
            WRITER.writeValue(OUT, benchmarkResult);
            OUT.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

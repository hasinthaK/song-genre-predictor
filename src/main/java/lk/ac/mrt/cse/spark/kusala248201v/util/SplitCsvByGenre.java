package lk.ac.mrt.cse.spark.kusala248201v.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitCsvByGenre {

    public static void main(String[] args) {
        String inputFilePath = "src/main/resources/training-set/tcc_ceds_music.csv";
        splitCsvByGenre(inputFilePath);
    }

    public static void splitCsvByGenre(String filePath) {
        try {
            // Reading the CSV file
            CSVReader reader = new CSVReader(new FileReader(filePath));
            List<String[]> allRows = reader.readAll();

            // Assuming the first row contains headers
            String[] headers = allRows.get(0);
            int genreIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if ("genre".equals(headers[i])) {
                    genreIndex = i;
                    break;
                }
            }

            if (genreIndex == -1) {
                System.out.println("The 'genre' column was not found.");
                return;
            }

            // Grouping rows by genre
            Map<String, List<String[]>> groupedByGenre = new HashMap<>();
            for (int i = 1; i < allRows.size(); i++) { // Skip headers
                String genre = allRows.get(i)[genreIndex];
                groupedByGenre.putIfAbsent(genre, new ArrayList<>());
                groupedByGenre.get(genre).add(allRows.get(i));
            }

            // Writing each group to a separate file
            for (Map.Entry<String, List<String[]>> entry : groupedByGenre.entrySet()) {
                String genre = entry.getKey();
                List<String[]> rows = entry.getValue();
                String fileName = "src/main/resources/training-set" + genre + ".csv";
                try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
                    writer.writeNext(headers); // Write headers
                    writer.writeAll(rows);
                }
            }

            System.out.println("CSV files have been created based on distinct genres.");

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
}

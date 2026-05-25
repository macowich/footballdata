package se.mac.footballdata.etl;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CsvConverter {
    static String rootDir = "C:\\data\\csv\\";

    public static void main(String[] args) {
        if (args.length > 0) {
            rootDir = args[0];
        }

        Path inputFile = Path.of(rootDir, "fixtures.csv");
        Path outputFile = Path.of(rootDir, "fixtures_converted.csv");

        try (
                CSVReader reader = new CSVReader(new FileReader(inputFile.toFile()));
                CSVWriter writer = new CSVWriter(new FileWriter(outputFile.toFile()))
        ) {

            String[] header = reader.readNext();
            if (header == null) {
                System.out.println("Empty input file");
                return;
            }

            // FIX BOM if it exists in first column
            if (header.length > 0) {
                header[0] = header[0].replace("\uFEFF", "");
            }

            // Build column index map
            Map<String, Integer> columns = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                columns.put(header[i].trim().toLowerCase(), i);
            }

            // Output header
            writer.writeNext(new String[]{
                    "league.string()",
                    "date.string()",
                    "time.string()",
                    "hometeam.string()",
                    "awayteam.string()",
                    "referee.string()",
                    "b365h.string()",
                    "b365d.string()",
                    "b365a.string()",
                    "b365_u25.string()",
                    "b365_o25.string()"
            });

            String[] row;

            while ((row = reader.readNext()) != null) {
                String league = getValue(row, columns, "Div");
                String date = getValue(row, columns, "date");
                String time = getValue(row, columns, "time");
                String homeTeam = getValue(row, columns, "hometeam");
                String awayTeam = getValue(row, columns, "awayteam");
                String referee = getValue(row, columns, "referee");
                String b365h = getValue(row, columns, "b365h");
                String b365d = getValue(row, columns, "b365d");
                String b365a = getValue(row, columns, "b365a");
                String b365_u25 = getValue(row, columns, "B365<2.5");
                String b365_o25 = getValue(row, columns, "B365>2.5");

                writer.writeNext(new String[]{
                        league,
                        date,
                        time,
                        homeTeam,
                        awayTeam,
                        referee,
                        b365h,
                        b365d,
                        b365a,
                        b365_u25,
                        b365_o25
                });
            }

            System.out.println("Conversion completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getValue(String[] row,
                                   Map<String, Integer> columns,
                                   String columnName) {
        Integer index = columns.get(columnName.toLowerCase());
        if (index == null || index >= row.length) {
            return "";
        }
        return row[index];
    }
}

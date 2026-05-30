package se.mac.footballdata.etl;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static se.mac.footballdata.Util.getLeagueName;
import static se.mac.footballdata.Util.toIso;

public class CsvConverter {
    public static final String RESULTS_CONVERTED_CSV = "results_converted.csv";
    static String rootDir = "C:\\data\\csv\\";

    public static void main(String[] args) {
        if (args.length > 0) {
            rootDir = args[0];
        }

        Path resultsConvertedpath = Path.of(rootDir, RESULTS_CONVERTED_CSV);
        try {
            boolean raderad = Files.deleteIfExists(resultsConvertedpath);
            if (raderad) {
                System.out.println("Filen har tagits bort.");
            } else {
                System.out.println("Filen kunde inte tas bort eftersom den inte existerar.");
            }
        } catch (IOException e) {
            System.err.println("Ett fel uppstod vid borttagning: " + e.getMessage());
            System.exit(1);
        }

        convertFixtures();
        convertResults("E0.csv", RESULTS_CONVERTED_CSV, true);
        convertResults("D1.csv", RESULTS_CONVERTED_CSV, false);
    }

    private static void convertFixtures() {
        System.out.println("Fixture conversion started...");

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
                        getLeagueName(league),
                        toIso(date),
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

            System.out.println("...done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void convertResults(String inputFileName, String outputFileName, boolean writeHeader) {
        System.out.println("Results conversion for: " + inputFileName + " started...");

        Path inputFile = Path.of(rootDir, inputFileName);
        Path outputFile = Path.of(rootDir, outputFileName);

        try (
                CSVReader reader = new CSVReader(new FileReader(inputFile.toFile()));
                CSVWriter writer = new CSVWriter(new FileWriter(outputFile.toFile(), true))
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

            if (writeHeader) {
                writer.writeNext(new String[]{
                        "league.string()",
                        "date.string()",
                        "time.string()",
                        "hometeam.string()",
                        "awayteam.string()",
                        "referee.string()",
                        "fthg.int32()",
                        "ftag.int32()",
                        "ftr.string()",
                        "hthg.int32()",
                        "htag.int32()",
                        "htr.string()",
                        "hs.int32()",
                        "as.int32()",
                        "hst.int32()",
                        "ast.int32()",
                        "hc.int32()",
                        "ac.int32()",
                        "hy.int32()",
                        "ay.int32()",
                        "hr.int32()",
                        "ar.int32()"
                });
            }

            String[] row;

            while ((row = reader.readNext()) != null) {
                String league = getValue(row, columns, "Div");
                String date = getValue(row, columns, "date");
                String time = getValue(row, columns, "time");
                String homeTeam = getValue(row, columns, "hometeam");
                String awayTeam = getValue(row, columns, "awayteam");
                String referee = getValue(row, columns, "referee");
                String fthg = getValue(row, columns, "fthg");
                String ftag = getValue(row, columns, "ftag");
                String ftr = getValue(row, columns, "ftr");
                String hthg = getValue(row, columns, "hthg");
                String htag = getValue(row, columns, "htag");
                String htr = getValue(row, columns, "htr");
                String hs = getValue(row, columns, "hs");
                String as = getValue(row, columns, "as");
                String hst = getValue(row, columns, "hst");
                String ast = getValue(row, columns, "ast");
                String hc = getValue(row, columns, "hc");
                String ac = getValue(row, columns, "ac");
                String hy = getValue(row, columns, "hy");
                String ay = getValue(row, columns, "ay");
                String hr = getValue(row, columns, "hr");
                String ar = getValue(row, columns, "ar");

                writer.writeNext(new String[]{
                        getLeagueName(league),
                        toIso(date),
                        time,
                        homeTeam,
                        awayTeam,
                        referee,
                        fthg,
                        ftag,
                        ftr,
                        hthg,
                        htag,
                        htr,
                        hs,
                        as,
                        hst,
                        ast,
                        hc,
                        ac,
                        hy,
                        ay,
                        hr,
                        ar
                });
            }

            System.out.println("...done");
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

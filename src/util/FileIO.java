package util;

import sendable.HighScore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class FileIO {
    public static final String TRAPPERS_FILE_NAME = "src/server/highscores/trappers.txt";
    public static final String CATS_FILE_NAME = "src/server/highscores/cats.txt";

    public static void readFile(List<HighScore> scoreList, String fileName) {

        try (BufferedReader br = new BufferedReader(new FileReader(fileName) ) ) {
            String line;

            while (true) {
                line = br.readLine();
                if (line == null) break;

                String[] parts = line.split("@@");

                System.out.println("name = " + parts[0] + " score = " + parts[1]);

                scoreList.add(new HighScore(parts[0], Integer.parseInt(parts[1])));
            }

        } catch (Exception e) {
            System.out.println("problem reading " + fileName);
            e.printStackTrace();
        }
    }

    public static void writeFile(List<HighScore> scoreList, String fileName) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))){

            for (HighScore score : scoreList) {
                StringBuffer sb = new StringBuffer();

                sb.append(score.getName());
                sb.append("@@");
                sb.append(score.getMoves());

                bw.write(sb.toString());
                bw.write("\n");
            }

        } catch (Exception e) {
            System.out.println("problem writing " + fileName);
            e.printStackTrace();
        }
    }
}

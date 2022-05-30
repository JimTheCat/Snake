package Components;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Highscore extends File{
    public Highscore(String pathname) {
        super(pathname);
    }

    public void checkingFile(){
        if (!this.exists()){
            try {
                this.createNewFile();
                PrintWriter writer = new PrintWriter(this, StandardCharsets.UTF_8);
                writer.println(0);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String result() throws FileNotFoundException {
        return new Scanner(this).nextLine();
    }

    public void changingHighScore(int newHighScore) throws IOException {
        int highScoreInFile = Integer.parseInt(new Scanner(this).nextLine());
        if(highScoreInFile < newHighScore) {
            try {
                PrintWriter writer = new PrintWriter(this, StandardCharsets.UTF_8);
                writer.println(newHighScore);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

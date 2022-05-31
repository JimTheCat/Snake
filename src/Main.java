import Components.AppFrame;

import javax.swing.*;
import java.io.IOException;

/*
    Ta aplikacja została napisana przez Patryka Kłosińskiego.
    Jeśli chcesz wykorzystać ten kod proszę o nie usuwanie tego komentarza!
    Bardzo dziękuje!
    ---------------------------------------------------------------------------
    This app was written by Patryk Kłosiński.
    If you want to use this code please don't delete this comment!
    Thank you very much!
    ---------------------------------------------------------------------------
    GitHub: https://github.com/JimTheCat
    E-Mail: klosinski.patryk2137@gmail.com
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SwingUtilities.invokeLater(() -> {
            try {
                new AppFrame();
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }
}

//TODO: Podzielić aplikacje na JPanele oraz poprawić interface
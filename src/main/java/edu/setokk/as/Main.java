package edu.setokk.as;

import com.formdev.flatlaf.FlatIntelliJLaf;
import edu.setokk.as.gui.SplashScreen;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
	public static void main(String[] args)
		throws
		IOException,
		InterruptedException,
		UnsupportedLookAndFeelException,
		ParseException {

		UIManager.setLookAndFeel(new FlatIntelliJLaf());
		UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 22));
		new SplashScreen();
	}
}

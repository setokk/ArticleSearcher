package edu.setokk.as.gui;

import edu.setokk.as.Main;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SplashScreen extends JFrame {

	private JLabel imgLabel;

	public SplashScreen() throws IOException, InterruptedException {
		Dimension windowSize = new Dimension(231, 140);

		this.setMinimumSize(windowSize);
		this.setResizable(false);
		this.setUndecorated(true);
		this.setLocationRelativeTo(null);

		byte[] imgBytes = Main.class.getResourceAsStream("logo.png").readAllBytes();
		ImageIcon image = new ImageIcon(imgBytes);

		imgLabel = new JLabel("");
		imgLabel.setMinimumSize(windowSize);
		imgLabel.setIcon(image);
		this.add(imgLabel);

		this.pack();
		this.setVisible(true);

		Thread.sleep(2000);
		this.dispose();

		new LoadingScreen();
	}
}

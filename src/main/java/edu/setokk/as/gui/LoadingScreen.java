package edu.setokk.as.gui;

import edu.setokk.as.Main;
import edu.setokk.as.gui.worker.EngineInitWorker;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class LoadingScreen extends JFrame {
	private JLabel loadingGifLabel;

	public LoadingScreen() throws IOException {
		int option = JOptionPane.showConfirmDialog(null,
			"Would you like to rebuild the index?\n(Press yes if it's the first time running the software)",
			"Rebuild Index", JOptionPane.YES_NO_OPTION);

		if (option == JOptionPane.CLOSED_OPTION)
			System.exit(0);

		boolean clearIndex = (option == JOptionPane.YES_OPTION);

		String dataPath = System.getProperty("user.dir") + File.separator + "data.txt";
		String indexPath = System.getProperty("user.dir") + File.separator + "index";

		// Init loading screen and add gif to show loading status
		this.setMinimumSize(new Dimension(300, 300));
		this.setResizable(false);
		this.setUndecorated(true);
		this.setLocationRelativeTo(null);

		ImageIcon loadingGif =
			new ImageIcon(Main.class.getResourceAsStream("loading-gif.gif").readAllBytes());
		loadingGifLabel = new JLabel("");
		loadingGifLabel.setIcon(loadingGif);
		this.add(loadingGifLabel);
		this.pack();
		this.setVisible(true);

		// Initialize search engine in the background
		EngineInitWorker engineInitWorker = new EngineInitWorker(clearIndex, dataPath, indexPath);
		engineInitWorker.addPropertyChangeListener((evt -> {
			if (evt.getPropertyName().equals("loading")) {
				boolean loading = (Boolean) evt.getNewValue();
				if (!loading) {
					this.dispose();
					try {
						new SearchScreen();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}));
		engineInitWorker.execute();
	}
}

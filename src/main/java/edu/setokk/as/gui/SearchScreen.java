package edu.setokk.as.gui;

import edu.setokk.as.Main;
import edu.setokk.as.engine.QueryResultWrapper;
import edu.setokk.as.engine.Result;
import edu.setokk.as.engine.SearchEngine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchScreen extends JFrame {
	private JPanel mainPanel;

	// Search panel components
	private JPanel searchPanel;
	private JLabel searchLabel;
	private JTextField searchField;
	private JButton searchBtn;
	private JLabel logoLabel;

	// Results panel components
	private JPanel wrapperResultsPanel;
	private JLabel totalHitsLabel;
	private JPanel resultContentPanel;
	private JScrollPane resultsScrollPane;

	public SearchScreen() throws IOException {
		InputStream logoInputStream = Main.class.getResourceAsStream("logo-small.png");
		BufferedImage logoImage = ImageIO.read(logoInputStream);

		this.setTitle("ArticleSearcher - Simple local search engine");
		this.setIconImage(logoImage);
		this.setMinimumSize(new Dimension(800, 540));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Defaults for re-setting
		int DEFAULT_FILL = c.fill;
		Insets DEFAULT_INSETS = c.insets;

		// Populate search panel
		searchPanel = new JPanel(new GridBagLayout());
		searchPanel.setMinimumSize(new Dimension(750, 100));
		searchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		logoLabel = new JLabel("");
		logoLabel.setIcon(new ImageIcon(logoImage));
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.05;
		c.weighty = 0.0;
		c.fill = DEFAULT_FILL;
		c.insets = new Insets(30, 0, 30, 0);
		searchPanel.add(logoLabel, c);

		searchLabel = new JLabel("Search:");
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = DEFAULT_FILL;
		searchPanel.add(searchLabel, c);

		searchField = new JTextField();
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0.4;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.BOTH;
		searchPanel.add(searchField, c);

		searchBtn = new JButton("Search \uD83D\uDD0E");
		c.gridx = 3;
		c.gridy = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.VERTICAL;
		searchPanel.add(searchBtn, c);
		this.getRootPane().setDefaultButton(searchBtn);
		addSearchBtnListener();

		// Populate results panel
		c.insets = DEFAULT_INSETS;
		wrapperResultsPanel = new JPanel();
		wrapperResultsPanel.setMinimumSize(new Dimension(750, 450));
		wrapperResultsPanel.setLayout(new BoxLayout(wrapperResultsPanel, BoxLayout.Y_AXIS));

		totalHitsLabel = new JLabel("Welcome to ArticleSearcher!");
		totalHitsLabel.setPreferredSize(new Dimension(750, 50));
		totalHitsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		wrapperResultsPanel.add(totalHitsLabel);

		resultContentPanel = new JPanel();
		resultContentPanel.setLayout(new BoxLayout(resultContentPanel, BoxLayout.Y_AXIS));

		resultsScrollPane = new JScrollPane(resultContentPanel);
		resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		resultsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		resultsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

		wrapperResultsPanel.add(resultsScrollPane);

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0.1;
		c.fill = GridBagConstraints.BOTH;
		mainPanel.add(searchPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0.9;
		c.fill = GridBagConstraints.BOTH;
		mainPanel.add(wrapperResultsPanel, c);

		this.setContentPane(mainPanel);
		this.pack();
		this.setVisible(true);
	}

	public void addSearchBtnListener() {
		searchBtn.addActionListener(e -> {
			resultContentPanel.removeAll();

			String query = searchField.getText().trim();
			try {
				// if "::" is at the end of the query, return custom number of results
				Pattern queryPattern = Pattern.compile("(.+?)(::([0-9]+))?");
				Matcher matcher = queryPattern.matcher(query);

				// Should not happen
				if (!matcher.matches())
					return;

				// Get query text and number of results
				String queryText = matcher.group(1);
				int numberOfResults = (matcher.group(3) != null) ? Integer.parseInt(matcher.group(3))
																  : SearchEngine.DEFAULT_NUMBER_OF_RESULTS;
				System.out.println(queryText + ": " + numberOfResults);
				QueryResultWrapper queryResult = SearchEngine.search(queryText, numberOfResults);
				if (queryResult == null)
					return;

				totalHitsLabel.setText("Total hits found: " + queryResult.getTotalHits());
				for (Result result : queryResult.getResults()) {
					resultContentPanel.add(new ResultBox(result));
				}
				resultContentPanel.revalidate();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		});
	}
}

package edu.setokk.as.gui;

import edu.setokk.as.engine.Result;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ResultBox extends JButton {
	private final JLabel titleLabel;
	private final JLabel authorsLabel;

	private final Result result;

	public ResultBox(Result result) {
		this.result = result;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
		this.addActionListener(e -> new ArticleInfoWindow(result));

		Border blackBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
		this.setBorder(blackBorder);

		this.titleLabel = new JLabel("Title: " + result.getTitle());
		this.authorsLabel = new JLabel("Authors: " + result.getAuthors());

		this.titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		this.authorsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));

		add(titleLabel);
		add(authorsLabel);
	}
}

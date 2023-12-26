package edu.setokk.as.gui;

import edu.setokk.as.engine.Result;

import javax.swing.*;
import java.awt.*;

public class ArticleInfoWindow extends JFrame {

	public ArticleInfoWindow(Result result) {
		this.setTitle("Info: " + result.getTitle());

		this.setMinimumSize(new Dimension(900, 600));
		this.setPreferredSize(new Dimension(900, 600));
		this.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

		String spaces = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		infoPanel.add(new JLabel("<html><b>" + spaces + "Title:</b><br/>"  + spaces + result.getTitle() + "<br/><br/></html>"));
		infoPanel.add(new JLabel("<html><b>" + spaces + "Abstract:</b><br/>" + spaces + result.getAb() + "<br/><br/></html>"));
		infoPanel.add(new JLabel("<html><b>" + spaces + "Authors:</b><br/>" + spaces + result.getAuthors() + "<br/><br/></html>"));
		infoPanel.add(new JLabel("<html><b>" + spaces + "PMID:</b><br/>" + spaces + result.getPmid() + "<br/><br/></html>"));
		infoPanel.add(new JLabel("<html><b>" + spaces + "Volume:</b><br/>" + spaces + result.getVolume() + "<br/><br/></html>"));
		infoPanel.add(new JLabel("<html><b>" + spaces + "Issue:</b><br/>" + spaces + result.getIssue() + "<br/><br/></html>"));
		infoPanel.add(new JLabel("<html><b>" + spaces + "Fpage:</b><br/>" + spaces + result.getFpage() + "<br/><br/></html>"));
		infoPanel.add(new JLabel("<html><b>" + spaces + "Lpage:</b><br/>" + spaces + result.getLpage() + "<br/><br/></html>"));
		infoPanel.add(new JLabel("<html><b>" + spaces + "Journal Title:</b><br/>" + spaces + result.getJTitle() + "<br/><br/></html>"));
		infoPanel.add(new JLabel("<html><b>" + spaces + "ISSN:</b><br/>" + spaces + (result.getIssn().equals("") ? "None" : result.getIssn()) + "<br/><br/></html>"));

		this.add(infoPanel);
		this.pack();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}

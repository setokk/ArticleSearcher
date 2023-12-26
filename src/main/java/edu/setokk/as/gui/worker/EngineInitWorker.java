package edu.setokk.as.gui.worker;

import edu.setokk.as.engine.SearchEngine;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class EngineInitWorker extends SwingWorker<Void, Boolean> {
	private final boolean clearIndex;
	private final String dataPath;
	private final String indexPath;

	public EngineInitWorker(boolean clearIndex,
							String dataPath,
							String indexPath) {
		super();
		this.clearIndex = clearIndex;
		this.dataPath = dataPath;
		this.indexPath = indexPath;
	}

	@Override
	protected Void doInBackground() throws IOException {
		SearchEngine.init(clearIndex, dataPath, indexPath);
		boolean loading = false;
		publish(loading);

		return null;
	}

	@Override
	protected void process(List<Boolean> chunks) {
		for (Boolean ignored : chunks) {
			firePropertyChange("loading", true, false);
		}
	}
}

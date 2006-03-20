package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;

import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Trace;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;

public class FileSaveAllTracesCommand extends AbstractCommand {
	private static String propertiesFileName = "analysis.properties";

	public FileSaveAllTracesCommand() {
		// empty
	}

	@Override
	public void execute()
	{
		Properties properties = new Properties();
		String lastDir = "";
		try {
			properties.load(new FileInputStream(propertiesFileName));
			lastDir = properties.getProperty("lastdir");
		} catch (IOException ex) {
			// ignore, leave default LastDir
		}

		JFileChooser chooser = new JFileChooser(lastDir);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File directory = chooser.getSelectedFile();
			if (!directory.exists()) {
				directory.mkdirs();
			}
			if (!directory.isDirectory()) {
				GUIUtil.showErrorMessage(GUIUtil.MSG_ERROR_NOT_A_DIRECTORY);
				return;
			}
			boolean hasFailures = false;
			boolean hasSuccesses = false;
			for (Trace trace: Heap.getTraceCollection()) {
				BellcoreStructure bs = trace.getPFTrace().getBS();
				String traceName = bs.title;
//				String traceName = trace.getKey(); // Это временный код для чтения результатов за сентябрь 2005.
				String fName = traceName.replaceAll("[<>\\\\/\\*\\?\\:]", "_"); // преобразуем к виду, приемлемуму для именования файлов
				// add .sor if not present yet
				if (!fName.matches(".*\\.[sS][oO][rR]")) {
					fName = fName + ".sor";
				}
				try {
					File file = new File(directory, fName);
					FileOutputStream fos = new FileOutputStream(file);
					BellcoreWriter bw = new BellcoreWriter();
					fos.write(bw.write(bs));
					hasSuccesses = true;
				} catch (IOException ex) {
					ex.printStackTrace();
					hasFailures = true;
				}
			}
			if (hasFailures) {
				if (hasSuccesses) {
					GUIUtil.showWarningMessage(
							GUIUtil.MSG_WARNING_FAILED_TO_SAVE_SOME_FILES);
				} else {
					GUIUtil.showErrorMessage(
							GUIUtil.MSG_ERROR_FAILED_TO_SAVE_ALL_FILES);
				}
			} else {
				GUIUtil.showInfoMessage(GUIUtil.MSG_INFO_FILES_SAVED);
			}

			try {
				properties.setProperty("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
				properties.store(new FileOutputStream(propertiesFileName), null);
			} catch (IOException ex) {
				// XXX: ignore?
			}
		}
	}
}

package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;

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
		try
		{
			properties.load(new FileInputStream(propertiesFileName));
			lastDir = properties.getProperty("lastdir");
		} catch (IOException ex)
		{
		}

		JFileChooser chooser = new JFileChooser(lastDir);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			File directory = chooser.getSelectedFile();
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
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			try
			{
				properties.setProperty("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
				properties.store(new FileOutputStream(propertiesFileName), null);
			} catch (IOException ex)
			{
			}
		}
	}
}

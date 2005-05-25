package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.*;
import java.util.Properties;

import javax.swing.JFileChooser;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.io.*;

public class FileSaveCommand extends AbstractCommand
{
	private ApplicationContext aContext;
	private String propertiesFileName = "analysis.properties";

	public FileSaveCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new FileSaveCommand(aContext);
	}

	public void execute()
	{
		Properties properties = new Properties();
		String lastDir = "";
		try
		{
			properties.load(new FileInputStream(propertiesFileName));
			lastDir = properties.getProperty("lastdir");
		}
		catch (IOException ex)
		{
		}

		JFileChooser chooser = new JFileChooser(lastDir);
		chooser.addChoosableFileFilter(new ChoosableFileFilter("sor", "Bellcore"));
		int returnVal = chooser.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			BellcoreStructure bs = Heap.getBSPrimaryTrace();
			try
			{
				FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile());
				BellcoreWriter bw = new BellcoreWriter();
				fos.write(bw.write(bs));

				if (!chooser.getSelectedFile().getAbsolutePath().equals(bs.title))
				{
					bs.title = chooser.getSelectedFile().getAbsolutePath().toLowerCase();
					Heap.setCurrentTracePrimary();
				}

				try
				{
					properties.setProperty("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
					properties.store(new FileOutputStream(propertiesFileName), null);
				}
				catch (IOException ex)
				{
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
}

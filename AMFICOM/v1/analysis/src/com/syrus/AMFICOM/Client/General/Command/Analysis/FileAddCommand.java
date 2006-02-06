package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.io.BellcoreStructure;

public class FileAddCommand extends AbstractCommand
{
	private ApplicationContext aContext;
	private String propertiesFileName = "analysis.properties";

	public FileAddCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	@Override
	public Object clone()
	{
		return new FileAddCommand(this.aContext);
	}

	@Override
	public void execute()
	{
		Properties properties = new Properties();
		String lastDir = "";
		try
		{
			properties.load(new FileInputStream(this.propertiesFileName));
			lastDir = properties.getProperty("lastdir");
		} catch (IOException ex)
		{
		}

		JFileChooser chooser = new JFileChooser(lastDir);
		chooser.addChoosableFileFilter(new ChoosableFileFilter("sor", "Bellcore"));
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			//System.out.println("DEBUG: the user has added file " + chooser.getSelectedFile().getAbsolutePath()); // FIX//ME: debugging purpose only
			File selectedFile = chooser.getSelectedFile();
			String id = selectedFile.getAbsolutePath().toLowerCase();
			if (Heap.hasSecondaryBSKey(id))
			{
				//JOptionPane cDialog = new JOptionPane();
				int ret = JOptionPane.showConfirmDialog (
						null,
						LangModelAnalyse.getString("messageFileAlreadyLoaded"),
						"",
						JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.NO_OPTION)
					 return;
				if (ret == JOptionPane.YES_OPTION)
					 new FileRemoveCommand(id, this.aContext).execute();
			}
			/*
			TraceReader tr = new TraceReader();
			BellcoreStructure bs = tr.getData(chooser.getSelectedFile());
			if (bs == null)
			{
				bs = tr.getData(chooser.getSelectedFile());
				if (bs == null)
				{
					JOptionPane.showMessageDialog (Environment.getActiveWindow(),
							LangModelAnalyse.getString("messageReadError") + ":\n" + chooser.getSelectedFile().getAbsolutePath(),
							LangModelAnalyse.getString("messageError"),
							JOptionPane.OK_OPTION);
					return;
				}
			}
			*/
			BellcoreStructure bs = FileOpenCommand.readTraceFromFile(selectedFile);
			bs.title = selectedFile.getName();
			Heap.putSecondaryTraceByKeyFromBS(id, bs);
			// Heap.secondaryTraceOpened(id, bs);
			Heap.setCurrentTrace(id);
			try
			{
				properties.setProperty("lastdir", selectedFile.getParent().toLowerCase());
				properties.store(new FileOutputStream(this.propertiesFileName), null);
			} catch (IOException ex)
			{
			}
		}
	}
}

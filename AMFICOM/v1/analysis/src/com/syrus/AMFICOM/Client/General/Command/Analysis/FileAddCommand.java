package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.TraceReader;

public class FileAddCommand extends AbstractCommand
{
	private ApplicationContext aContext;
	private String propertiesFileName = "analysis.properties";

	public FileAddCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new FileAddCommand(aContext);
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
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			System.out.println("DEBUG: the user has added file " + chooser.getSelectedFile().getAbsolutePath()); // FIXME: debugging purpose only
			String id = chooser.getSelectedFile().getAbsolutePath().toLowerCase();
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
					 new FileRemoveCommand(id, aContext).execute();
			}
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
			bs.title = chooser.getSelectedFile().getName();
			Heap.putSecondaryTraceByKey(id, bs);
			// Heap.secondaryTraceOpened(id, bs);
			Heap.setCurrentTrace(id);
			try
			{
				properties.setProperty("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
				properties.store(new FileOutputStream(propertiesFileName), null);
			}
			catch (IOException ex)
			{
			}
		}
	}
}

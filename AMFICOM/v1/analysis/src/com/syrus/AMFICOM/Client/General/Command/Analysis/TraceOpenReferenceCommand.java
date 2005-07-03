package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.*;
import java.util.Properties;

import javax.swing.JFileChooser;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.io.*;

public class TraceOpenReferenceCommand extends AbstractCommand
{
	private ApplicationContext aContext;
	private String propertiesFileName = "analysis.properties";

	public TraceOpenReferenceCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new TraceOpenReferenceCommand(aContext);
	}

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
		chooser.addChoosableFileFilter(new ChoosableFileFilter("sor", "Bellcore"));
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			String id = chooser.getSelectedFile().getAbsolutePath().toLowerCase();
			TraceReader tr = new TraceReader();
			BellcoreStructure bs;
			bs = tr.getData(chooser.getSelectedFile());
			if (bs == null)
			{
				 System.out.println("Error reading file: " + id);
				 return;
			}
			if (!Heap.hasEmptyAllBSMap())
			{
				if (Heap.getBSReferenceTrace() != null)
					 new FileRemoveCommand (Heap.REFERENCE_TRACE_KEY, aContext).execute();
			}

			String activeRefId = chooser.getSelectedFile().getAbsolutePath().toLowerCase();
			bs.title = activeRefId;
			Heap.setBSReferenceTrace(bs);
			//Heap.referenceTraceOpened(activeRefId, bs);
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

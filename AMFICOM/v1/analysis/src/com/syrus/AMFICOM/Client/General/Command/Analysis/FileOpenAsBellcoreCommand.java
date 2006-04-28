package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.AnalysisIniFile;
import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;

public class FileOpenAsBellcoreCommand extends AbstractCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;
	private String propertiesFileName = AnalysisIniFile.INI_FILE_NAME;

	public FileOpenAsBellcoreCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	@Override
	public Object clone()
	{
		return new FileOpenAsBellcoreCommand(this.dispatcher, this.aContext);
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
		String[] traces = {"sor"};
		chooser.addChoosableFileFilter(new ChoosableFileFilter(traces, "Bellcore Traces"));
		int returnVal = chooser.showOpenDialog(Environment.getActiveWindow());
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			BellcoreReader br = new BellcoreReader();
			BellcoreStructure bs = br.getData(chooser.getSelectedFile());
			if (bs == null)
			{
				JOptionPane.showMessageDialog (Environment.getActiveWindow(),
																			 LangModelAnalyse.getString("messageReadError") + ": " + chooser.getSelectedFile().getAbsolutePath(),
																			 LangModelAnalyse.getString("messageError"),
																			 JOptionPane.OK_OPTION);
				return;
			}
			bs.title = chooser.getSelectedFile().getName();
			Heap.openPrimaryTraceFromBS(bs, bs.title);
			Heap.makePrimaryAnalysis();

			try
			{
				properties.setProperty("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
				properties.store(new FileOutputStream(this.propertiesFileName), null);
			} catch (IOException ex)
			{
			}
			Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}

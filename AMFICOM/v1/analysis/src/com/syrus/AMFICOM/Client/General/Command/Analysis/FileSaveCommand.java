package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;

public class FileSaveCommand extends AbstractCommand
{
	private ApplicationContext aContext;
	private String propertiesFileName = "analysis.properties"; // FIXME: вынести работу с этим файлом в отдельный метод

	public FileSaveCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	@Override
	public Object clone()
	{
		return new FileSaveCommand(this.aContext);
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
		int returnVal = chooser.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			BellcoreStructure bs = Heap.getPFTracePrimary().getBS();
			try
			{
				FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile());
				BellcoreWriter bw = new BellcoreWriter();
				fos.write(bw.write(bs));
				fos.close();

				try
				{
					properties.setProperty("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
					properties.store(new FileOutputStream(this.propertiesFileName), null);
				} catch (IOException ex)
				{
				}
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
}

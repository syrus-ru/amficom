package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.*;
import java.util.Properties;

import java.awt.Cursor;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.io.*;

public class FileOpenCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private BellcoreStructure bs;
	private ApplicationContext aContext;
	private Checker checker;
	private String propertiesFileName = "analysis.properties";

	public FileOpenCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher )value);
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public Object clone()
	{
		return new FileOpenCommand(dispatcher, aContext);
	}

	public void execute()
	{
		try
		{
			this.checker = new Checker(this.aContext.getSessionInterface());
			if(!checker.checkCommand(checker.openReflectogrammFile))
			{
				return;
			}
		}
		catch (NullPointerException ex)
		{
			System.out.println("Application context and/or user are not defined");
			return;
		}

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
		chooser.addChoosableFileFilter(new ChoosableFileFilter("sor", "Bellcore GR-196-CORE "));
		chooser.addChoosableFileFilter(new ChoosableFileFilter(new String[] {"dat", "ref", "trc"}, "NetTest / Laser Precision "));
		chooser.addChoosableFileFilter(new ChoosableFileFilter("tfw", "Acterna / Wavetek "));
		//chooser.addChoosableFileFilter(new ChoosableFileFilter(new String[] {"tra", "trb", "trc"}, "Ando "));
		ChoosableFileFilter all = new ChoosableFileFilter(
			//	new String[] {"sor", "dat", "ref", "tra", "trb", "trc", "tfw", "001", "002", "003", "004", "005", "006", "007", "008", "009"},
					new String[] {"sor", "dat", "ref", "trc", "tfw", "001", "002", "003", "004", "005", "006", "007", "008", "009"},
				"All Known Trace Formats");
		all.setExtensionListInDescription(false);
		chooser.addChoosableFileFilter(all);
		int returnVal = chooser.showOpenDialog(Environment.getActiveWindow());
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			Environment.getActiveWindow().setCursor(new Cursor(Cursor.WAIT_CURSOR));
			TraceReader tr = new TraceReader();
			bs = tr.getData(chooser.getSelectedFile());
			if (bs == null)
			{
				bs = tr.getData(chooser.getSelectedFile());
				if (bs == null)
				{
					JOptionPane.showMessageDialog (Environment.getActiveWindow(),
							LangModelAnalyse.getString("messageReadError") + ":\n" + chooser.getSelectedFile().getAbsolutePath(),
							LangModelAnalyse.getString("messageError"),
							JOptionPane.OK_OPTION);
					Environment.getActiveWindow().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					return;
				}
			}
			if (Pool.getMap("bellcorestructure") != null )
			{
				if ((BellcoreStructure)Pool.get("bellcorestructure", "primarytrace") != null)
					new FileCloseCommand(dispatcher, aContext).execute();
			}

			String activeRefId = chooser.getSelectedFile().getName();
			bs.title = activeRefId;
			Pool.put("bellcorestructure", "primarytrace", bs);
			Pool.put("activecontext", "activepathid", bs.supParams.OT);

			Environment.getActiveWindow().setCursor(new Cursor(Cursor.WAIT_CURSOR));

			new InitialAnalysisCommand().execute();
			//new MinuitAnalyseCommand(dispatcher, "primarytrace", aContext).execute();

/*

			File f = new File("E:\\Incoming\\Arseniy\\20040517 134015-revents");
			byte[] buf = new byte[(int)f.length()];
			try
			{
				FileInputStream in = new FileInputStream(f);
				in.read(buf);
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}

			ReflectogramEvent[] re = ReflectogramEvent.fromByteArray(buf);
			Pool.put("eventparams", "primarytrace", re);

			f = new File("E:\\Incoming\\Arseniy\\20040517 134015-thresholds");
			buf = new byte[(int)f.length()];
			try
			{
				FileInputStream in = new FileInputStream(f);
				in.read(buf);
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}

			Threshold[] threshs = Threshold.fromByteArray(buf);
			for (int i = 0; i < re.length; i++)
				re[i].setThreshold(threshs[i]);

			f = new File("E:\\Incoming\\Arseniy\\20040517 134015-etalon");
			buf = new byte[(int)f.length()];
			try
			{
				FileInputStream in = new FileInputStream(f);
				in.read(buf);
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}

			ReflectogramEvent[] etalon = ReflectogramEvent.fromByteArray(buf);
			for (int i = 0; i < re.length; i++)
				etalon[i].setThreshold(threshs[i]);
			Pool.put("eventparams", "etalon", etalon);

			ReflectogramComparer comp = new ReflectogramComparer(re, etalon, threshs, false);
			ReflectogramAlarm[] alarms = comp.getAlarms();
*/
			dispatcher.notify(new RefChangeEvent("primarytrace",
											RefChangeEvent.OPEN_EVENT + RefChangeEvent.SELECT_EVENT));
			dispatcher.notify(new RefUpdateEvent("primarytrace", RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));

//			dispatcher.notify(new RefUpdateEvent("etalon",
//											RefUpdateEvent.THRESHOLDS_UPDATED_EVENT));

			try
			{
				properties.setProperty("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
				properties.store(new FileOutputStream(propertiesFileName), null);
			}
			catch (IOException ex)
			{
			}

			Environment.getActiveWindow().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
}


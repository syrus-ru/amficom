package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import java.awt.Cursor;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.io.*;

public class FileOpenCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;
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
		if (!AnalyseMainFrameSimplified.DEBUG) // XXX: saa: security bypass
		{
			try
			{
				Checker checker = new Checker(this.aContext.getSessionInterface());
				if(!checker.checkCommand(Checker.openReflectogrammFile))
				{
					return;
				}
			}
			catch (NullPointerException ex)
			{
				System.out.println("Application context and/or user are not defined");
				return;
			}
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

		JFileChooser chooser = new JFileChooser(lastDir); // XXX: ��� ���������������� ������� ������ � drive Letter-��� ��� ����� ���������

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
			System.out.println("DEBUG: the user has opened file " + chooser.getSelectedFile().getAbsolutePath()); // FIXME: debugging purpose only
			Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			TraceReader tr = new TraceReader();
			BellcoreStructure bs;
			try
			{
				bs = tr.getData(chooser.getSelectedFile());
			}
			catch (UnsatisfiedLinkError e)
			{
				bs = null;
			}
			if (bs == null)
			{
				if (true && AnalyseMainFrameSimplified.DEBUG) // XXX: saa: probable security bypass
				{
					// ���� ��� ������� ��� ���������� ����� � ������������ ���
					// ���������� �������������� �� ���������� �����,
					// ���� �� ������� ������� � ������� ���������� �������
					try
					{
						//bs = new BellcoreCreator(new double[] { 30, 30, 30, 22, 21, 20, 19, 10, 1, 1 }).getBS();
						{
							FileInputStream fis = new FileInputStream(chooser.getSelectedFile());
							DataInputStream dis = new DataInputStream(fis);
							ArrayList al = new ArrayList();
							String s;
							while ((s = dis.readLine()) != null)
								al.add(s);
							int N = al.size();
							System.out.println("reading file: N=" + N);
							double[] dl = new double[N];
							for (int i = 0; i < N; i++)
								dl[i] = Double.parseDouble((String )al.get(i));
							bs = new BellcoreCreator(dl).getBS();
						}
					} catch (IOException e1)
					{
						// @todo Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else
				{
					bs = tr.getData(chooser.getSelectedFile());
					if (bs == null)
					{
						JOptionPane.showMessageDialog (Environment.getActiveWindow(),
								LangModelAnalyse.getString("messageReadError") + ":\n" + chooser.getSelectedFile().getAbsolutePath(),
								LangModelAnalyse.getString("messageError"),
								JOptionPane.OK_OPTION);
						Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						return;
					}
				}
			}
			if (!Heap.hasEmptyAllBSMap())
			{
				if (Heap.getBSPrimaryTrace() != null)
					new FileCloseCommand(dispatcher, aContext).execute();
			}

			String activeRefId = chooser.getSelectedFile().getName();
			bs.title = activeRefId;
			Heap.setBSPrimaryTrace(bs);
			Heap.setActiveContextActivePathIDToEmptyString();
			Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			new InitialAnalysisCommand().execute();
			//new MinuitAnalyseCommand(dispatcher, RefUpdateEvent.PRIMARY_TRACE, aContext).execute();
			Heap.primaryTraceOpened(bs);

			Heap.setCurrentTracePrimary();
			dispatcher.notify(new RefUpdateEvent(RefUpdateEvent.PRIMARY_TRACE, RefUpdateEvent.ANALYSIS_PERFORMED_EVENT));
			try
			{
				properties.setProperty("lastdir", chooser.getSelectedFile().getParent().toLowerCase());
				properties.store(new FileOutputStream(propertiesFileName), null);
			}
			catch (IOException ex)
			{
			}

			Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}


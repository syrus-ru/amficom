package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.io.BellcoreCreator;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.TraceReader;

public class FileOpenCommand extends AbstractCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;
	private String propertiesFileName = "analysis.properties";

	public FileOpenCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new FileOpenCommand(dispatcher, aContext);
	}

	// may return null
	public static BellcoreStructure readTraceFromFile(File file) {
		TraceReader tr = new TraceReader();
		BellcoreStructure bs = null;
		//System.out.println("FileName: " + file.getName());
		bs = tr.getData(file); // note: UnsatisfiedLinkError is possible if no DLL loaded. Stas says that this needs not be catched
		if (bs != null)
			return bs;
		if (true && AnalyseMainFrameSimplified.DEBUG)
		{
			// FIXME: debug-only code
			// этот код написан для отладочных целей и предназначен для
			// считывания рефлектограммы из текстового файла,
			// если не удается считать с помощью известного формата
			try
			{
				{
					FileReader fr = new FileReader(file);
					BufferedReader br = new BufferedReader(fr);
					ArrayList al = new ArrayList();
					String s;
					while ((s = br.readLine()) != null)
						al.add(s);
					final int N = al.size();
					//System.out.println("reading file: N=" + N);
					double[] dl = new double[N];
					for (int i = 0; i < N; i++)
						dl[i] = Double.parseDouble(((String)al.get(i))
								.replaceFirst("\\S+\\s+", "")); // cut first column if present
					bs = new BellcoreCreator(dl).getBS();
					br.close();
				}
			} catch (IOException e1)
			{
				// FIXME: exceptions: (debug only) could not load text mode trace
				e1.printStackTrace();
			}
		} else
		{
			bs = tr.getData(file); // second attempt
		}
		return bs;
	}

	private static BellcoreStructure loadBS(File selectedFile) {
		//System.out.println("DEBUG: the user is opening file " + selectedFile.getAbsolutePath()); // FIX//ME: debugging purpose only
		Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		BellcoreStructure bs = readTraceFromFile(selectedFile);
		if (bs != null) {
			bs.title = selectedFile.getName();
		}
		return bs;
	}

	private void processBS(BellcoreStructure bs) {
		boolean testBehaviour = false && AnalyseMainFrameSimplified.DEBUG; // FIXME: debug only: for local comparison; should be false
		Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Heap.openPrimaryTraceFromBS(bs, bs.title);
		Heap.makePrimaryAnalysis();

		// FIXME: debug-only code
		if (testBehaviour && Heap.getMTMEtalon() != null) // XXX: наличие необходимости такого кода (пусть даже при отладке) говорит о неправильной подписке или обработке событий
			Heap.setMTMEtalon(Heap.getMTMEtalon());
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

		JFileChooser chooser = new JFileChooser(lastDir); // XXX: при подмонтированных сетевых дисках с drive Letter-ами тут может тормозить

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
		try {
			File selectedFile = chooser.getSelectedFile();
			BellcoreStructure bs = loadBS(selectedFile);

			if (bs == null) {
				JOptionPane.showMessageDialog (Environment.getActiveWindow(),
						LangModelAnalyse.getString("messageReadError") + ":\n" + selectedFile.getAbsolutePath(),
						LangModelAnalyse.getString("messageError"),
						JOptionPane.OK_OPTION);
				Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}

			processBS(bs);

			try
			{
				properties.setProperty("lastdir", selectedFile.getParent().toLowerCase());
				properties.store(new FileOutputStream(propertiesFileName), null);
			} catch (IOException ex)
			{
			}
		} finally {
			Environment.getActiveWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}

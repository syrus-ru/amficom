package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.text.SimpleDateFormat;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.io.BellcoreStructure;

public class PrimaryParametersFrame extends ATableFrame
																		implements OperationListener
{
	private static StringBuffer km = new StringBuffer(" ").append(LangModelAnalyse.getString("km"));
	private static StringBuffer mt = new StringBuffer(" ").append(LangModelAnalyse.getString("mt"));
	private static StringBuffer nm = new StringBuffer(" ").append(LangModelAnalyse.getString("nm"));
	private static StringBuffer db = new StringBuffer(" ").append(LangModelAnalyse.getString("dB"));
	private static StringBuffer ns = new StringBuffer(" ").append(LangModelAnalyse.getString("ns"));

	private Dispatcher dispatcher;
	private FixedSizeEditableTableModel tModel;
	private ATable jTable;

	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();

	public PrimaryParametersFrame()
	{
		this(new Dispatcher());
	}

	public PrimaryParametersFrame(Dispatcher dispatcher)
	{
		super();

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, RefChangeEvent.typ);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefChangeEvent.typ))
		{
			RefChangeEvent rce = (RefChangeEvent)ae;
			if(rce.OPEN)
			{
				String id = (String)(rce.getSource());
				if (id.equals("primarytrace"))
				{
					setVisible(true);
				}
			}
			if(rce.SELECT)
			{
				String id = (String)(rce.getSource());
				updTableModel (id);
			}
			if(rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				if (id.equals("all"))
				{
				//	tModel.clearTable();
					setVisible(false);
				}
			}
		}
	}

	public String getReportTitle()
	{
		return LangModelAnalyse.getString("parametersTitle");
	}

	public TableModel getTableModel()
	{
		return tModel;
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		tModel = new FixedSizeEditableTableModel(
					new String[] {LangModelAnalyse.getString("parametersKey"),
												LangModelAnalyse.getString("parametersValue")},
					new String[] {""},
					new String[] {
						LangModelAnalyse.getString("module id"),
						LangModelAnalyse.getString("wavelength"),
						LangModelAnalyse.getString("pulsewidth"),
						LangModelAnalyse.getString("groupindex"),
						LangModelAnalyse.getString("averages"),
						LangModelAnalyse.getString("resolution"),
						LangModelAnalyse.getString("range"),
						LangModelAnalyse.getString("date"),
						LangModelAnalyse.getString("time"),
						LangModelAnalyse.getString("backscatter")
					},
					null);
		jTable = new ATable(tModel);
		jTable.getColumnModel().getColumn(0).setPreferredWidth(160);

		setContentPane(mainPanel);
		this.setSize(new Dimension(200, 213));
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		//this.setMaximizable(true);
		this.setTitle(LangModelAnalyse.getString("parametersTitle"));

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
//		jTable.setMaximumSize(new Dimension(200, 213));
		jTable.setMinimumSize(new Dimension(200, 213));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
		updColorModel();
	}

	private void updColorModel()
	{
		scrollPane.getViewport().setBackground(SystemColor.window);
		jTable.setBackground(SystemColor.window);
		jTable.setForeground(ColorManager.getColor("textColor"));
		jTable.setGridColor(ColorManager.getColor("tableGridColor"));
	}

	void updTableModel(String id)
	{
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
		if (bs == null)
			return;

//		double res = (double)bs.fxdParams.DS[0] * .3d / (double)bs.fxdParams.GI;
//		double res2 = (bs.fxdParams.AR - bs.fxdParams.AO) * 3d / ((double)bs.dataPts.TNDP * (double)bs.fxdParams.GI/1000d);
		double res = bs.getResolution();
		double range = bs.getRange();
		String date = sdf.format(bs.getDate());

		tModel.updateColumn(new Object[] {
			bs.getOpticalModuleId(),
			new StringBuffer().append(bs.getWavelength()).append(nm).toString(),
			new StringBuffer().append(bs.getPulsewidth()).append(ns).toString(),
			String.valueOf(bs.getIOR()),
			String.valueOf(bs.getAverages()),
			new StringBuffer().append(Math.round(res)).append(mt).toString(),
			new StringBuffer().append(Math.round(range)).append(km).toString(),
			date.substring(0, 9),
			date.substring(9),
			new StringBuffer().append(bs.getBackscatter()).append(db).toString(),
		}, 1);
		jTable.updateUI();
	}
}

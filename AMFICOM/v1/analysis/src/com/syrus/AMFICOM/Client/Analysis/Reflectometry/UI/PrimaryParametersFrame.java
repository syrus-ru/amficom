package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.UI.ATable;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.io.BellcoreStructure;

public class PrimaryParametersFrame extends ATableFrame
																		implements OperationListener
{

	private Dispatcher dispatcher;
	private FixedSizeEditableTableModel tModel;
	private ATable jTable;

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
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
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
		SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
		String date = sdf.format(bs.getDate());

		tModel.updateColumn(new Object[] {
			bs.getOpticalModuleId(),
			bs.getWavelength() + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NM),
			bs.getPulsewidth() + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NS),
			String.valueOf(bs.getIOR()),
			String.valueOf(bs.getAverages()),
			Math.round(res) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_MT),
			Math.round(range) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
			date.substring(0, 9),
			date.substring(9),
			bs.getBackscatter() + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
		}, 1);
		jTable.updateUI();
	}
}

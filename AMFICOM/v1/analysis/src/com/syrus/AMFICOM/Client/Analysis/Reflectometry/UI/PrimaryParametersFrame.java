package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.UI.GeneralTableModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.General.UI.ATable;

import com.syrus.io.BellcoreStructure;

public class PrimaryParametersFrame extends ATableFrame
																		implements OperationListener
{
	private Dispatcher dispatcher;
	private GeneralTableModel tModel;
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
					setTableModel();
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
					tModel.clearTable();
					setVisible(false);
				}
			}
		}
	}

	public String getReportTitle()
	{
		return LangModelAnalyse.String("parametersTitle");
	}

	public TableModel getTableModel()
	{
		return tModel;
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		tModel = new GeneralTableModel(
					new String[] {LangModelAnalyse.String("parametersKey"),
												LangModelAnalyse.String("parametersValue")},
					new String[] {"1", "2"},
					0);
		jTable = new ATable(tModel);
		jTable.getColumnModel().getColumn(0).setPreferredWidth(160);

		setContentPane(mainPanel);
		this.setSize(new Dimension(200, 213));
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		//this.setMaximizable(true);
		this.setTitle(LangModelAnalyse.String("parametersTitle"));

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		jTable.setSelectionMode(jTable.getSelectionModel().SINGLE_SELECTION);
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
		setTableModel();
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
		if (bs == null)
			return;
		tModel.setValueAt(bs.supParams.OMID, 0, 1);
		tModel.setValueAt(String.valueOf(bs.fxdParams.AW / 10)  + " " + LangModelAnalyse.String("nm"), 1, 1);
		tModel.setValueAt(String.valueOf(bs.fxdParams.PWU[0]) + " " + LangModelAnalyse.String("ns"), 2, 1);
		tModel.setValueAt(String.valueOf((double)bs.fxdParams.GI / 100000d), 3, 1);
		tModel.setValueAt(String.valueOf(bs.fxdParams.NAV), 4, 1);

		double tmp = (double)bs.fxdParams.DS[0] * .3d / (double)bs.fxdParams.GI;
		if (bs.fxdParams.UD.equals("km")) tmp*=1000d;
		tModel.setValueAt(String.valueOf(Math.round(tmp)) + " " + LangModelAnalyse.String("mt"), 5, 1);

		tmp = (double)bs.fxdParams.AR * 3d / (double)bs.fxdParams.GI * 1000;
		if (bs.fxdParams.UD.equals("mt")) tmp/=1000d;
		tModel.setValueAt(String.valueOf(Math.round(tmp)) + " " + LangModelAnalyse.String("km"), 6, 1);

		String str = sdf.format(new Date(bs.fxdParams.DTS * 1000));
		tModel.setValueAt(str.substring(0, 9), 7, 1);
		tModel.setValueAt(str.substring(9), 8, 1);

		str = String.valueOf(bs.fxdParams.BC);
		tModel.setValueAt(String.valueOf(- (double)bs.fxdParams.BC / 10. + " " + LangModelAnalyse.String("dB")), 9, 1);

		jTable.updateUI();
	}

	void setTableModel()
	{
		tModel.clearTable();

		Vector omid = new Vector(2);
		omid.add(LangModelAnalyse.String("module id"));
		omid.add("");
		tModel.insertRow(omid);

		Vector aw = new Vector(2);
		aw.add(LangModelAnalyse.String("wavelength"));
		aw.add("");
		tModel.insertRow(aw);

		Vector pwu = new Vector(2);
		pwu.add(LangModelAnalyse.String("pulsewidth"));
		pwu.add("");
		tModel.insertRow(pwu);

		Vector gi  = new Vector(2);
		gi.add(LangModelAnalyse.String("groupindex"));
		gi.add("");
		tModel.insertRow(gi);

		Vector nav = new Vector(2);
		nav.add(LangModelAnalyse.String("averages"));
		nav.add("");
		tModel.insertRow(nav);

		Vector res = new Vector(2);
		res.add(LangModelAnalyse.String("resolution"));
		res.add("");
		tModel.insertRow(res);

		Vector range  = new Vector(2);
		range.add(LangModelAnalyse.String("range"));
		range.add("");
		tModel.insertRow(range);

		Vector date = new Vector(2);
		date.add(LangModelAnalyse.String("date"));
		date.add("");
		tModel.insertRow(date);
		Vector time = new Vector(2);
		time.add(LangModelAnalyse.String("time"));
		time.add("");
		tModel.insertRow(time);

		Vector bc = new Vector(2);
		bc.add(LangModelAnalyse.String("backscatter"));
		bc.add("");
		tModel.insertRow(bc);
	}
}
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.table.JTableHeader;

import com.syrus.AMFICOM.Client.General.UI.ATable;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.UI.GeneralTableModel;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.io.BellcoreStructure;
import com.syrus.AMFICOM.analysis.dadara.MathRef;

public class MarkersInfoFrame extends JInternalFrame
																		implements OperationListener
{
	private Dispatcher dispatcher;
	private GeneralTableModel tModel;
	private ATable jTable;
	private MarkersInfo mInfo;

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	double sigma;
	BellcoreStructure bs;



	public MarkersInfoFrame()
	{
		this(new Dispatcher());
	}

	public MarkersInfoFrame(Dispatcher dispatcher)
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
		dispatcher.register(this, RefUpdateEvent.typ);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefChangeEvent.typ))
		{
			RefChangeEvent rce = (RefChangeEvent)ae;
			if (rce.OPEN)
			{
				String id = (String)(rce.getSource());
				if (id.equals("primarytrace"))
				{
					bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
					sigma = MathRef.calcSigma(bs.fxdParams.AW/10, bs.fxdParams.PWU[0]);
					setTableModel();
					setVisible(true);
				}
			}
			if (rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				if (id.equals("all"))
				{
					tModel.clearTable();
					setVisible(false);
				}
			}

		}
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;

			if (rue.MARKER_MOVED)
			{
				updTableModel ((MarkersInfo)rue.getSource());
			}
		}
	}

	private void jbInit() throws Exception
	{
		//setFocusCycleRoot(false);
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		String markerInfoKey = LangModelAnalyse.getString("markerInfoKey");
		String markerInfoValue = LangModelAnalyse.getString("markerInfoValue");
		tModel = new GeneralTableModel(
					new String[] {markerInfoKey, markerInfoValue},
					new String[] {"1", "2"},
					0);
		jTable = new ATable(tModel);

		jTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		jTable.getColumnModel().getColumn(1).setPreferredWidth(80);

//		this.getContentPane().add(mainPanel);
		this.getContentPane().add(mainPanel);

		this.setSize(new Dimension(200, 213));
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		//this.setMaximizable(true);
		this.setTitle(LangModelAnalyse.getString("markerInfoTitle"));

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		jTable.setSelectionMode(jTable.getSelectionModel().SINGLE_SELECTION);
		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTable.setMaximumSize(new Dimension(200, 213));
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

	void updTableModel(MarkersInfo mInfo)
	{
		// положение курсора А
		tModel.setValueAt(String.valueOf(Math.round(mInfo.a_pos_m))+ " " + LangModelAnalyse.getString("mt"), 0, 1);
		if (mInfo.a_type == MarkersInfo.NONREFLECTIVE)
		{// потери в А
			tModel.setValueAt(LangModelAnalyse.getString("markerALoss"), 1, 0);
			tModel.setValueAt(String.valueOf(MathRef.round_4(mInfo.a_loss))+ " " + LangModelAnalyse.getString("dB"), 1, 1);
		}
		if (mInfo.a_type == MarkersInfo.REFLECTIVE)
		{// отражение в А
			tModel.setValueAt(LangModelAnalyse.getString("markerAReflection"), 1, 0);
			if (mInfo.a_reflectance > 0)
				tModel.setValueAt(Double.toString( MathRef.round_4 (MathRef.calcReflectance(sigma, mInfo.a_reflectance)))+ " " + LangModelAnalyse.getString("dB"), 1, 1);
			else
				tModel.setValueAt("-----", 1, 1);
		}
		if (mInfo.a_type == MarkersInfo.NOANALYSIS)
		{
			tModel.setValueAt("-----", 1, 1);
			tModel.setValueAt("-----", 2, 1);
			tModel.setValueAt("-----", 3, 1);
		}
		else
		{ // затухание в А
			tModel.setValueAt(String.valueOf(MathRef.round_4(mInfo.a_attfactor))+ " " + LangModelAnalyse.getString("dB")+ "/" + LangModelAnalyse.getString("km"), 2, 1);
		// накапливаемые потери в А
			 tModel.setValueAt(String.valueOf(MathRef.round_4(mInfo.a_cumulative_loss))+ " " + LangModelAnalyse.getString("dB"), 3, 1);
		}
		// положение курсора В
		tModel.setValueAt(String.valueOf(Math.round(mInfo.b_pos_m))+ " " + LangModelAnalyse.getString("mt"), 4, 1);
		// расстояние А-В
		if (mInfo.a_pos < mInfo.b_pos)
			tModel.setValueAt(LangModelAnalyse.getString("markerBAdist"), 5, 0);
		else
			tModel.setValueAt(LangModelAnalyse.getString("markerABdist"), 5, 0);
		tModel.setValueAt(String.valueOf(Math.round(mInfo.a_b_distance_m))+ " " + LangModelAnalyse.getString("mt"), 5, 1);
			// 2pt. loss
			tModel.setValueAt(String.valueOf(MathRef.round_4(mInfo.a_b_loss))+ " " + LangModelAnalyse.getString("dB"), 6, 1);
			// 2pt. Att
			tModel.setValueAt(String.valueOf(MathRef.round_4(mInfo.a_b_attenuation))+ " " + LangModelAnalyse.getString("dB") + "/" + LangModelAnalyse.getString("km"), 7, 1);
			// LSA Att
			tModel.setValueAt(String.valueOf(MathRef.round_4(mInfo.lsa_attenuation))+ " " + LangModelAnalyse.getString("dB") + "/" + LangModelAnalyse.getString("km"), 8, 1);
			// 2pt. ORL
			if (mInfo.a_b_orl > 0)
				tModel.setValueAt(String.valueOf(MathRef.round_4(mInfo.a_b_orl))+ " " + LangModelAnalyse.getString("dB"), 9, 1);
			else
				tModel.setValueAt("-----", 9, 1);
		jTable.updateUI();
	}

	void setTableModel()
	{
		tModel.clearTable();

		Vector apos = new Vector(2);
		apos.add(LangModelAnalyse.getString("markerAPos"));
		apos.add("");
		tModel.insertRow(apos);

		Vector lost = new Vector(2);
		lost.add(LangModelAnalyse.getString("markerALoss"));
		lost.add("");
		tModel.insertRow(lost);

		Vector att = new Vector(2);
		att.add(LangModelAnalyse.getString("markerAAttenuation"));
		att.add("");
		tModel.insertRow(att);

		Vector cumloss = new Vector(2);
		cumloss.add(LangModelAnalyse.getString("markerACumloss"));
		cumloss.add("");
		tModel.insertRow(cumloss);

		Vector bpos = new Vector(2);
		bpos.add(LangModelAnalyse.getString("markerBPos"));
		bpos.add("");
		tModel.insertRow(bpos);

		Vector abdist = new Vector(2);
		abdist.add(LangModelAnalyse.getString("markerBAdist"));
		abdist.add("");
		tModel.insertRow(abdist);

		Vector loss_2pt = new Vector(2);
		loss_2pt.add(LangModelAnalyse.getString("markerABloss"));
		loss_2pt.add("");
		tModel.insertRow(loss_2pt);

		Vector att_2pt = new Vector(2);
		att_2pt.add(LangModelAnalyse.getString("markerABatt"));
		att_2pt.add("");
		tModel.insertRow(att_2pt);

		Vector att_lsa = new Vector(2);
		att_lsa.add(LangModelAnalyse.getString("markerABlsaatt"));
		att_lsa.add("");
		tModel.insertRow(att_lsa);

		Vector orl_2pt = new Vector(2);
		orl_2pt.add(LangModelAnalyse.getString("markerABorl"));
		orl_2pt.add("");
		tModel.insertRow(orl_2pt);
	}
}

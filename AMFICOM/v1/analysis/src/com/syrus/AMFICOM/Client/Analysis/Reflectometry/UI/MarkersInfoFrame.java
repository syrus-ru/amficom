package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.io.BellcoreStructure;

public class MarkersInfoFrame extends JInternalFrame implements OperationListener
{
	private static StringBuffer km = new StringBuffer(" ").append(LangModelAnalyse.getString("km"));
	private static StringBuffer mt = new StringBuffer(" ").append(LangModelAnalyse.getString("mt"));
	private static StringBuffer db = new StringBuffer(" ").append(LangModelAnalyse.getString("dB"));
	private static StringBuffer dbkm = new StringBuffer(" ").append(LangModelAnalyse.getString("dB")).
			append('/').append(LangModelAnalyse.getString("km"));
	private static String dash = "-----";

	private Dispatcher dispatcher;
	private FixedSizeEditableTableModel tModel;
	private ATable jTable;
	private MarkersInfo mInfo;

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	double sigma;
	BellcoreStructure bs;

	private String[] data = new String[10];

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
					sigma = MathRef.calcSigma(bs.getWavelength(), bs.getPulsewidth());
					setVisible(true);
				}
			}
			if (rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				if (id.equals("all"))
				{
//					tModel.clearTable();
					setVisible(false);
				}
			}

		}
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;

			if (rue.markerMoved())
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
		tModel = new FixedSizeEditableTableModel(
				new String[] {markerInfoKey, markerInfoValue},
				new String[] {"", ""},
				new String[] {
					LangModelAnalyse.getString("markerAPos"),
					LangModelAnalyse.getString("markerALoss"),
					LangModelAnalyse.getString("markerAAttenuation"),
					LangModelAnalyse.getString("markerACumloss"),
					LangModelAnalyse.getString("markerBPos"),
					LangModelAnalyse.getString("markerBAdist"),
					LangModelAnalyse.getString("markerABloss"),
					LangModelAnalyse.getString("markerABatt"),
					LangModelAnalyse.getString("markerABlsaatt"),
					LangModelAnalyse.getString("markerABorl")
				},
				null);

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

		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
		data[0] = new StringBuffer().append(Math.round(mInfo.a_pos_m)).append(mt).toString();
		if (mInfo.a_type == MarkersInfo.NOANALYSIS)
		{
			data[1] = dash;
			data[2] = dash;
			data[3] = dash;
		}
		else
		{
			if (mInfo.a_type == MarkersInfo.NONREFLECTIVE)
			{// потери в А
				tModel.setValueAt(LangModelAnalyse.getString("markerALoss"), 1, 0);
				data[1] = new StringBuffer().append(MathRef.round_4(mInfo.a_loss)).append(db).toString();
			}
			else if (mInfo.a_type == MarkersInfo.REFLECTIVE)
			{// отражение в А
				tModel.setValueAt(LangModelAnalyse.getString("markerAReflection"), 1, 0);
				if (mInfo.a_reflectance > 0)
					data[1] = new StringBuffer().append(MathRef.round_4(MathRef.calcReflectance(sigma, mInfo.a_reflectance))).append(db).toString();
				else
					data[1] = dash;
			}
			// затухание в А
			data[2] = new StringBuffer().append(MathRef.round_4(mInfo.a_attfactor)).append(dbkm).toString();
			// накапливаемые потери в А
			data[3] = new StringBuffer().append(MathRef.round_4(mInfo.a_cumulative_loss)).append(db).toString();
		}
		// положение курсора В
		data[4] = new StringBuffer().append(Math.round(mInfo.b_pos_m)).append(mt).toString();
		// расстояние А-В
		if (mInfo.a_pos < mInfo.b_pos)
			tModel.setValueAt(LangModelAnalyse.getString("markerBAdist"), 5, 0);
		else
			tModel.setValueAt(LangModelAnalyse.getString("markerABdist"), 5, 0);
		data[5] = new StringBuffer().append(Math.round(mInfo.a_b_distance_m)).append(mt).toString();
			// 2pt. loss
		data[6] = new StringBuffer().append(MathRef.round_4(mInfo.a_b_loss)).append(db).toString();
			// 2pt. Att
		data[7] = new StringBuffer().append(MathRef.round_4(mInfo.a_b_attenuation)).append(dbkm).toString();
			// LSA Att
		data[8] = new StringBuffer().append(MathRef.round_4(mInfo.lsa_attenuation)).append(dbkm).toString();
			// 2pt. ORL
		if (mInfo.a_b_orl > 0)
			data[9] = new StringBuffer().append(MathRef.round_4(mInfo.a_b_orl)).append(db).toString();
		else
			data[9] = dash;
		tModel.updateColumn(data, 1);
		jTable.updateUI();
	}
}

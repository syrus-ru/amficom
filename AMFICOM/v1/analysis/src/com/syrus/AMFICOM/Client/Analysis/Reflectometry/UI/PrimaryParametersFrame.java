package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.text.DateFormat;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.CurrentTraceChangeListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.UI.ATable;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.io.BellcoreStructure;

public class PrimaryParametersFrame extends ATableFrame
implements BsHashChangeListener, CurrentTraceChangeListener
{

	private FixedSizeEditableTableModel tModel;
	private ATable jTable;

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();

	public PrimaryParametersFrame()
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

		init_module();
	}

	void init_module()
	{
		Heap.addBsHashListener(this);
		Heap.addCurrentTraceChangeListener(this);
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
//		jTable.getColumnModel().getColumn(0).setPreferredWidth(160);

		setContentPane(mainPanel);
//		this.setSize(new Dimension(200, 213));
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
//		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
//		jTable.setMaximumSize(new Dimension(200, 213));
//		jTable.setMinimumSize(new Dimension(200, 213));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
		updColorModel();
	}

	private void updColorModel()
	{
//		scrollPane.getViewport().setBackground(SystemColor.window);
//		jTable.setBackground(SystemColor.window);
//		jTable.setForeground(ColorManager.getColor("textColor"));
//		jTable.setGridColor(ColorManager.getColor("tableGridColor"));
	}

	void updTableModel(String id)
	{
		BellcoreStructure bs = Heap.getAnyBSTraceByKey(id);
		if (bs == null)
			return;

		double res = bs.getResolution();
		double range = bs.getRange();
		// XXX: date formatting: temporal fix
		//SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
		String date = DateFormat.getDateInstance().format(bs.getDate());
		String time = DateFormat.getTimeInstance().format(bs.getDate());

		tModel.updateColumn(new Object[] {
			bs.getOpticalModuleId(),
			bs.getWavelength() + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NM),
			bs.getPulsewidth() + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_NS),
			String.valueOf(bs.getIOR()),
			String.valueOf(bs.getAverages()),
			// XXX: we round resolution with 3 digits;
			// 4 digits can sometimes give 3.999 m resolution;
			// 2 digits are not enough for 0.125 m
			MathRef.floatRound(res, 3) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_MT),
			Math.round(range) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM),
			date,
			time,
			bs.getBackscatter() + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB),
		}, 1);
		jTable.updateUI();
	}

	public void bsHashAdded(String key, BellcoreStructure bs)
	{
		if (key.equals(RefUpdateEvent.PRIMARY_TRACE))
		{
			setVisible(true);
		}
	}

	public void bsHashRemoved(String key)
	{
	}

	public void bsHashRemovedAll()
	{
		setVisible(false);
	}

	public void currentTraceChanged(String id)
	{
		updTableModel (id);
	}
}

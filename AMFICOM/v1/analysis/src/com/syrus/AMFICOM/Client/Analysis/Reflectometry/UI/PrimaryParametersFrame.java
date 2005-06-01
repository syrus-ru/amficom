package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.*;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.io.BellcoreStructure;

public class PrimaryParametersFrame extends JInternalFrame
implements BsHashChangeListener, CurrentTraceChangeListener, ReportTable
{
	private WrapperedPropertyTableModel tModel;
	private WrapperedPropertyTable jTable;
	private PrimaryParameters p;

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
		p = new PrimaryParameters();
		tModel = new WrapperedPropertyTableModel(PrimaryParameretrsWrapper.getInstance(), p,
				new String[] { PrimaryParameretrsWrapper.KEY_MODULE_ID,
						PrimaryParameretrsWrapper.KEY_WAVELENGTH,
						PrimaryParameretrsWrapper.KEY_PULSEWIDTH,
						PrimaryParameretrsWrapper.KEY_GROUPINDEX,
						PrimaryParameretrsWrapper.KEY_AVERAGES,
						PrimaryParameretrsWrapper.KEY_RESOLUTION,
						PrimaryParameretrsWrapper.KEY_RANGE,
						PrimaryParameretrsWrapper.KEY_DATE,
						PrimaryParameretrsWrapper.KEY_TIME,
						PrimaryParameretrsWrapper.KEY_BACKSCATTER });
		jTable = new WrapperedPropertyTable(tModel);
		jTable.getColumnModel().getColumn(0).setPreferredWidth(170);

		setContentPane(mainPanel);
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
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
	}

	void updTableModel(String id)
	{
		BellcoreStructure bs = Heap.getAnyBSTraceByKey(id);
		if (bs == null)
			return;

		p.setBellcoreStructure(bs);
		jTable.updateUI();
	}

	public void bsHashAdded(String key, BellcoreStructure bs)
	{
		if (key.equals(Heap.PRIMARY_TRACE_KEY))
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

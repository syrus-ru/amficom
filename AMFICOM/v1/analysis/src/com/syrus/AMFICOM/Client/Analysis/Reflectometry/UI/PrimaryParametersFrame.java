package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.CurrentTraceChangeListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.PrimaryParameters;
import com.syrus.AMFICOM.analysis.PrimaryParametersWrapper;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class PrimaryParametersFrame extends JInternalFrame implements BsHashChangeListener, CurrentTraceChangeListener,
		ReportTable {
	private static final long serialVersionUID = -157147137393451579L;

	private WrapperedPropertyTableModel<PrimaryParameters> tModel;
	private WrapperedPropertyTable<PrimaryParameters> jTable;
	private PrimaryParameters p;

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();

	public PrimaryParametersFrame() {
		super();

		try {
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.initModule();
	}

	void initModule() {
		Heap.addBsHashListener(this);
		Heap.addCurrentTraceChangeListener(this);
	}

	public String getReportTitle() {
		return LangModelAnalyse.getString("parametersTitle");
	}

	public TableModel getTableModel() {
		return this.tModel;
	}

	private void jbInit() throws Exception {
		this.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.p = new PrimaryParameters();
		this.tModel = new WrapperedPropertyTableModel<PrimaryParameters>(PrimaryParametersWrapper.getInstance(),
				this.p,
				new String[] { PrimaryParametersWrapper.KEY_MODULE_ID,
						PrimaryParametersWrapper.KEY_WAVELENGTH,
						PrimaryParametersWrapper.KEY_PULSEWIDTH,
						PrimaryParametersWrapper.KEY_GROUPINDEX,
						PrimaryParametersWrapper.KEY_AVERAGES,
						PrimaryParametersWrapper.KEY_RESOLUTION,
						PrimaryParametersWrapper.KEY_RANGE,
						PrimaryParametersWrapper.KEY_DATE,
						PrimaryParametersWrapper.KEY_TIME,
						PrimaryParametersWrapper.KEY_BACKSCATTER });
		this.jTable = new WrapperedPropertyTable<PrimaryParameters>(this.tModel);
		this.jTable.setTableHeader(null);
		this.jTable.getColumnModel().getColumn(0).setPreferredWidth(170);

		this.setContentPane(this.mainPanel);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		// this.setMaximizable(true);
		this.setTitle(I18N.getString(AnalysisResourceKeys.FRAME_PRIMARY_PARAMETERS));

		this.mainPanel.setLayout(new BorderLayout());
		this.mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		this.scrollPane.setViewport(this.viewport);
		this.scrollPane.setAutoscrolls(true);

		this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.mainPanel.add(this.scrollPane, BorderLayout.CENTER);
		this.scrollPane.getViewport().add(this.jTable);
	}

	void updTableModel(final String id) {
		final PFTrace pf = Heap.getAnyPFTraceByKey(id);
		if (pf == null) {
			return;
		}

		this.p.init(pf.getBS());
		this.jTable.getModel().fireTableDataChanged();
	}

	public void bsHashAdded(final String key) {
		if (key.equals(Heap.PRIMARY_TRACE_KEY)) {
			this.setVisible(true);
		}
	}

	public void bsHashRemoved(final String key) {
	}

	public void bsHashRemovedAll() {
		this.setVisible(false);
	}

	public void currentTraceChanged(final String id) {
		this.updTableModel(id);
	}
}

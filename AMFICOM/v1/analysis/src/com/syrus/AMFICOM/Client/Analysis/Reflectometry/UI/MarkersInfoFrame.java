package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.MarkerResource;
import com.syrus.AMFICOM.analysis.MarkerResourceWrapper;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class MarkersInfoFrame extends JInternalFrame implements PropertyChangeListener, BsHashChangeListener {
	private static final long serialVersionUID = 1645657805656146998L;

	// Don't change sequence
	private static String[] DEFAULT_KEYS = new String[] { MarkerResourceWrapper.KEY_A_POSITION, // 0
			MarkerResourceWrapper.KEY_A_LOSS, // 1 (changes to KEY_A_REFLECTANCE)
			MarkerResourceWrapper.KEY_A_ATTENUATION, // 2
			MarkerResourceWrapper.KEY_A_CUMULATIVE_LOSS, // 3
			MarkerResourceWrapper.KEY_B_POSITION, // 4
			MarkerResourceWrapper.KEY_BA_DISTANCE, // 5 (changes to KEY_AB_DISTANCE)
			MarkerResourceWrapper.KEY_AB_LOSS, // 6
			MarkerResourceWrapper.KEY_AB_ATTENUATION, // 7
			MarkerResourceWrapper.KEY_AB_LSA_ATTENUATION, // 8
			MarkerResourceWrapper.KEY_AB_ORL // 9
	};
	MarkerResource res;

	private WrapperedPropertyTableModel<MarkerResource> tModel;
	private WrapperedPropertyTable<MarkerResource> jTable;

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	double sigma;

	public MarkersInfoFrame() {
		this(new Dispatcher());
	}

	public MarkersInfoFrame(final Dispatcher dispatcher) {
		super();

		try {
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.initModule(dispatcher);
	}

	void initModule(final Dispatcher dispatcher) {
		// dispatcher.register(this, RefChangeEvent.typ);
		dispatcher.addPropertyChangeListener(RefUpdateEvent.typ, this);
		Heap.addBsHashListener(this);
	}

	public void propertyChange(final PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(RefUpdateEvent.typ)) {
			final RefUpdateEvent rue = (RefUpdateEvent) ae;
			if (rue.markerMoved()) {
				updTableModel((MarkersInfo) rue.getNewValue());
			}
		}
	}

	private void jbInit() throws Exception {
		// setFocusCycleRoot(false);
		this.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.res = new MarkerResource();
		this.tModel = new WrapperedPropertyTableModel<MarkerResource>(MarkerResourceWrapper.getInstance(), this.res, DEFAULT_KEYS);

		this.jTable = new WrapperedPropertyTable<MarkerResource>(this.tModel);
		this.jTable.setTableHeader(null);

		this.jTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		this.jTable.getColumnModel().getColumn(1).setPreferredWidth(80);

		this.getContentPane().add(this.mainPanel);

		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		// this.setMaximizable(true);
		this.setTitle(LangModelAnalyse.getString("markerInfoTitle"));

		this.mainPanel.setLayout(new BorderLayout());
		this.mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		this.scrollPane.setViewport(this.viewport);
		this.scrollPane.setAutoscrolls(true);

		this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.mainPanel.add(this.scrollPane, BorderLayout.CENTER);
		this.scrollPane.getViewport().add(this.jTable);
	}

	void updTableModel(final MarkersInfo mInfo) {
		final String mt = " " + LangModelAnalyse.getString("mt");
		final String dB = " " + LangModelAnalyse.getString("dB");
		final String dbkm = " " + LangModelAnalyse.getString("dB/km");

		// setting keys
		if (mInfo.a_type == MarkersInfo.REFLECTIVE) {
			DEFAULT_KEYS[1] = MarkerResourceWrapper.KEY_A_REFLECTION;
		} else if (mInfo.a_type == MarkersInfo.NONREFLECTIVE) {
			DEFAULT_KEYS[1] = MarkerResourceWrapper.KEY_A_LOSS;
		}

		if (mInfo.a_pos < mInfo.b_pos) {
			DEFAULT_KEYS[5] = MarkerResourceWrapper.KEY_BA_DISTANCE;
		} else {
			DEFAULT_KEYS[5] = MarkerResourceWrapper.KEY_AB_DISTANCE;
		}

		// setting values
		this.res.setAPosition(Math.round(mInfo.a_pos_m) + mt);
		if (mInfo.a_type == MarkersInfo.NOANALYSIS) {
			this.res.setALoss(MarkerResource.DASH);
			this.res.setAReflectance(MarkerResource.DASH);
			this.res.setAAttenuation(MarkerResource.DASH);
			this.res.setACumulativeLoss(MarkerResource.DASH);
		} else {
			if (mInfo.a_type == MarkersInfo.NONREFLECTIVE) {// потери в А
				this.res.setALoss(MathRef.round_4(mInfo.a_loss) + dB);
			} else if (mInfo.a_type == MarkersInfo.REFLECTIVE) {// отражение в А
				if (mInfo.a_reflectance > 0) {
					this.res.setAReflectance(MathRef.round_4(MathRef.calcReflectance(this.sigma, mInfo.a_reflectance)) + dB);
				} else {
					this.res.setAReflectance(MarkerResource.DASH);
				}
			}
			this.res.setAAttenuation(MathRef.round_4(mInfo.a_attfactor) + dbkm);
			this.res.setACumulativeLoss(MathRef.round_4(mInfo.a_cumulative_loss) + dB);
		}
		this.res.setBPosition(Math.round(mInfo.b_pos_m) + mt);
		this.res.setAbDistance(Math.round(mInfo.a_b_distance_m) + mt);
		this.res.setAbLoss(MathRef.round_4(mInfo.a_b_loss) + dB);
		this.res.setAbAttenuation(MathRef.round_4(mInfo.a_b_attenuation) + dbkm);
		this.res.setLsaAttenuation(MathRef.round_4(mInfo.lsa_attenuation) + dbkm);
		if (mInfo.a_b_orl > 0) {
			this.res.setAbOrl(MathRef.round_4(mInfo.a_b_orl) + dB);
		} else {
			this.res.setAbOrl(MarkerResource.DASH);
		}
		this.jTable.getModel().fireTableDataChanged();
	}

	public void bsHashAdded(final String key) {
		if (key.equals(Heap.PRIMARY_TRACE_KEY)) {
			final PFTrace pf = Heap.getPFTracePrimary();
			this.sigma = MathRef.calcSigma(pf.getWavelength(), pf.getPulsewidth());
			this.setVisible(true);
		}
	}

	public void bsHashRemoved(final String key) {
	}

	public void bsHashRemovedAll() {
		this.setVisible(false);
	}
}

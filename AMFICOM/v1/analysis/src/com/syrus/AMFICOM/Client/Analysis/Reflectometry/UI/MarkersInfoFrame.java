package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.beans.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.*;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.io.BellcoreStructure;

public class MarkersInfoFrame extends JInternalFrame
implements PropertyChangeListener, BsHashChangeListener
{
	//Don't change sequence
	private static String[] DEFAULT_KEYS = new String[] {
			MarkerResourceWrapper.KEY_A_POSITION, // 0
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
	
	private WrapperedPropertyTableModel tModel;
	private WrapperedPropertyTable jTable;

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
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher)
	{
		//dispatcher.register(this, RefChangeEvent.typ);
		dispatcher.addPropertyChangeListener(RefUpdateEvent.typ, this);
		Heap.addBsHashListener(this);
	}

	public void propertyChange(PropertyChangeEvent ae)
	{
		if(ae.getPropertyName().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;

			if (rue.markerMoved())
			{
				updTableModel ((MarkersInfo)rue.getNewValue());
			}
		}
	}

	private void jbInit() throws Exception
	{
		//setFocusCycleRoot(false);
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		res = new MarkerResource();
		tModel = new WrapperedPropertyTableModel(MarkerResourceWrapper.getInstance(),
				res, DEFAULT_KEYS);

		jTable = new WrapperedPropertyTable(tModel);
		jTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		jTable.getColumnModel().getColumn(1).setPreferredWidth(80);

		this.getContentPane().add(mainPanel);

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
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
	}

	void updTableModel(MarkersInfo mInfo)
	{
		String mt = " " + LangModelAnalyse.getString("mt");
		String dB = " " + LangModelAnalyse.getString("dB");
		String dbkm = " " + LangModelAnalyse.getString("dB/km");
		
		// setting keys
		if (mInfo.a_type == MarkersInfo.REFLECTIVE)
			DEFAULT_KEYS[1] = MarkerResourceWrapper.KEY_A_REFLECTION;
		else if (mInfo.a_type == MarkersInfo.NONREFLECTIVE)
			DEFAULT_KEYS[1] = MarkerResourceWrapper.KEY_A_LOSS;

		if (mInfo.a_pos < mInfo.b_pos)
			DEFAULT_KEYS[5] = MarkerResourceWrapper.KEY_BA_DISTANCE;
		else
			DEFAULT_KEYS[5] = MarkerResourceWrapper.KEY_AB_DISTANCE;
		
		// setting values
		res.setAPosition(Math.round(mInfo.a_pos_m) + mt);
		if (mInfo.a_type == MarkersInfo.NOANALYSIS) {
			res.setALoss(MarkerResource.DASH);
			res.setAReflectance(MarkerResource.DASH);
			res.setAAttenuation(MarkerResource.DASH);
			res.setACumulativeLoss(MarkerResource.DASH);
		} else
		{
			if (mInfo.a_type == MarkersInfo.NONREFLECTIVE) {// потери в А
				res.setALoss(MathRef.round_4(mInfo.a_loss) + dB);
			} else if (mInfo.a_type == MarkersInfo.REFLECTIVE) {// отражение в А
				if (mInfo.a_reflectance > 0)
					res.setAReflectance(MathRef.round_4(MathRef.calcReflectance(sigma, mInfo.a_reflectance)) + dB);
				else
					res.setAReflectance(MarkerResource.DASH);
			}
			res.setAAttenuation(MathRef.round_4(mInfo.a_attfactor) + dbkm);
			res.setACumulativeLoss(MathRef.round_4(mInfo.a_cumulative_loss) + dB);
		}
		res.setBPosition(Math.round(mInfo.b_pos_m) + mt);
		res.setAbDistance(Math.round(mInfo.a_b_distance_m) + mt);
		res.setAbLoss(MathRef.round_4(mInfo.a_b_loss) + dB);
		res.setAbAttenuation(MathRef.round_4(mInfo.a_b_attenuation) + dbkm);
		res.setLsaAttenuation(MathRef.round_4(mInfo.lsa_attenuation) + dbkm);
		if (mInfo.a_b_orl > 0)
			res.setAbOrl(MathRef.round_4(mInfo.a_b_orl) + dB);
		else
			res.setAbOrl(MarkerResource.DASH);
		jTable.updateUI();
	}

	public void bsHashAdded(String key, BellcoreStructure bs)
	{
		if (key.equals(Heap.PRIMARY_TRACE_KEY))
		{
			bs = Heap.getBSPrimaryTrace();
			sigma = MathRef.calcSigma(bs.getWavelength(), bs.getPulsewidth());
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
}

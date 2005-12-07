package com.syrus.AMFICOM.Client.Model;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Trace;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ReportTable;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelModel;
import com.syrus.AMFICOM.Client.Model.ModelMath.ModelGenerator;
import com.syrus.AMFICOM.Client.Model.ModelMath.ModelingEvent;
import com.syrus.AMFICOM.Client.Model.ModelMath.NewModelGenerator;
import com.syrus.AMFICOM.Client.Model.ModelMath.PhysModelGenerator;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.client.UI.ATable;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.modelling.ModelEvent;
import com.syrus.AMFICOM.modelling.TraceGenerator;
import com.syrus.AMFICOM.modelling.TraceGenerator.Parameters;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.io.BellcoreModelWriter;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

public class RefModelParamsFrame extends JInternalFrame 
		implements PropertyChangeListener, ReportTable {
	
	private static final long serialVersionUID = 5115725103762876658L;

	public static final String NAME = "RefModelParamsFrame";
	
	ApplicationContext aContext;

	SchemePath path;
	
	JTextField pathTextField = new JTextField();
	ATable table;
	NoiseParamTableModel noiseTableModel;
	PhysParamsTableModel physTableModel;
	boolean noiseModeling = true;

	public RefModelParamsFrame(ApplicationContext aContext) {
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		setContext(aContext);
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		aContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);
	}

	private void jbInit() throws Exception {
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setTitle(LangModelModel.getString("ParamsTitle"));
		setClosable(true);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setIconifiable(true);
		setResizable(true);
		getContentPane().setLayout(new BorderLayout());

		this.table = new ATable();
		
		JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.getViewport().add(this.table);

		JButton doItButton = new JButton();
		doItButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doIt();
			}
		});
		doItButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/perform_analysis.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		doItButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		doItButton.setToolTipText(LangModelModel.getString("menuViewPerformModeling"));
		
		final JComboBox modelTypeChooseCB = new JComboBox(new String[] {
				"Шумовая модель", "Физическая модель"
		});
		modelTypeChooseCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				setModelingType(modelTypeChooseCB.getSelectedIndex() == 0);
			}
		});
		
		JPanel north = new JPanel(new BorderLayout());
		north.add(modelTypeChooseCB, BorderLayout.NORTH);
		north.add(this.pathTextField, BorderLayout.CENTER);
		north.add(doItButton, BorderLayout.EAST);
		JPanel basePanel = new JPanel(new BorderLayout());
		basePanel.add(north, BorderLayout.NORTH);
		basePanel.add(jScrollPane1, BorderLayout.CENTER);
		getContentPane().add(basePanel, BorderLayout.CENTER);

		jScrollPane1.getViewport().setBackground(SystemColor.window);
		setModelingType(this.noiseModeling);
	}
	
	void setModelingType(boolean isNoiseModeling) {
		this.noiseModeling = isNoiseModeling;
		
		if (isNoiseModeling) {
			if (this.noiseTableModel == null) {
				this.noiseTableModel = new NoiseParamTableModel();
				this.noiseTableModel.updateData(new Object[] {
						new Integer(64),
						new Double(4),
						new Integer(1000),
						new Integer(1625),
						new Double(40),
						new Integer(0),
						new Integer(0),
						new Double(0.1),
						new Double( -40),
						new Double(0.5),
						new Double(0.2)});
				
				this.table.setDefaultRenderer(Object.class, new NoiseModelParamsTableRenderer(this.noiseTableModel));
				this.table.setDefaultEditor(Object.class, new NoiseModelParamsTableEditor(this.noiseTableModel));
			}
			this.table.setModel(this.noiseTableModel);
		} else {
			if (this.physTableModel == null) {
				this.physTableModel = new PhysParamsTableModel();
				this.physTableModel.updateData(new Object[] {
						new Integer(64),
						new Double(4),
						new Integer(1000),
						new Integer(1625),
						new Double(40),
						new Double(0.1),
						new Double(0.95),
						new Double(0.1),
						new Double(-40),
						new Double(0.5),
						new Double(0.2)});
				
				this.table.setDefaultRenderer(Object.class, new PhysModelParamsTableRenderer(this.physTableModel));
				this.table.setDefaultEditor(Object.class, new PhysModelParamsTableEditor(this.physTableModel));
			}
			this.table.setModel(this.physTableModel);
		}
		this.table.updateUI();
		this.table.getColumnModel().getColumn(0).setPreferredWidth(150);
		this.table.getColumnModel().getColumn(1).setPreferredWidth(50);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent se = (ObjectSelectedEvent)evt;
			if (se.isSelected(ObjectSelectedEvent.SCHEME_PATH)) {
				setModelingPath((SchemePath)se.getSelectedObject());
			}
		}
	}

	public String getReportTitle() {
		return LangModelModel.getString("ParamsTitle");
	}

	public TableModel getTableModel() {
		if (this.noiseModeling)
			return this.noiseTableModel;
		return this.physTableModel;
	}

	void doIt() {
		if (this.path == null) {
			String error = "Не задан маршрут моделирования.\n";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка",
																		JOptionPane.OK_OPTION);
			return;
		}

		BellcoreStructure bs;
		if (this.noiseModeling) {
			bs = getNoiseTrace();
		} else {
			bs = getPhysTrace();
		}
		
		if (bs == null)
			return;
		bs.title = "Модель \"" + this.path.getName() + "\"";
		bs.schemePathId = this.path.getId().getIdentifierString();
		
		Trace tr = new Trace(bs, Heap.MODELED_TRACE_KEY, Heap.getMinuitAnalysisParams());
		Heap.openPrimaryTrace(tr);
		Heap.setRefAnalysisPrimary(new RefAnalysis(tr.getPFTrace()));
		Heap.primaryTraceOpened();
	}

	private void setModelingPath(SchemePath path) {
		this.path = path;
		this.pathTextField.setText(path.getName());
	}

	private BellcoreStructure getNoiseTrace() {
		double length = ((Integer)this.noiseTableModel.getValueAt(0, 1)).doubleValue() * 1000;
		double resolution = ((Double)this.noiseTableModel.getValueAt(1, 1)).doubleValue();
		int pulsWidth = ((Integer)this.noiseTableModel.getValueAt(2, 1)).intValue();
		int wave_length = ((Integer)this.noiseTableModel.getValueAt(3, 1)).intValue();
		double dinam_area = ((Double)this.noiseTableModel.getValueAt(4, 1)).doubleValue();
		int rcFilter = ((Integer)this.noiseTableModel.getValueAt(5,1)).intValue();
		int bcFilter = ((Integer)this.noiseTableModel.getValueAt(6,1)).intValue();
		double weldAtt = ((Double)this.noiseTableModel.getValueAt(7, 1)).doubleValue();
		double connectorRef = ((Double)this.noiseTableModel.getValueAt(8, 1)).doubleValue();
		double connectorAtt = ((Double)this.noiseTableModel.getValueAt(9, 1)).doubleValue();
		double linearAtt = ((Double)this.noiseTableModel.getValueAt(10, 1)).doubleValue();
		
		List reflectoElements;
		try {
			reflectoElements = new ModelGenerator(this.path, wave_length, connectorAtt, weldAtt, connectorRef, linearAtt).createModelingEvents();
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return null;
		}
		//mode((int)wave_length, (int)pulsWidth, formFactor, resolution, connectorAtt, weldAtt, connectorRef, linearAtt);
		if(reflectoElements == null) {
			String error = "Неправильно заданы либо отсутствуют данные в схеме.\n";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return null;
		}
		ModelEvent[] rmip = (ModelEvent[])reflectoElements.toArray(new ModelEvent[reflectoElements.size()]);
		if(rmip.length < 2) {
			String error = "Ошибка при определении параметров маршрута.";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return null;
		}
		
		Parameters pars = new Parameters(-5.0, -dinam_area + 10.0, -dinam_area,
				resolution, length, wave_length, pulsWidth, 1.468);
		
		TraceGenerator generator = new TraceGenerator(pars, rmip);

		// При желании, проводим фильтрацию
		if (rcFilter != 0) {
			generator.performRCFiltering(rcFilter);
		}
		if (bcFilter != 0) {
			generator.performBoxCarFiltering(bcFilter);
		}
		return generator.getBellcore();
	}
	
	private BellcoreStructure getPhysTrace() {
		double length = ((Integer)this.physTableModel.getValueAt(0, 1)).doubleValue() * 1000;
		double resolution = ((Double)this.physTableModel.getValueAt(1, 1)).doubleValue();
		int pulsWidth = ((Integer)this.physTableModel.getValueAt(2, 1)).intValue();
		int wave_length = ((Integer)this.physTableModel.getValueAt(3, 1)).intValue();
		double dinam_area = ((Double)this.physTableModel.getValueAt(4, 1)).doubleValue();
		double addNoise = ((Double)this.physTableModel.getValueAt(5,1)).doubleValue();
		double formFactor = ((Double)this.physTableModel.getValueAt(6,1)).doubleValue();
		double weldAtt = ((Double)this.physTableModel.getValueAt(7, 1)).doubleValue();
		double connectorRef = ((Double)this.physTableModel.getValueAt(8, 1)).doubleValue();
		double connectorAtt = ((Double)this.physTableModel.getValueAt(9, 1)).doubleValue();
		double linearAtt = ((Double)this.physTableModel.getValueAt(10, 1)).doubleValue();
		
		List reflectoElements;
		try {
			reflectoElements = new PhysModelGenerator(this.path, wave_length, connectorAtt, weldAtt, connectorRef, linearAtt).createModelingEvents();
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return null;
		}
		//mode((int)wave_length, (int)pulsWidth, formFactor, resolution, connectorAtt, weldAtt, connectorRef, linearAtt);
		if(reflectoElements == null) {
			String error = "Неправильно заданы либо отсутствуют данные в схеме.\n";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return null;
		}
		ModelingEvent[] rmip = (ModelingEvent[])reflectoElements.toArray(new ModelingEvent[reflectoElements.size()]);
		if(rmip.length < 2) {
			String error = "Ошибка при определении параметров маршрута.";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), error, "Ошибка", JOptionPane.OK_OPTION);
			return null;
		}

		NewModelGenerator mg = new NewModelGenerator(rmip,
				resolution,
				dinam_area,
				pulsWidth,
				length,
				addNoise,
				formFactor);
		
		double []y = mg.getModelArray();
		return createBS (y, resolution/1000., wave_length, pulsWidth);
	}
	
	private BellcoreStructure createBS (double[] y, double delta_x, double wl, int pw) {
		BellcoreStructure bs = new BellcoreStructure();
		double groupindex = 1.46800;

		BellcoreModelWriter writer = new BellcoreModelWriter(bs);
		writer.setWavelength((int)wl);
		writer.setPulseWidth(pw);
		writer.setTime(System.currentTimeMillis() / 1000);
		writer.setOpticalModule("AMFICOM generated");
		writer.setPathId(this.path.getId().getIdentifierString());
		writer.setUnits("mt");
		writer.setAverages(1);
		writer.setRangeParameters(groupindex, delta_x, delta_x * y.length);
		writer.setData(y);
		writer.finalizeChanges();
		return bs;
	}
}

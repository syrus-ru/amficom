package com.syrus.AMFICOM.Client.Schedule.UI;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.AccessPort;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.io.*;
import com.syrus.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.event.*;

public class MyParamFrame extends JInternalFrame implements OperationListener {

	public static final String		PARAMETER_PREFIX			= "ref_";
	public static final String		PARAMETER_AVERAGE_QUANTITY	= "ref_scans";					//$NON-NLS-1$
	public static final String		MAX_DISTANCE				= "trclen";					//$NON-NLS-1$
	public static final String		PARAMETER_MAX_DISTANCE		= PARAMETER_PREFIX
																		+ MAX_DISTANCE;		//$NON-NLS-1$
	public static final String		PULSE_WIDTH					= "pulswd";					//$NON-NLS-1$
	public static final String		PARAMETER_PULSE_WIDTH		= PARAMETER_PREFIX
																		+ PULSE_WIDTH;			//$NON-NLS-1$
	public static final String		PARAMETER_REFRACTION		= "ref_ior";					//$NON-NLS-1$
	public static final String		RESOLUTION					= "res";						//$NON-NLS-1$
	public static final String		PARAMETER_RESOLUTION		= PARAMETER_PREFIX
																		+ RESOLUTION;			//$NON-NLS-1$
	public static final String		PARAMETER_WAVELENGHT		= "ref_wvlen";					//$NON-NLS-1$

	public static final String		CHARACTER_WAVE_LENGTH		= "Work_Wave_Length";
	public static final String		CHARACTER_MAX_REFRACTION	= "Max_Coef_Preloml";
	public static final String		CHARACTER_MIN_REFRACTION	= "Min_Coef_Preloml";

	Dispatcher						internal_dispatcher;
	ScheduleMDIMain					parent;
	String							meid						= "";
	String							testtypeid					= "";
	Vector							res							= new Vector();
	Vector							pulswd						= new Vector();
	Vector							Par_Objects;												;
	//	String[] izmer = { "4000",
	//			"8000", "16000", "32000", "64000", "128000", "256000"};
	//	String[] volna = { "1310",
	//			"1550", "1625" };
	//	String[] rres = { "0.25",
	//			"0.5", "1", "2", "4", "8", "16" };
	//	String[] ppulswd = { "100",
	//			"200", "500" };
	//	String[] reflect = { "4.096",
	//			"8.192", "16.384", "32.768", "65.536", "131.072", "262.144"};
	ApplicationContext				aContext;

	MouseListener					ml;
	HashMap							pulseWidthMap;
	HashMap							resolutionMap;
	double							minIndexOfRefraction		= 1.467;
	double							maxIndexOfRefraction		= 1.467;

	public static IniFile			iniFile;
	static String					iniFileName					= "My.properties";

	boolean							initial_init				= true;
	private BorderLayout			borderLayout1				= new BorderLayout();
	private JScrollPane				jScrollPane1				= new JScrollPane();
	private JPanel					jPanel1						= new JPanel();
	private JPanel					jPanel3						= new JPanel();
	private JPanel					jPanel2						= new JPanel();
	private BorderLayout			borderLayout2				= new BorderLayout();
	private ObjectResourceListBox	orList						= new ObjectResourceListBox();
	private JLabel					jLabel_1					= new JLabel();
	private BorderLayout			borderLayout3				= new BorderLayout();
	private BorderLayout			borderLayout4				= new BorderLayout();
	private BorderLayout			borderLayout5				= new BorderLayout();
	private JPanel					jPanel7						= new JPanel();
	private JLabel					testParameterLabe			= new JLabel();
	private JPanel					jPanel4						= new JPanel();
	private JPanel					jPanelNorth1				= new JPanel();
	private BorderLayout			borderLayout6				= new BorderLayout();
	private GridLayout				gridLayout1					= new GridLayout();
	private JPanel					jPanel5						= new JPanel();
	private JPanel					jPanelNorth2				= new JPanel();
	private BorderLayout			borderLayout7				= new BorderLayout();
	private GridLayout				gridLayout2					= new GridLayout();
	private JPanel					jPanelNorth3				= new JPanel();
	private JPanel					jPanel8						= new JPanel();
	private BorderLayout			borderLayout8				= new BorderLayout();
	private GridLayout				gridLayout3					= new GridLayout();
	private JPanel					jPanelNorth4				= new JPanel();
	private JPanel					jPanel9						= new JPanel();
	private GridLayout				gridLayout4					= new GridLayout();
	private BorderLayout			borderLayout9				= new BorderLayout();
	private JPanel					jPanelNorth5				= new JPanel();
	private JPanel					jPanel10					= new JPanel();
	private GridLayout				gridLayout5					= new GridLayout();
	private BorderLayout			borderLayout10				= new BorderLayout();
	private JPanel					jPanelNorth6				= new JPanel();
	private GridLayout				gridLayout6					= new GridLayout();
	private BorderLayout			borderLayout11				= new BorderLayout();
	private JLabel					jLabel1						= new JLabel();
	private JTextField				refractTextField			= new JTextField();
	private JLabel					jLabel2						= new JLabel();

	private JLabel					jLabel3						= new JLabel();
	private JLabel					jLabel4						= new JLabel();
	private JLabel					jLabel5						= new JLabel();
	private JLabel					jLabel6						= new JLabel();
	private AComboBox				waveLengthComboBox			= new AComboBox();
	private AComboBox				averageQuantityComboBox		= new AComboBox();
	private AComboBox				maxDistanceComboBox			= new AComboBox();
	private AComboBox				resolutionComboBox			= new AComboBox();
	private AComboBox				pulseWidthComboBox			= new AComboBox();
	ListNumberComparator			comparator					= new ListNumberComparator();

	public MyParamFrame(ScheduleMDIMain parent, ApplicationContext aContext) {
		this.aContext = aContext;
		this.parent = parent;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setContext(aContext);
	}

	private void jbInit() throws Exception {
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/general.gif")));
		internal_dispatcher = parent.getInternalDispatcher();
		internal_dispatcher.register(this, "KISType");
		internal_dispatcher.register(this, "PortType");
		internal_dispatcher.register(this, "StopAnalysis");
		internal_dispatcher.register(this, "RemoveParamFrame");
		internal_dispatcher.register(this, "Remove3aFrame");
		internal_dispatcher.register(this, "TestType");
		internal_dispatcher.register(this, "METype");
		internal_dispatcher.register(this, "VisualTestParams");
		internal_dispatcher.register(this, "ExtendedAfterUsual_RootFrame");
		this.setClosable(true);
		this.setIconifiable(true);
		this.setResizable(true);
		this.setTitle(LangModelScheduleOld.String("MyParamTitle"));
		this.getContentPane().setLayout(borderLayout11);

		ml = new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				this_mousePressed(e);
			}
		};

		this
				.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {

					public void internalFrameActivated(InternalFrameEvent e) {
						this_internalFrameActivated(e);
					}

					public void internalFrameOpened(InternalFrameEvent e) {
						this_internalFrameOpened(e);
					}
				});
		this.addComponentListener(new java.awt.event.ComponentAdapter() {

			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});
		jPanel1.setLayout(borderLayout2);
		jScrollPane1.setAutoscrolls(true);
		jScrollPane1.setBorder(BorderFactory.createEtchedBorder());
		jPanel3.setLayout(borderLayout3);
		jPanel2.setLayout(borderLayout4);
		jLabel_1.setText(LangModelScheduleOld.String("labelTipycalTests"));
		jPanel7.setBorder(BorderFactory.createEtchedBorder());
		jPanel7.setLayout(borderLayout5);
		testParameterLabe.setText(LangModelScheduleOld
				.String("labelParamTestValue"));
		orList.setBorder(BorderFactory.createEtchedBorder());
		jPanel4.setLayout(borderLayout6);
		jPanelNorth1.setLayout(gridLayout1);
		jPanel5.setLayout(borderLayout7);
		jPanelNorth2.setLayout(gridLayout2);
		jPanel8.setLayout(borderLayout8);
		jPanelNorth3.setLayout(gridLayout3);
		jPanelNorth4.setLayout(gridLayout4);
		jPanel9.setLayout(borderLayout9);
		jPanelNorth5.setLayout(gridLayout5);
		jPanel10.setLayout(borderLayout10);
		jPanelNorth6.setLayout(gridLayout6);
		this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(jPanel1, null);
		jPanel1.add(jPanel3, BorderLayout.CENTER);
		jPanel3.add(orList, BorderLayout.CENTER);
		//jPanel3.add(new JLabel("A"), BorderLayout.SOUTH);
		jPanel3.add(jLabel_1, BorderLayout.NORTH);
		jPanel1.add(jPanel2, BorderLayout.SOUTH);
		jPanel2.add(jPanel7, BorderLayout.SOUTH);
		jPanel7.add(jPanel4, BorderLayout.CENTER);
		jPanel7.add(jPanelNorth1, BorderLayout.NORTH);
		jPanelNorth1.add(jLabel1, null);
		jPanelNorth1.add(refractTextField, null);
		jPanel2.add(testParameterLabe, BorderLayout.NORTH);
		jPanel4.add(jPanel5, BorderLayout.CENTER);
		jPanel4.add(jPanelNorth2, BorderLayout.NORTH);
		jPanelNorth2.add(jLabel2, null);
		jPanelNorth2.add(waveLengthComboBox, null);
		jPanel5.add(jPanelNorth3, BorderLayout.NORTH);
		jPanelNorth3.add(jLabel3, null);
		jPanelNorth3.add(averageQuantityComboBox, null);
		jPanel5.add(jPanel8, BorderLayout.CENTER);
		jPanel8.add(jPanelNorth4, BorderLayout.NORTH);
		jPanelNorth4.add(jLabel4, null);
		jPanelNorth4.add(pulseWidthComboBox, null);
		jPanel8.add(jPanel9, BorderLayout.CENTER);
		jPanel9.add(jPanelNorth5, BorderLayout.NORTH);
		jPanelNorth5.add(jLabel5, null);
		jPanelNorth5.add(resolutionComboBox, null);
		jPanel9.add(jPanel10, BorderLayout.CENTER);
		jPanel10.add(jPanelNorth6, BorderLayout.NORTH);
		jPanelNorth6.add(jLabel6, null);
		jPanelNorth6.add(maxDistanceComboBox, null);
		orList.addMouseListener(ml);
		refractTextField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JTextField textField = (JTextField) e.getSource();
				String value = textField.getText();
				double refract = 0.0;
				boolean isDouble = false;
				try {
					refract = Double.parseDouble(value);
					isDouble = true;
				} catch (NumberFormatException nfe) {
					isDouble = false;
				}

				if (!isDouble) {
					textField.setText(Double.toString(minIndexOfRefraction));
				} else {
					if (refract < minIndexOfRefraction) {
						textField
								.setText(Double.toString(minIndexOfRefraction));
					} else {
						if (refract > maxIndexOfRefraction) {
							textField.setText(Double
									.toString(maxIndexOfRefraction));
						}
					}
				}
				jTextField1_actionPerformed(e);

			}
		});
		waveLengthComboBox.addItemListener(new java.awt.event.ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
						jComboBox1_itemStateChanged(e);
			}
		});
		averageQuantityComboBox
				.addItemListener(new java.awt.event.ItemListener() {

					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED)
								jComboBox2_itemStateChanged(e);
					}
				});
		maxDistanceComboBox.addItemListener(new java.awt.event.ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
						jComboBox3_itemStateChanged(e);
			}
		});
		resolutionComboBox.addItemListener(new java.awt.event.ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
						jComboBox4_itemStateChanged(e);
			}
		});
		pulseWidthComboBox.addItemListener(new java.awt.event.ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
						jComboBox5_itemStateChanged(e);
			}
		});
		refractTextField.setVisible(false);
		waveLengthComboBox.setVisible(false);
		averageQuantityComboBox.setVisible(false);
		maxDistanceComboBox.setVisible(false);
		resolutionComboBox.setVisible(false);
		pulseWidthComboBox.setVisible(false);

		Par_Objects = new Vector();
		Par_Objects.add(refractTextField.getText());
		Par_Objects.add(waveLengthComboBox.getSelectedItem());
		Par_Objects.add(averageQuantityComboBox.getSelectedItem());
		Par_Objects.add(maxDistanceComboBox.getSelectedItem());
		Par_Objects.add(resolutionComboBox.getSelectedItem());
		Par_Objects.add(pulseWidthComboBox.getSelectedItem());
	}

	public void init_module() {
		initial_init = false;
		ApplicationModel aModel = aContext.getApplicationModel();
		// load values from properties file
		try {
			iniFile = new IniFile(iniFileName);
			setFromIniFile();
		} catch (java.io.IOException e) {
			setDefaults();
		}

		aModel.setCommand("myEntry", new VoidCommand());

		aModel.setEnabled("myEntry", true);
		aModel.fireModelChanged("");
	}

	public void setFromIniFile() {
	}

	public void setDefaults() {
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		if (aContext == null) return;
		if (aContext.getApplicationModel() == null)
				aContext.setApplicationModel(new ApplicationModel());
		setModel(aContext.getApplicationModel());

		if (aContext.getDispatcher() != null)
				aContext.getDispatcher().register(this, "myevent");
	}

	public ApplicationContext getContext() {
		return aContext;
	}

	public void setModel(ApplicationModel aModel) {
		//		aModel.addListener(toolBar);
		//		toolBar.setModel(aModel);

		aModel.fireModelChanged("");
	}

	public void setPort(AccessPort port) {
		Hashtable table = port.characteristics;
		if (this.resolutionMap == null)
			this.resolutionMap = new HashMap();
		else
			this.resolutionMap.clear();
		if (this.pulseWidthMap == null)
			this.pulseWidthMap = new HashMap();
		else
			this.pulseWidthMap.clear();
		Pattern pattern = Pattern.compile(MAX_DISTANCE + "_(\\d+)_("
				+ PULSE_WIDTH + "|" + RESOLUTION + ")");
		for (Iterator it = table.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			Characteristic character = (Characteristic) table.get(key);
			//			System.out.println(key + "\t" + character.id + "\t"
			//					+ character.value);
			if (key.equals(PARAMETER_AVERAGE_QUANTITY)) {
				String[] values = character.value.split("\\s+");
				Arrays.sort(values, comparator);
				averageQuantityComboBox.removeAllItems();
				for (int i = 0; i < values.length; i++)
					averageQuantityComboBox.addItem(values[i]);

			} else if (key.equals(CHARACTER_WAVE_LENGTH)) {
				String[] values = character.value.split("\\s+");
				Arrays.sort(values, comparator);
				waveLengthComboBox.removeAllItems();
				for (int i = 0; i < values.length; i++)
					waveLengthComboBox.addItem(values[i]);

			} else if (key.equals(CHARACTER_MAX_REFRACTION)) {
				try {
					maxIndexOfRefraction = Double.parseDouble(character.value);
				} catch (NumberFormatException nfe) {
					// nothing to do
				}

			} else if (key.equals(CHARACTER_MIN_REFRACTION)) {
				try {
					minIndexOfRefraction = Double.parseDouble(character.value);
				} catch (NumberFormatException nfe) {
					//					 nothing to do
				}
			} else {
				Matcher matcher = pattern.matcher(key);
				if (matcher.find()) {
					String maxLength = null;
					String suffix = null;
					for (int j = 0; j <= matcher.groupCount(); j++) {
						//System.out.println("j:"+j+"\t"+);
						String substring = key.substring(matcher.start(j),
								matcher.end(j));
						switch (j) {
							case 1:
								maxLength = substring;
								break;
							case 2:
								suffix = substring;
								break;
						}
					}
					if ((maxLength != null) && (suffix != null)) {
						HashMap map = null;
						if (suffix.equals(RESOLUTION))
							map = this.resolutionMap;
						else if (suffix.equals(PULSE_WIDTH)) {
							map = this.pulseWidthMap;
						}

						if (map != null) {
							map.put(maxLength, character.value);
						}
					}
				}
			}
		}
		if (waveLengthComboBox.getItemCount() == 0)
				waveLengthComboBox.addItem("1625");
		if (averageQuantityComboBox.getItemCount() == 0)
				averageQuantityComboBox.addItem("4000");
		refractTextField.setText(Double.toString(minIndexOfRefraction));
		String defaultMaxDistance = "131072";
		if (this.resolutionMap.keySet().size() == this.pulseWidthMap.keySet()
				.size()) {

			Set set = this.resolutionMap.keySet();
			Object[] values = set.toArray();
			Arrays.sort(values, comparator);
			if (this.resolutionMap.keySet().size() == 0) {
				resolutionMap.put(defaultMaxDistance, "8");
				pulseWidthMap.put(defaultMaxDistance, "5000");
			}
			if (values.length == 0)
					values = new String[] { defaultMaxDistance};
			maxDistanceComboBox.removeAllItems();
			for (int i = 0; i < values.length; i++)
				maxDistanceComboBox.addItem(values[i].toString());
			maxDistanceComboBox.setSelectedIndex(0);
		}

	}

	public ApplicationModel getModel() {
		return aContext.getApplicationModel();
	}

	public Dispatcher getInternalDispatcher() {
		return internal_dispatcher;
	}

	public void operationPerformed(OperationEvent ae) {
		ApplicationModel aModel = aContext.getApplicationModel();
		//System.out.println(" > ae.getActionCommand():" +
		// ae.getActionCommand());
		if (ae.getActionCommand().equals("StopAnalysis")) {
			ListModel lm = orList.getModel();
			orList.setModel(lm);
		}
		if (ae.getActionCommand().equals("METype")) {
			orList.setContents("");
			meid = ae.getSource().toString();
		}
		if (ae.getActionCommand().equals("ExtendedAfterUsual_RootFrame")) {
			orList.setContents("");
		}
		if (ae.getActionCommand().equals("KISType")) {
			orList.setContents("");
			internal_dispatcher
					.notify(new OperationEvent("", 0, "StopAnalysis"));
		}
		//||
		if (ae.getActionCommand().equals("PortType")) {

			String portid = ae.getSource().toString();
			System.out.println("portID:" + portid);
			AccessPort port = (AccessPort) Pool.get(AccessPort.typ, portid);
			setPort(port);
			orList.setContents("");
			internal_dispatcher
					.notify(new OperationEvent("", 0, "StopAnalysis"));
		}
		if (ae.getActionCommand().equals("VisualTestParams")) {
			orList.setContents("");
			Test par_test = (Test) ae.getSource();
			DataSourceInterface dsi = aContext.getDataSourceInterface();
			meid = par_test.monitored_element_id;
			testtypeid = par_test.test_type_id;
			String[] ts_me = new SurveyDataSourceImage(dsi)
					.getTestSetupByME(meid);
			//			String[] ts_me = dsi.getTestSetupsByME(meid);
			String[] ts_tt = new SurveyDataSourceImage(dsi)
					.getTestSetupByTestType(testtypeid);
			//			String[] ts_tt = dsi.getTestSetupsByTestType(testtypeid);
			String[] ts = new String[ts_me.length + ts_tt.length];
			for (int i = 0; i < ts_me.length; i++) {
				ts[i] = ts_me[i];
				orList.add((TestSetup) Pool.get(TestSetup.typ, ts[i]));
			}
			for (int i = 0; i < ts_tt.length; i++) {
				ts[i + ts_me.length] = ts_tt[i];
				orList.add((TestSetup) Pool.get(TestSetup.typ, ts[i
						+ ts_me.length]));
			}

			dsi
					.LoadTestArgumentSets(new String[] { par_test.test_argument_set_id});
			TestArgumentSet as = (TestArgumentSet) Pool.get(
					TestArgumentSet.typ, par_test.test_argument_set_id);
			Vector arg = as.arguments;
			if (arg.size() == 1) {
				try {
					Parameter param1 = (Parameter) arg.elementAt(0);
					String par1 = (new ByteArray(param1.value)).toUTFString();
					waveLengthComboBox.setSelectedItem(String.valueOf(par1));
				} catch (java.io.IOException ex) {
				}
			} else if (arg.size() == 6) {
				try {
					for (int i = 0; i < arg.size(); i++) {
						Parameter par = (Parameter) arg.elementAt(i);
						if (par.codename.equals("ref_ior")) {
							refractTextField.setText(String
									.valueOf((new ByteArray(par.value))
											.toDouble()));
						} else if (par.codename.equals("ref_wvlen")) {
							waveLengthComboBox
									.setSelectedItem(String
											.valueOf((new ByteArray(par.value))
													.toInt()));
						} else if (par.codename.equals("ref_scans")) {
							averageQuantityComboBox.setSelectedItem(String
									.valueOf((new ByteArray(par.value))
											.toDouble()));
						} else if (par.codename.equals("ref_trclen")) {
							maxDistanceComboBox.setSelectedItem(String
									.valueOf((new ByteArray(par.value))
											.toDouble()));
						} else if (par.codename.equals("ref_res")) {
							maxDistanceComboBox.setSelectedItem(String
									.valueOf((new ByteArray(par.value))
											.toDouble()));
						} else if (par.codename.equals("ref_pulswd")) {
							pulseWidthComboBox.setSelectedItem(String
									.valueOf((new ByteArray(par.value))
											.toLong()));
						}
					}
				} catch (java.io.IOException ex) {
				}
			}
			TestSetup testsetup = (TestSetup) Pool.get(TestSetup.typ,
					par_test.test_setup_id);
			if (testsetup != null) {
				orList.setSelected(testsetup);
				internal_dispatcher.notify(new OperationEvent(testsetup, 0,
						"TestSetup"));
			}
			internal_dispatcher.notify(new OperationEvent(par_test, 0,
					"VisualTestSetup"));
		}
		if (ae.getActionCommand().equals("RemoveParamFrame")) {
			this.dispose();
		}
		if (ae.getActionCommand().equals("Remove3aFrame")) {
			this.dispose();
		}
		if (ae.getActionCommand().equals("TestType")) {
			testtypeid = ae.getSource().toString();
			orList.setContents("");
			DataSourceInterface dsi = aContext.getDataSourceInterface();
			String[] ts_me = new SurveyDataSourceImage(dsi)
					.getTestSetupByME(meid);
			String[] ts_tt = new SurveyDataSourceImage(dsi)
					.getTestSetupByTestType(testtypeid);
			//			String[] ts_me = dsi.getTestSetupsByME(meid);
			//			String[] ts_tt = dsi.getTestSetupsByTestType(testtypeid);
			String[] ts = new String[ts_me.length + ts_tt.length];
			for (int i = 0; i < ts_me.length; i++) {
				ts[i] = ts_me[i];
				orList.add((TestSetup) Pool.get(TestSetup.typ, ts[i]));
			}
			for (int i = 0; i < ts_tt.length; i++) {
				ts[i + ts_me.length] = ts_tt[i];
				TestSetup tst = (TestSetup) Pool.get(TestSetup.typ, ts[i
						+ ts_me.length]);
				if (tst.monitored_element_ids.length == 0) orList.add(tst);
			}

			if (ae.getSource().toString().equals("trace_and_analyse")) {
				Par_Objects = new Vector();

				jLabel1.setText(LangModelScheduleOld.String("labelReflect"));
				jLabel2.setText(LangModelScheduleOld.String("labelWaveLength"));
				jLabel3.setText(LangModelScheduleOld.String("labelAverCount"));
				jLabel4.setText(LangModelScheduleOld.String("labelImpuls"));
				jLabel5.setText(LangModelScheduleOld.String("labelDetalM"));
				jLabel6.setText(LangModelScheduleOld.String("labelMaxDistance"));

				//refractTextField.setText("1.467");

				if (ts.length == 0) {
					internal_dispatcher.notify(new OperationEvent("", 0,
							"StopAnalysis"));
				}

				Par_Objects.add(refractTextField.getText());
				Par_Objects.add(waveLengthComboBox.getSelectedItem());
				Par_Objects.add(averageQuantityComboBox.getSelectedItem());
				Par_Objects.add(maxDistanceComboBox.getSelectedItem());
				Par_Objects.add(resolutionComboBox.getSelectedItem());
				Par_Objects.add(pulseWidthComboBox.getSelectedItem());

				jLabel1.setVisible(true);
				jLabel2.setVisible(true);
				jLabel3.setVisible(true);
				jLabel4.setVisible(true);
				jLabel5.setVisible(true);
				jLabel6.setVisible(true);

				refractTextField.setVisible(true);
				waveLengthComboBox.setVisible(true);
				averageQuantityComboBox.setVisible(true);
				maxDistanceComboBox.setVisible(true);
				resolutionComboBox.setVisible(true);
				pulseWidthComboBox.setVisible(true);
			} else if (ae.getSource().toString().equals("voice_analyse")) {
				Par_Objects = new Vector();
				if (ts.length == 0) {
					internal_dispatcher.notify(new OperationEvent("", 0,
							"StopAnalysis"));
				}
				jLabel1.setText(LangModelScheduleOld.String("labelIdIzmer"));
				refractTextField.setText("");
				Par_Objects.add(refractTextField.getText());

				jLabel1.setVisible(true);
				jLabel2.setVisible(false);
				jLabel3.setVisible(false);
				jLabel4.setVisible(false);
				jLabel5.setVisible(false);
				jLabel6.setVisible(false);

				refractTextField.setVisible(true);
				waveLengthComboBox.setVisible(false);
				averageQuantityComboBox.setVisible(false);
				maxDistanceComboBox.setVisible(false);
				resolutionComboBox.setVisible(false);
				pulseWidthComboBox.setVisible(false);
			}
		}
		aModel.fireModelChanged("");
	}

	void this_componentShown(ComponentEvent e) {
		if (initial_init) init_module();
	}

	void this_internalFrameActivated(InternalFrameEvent e) {
		this.grabFocus();
	}

	void this_internalFrameOpened(InternalFrameEvent e) {
		this.grabFocus();
	}

	void jComboBox2_itemStateChanged(ItemEvent e) {
		if (averageQuantityComboBox.getSelectedItem() != null) {
			Par_Objects.setElementAt(averageQuantityComboBox.getSelectedItem(),
					2);
			internal_dispatcher.notify(new OperationEvent(Par_Objects, 0,
					"Parameter"));
			internal_dispatcher
					.notify(new OperationEvent("", 0, "StopAnalysis"));
		}
	}

	void jComboBox1_itemStateChanged(ItemEvent e) {
		if (waveLengthComboBox.getSelectedItem() != null) {
			Par_Objects.setElementAt(waveLengthComboBox.getSelectedItem(), 1);
			internal_dispatcher.notify(new OperationEvent(Par_Objects, 0,
					"Parameter"));
			internal_dispatcher
					.notify(new OperationEvent("", 0, "StopAnalysis"));
		}
	}

	void jTextField1_actionPerformed(ActionEvent e) {
		Par_Objects.setElementAt(refractTextField.getText(), 0);
		internal_dispatcher.notify(new OperationEvent(Par_Objects, 0,
				"Parameter"));
		internal_dispatcher.notify(new OperationEvent("", 0, "StopAnalysis"));
	}

	void jComboBox5_itemStateChanged(ItemEvent e) {
		if (pulseWidthComboBox.getSelectedItem() != null) {
			Par_Objects.setElementAt(pulseWidthComboBox.getSelectedItem(), 5);
			internal_dispatcher.notify(new OperationEvent(Par_Objects, 0,
					"Parameter"));
			internal_dispatcher
					.notify(new OperationEvent("", 0, "StopAnalysis"));
		}
	}

	void jComboBox4_itemStateChanged(ItemEvent e) {
		if (resolutionComboBox.getSelectedItem() != null) {
			Par_Objects.setElementAt(resolutionComboBox.getSelectedItem(), 4);
			internal_dispatcher.notify(new OperationEvent(Par_Objects, 0,
					"Parameter"));
			internal_dispatcher
					.notify(new OperationEvent("", 0, "StopAnalysis"));
		}
	}

	void jComboBox3_itemStateChanged(ItemEvent e) {
		//System.out.println(Par_Objects.size());
		if (maxDistanceComboBox.getSelectedItem() != null) {
			Par_Objects.setElementAt(maxDistanceComboBox.getSelectedItem(), 3);
			//int i = maxDistanceComboBox.getSelectedIndex();
			//AComboBox comboBox = (AComboBox) e.getSource();
			String maxLength = (String) maxDistanceComboBox.getSelectedItem();
			for (int index = 0; index <= 1; index++) {
				HashMap map;
				AComboBox aComboBox;
				if (index == 0) {
					map = pulseWidthMap;
					aComboBox = pulseWidthComboBox;
				} else {
					map = resolutionMap;
					aComboBox = resolutionComboBox;
				}
				if (map != null) {
					String value = (String) map.get(maxLength);
					String[] values = value.split("\\s+");
					Arrays.sort(values, comparator);
					aComboBox.removeAllItems();
					for (int i = 0; i < values.length; i++)
						aComboBox.addItem(values[i]);
				}
			}
			internal_dispatcher.notify(new OperationEvent(Par_Objects, 0,
					"Parameter"));
			internal_dispatcher
					.notify(new OperationEvent("", 0, "StopAnalysis"));
		}
	}

	void this_mousePressed(MouseEvent e) {
		TestSetup testsetup = (TestSetup) orList.getSelectedObjectResource();
		if (testsetup != null) {
			TestArgumentSet as;
			DataSourceInterface dsi = aContext.getDataSourceInterface();
			dsi
					.LoadTestArgumentSets(new String[] { testsetup.test_argument_set_id});
			as = (TestArgumentSet) Pool.get(TestArgumentSet.typ,
					testsetup.test_argument_set_id);
			Vector arg = as.arguments;
			if (testtypeid.equals("trace_and_analyse")) {
				try {
					for (int i = 0; i < arg.size(); i++) {
						Parameter par = (Parameter) arg.elementAt(i);
						String s = "";
						String cbval = "";
						if (par.codename.equals("ref_ior")) {
							double d = (new ByteArray(par.value)).toDouble();
							s = String.valueOf(d);
							refractTextField.setText(String
									.valueOf((new ByteArray(par.value))
											.toDouble()));
							refractTextField.repaint();

							cbval += "	" + refractTextField.getText() + "\n";
							
						} else if (par.codename.equals("ref_wvlen")) {
							int d = (new ByteArray(par.value)).toInt();
							s = String.valueOf(d);
							waveLengthComboBox
									.setSelectedItem(String
											.valueOf((new ByteArray(par.value))
													.toInt()));
							waveLengthComboBox.repaint();

							int count = waveLengthComboBox.getModel().getSize();
							for (int j = 0; j < count; j++) 
							{
								cbval += "	" + waveLengthComboBox.getModel().getElementAt(j) + "\n";
							}
							
						} else if (par.codename.equals("ref_scans")) {
							double d = (new ByteArray(par.value)).toDouble();
							s = String.valueOf(d);
							averageQuantityComboBox.setSelectedItem(String
									.valueOf((new ByteArray(par.value))
											.toDouble()));
							averageQuantityComboBox.repaint();

							int count = averageQuantityComboBox.getModel().getSize();
							for (int j = 0; j < count; j++) 
							{
								cbval += "	" + averageQuantityComboBox.getModel().getElementAt(j) + "\n";
							}
							
						} else if (par.codename.equals("ref_trclen")) {
							double d = (new ByteArray(par.value)).toDouble();
							s = String.valueOf(d);
							maxDistanceComboBox.setSelectedItem(String
									.valueOf((new ByteArray(par.value))
											.toDouble()));
							maxDistanceComboBox.repaint();

							int count = maxDistanceComboBox.getModel().getSize();
							for (int j = 0; j < count; j++) 
							{
								cbval += "	" + maxDistanceComboBox.getModel().getElementAt(j) + "\n";
							}
							
						} else if (par.codename.equals("ref_res")) {
							double d = (new ByteArray(par.value)).toDouble();
							s = String.valueOf(d);
							resolutionComboBox.setSelectedItem(String
									.valueOf((new ByteArray(par.value))
											.toDouble()));
							resolutionComboBox.repaint();

							int count = resolutionComboBox.getModel().getSize();
							for (int j = 0; j < count; j++) 
							{
								cbval += "	" + resolutionComboBox.getModel().getElementAt(j) + "\n";
							}
							
						} else if (par.codename.equals("ref_pulswd")) {
							long d = (new ByteArray(par.value)).toLong();
							s = String.valueOf(d);
							pulseWidthComboBox.setSelectedItem(String
									.valueOf((new ByteArray(par.value))
											.toLong()));
							pulseWidthComboBox.repaint();

							int count = pulseWidthComboBox.getModel().getSize();
							for (int j = 0; j < count; j++) 
							{
								cbval += "	" + pulseWidthComboBox.getModel().getElementAt(j) + "\n";
							}
							
						}
						System.out.println("Set " + par.getName() + " = " + s);
						System.out.println("	Values are");
						System.out.println(cbval);
						
					}
				} catch (java.io.IOException ex) {
					ex.printStackTrace();
				}
			} else if (testtypeid.equals("voice_analyse")) {
				try {
					Parameter param1 = (Parameter) arg.elementAt(0);
					String par1 = (new ByteArray(param1.value)).toUTFString();
					waveLengthComboBox.setSelectedItem(String.valueOf(par1));
				} catch (java.io.IOException ex) {
				}
			}
			internal_dispatcher.notify(new OperationEvent(testsetup, 0,
					"TestSetup"));
		}
		orList.setSelected(testsetup);
	}

	private class ListNumberComparator implements java.util.Comparator {

		private int	direction	= 1;

		public ListNumberComparator() {
			direction = 1;
		}

		public ListNumberComparator(int direction) {
			this.direction = direction;
		}

		public int compare(Object o1, Object o2) {
			String s1 = o1.toString();
			String s2 = o2.toString();
			double d1 = 0;
			double d2 = 0;
			boolean isDoubleNumber = false;
			int result = 0;

			try {
				d1 = Double.parseDouble(s1);
				d2 = Double.parseDouble(s2);
				isDoubleNumber = true;
			} catch (NumberFormatException nfe) {
				isDoubleNumber = false;
			}

			if (isDoubleNumber) {
				if (d1 < d2)
					result = -direction;
				else if (d1 == d2)
					result = 0;
				else
					result = direction;
			} else {
				result = direction * s1.compareTo(s2);
			}

			return result;
		}
	}

}
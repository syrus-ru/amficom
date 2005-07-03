package com.syrus.AMFICOM.Client.Survey.Result;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.MapMarkersLayeredPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.MapMarkersPanel;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.SurveyEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.GeneralTableModel;
import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramAlarm;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.TraceEvent;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.ParameterTypeCodenames;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.ResultSortCondition;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.ByteArrayCollector;
import com.syrus.util.ByteArray;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;

public class ResultFrame extends JInternalFrame implements OperationListener
{
	ApplicationContext aContext;
	Dispatcher internal_dispatcher;
	Identifier userId;

	String user_action;

	String received_user_action;
	String received_id;

	String title;

	GeneralTableModel tModelPar;
	GeneralTableModel tModelRes;
	JViewport paramViewport = new JViewport();
	JViewport resultViewport = new JViewport();
	JViewport jViewport = new JViewport();
	BorderLayout borderLayout1 = new BorderLayout();
	JTabbedPane jTabbedPane1 = new JTabbedPane();
	JScrollPane paramPane = new JScrollPane();
	JScrollPane resultPane = new JScrollPane();
	JSplitPane splitPane;

	JTable paramTable;
	JTable resultTable;

	MapMarkersLayeredPanel tracePanel;
	MapMarkersPanel map_markers_panel;

	public ResultFrame(ApplicationContext aContext)
	{
		setContext(aContext);

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//Environment.addWindow(this);

		init_module();

		this.tracePanel = new MapMarkersLayeredPanel(this.internal_dispatcher);
		getActiveContext();

		aContext.getDispatcher().notify (
			new OperationEvent (this,0,SurveyEvent.RESULT_FRAME_DISPLAYED));
	}

	public ResultFrame()
	{
		this(new ApplicationContext());
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		userId = new Identifier(aContext.getSessionInterface().getUserId());
	}

	public ApplicationContext getContext()
	{
		return this.aContext;
	}

	public Dispatcher getInternalDispatcher()
	{
		return this.internal_dispatcher;
	}

	public void init_module()
	{
		this.internal_dispatcher = this.aContext.getDispatcher();

		this.internal_dispatcher.register(this, "activecontextevent");
		this.internal_dispatcher.register(this, TreeDataSelectionEvent.type);
	}

	public void showActiveResult(Identifier id)
	{
		try {
			this.title = null;

			if (this.user_action.equals("result_selected")) {
				Result result = (Result)MeasurementStorableObjectPool.getStorableObject(id, true);
				showResult(result);
			}
		}
		catch (Exception ex) {
			System.out.println("Could not show result - " + ex.getMessage());
			System.out.println("(user_action = " + user_action + ", id = " + id + ")");
			showResult(null);
		}
		if (title == null)
			title = "Результаты ";
		setTitle(title);
	}

	void showResult(Result result)
	{
		tModelPar.clearTable();
		tModelRes.clearTable();
		deleteReflectogramTab();

		if (result == null)
			return;

		Result addOnResult1 = null;
		Result addOnResult2 = null;

		Set argumentSet = null;

		Identifier meId = null;
		String schemePathId = null;

		try {
			if (result.getSort().equals(ResultSort.RESULT_SORT_MODELING)) {
				Modeling modeling = (Modeling)result.getAction();
				argumentSet = modeling.getArgumentSet();
				schemePathId = modeling.getSchemePathId();
			}
			else if (result.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
				Measurement m = (Measurement)result.getAction();
				MeasurementSetup ms = m.getSetup();
				argumentSet = ms.getParameterSet();
				meId = m.getMonitoredElementId();

				Vector vect = new Vector();
				vect.add("Шаблон исследования");
				vect.add(ms.getDescription());
				this.tModelPar.insertRow(vect);

				ResultSortCondition condition = new ResultSortCondition(m, ResultSort.RESULT_SORT_ANALYSIS);
				List resIds = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
				if (resIds.size() != 0)
					addOnResult1 = (Result)MeasurementStorableObjectPool.getStorableObject(
							(Identifier)resIds.get(0), true);

				condition = new ResultSortCondition(m, ResultSort.RESULT_SORT_EVALUATION);
				resIds = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
				if (resIds.size() != 0)
					addOnResult2 = (Result)MeasurementStorableObjectPool.getStorableObject(
							(Identifier)resIds.get(0), true);

				AnalysisUtil.load_Etalon(ms);
				AnalysisUtil.load_CriteriaSet(ms);
				AnalysisUtil.load_Thresholds(userId, ms);
			}
		}
		catch (Exception e) {
		}

		proccessParameters(result.getParameters(), argumentSet.getParameters(), meId , schemePathId);
		if(addOnResult1 != null)
			proccessParameters(addOnResult1.getParameters(), argumentSet.getParameters(), meId , schemePathId);

		if(addOnResult2 != null)
			proccessParameters(addOnResult2.getParameters(), argumentSet.getParameters(), meId , schemePathId);

		SetParameter[] parameters = argumentSet.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			SetParameter arg = (SetParameter)parameters[i];
			ParameterType ptype = (ParameterType)parameters[i].getType();
			String codename = ptype.getCodename();
			String name = ptype.getName();
			byte[] value = arg.getValue();

			if (codename.equals("reflectogramm")) {
				BellcoreStructure bs = new BellcoreReader().getData(value);
				jbInitReflectogram(bs, parameters, meId, schemePathId);
			}
			else {
				Object val = parseSimpleData(ptype.getSort(), value);

				Vector vect = new Vector();
				vect.add(name);
				vect.add(val);
				tModelRes.insertRow(vect);
			}
		}
		this.paramTable.updateUI();
		this.resultTable.updateUI();
	}

	private void proccessParameters(SetParameter[] parameters, SetParameter[] arguments, Identifier meId, String schemePathId) {
		for(int i = 0; i < parameters.length; i++) {
			SetParameter param = (SetParameter)parameters[i];
			ParameterType ptype = (ParameterType)param.getType();

			String codename = "";
			String name = "";
			byte[] value = param.getValue();
			codename = ptype.getCodename();
			name = ptype.getName();

			if (codename.equals(ParameterTypeCodenames.TRACE_EVENTS))
			{
				ByteArrayCollector bac = new ByteArrayCollector();
				byte[][] bevents =  bac.decode(value);
				TraceEvent[] events = new TraceEvent[bevents.length];
				for (int j = 0; j < bevents.length; j++)
					events[j] = new TraceEvent(bevents[j]);
				RefAnalysis analysis = new RefAnalysis();
				analysis.events = events;
				Pool.put("refanalysis", "primarytrace", analysis);
			}
			if (codename.equals(ParameterTypeCodenames.DADARA_ALARMS))
			{
				ReflectogramAlarm[] alarms = ReflectogramAlarm.fromByteArray(value);
				if (map_markers_panel != null)
					map_markers_panel.updateAlarms(alarms);
			}
			else if (codename.equals(ParameterTypeCodenames.DADARA_EVENTS))
			{
				ReflectogramEvent[] ep = ReflectogramEvent.fromByteArray(value);
				jbInitReflectogram(ep, arguments, meId, schemePathId);
			}
			else if (codename.equals("reflectogramm"))
			{
				BellcoreStructure bs = new BellcoreReader().getData(value);
				jbInitReflectogram(bs, arguments, meId, schemePathId);
			}
			else
			{
				Object val = parseSimpleData(ptype.getSort(), value);

				Vector vect = new Vector();
				vect.add(name);
				vect.add(val);
				tModelRes.insertRow(vect);
			}
		}
	}

	private Object parseSimpleData(DataType sort, byte[] value)
	{
		Object val = null;
		try {
			if (sort.equals(DataType.DATA_TYPE_STRING))
				val = String.valueOf(new ByteArray(value).toUTFString());
			else if (sort.equals(DataType.DATA_TYPE_INTEGER))
				val = String.valueOf(new ByteArray(value).toInt());
			else if (sort.equals(DataType.DATA_TYPE_LONG))
				val = String.valueOf(new ByteArray(value).toLong());
			else if (sort.equals(DataType.DATA_TYPE_DOUBLE))
				val = String.valueOf(new ByteArray(value).toDouble());
		}
		catch (Exception ex) {
		}
		return val;
	}

	private void jbInitReflectogram(ReflectogramEvent[] ep, SetParameter[] args, Identifier monitored_element_id, String scheme_path_id)
	{
		if (ep == null)
			return;

		int len = 0;
		double delta_x = 0;

		try
		{
			for (int i = 0; i < args.length; i++)
			{
				ParameterType ptype = (ParameterType)args[i].getType();
				String codename = ptype.getCodename();
				byte[] value = args[i].getValue();
				if (codename.equals(ParameterTypeCodenames.TRACE_RESOLUTION))
					delta_x = new ByteArray(value).toDouble();
				else if (codename.equals(ParameterTypeCodenames.TRACE_LENGTH))
					len = (int)new ByteArray(value).toDouble();
			}
			if (delta_x == 0 || len == 0)
				return;

			int n = (int)(len * 1000 / delta_x);

			double[] y = new double[n];
			for (int i = 0; i < ep.length; i++)
			{
				for (int j = ep[i].begin; j <= ep[i].end && j < n; j++)
					y[j] = ep[i].refAmpl(j)[0];
			}
			y = com.syrus.AMFICOM.analysis.dadara.MathRef.correctReflectogramm(y);

			if (map_markers_panel == null)
			{
				map_markers_panel = new MapMarkersPanel(tracePanel, internal_dispatcher, y, delta_x);
				map_markers_panel.updateEvents(ep);
				map_markers_panel.updateThresholds((ReflectogramEvent[])Pool.get("eventparams", "etalon"));
				map_markers_panel.paint_all_thresholds = true;

				//p.draw_modeled = false;
				if (monitored_element_id != null)
					map_markers_panel.setMonitoredElementId(monitored_element_id);
				if (scheme_path_id != null)
					map_markers_panel.setSchemePathId(scheme_path_id);
				map_markers_panel.setColorModel("primarytrace");
				tracePanel.addGraphPanel(map_markers_panel);
				tracePanel.updEvents("primarytrace");
				tracePanel.drawEvents(true);
				tracePanel.updScale2fit();

//					ReflectogramAlarm[] alarms = (ReflectogramAlarm[])Pool.get("refalarms", result.getId());
//					map_markers_panel.updateAlarms(alarms);

				jTabbedPane1.addTab("Рефлектограмма", tracePanel);
				jTabbedPane1.setSelectedComponent(tracePanel);
				paramTable.updateUI();
				resultTable.updateUI();
			}
			else
				map_markers_panel.updateEvents(ep);

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void jbInitReflectogram(BellcoreStructure bs, SetParameter[] args, Identifier monitored_element_id, String scheme_path_id)
	{
		int n = bs.dataPts.TNDP;
		double delta_x = (double)(bs.fxdParams.AR - bs.fxdParams.AO) * 3d /
										((double)n * (double)bs.fxdParams.GI/1000d);
		double[] y = new double[n];

		for (int i = 0; i < bs.dataPts.TPS[0]; i++)
			y[i] = (double)(65535 - bs.dataPts.DSF[0][i])/1000d;
//		y = com.syrus.AMFICOM.Client.Analysis.MathRef.correctReflectogramm(y);
		y = com.syrus.AMFICOM.analysis.dadara.MathRef.correctReflectogramm(y);

		if (map_markers_panel == null)
		{
			map_markers_panel = new MapMarkersPanel(tracePanel, internal_dispatcher, y, delta_x);
			map_markers_panel.updateThresholds((ReflectogramEvent[])Pool.get("eventparams", "etalon"));
			map_markers_panel.paint_all_thresholds = true;

			if (monitored_element_id != null)
				map_markers_panel.setMonitoredElementId(monitored_element_id);
			if (scheme_path_id != null)
				map_markers_panel.setSchemePathId(scheme_path_id);
			map_markers_panel.setColorModel("primarytrace");
			tracePanel.addGraphPanel(map_markers_panel);
			tracePanel.updEvents("primarytrace");
			tracePanel.drawEvents(true);
			tracePanel.updScale2fit();

//			ReflectogramAlarm[] alarms = (ReflectogramAlarm[])Pool.get("refalarms", result.getId());
//			map_markers_panel.updateAlarms(alarms);

			jTabbedPane1.addTab("Рефлектограмма", tracePanel);
			jTabbedPane1.setSelectedComponent(tracePanel);

			paramTable.updateUI();
			resultTable.updateUI();
		}
		else
		{
			map_markers_panel.init(y, delta_x);
			map_markers_panel.setDefaultScales();
			map_markers_panel.draw_modeled = true;
		}
	}

	private void deleteReflectogramTab()
	{
		if (jTabbedPane1.getComponentCount() > 1)
		{
			if (Pool.get("refanalysis", "primarytrace") != null)
				Pool.remove("refanalysis", "primarytrace");
			for (int i = 1; i < jTabbedPane1.getComponentCount(); i++)
				if (jTabbedPane1.getComponentAt(i).equals(tracePanel))
				{
					tracePanel.removeAllGraphPanels();
					jTabbedPane1.remove(tracePanel);
					map_markers_panel = null;
					return;
				}
		}
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setResizable(true);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setIconifiable(true);
		this.setTitle(LangModelSurvey.getString("Results"));
		this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter()
		{
			public void internalFrameOpened(InternalFrameEvent e)
			{
				this_internalFrameOpened(e);
			}
		});
		this.addPropertyChangeListener(JInternalFrame.IS_MAXIMUM_PROPERTY, new java.beans.PropertyChangeListener()
		{
			public void propertyChange(java.beans.PropertyChangeEvent evt)
			{
				this_propertyChanged(evt);
			}
		});
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				this_mouseDragged(e);
			}
		});

		this.getContentPane().setLayout(borderLayout1);
		jTabbedPane1.setBorder(BorderFactory.createEmptyBorder());

		tModelPar = new GeneralTableModel(
				new String[] {LangModelSurvey.getString("Input_parameter"),
				LangModelSurvey.getString("Value")},
				new String[] {"11", "11"},
				0);
		tModelRes = new GeneralTableModel(
				new String[] {LangModelSurvey.getString("Output_parameter"),
				LangModelSurvey.getString("Value")},
				new String[] {"22", "22"},
				0);

		paramTable = new JTable(tModelPar);
		resultTable = new JTable(tModelRes);

		paramTable.setDefaultRenderer(Object.class, new ValueTableRenderer());
		resultTable.setDefaultRenderer(Object.class, new ValueTableRenderer());

		paramPane.setViewport(paramViewport);
		resultPane.setViewport(resultViewport);
		paramPane.setAutoscrolls(true);
		resultPane.setAutoscrolls(true);
		paramTable.setSelectionMode(paramTable.getSelectionModel().SINGLE_SELECTION);
		resultTable.setSelectionMode(resultTable.getSelectionModel().SINGLE_SELECTION);
		paramPane.getViewport().add(paramTable);
		resultPane.getViewport().add(resultTable);

		splitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				paramPane,
				resultPane);
		splitPane.setOneTouchExpandable(false);
		splitPane.setResizeWeight(.5);

		jTabbedPane1.add(LangModelSurvey.getString("Parameters"), splitPane);

		this.getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
		this.setVisible(true);
	}

	boolean getActiveContext ()
	{
		String u_a = (String)Pool.get("activecontext", "useractionselected");
		if (u_a == null)
			u_a = "";

		String i = (String)Pool.get("activecontext", "selected_id");
		if (i == null)
			i = "";

		if(u_a.equals(received_user_action) && i.equals(received_id))
			return false;

		received_user_action = user_action = u_a;
		received_id = i;
		return true;
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("activecontextevent"))
		{
//			System.out.println(" event " + ae);
			if(getActiveContext())
			{
				Identifier id = (Identifier)Pool.get("activecontext", "selected_id");
				showActiveResult(id);
			}
		}
		if(ae.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			if (ae instanceof TreeDataSelectionEvent)
			{
				TreeDataSelectionEvent ev = (TreeDataSelectionEvent)ae;
				if (ev.getSelectionNumber() != -1)
				{
					List data = ev.getList();
					ObjectResource resource = (ObjectResource )data.get(ev.getSelectionNumber());

					Pool.put("activecontext", "selected_id", resource.getId());

					if (resource instanceof Alarm)
						Pool.put("activecontext", "useractionselected", "alarm_selected");
					else if (resource instanceof Test)
						Pool.put("activecontext", "useractionselected", "test_selected");
					else if (resource instanceof Analysis)
						Pool.put("activecontext", "useractionselected", "analysis_selected");
					else if (resource instanceof Modeling)
						Pool.put("activecontext", "useractionselected", "modeling_selected");
					else if (resource instanceof Evaluation)
						Pool.put("activecontext", "useractionselected", "evaluation_selected");
					else if (resource instanceof Result)
						Pool.put("activecontext", "useractionselected", "result_selected");
					else if (resource instanceof TransmissionPath)
						Pool.put("activecontext", "useractionselected", "path_selected");
					else
						Pool.remove("activecontext", "useractionselected");

					if(getActiveContext())
					{
						Identifier id = (Identifier)Pool.get("activecontext", "selected_id");
						showActiveResult(id);
					}
				}
			}
		}
	}


	void this_internalFrameOpened(InternalFrameEvent e)
	{
		//init_module();
	}

	void this_propertyChanged(java.beans.PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(JInternalFrame.IS_MAXIMUM_PROPERTY))
			if (tracePanel != null)
				tracePanel.resize();
	}

	void this_mouseDragged(MouseEvent e)
	{
		if (tracePanel != null)
			tracePanel.resize();
	}

	public MapMarkersPanel getReflectPicture()
	{
		return map_markers_panel;
	}
}

class ValueTableRenderer extends DefaultTableCellRenderer
{
	public ValueTableRenderer()
	{
		super();
	}

	public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column)
	{
		try
		{
			Component comp = (Component )value;
			return comp;
		}
		catch(Exception ex)
		{
			return super.getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column);
		}
	}
}

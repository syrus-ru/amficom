/*-
 * $Id: ResultFrame.java,v 1.4 2005/10/31 12:30:02 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.observer.ui;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.MapThresholdsLayeredPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ThresholdsPanel;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.EtalonComparison;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.DataStreamableUtil;
import com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.controllers.ParameterController;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;
import com.syrus.AMFICOM.resource.LangModelObserver;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.DataFormatException;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/10/31 12:30:02 $
 * @module surveyclient_v1
 */

public class ResultFrame extends JInternalFrame implements PropertyChangeListener {
	private static final long serialVersionUID = 3258417226830002487L;

	public static final String	NAME = "resultFrame"; //$NON-NLS-1$
	
	ApplicationContext aContext;
	
	JTabbedPane tabs;
	WrapperedTableModel inParamsTableModel;
	WrapperedTableModel outParamsTableModel;
	
	MapThresholdsLayeredPanel layeredPanel;
//	private Map<String, JComponent> traceMap;
	private Identifier meId = null;
	private Identifier spId = null;
	
	public ResultFrame(ApplicationContext aContext) {
		this();
		setContext(aContext);
	}
	
	public ResultFrame() {
		this.setClosable(true);
		this.setMaximizable(true);
		this.setIconifiable(true);
		this.setResizable(true);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setFrameIcon(UIManager.getIcon(ResourceKeys.ICON_GENERAL));
		this.setName(NAME);
		this.setTitle(LangModelObserver.getString("title.result_frame")); //$NON-NLS-1$
		
		this.addPropertyChangeListener(JInternalFrame.IS_MAXIMUM_PROPERTY, new java.beans.PropertyChangeListener() {

			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(JInternalFrame.IS_MAXIMUM_PROPERTY)) {
					if (ResultFrame.this.layeredPanel != null)
						ResultFrame.this.layeredPanel.resize();
				}
			}
		});
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				if (ResultFrame.this.layeredPanel != null)
					ResultFrame.this.layeredPanel.resize();
			}
		});
		
		this.inParamsTableModel = new WrapperedTableModel(ParameterController.getInstance(), 
				new String[] {StorableObjectWrapper.COLUMN_NAME, ParameterController.COLUMN_VALUE });
		this.outParamsTableModel = new WrapperedTableModel(ParameterController.getInstance(), 
				new String[] {StorableObjectWrapper.COLUMN_NAME, ParameterController.COLUMN_VALUE });

		WrapperedTable inParamsTable = new WrapperedTable(this.inParamsTableModel);
		WrapperedTable outParamsTable = new WrapperedTable(this.outParamsTableModel);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				inParamsTable, outParamsTable);
		splitPane.setOneTouchExpandable(false);
		splitPane.setResizeWeight(.5);
		
		this.tabs = new JTabbedPane();
		this.tabs.add(LangModelObserver.getString("title.parameters"), splitPane); //$NON-NLS-1$

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(this.tabs, BorderLayout.CENTER);
		
//		this.traceMap = new HashMap<String, JComponent>();
		Heap.addBsHashListener(new BsHashChangeListener() {
			public void bsHashAdded(String key) {
				PFTrace bs = Heap.getAnyPFTraceByKey(key);
				if (bs == null)
					return;
				initReflectogramTab(key, bs.getBS());
			}

			public void bsHashRemoved(String key) {
				removeReflectogramTab(key);
			}

			public void bsHashRemovedAll() {
				removeReflectogramTab(Heap.PRIMARY_TRACE_KEY);
			}
		});
	}
	
	void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(ObjectSelectedEvent.TYPE, this);
		}
		this.aContext = aContext;
		this.aContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);
	}
	
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ev = (ObjectSelectedEvent)e;
			Object selected = ev.getSelectedObject();
			if (ev.isSelected(ObjectSelectedEvent.MEASUREMENT)) {
				Measurement m = (Measurement)selected;
				Result r = null;
				try {
					StorableObjectCondition condition2 = new LinkedIdsCondition(m.getId(), ObjectEntities.RESULT_CODE);
					Set<StorableObject> resultSet = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
					for (Iterator<StorableObject> iter = resultSet.iterator(); iter.hasNext();) {
						Result res = (Result) iter.next();
						if (res.getSort().value() == ResultSort.RESULT_SORT_MEASUREMENT.value()) {
							r = res;
							break;
						}
					}
				} catch (ApplicationException e1) {
					Log.errorMessage(e1);
				}
				showResult(r);
			}
		}
	}
	
	public void showResult(Result result) {
		this.inParamsTableModel.clear();
		this.outParamsTableModel.clear();
		
		if (result.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
			Measurement m = (Measurement)result.getAction();
			this.meId = m.getMonitoredElementId();
			MeasurementSetup ms = m.getSetup();
			ParameterSet argumentSet = ms.getParameterSet();
			
			List<Parameter> parameters = new ArrayList<Parameter>(Arrays.asList(result.getParameters()));
			
			LinkedIdsCondition condition1 = new LinkedIdsCondition(m.getId(), ObjectEntities.ANALYSIS_CODE);
			try {
				Set<Analysis> analyse = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
				for (Analysis analysis : analyse) {
					LinkedIdsCondition condition2 = new LinkedIdsCondition(analysis.getId(), ObjectEntities.RESULT_CODE);
					Set<Result> results = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
					for (Result result1 : results) {
						parameters.addAll(Arrays.asList(result1.getParameters()));
					}
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
			
			AnalysisResult ar = null;
			// get Dadara Analysis
			for (Parameter parameter : parameters) {
				if (parameter.getType().equals(ParameterType.DADARA_ANALYSIS_RESULT)) {
					try {
						ar = (AnalysisResult) DataStreamableUtil.readDataStreamableFromBA(parameter.getValue(), AnalysisResult.getDSReader());
					} catch (DataFormatException e2) {
						Log.errorMessage(e2);
					}
				}
			}
			
			try {
				AnalysisUtil.loadCriteriaSet(LoginManager.getUserId(), ms);
				if (ar != null)
					Heap.openPrimaryTraceAndNotify(result, ar);
				else
					Heap.openPrimaryTraceAndNotify(result);
			} catch (SimpleApplicationException e) {
				Log.errorMessage(e);
			} catch (DataFormatException e) {
				Log.errorMessage(e);
			}

			// get Alarms
			for (Parameter parameter : parameters) {
				if (parameter.getType().equals(ParameterType.DADARA_ALARMS)) {
					try {
						ReflectogramMismatchImpl[] alarms = ReflectogramMismatchImpl.alarmsFromByteArray(parameter.getValue());
						// XXX assume the only alarm
						if (alarms.length == 1) {
							//Heap.setRefMismatch(alarms[0]);
							final List<ReflectogramMismatchImpl> alarmList =
								new ArrayList<ReflectogramMismatchImpl>();
							alarmList.add(alarms[0]);
							Heap.setEtalonComparison(new EtalonComparison(){
								public List<ReflectogramMismatchImpl> getAlarms() {
									return alarmList;
								}
								public EvaluationPerEventResult getPerEventResult() {
									throw new UnsupportedOperationException();
								}
								public 	ReflectometryEvaluationOverallResult getOverallResult() {
									throw new UnsupportedOperationException();
								}
							});
						}
					} catch (DataFormatException e) {
						Log.errorMessage(e);
					}
				}
			}

			processParameters(parameters, argumentSet);

		} else if (result.getSort().equals(ResultSort.RESULT_SORT_MODELING)) {
			Modeling modeling = (Modeling)result.getAction();
			this.meId = modeling.getMonitoredElementId();
			ParameterSet argumentSet = modeling.getArgumentSet();

			processParameters(null, argumentSet);
			
		}
	}
	
	private void processParameters(Collection<Parameter> parameters, ParameterSet arguments) {
		if (parameters != null) {
			for (Parameter parameter : parameters) {
				if (isNeedShowing(parameter))
					this.inParamsTableModel.addObject(parameter);
			}
		}
		
		if (arguments != null) {
			for (Parameter argument : arguments.getParameters()) {
				if (isNeedShowing(argument))
					this.outParamsTableModel.addObject(argument);
			}
		}
	}
	
	/**
	 * @param parameter
	 * @return <code>true</code> if need to show parameter in table, otherwise <code>false</code>  
	 */
	private boolean isNeedShowing(Parameter parameter) {
		ParameterType parameterType = parameter.getType();
		if (parameterType.equals(ParameterType.REFLECTOGRAMMA)) {
			return false;
		} 
		if (parameterType.equals(ParameterType.DADARA_ALARMS)) {
			return false;
		} 
		else if (parameterType.equals(ParameterType.DADARA_ANALYSIS_RESULT)) {
			return false;
		}
		return true;
	}
	
	void removeReflectogramTab(String key) {
		Log.debugMessage(ResultFrame.class.getName() + ".removeReflectogramTab() perform", Level.FINEST); //$NON-NLS-1$
		this.tabs.remove(this.layeredPanel);
	}
	
	void initReflectogramTab(String key, BellcoreStructure bs) {
		Log.debugMessage(ResultFrame.class.getName() + ".initReflectogramTab() perform", Level.FINEST); //$NON-NLS-1$
		
		double[] data = bs.getTraceData();
		double deltaX = bs.getResolution();
		
		if (this.layeredPanel == null) {
			this.layeredPanel = new MapThresholdsLayeredPanel(this.aContext.getDispatcher());
		} else {
			this.layeredPanel.deleteNonAlarmMarkers();
		}
			
		ThresholdsPanel reflectogramPanel = new ThresholdsPanel(this.layeredPanel, this.aContext.getDispatcher(), data, deltaX);
		reflectogramPanel.setColorModel(Heap.PRIMARY_TRACE_KEY);
			
		if (this.meId != null)
			reflectogramPanel.setMonitoredElementId(this.meId);
		if (this.spId != null)
			reflectogramPanel.setSchemePathId(this.spId);
						
		this.layeredPanel.setGraphPanel(reflectogramPanel);
		reflectogramPanel.select_by_mouse = true;
		this.layeredPanel.updScale2fit();

		this.tabs.addTab(LangModelAnalyse.getString("trace"), this.layeredPanel);  //$NON-NLS-1$
		this.tabs.setSelectedComponent(this.layeredPanel);
//		this.traceMap.put(key, this.layeredPanel);
	}
}

package com.syrus.AMFICOM.Client.Survey.Result;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.MapMarkersLayeredPanel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.MapMarkersPanel;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.GeneralTableModel;
import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Resource.Alarm.SystemEvent;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.SurveyDataSourceImage;
import com.syrus.AMFICOM.Client.Survey.SurveyMDIMain;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.*;
import com.syrus.util.ByteArray;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;

public class ResultFrame extends JInternalFrame implements OperationListener
{
	ApplicationContext aContext;
	Dispatcher internal_dispatcher;
	DataSourceInterface dataSource;
	Result result;

	String user_action;
	String id;

	String received_user_action;
	String received_id;

	String title = "Результат ";

	Alarm displayed_alarm;
	Test displayed_test;
	Evaluation displayed_evaluation;
	Analysis displayed_analysis;
	Modeling displayed_modeling;

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
      new OperationEvent (this,0,SurveyMDIMain.resultFrameDisplayed));
	}

	public ResultFrame()
	{
		this(new ApplicationContext());
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		this.dataSource = aContext.getDataSourceInterface();
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

	public Result addOnResult1;
	public Result addOnResult2;

	public void showActiveResult()
	{
		try
		{

		Result r;
		String res_id = "";
		this.addOnResult1 = null;
		this.addOnResult2 = null;

		this.displayed_test = null;
		this.displayed_evaluation = null;
		this.displayed_analysis = null;
		this.displayed_modeling = null;
		this.title = null;

		if (this.user_action.equals("alarm_selected"))
		{
			Alarm alarm = (Alarm )Pool.get(Alarm.typ, this.id);
			this.title = "Результат: " + alarm.getName();
			SystemEvent event = (SystemEvent )Pool.get("event", alarm.event_id);

			this.displayed_alarm = alarm;
			this.user_action = "result_selected";
			this.id = event.descriptor;
		} else 
		if (this.user_action.equals("test_selected"))
		{
//			res_id = dataSource.GetTestResult(id);
			res_id = new SurveyDataSourceImage(this.dataSource).GetTestResult(this.id);
			Result re = (Result )Pool.get(Result.TYPE, res_id);
			this.displayed_test = (Test )Pool.get(Test.TYPE, this.id);
			this.title = "Результат: " + re.getName();
		} else 
		if (this.user_action.equals("path_selected"))
		{
//			res_id = dataSource.GetLastResult(id);
			res_id = new SurveyDataSourceImage(this.dataSource).GetLastResult(this.id);
			Result re = (Result )Pool.get(Result.TYPE, res_id);
			this.displayed_test = (Test )Pool.get(Test.TYPE, re.getActionId());
			this.title = "Результат: " + re.getName();
		} else
		if (this.user_action.equals("analysis_selected"))
		{
//			res_id = dataSource.GetAnalysisResult(id);
			res_id = new SurveyDataSourceImage(this.dataSource).GetAnalysisResult(this.id);
			Result re = (Result )Pool.get(Result.TYPE, res_id);
			this.displayed_analysis = (Analysis )Pool.get(Analysis.TYPE, this.id);
			this.title = "Результат: " + re.getName();
		} else
		if (this.user_action.equals("modeling_selected"))
		{
//			res_id = dataSource.GetModelingResult(id);
			res_id = new SurveyDataSourceImage(this.dataSource).GetModelingResult(this.id);
			this.displayed_modeling = (Modeling )Pool.get(Modeling.TYPE, this.id);
			this.title = "Результат: " + this.displayed_modeling.getName();
		} else
		if (this.user_action.equals("evaluation_selected"))
		{
//			res_id = dataSource.GetEvaluationResult(id);
			res_id = new SurveyDataSourceImage(this.dataSource).GetEvaluationResult(this.id);
			Result re = (Result )Pool.get(Result.TYPE, res_id);
			this.displayed_evaluation = (Evaluation )Pool.get(Evaluation.TYPE, this.id);
			this.title = "Результат: " + re.getName();
		} else 
		if (this.user_action.equals("result_selected"))
		{
//			new SurveyDataSourceImage(dataSource).GetResult(id);
			res_id = this.id;

			Result rrr = (Result )Pool.get(Result.TYPE, res_id);
			String resultType = rrr.getResultType();
			String resultActionId = rrr.getActionId();			
			if(resultType.equals(Test.TYPE))
			{
				new SurveyDataSourceImage(this.dataSource).GetTests(new String[] {resultActionId} );
				Test test = (Test )Pool.get(Test.TYPE, resultActionId);
				this.displayed_test = test;
				if(this.title == null)
					this.title = "Результат: " + rrr.getName();

				Analysis anal = null;
				if(test.getAnalysisId() != null && test.getAnalysisId().length()>0)
				{
					new SurveyDataSourceImage(this.dataSource).GetAnalysis(test.getAnalysisId());
					anal = (Analysis )Pool.get(Analysis.TYPE, test.getAnalysisId());
					if(anal != null)
					{
						this.displayed_analysis = anal;
//						dataSource.GetAnalysisResult(anal.getId());
//						new SurveyDataSourceImage(dataSource).GetAnalysisResult(anal.getId());
					}
				}

				Evaluation eval = null;
				if(test.getEvaluationId() != null && test.getEvaluationId().length()>0)
				{
					new SurveyDataSourceImage(this.dataSource).GetEvaluation(test.getEvaluationId());
					eval = (Evaluation )Pool.get(Evaluation.TYPE, test.getEvaluationId());
					if(eval != null)
					{
						this.displayed_evaluation = eval;
//						dataSource.GetEvaluationResult(eval.getId());
//						new SurveyDataSourceImage(dataSource).GetEvaluationResult(eval.getId());
					}
				}
				
				String[] resultId = test.getResultIds();
				for(int i = 0; i < resultId.length; i++)
					if(resultId[i].equals(this.id)) {
						
						if(anal != null){
							String[] resultIds = anal.getResultIds();
							if(resultIds.length > i) {
								String ri = resultIds[i];
								new SurveyDataSourceImage(this.dataSource).GetResult(ri);
								this.addOnResult1 = (Result )Pool.get(Result.TYPE, ri);
								}
						}

						if(eval != null){
							String[] evalResultIds = eval.getResultIds();
							if(evalResultIds.length > i){
								String ri = evalResultIds[i];
								new SurveyDataSourceImage(this.dataSource).GetResult(ri);
								this.addOnResult2 = (Result )Pool.get(Result.TYPE, ri);
							}
						}
					}
			}
			else
			if(resultType.equals(Analysis.TYPE)){
				new SurveyDataSourceImage(this.dataSource).GetAnalysis(resultActionId);
				Analysis anal = (Analysis )Pool.get(Analysis.TYPE, resultActionId);
				this.displayed_analysis = anal;
				if(this.title == null)
					this.title = "Результат: " + rrr.getName();

				Test test = (Test )Pool.get(Test.TYPE, new SurveyDataSourceImage(this.dataSource).GetTestForAnalysis(anal.getId()));
				if(test != null){
					String[] resultIds = anal.getResultIds();
					for(int i = 0; i < resultIds.length; i++)
						if(resultIds[i].equals(this.id))
						{
							this.displayed_test = test;
							if(test.getResultIds().length - 1 < i)
								return;
							String ri = test.getResultIds()[i];
//							dataSource.GetTestResult(test.getId());
//							new SurveyDataSourceImage(dataSource).GetTestResult(test.getId());
							new SurveyDataSourceImage(this.dataSource).GetResult(ri);
							this.addOnResult1 = rrr;
							res_id = ri;
//							addOnResult1 = (Result )Pool.get("result", ri);
						}
				}
			}
			else
			if(resultType.equals(Evaluation.TYPE))
			{
				new SurveyDataSourceImage(dataSource).GetEvaluation(resultActionId);
				Evaluation eval = (Evaluation )Pool.get(Evaluation.TYPE, resultActionId);
				this.displayed_evaluation = eval;
				if(title == null)
					this.title = "Результат: " + rrr.getName();

				Test test = (Test )Pool.get(Test.TYPE, new SurveyDataSourceImage(dataSource).GetTestForEvaluation(eval.getId()));
				if(test != null){
					String[] resultIds = eval.getResultIds();
					for(int i = 0; i < resultIds.length; i++)
						if(resultIds[i].equals(id))
						{
							displayed_test = test;
							if(test.getResultIds().length - 1 < i)
								return;
							String ri = test.getResultIds()[i];
							new SurveyDataSourceImage(dataSource).GetResult(ri);
							addOnResult1 = rrr;
							res_id = ri;

							Analysis anal = null;
							if(test.getAnalysisId() != null && test.getAnalysisId().length()>0)
							{
								new SurveyDataSourceImage(dataSource).GetAnalysis(test.getAnalysisId());
								anal = (Analysis )Pool.get(Analysis.TYPE, test.getAnalysisId());
								if(anal != null){
									String[] analysisResultIds = anal.getResultIds();
									if(analysisResultIds.length > i)
									{
										displayed_analysis = anal;
										String ri2 = analysisResultIds[i];
										new SurveyDataSourceImage(dataSource).GetResult(ri2);
										addOnResult2 = (Result )Pool.get(Result.typ, ri2);
									}
								}
							}
							break;
						}
				}
			}
			rrr.updateLocalFromTransferable();
		}
		r = (Result)Pool.get("result", res_id);
		r.updateLocalFromTransferable();
		showResult(r);

		}
		catch (Exception ex)
		{
			System.out.println("Could not show result - " + ex.getMessage());
			System.out.println("(user_action = " + user_action + ", id = " + id + ")");
//			ex.printStackTrace();
			showResult(null);
		}
		if(title == null)
			title = "Результаты ";
		setTitle(title);
	}

	public void setResult(Result result)
	{
		this.result = result;
		showResult(result);
	}

	void showResult(Result result)
	{
		tModelPar.clearTable();
		tModelRes.clearTable();
		deleteReflectogramTab();

		if (result == null)
			return;
		
		List arguments = null;

		String meId = null;
		String mapPathId = null;

	try
	{
		String resultType = result.getResultType();
		String actionId = result.getActionId();
		if (resultType.equals(Analysis.TYPE))
		{
			Analysis a = (Analysis)Pool.get(Analysis.TYPE, actionId);
			arguments = a.getArgumentList();
			meId = a.getMonitoredElementId();
		}
		else
		if (resultType.equals(Modeling.TYPE))
		{
			Modeling m = (Modeling )Pool.get(Modeling.TYPE, actionId);
			arguments = m.getArgumentList();
			mapPathId = m.getSchemePathId();
		}
		else
		if (resultType.equals(Evaluation.TYPE))
		{
			Evaluation e = (Evaluation )Pool.get(Evaluation.TYPE, actionId);
			arguments = e.getArgumentList();
			meId = e.getMonitoredElementId();
/*
			Result re = (Result )Pool.get("result", e.result_id);
			if (re.result_type.equals("test"))
			{
				Test t = (Test )Pool.get("test", re.test_id);
				monitored_element_id = t.monitored_element_id;
			}
*/
		}
		else
		if (resultType.equals(Test.TYPE))
		{
			Test t = (Test )Pool.get(Test.TYPE, actionId);
			dataSource.LoadTestArgumentSets(new String[] {t.getTestArgumentSetId()});
			TestArgumentSet tas = (TestArgumentSet )Pool.get(TestArgumentSet.TYPE, t.getTestArgumentSetId());
			tas.updateTestArgumentSet(t.getTestTypeId());
			arguments = tas.getArgumentList();
			meId = t.getMonitoredElementId();
		}
		
	}
	catch(Exception e){
			
	}


	
		proccessParameters(result.getParameterList(), meId , mapPathId);
		if(this.addOnResult1 != null){
			this.addOnResult1.updateLocalFromTransferable();			
			proccessParameters(this.addOnResult1.getParameterList(), meId , mapPathId);
		}

		if(this.addOnResult2 != null){
			this.addOnResult2.updateLocalFromTransferable();			
			proccessParameters(this.addOnResult2.getParameterList(), meId , mapPathId);
		}


		if (arguments!=null){
			for(Iterator it=arguments.iterator();it.hasNext();){
				Parameter arg = (Parameter)it.next();
		//				if (arg.code_name.equals("reflectogramm"))
				String codename = "";
				String name = "";
				ActionParameterType apt = arg.getApt();
				GlobalParameterType gpt = arg.getGpt();
				String data_type = gpt.getValueType();
				byte[] value = arg.getValue();			
				if(apt != null)
				{
					codename = apt.getCodename();
					name = apt.getName();
				}
				else
				if(gpt != null)
				{
					codename = gpt.getCodename();
					name = gpt.getName();
				}
	
				if (codename.equals("reflectogramm"))
				{
					BellcoreStructure bs = new BellcoreReader().getData(value);
					jbInitReflectogram(bs, this.displayed_test, meId, mapPathId);
				}
				else
				{
					Object val = new String("");// = arg.value.toString();
					try
					{
						if(data_type.equals("string"))
						{
							val = String.valueOf(new ByteArray(value).toString());
						}
						else if(data_type.equals("integer"))
						{
							val = String.valueOf(new ByteArray(value).toInt());
						}
						else if(data_type.equals("long"))
						{
							val = String.valueOf(new ByteArray(value).toLong());
						}
						else if(data_type.equals("double"))
						{
							val = String.valueOf(new ByteArray(value).toDouble());
						}
						else if(data_type.equals("tracechangescoordinatesarray"))
						{
							val = String.valueOf(new ByteArray(value).toString());
						}
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
					Vector vect = new Vector();
					vect.add(name);
					vect.add(val);
					this.tModelPar.insertRow(vect);
				}
			}
		}

		if(this.displayed_test != null)
		{
			Vector vect = new Vector();
			vect.add("Шаблон исследования");
			vect.add(Pool.getName(TestSetup.TYPE, this.displayed_test.getTestSetupId()));
			this.tModelPar.insertRow(vect);
		}


		this.paramTable.updateUI();
		this.resultTable.updateUI();
	}
	
	private void proccessParameters(List parameterList, String meId, String mapPathId){
		for(Iterator it=parameterList.iterator();it.hasNext();){
			Parameter param = (Parameter )it.next();
			String codename = "";
			String name = "";
			ActionParameterType apt = param.getApt();
			GlobalParameterType gpt = param.getGpt();
			String data_type = gpt.getValueType();
			byte[] value = param.getValue();			
			if(apt != null)
			{
				codename = apt.getCodename();
				name = apt.getName();
			}
			else
			if(gpt != null)
			{
				codename = gpt.getCodename();
				name = gpt.getName();
			}

			if (codename.equals("traceevents"))
			{
				ByteArrayCollector bac = new ByteArrayCollector();
				byte[][] bevents =  bac.decode(value);
				TraceEvent[] events = new TraceEvent[bevents.length];
				for (int i = 0; i < bevents.length; i++)
					events[i] = new TraceEvent(bevents[i]);
				//param.value;
				RefAnalysis analysis = new RefAnalysis();
				analysis.events = events;
				Pool.put("refanalysis", "primarytrace", analysis);
			}
			if (codename.equals("dadara_alarm_array"))
			{
				ReflectogramAlarm[] alarms = ReflectogramAlarm.fromByteArray(value);
				if (map_markers_panel != null)
					map_markers_panel.updateAlarms(alarms);
			}
			if (codename.equals("dadara_event_array"))
			{
				ReflectogramEvent[] ep = ReflectogramEvent.fromByteArray(value);
				jbInitReflectogram(ep, displayed_test, meId, mapPathId);
			}
			else
			if (codename.equals("concavities"))
			{}
			else
	//			if (param.code_name.equals("reflectogramm"))
			if (codename.equals("reflectogramm"))
			{
				BellcoreStructure bs = new BellcoreReader().getData(value);
				jbInitReflectogram(bs, displayed_test, meId, mapPathId);
			}
			else
			{
				Object val = new String("");//param.value.toString();
				try
				{
					if(data_type.equals("string"))
					{
						val = String.valueOf(new ByteArray(value).toUTFString());
					}
					else
					if(data_type.equals("integer"))
					{
						val = String.valueOf(new ByteArray(value).toInt());
					}
					else if(data_type.equals("long"))
					{
						val = String.valueOf(new ByteArray(value).toLong());
					}
					else
					if(data_type.equals("double"))
					{
						val = String.valueOf(new ByteArray(value).toDouble());
					}
					else
					if(data_type.equals("traceeventarray"))
					{
						val = String.valueOf(new ByteArray(value).toUTFString());
					}
					else
					if(data_type.equals("characterizationidentity"))
					{
						val = new ResourceButton(dataSource);
					}
				}
				catch(Exception ex)
				{
				}
				Vector vect = new Vector();
				vect.add(name);
				vect.add(val);
				tModelRes.insertRow(vect);
			}
		}
	}

	private void jbInitReflectogram(ReflectogramEvent[] ep, Test test, String monitored_element_id, String map_path_id)
	{
		if (ep == null || test == null)
			return;

		TestArgumentSet metas = (TestArgumentSet)Pool.get(TestArgumentSet.typ, test.getTestArgumentSetId());
		if (metas == null)
			return;

		int len = 0;
		double delta_x = 0;

		try
		{
			for (Iterator it = metas.getArgumentList().iterator(); it.hasNext();)
			{
				Parameter p = (Parameter)it.next();
				String codename = p.getCodename();
				byte[] value = p.getValue();
				if (codename.equals("ref_res"))
				{
					delta_x = new ByteArray(value).toDouble();
				} else 
				if (codename.equals("ref_trclen"))
				{
					len = (int)new ByteArray(value).toDouble();
				}
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
//			y = com.syrus.AMFICOM.Client.Analysis.MathRef.correctReflectogramm(y);
			y = com.syrus.AMFICOM.analysis.dadara.MathRef.correctReflectogramm(y);

			if (test != null)
			{
				this.dataSource.loadTestSetup(test.getTestSetupId());
				TestSetup ts = (TestSetup)Pool.get(TestSetup.typ, test.getTestSetupId());
				AnalysisUtil.load_Etalon(dataSource, ts);
				AnalysisUtil.load_Thresholds(dataSource, ts);
			}

			if (map_markers_panel == null)
			{
				map_markers_panel = new MapMarkersPanel(tracePanel, internal_dispatcher, y, delta_x);
				map_markers_panel.updateEvents(ep);
				map_markers_panel.updateThresholds((ReflectogramEvent[])Pool.get("eventparams", "etalon"));
				map_markers_panel.paint_all_thresholds = true;

				//p.draw_modeled = false;
				if (monitored_element_id != null)
					map_markers_panel.setMonitoredElementId (monitored_element_id);
				if (map_path_id != null)
					map_markers_panel.setMapPathId(map_path_id);
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

	private void jbInitReflectogram(BellcoreStructure bs, Test test, String monitored_element_id, String map_path_id)
	{
		int n = bs.dataPts.TNDP;
		double delta_x = (double)(bs.fxdParams.AR - bs.fxdParams.AO) * 3d /
										((double)n * (double)bs.fxdParams.GI/1000d);
		double[] y = new double[n];

		for (int i = 0; i < bs.dataPts.TPS[0]; i++)
			y[i] = (double)(65535 - bs.dataPts.DSF[0][i])/1000d;
//		y = com.syrus.AMFICOM.Client.Analysis.MathRef.correctReflectogramm(y);
		y = com.syrus.AMFICOM.analysis.dadara.MathRef.correctReflectogramm(y);
		if (test != null)
		{
			dataSource.loadTestSetup(test.getTestSetupId());
			TestSetup ts = (TestSetup)Pool.get(TestSetup.typ, test.getTestSetupId());
			AnalysisUtil.load_Etalon(dataSource, ts);
			AnalysisUtil.load_Thresholds(dataSource, ts);
		}

		if (map_markers_panel == null)
		{
			map_markers_panel = new MapMarkersPanel(tracePanel, internal_dispatcher, y, delta_x);
			map_markers_panel.updateThresholds((ReflectogramEvent[])Pool.get("eventparams", "etalon"));
			map_markers_panel.paint_all_thresholds = true;

			if (monitored_element_id != null)
				map_markers_panel.setMonitoredElementId (monitored_element_id);
			if (map_path_id != null)
				map_markers_panel.setMapPathId(map_path_id);
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
		received_id = id = i;
		return true;
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("activecontextevent"))
		{
//			System.out.println(" event " + ae);
			if(getActiveContext())
				showActiveResult();
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
						showActiveResult();
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

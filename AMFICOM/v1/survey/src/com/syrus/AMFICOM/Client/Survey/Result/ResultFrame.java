package com.syrus.AMFICOM.Client.Survey.Result;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ColorManager;
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
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Result.Analysis;
import com.syrus.AMFICOM.Client.Resource.Result.Evaluation;
import com.syrus.AMFICOM.Client.Resource.Result.Etalon;
import com.syrus.AMFICOM.Client.Resource.Result.Modeling;
import com.syrus.AMFICOM.Client.Resource.Result.Parameter;
import com.syrus.AMFICOM.Client.Resource.Result.Result;
import com.syrus.AMFICOM.Client.Resource.Result.Test;
import com.syrus.AMFICOM.Client.Resource.Result.TestArgumentSet;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;
import com.syrus.AMFICOM.Client.Resource.SurveyDataSourceImage;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramAlarm;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.TraceEvent;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.ByteArray;
import com.syrus.io.ByteArrayCollector;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import java.util.Enumeration;
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
	private String monitred_el_id = "";

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

		tracePanel = new MapMarkersLayeredPanel(internal_dispatcher);
		getActiveContext();
	}

	public ResultFrame()
	{
		this(new ApplicationContext());
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		dataSource = aContext.getDataSourceInterface();
	}

	public ApplicationContext getContext()
	{
		return aContext;
	}

	public Dispatcher getInternalDispatcher()
	{
		return internal_dispatcher;
	}

	public void init_module()
	{
		internal_dispatcher = aContext.getDispatcher();

		internal_dispatcher.register(this, "activecontextevent");
		internal_dispatcher.register(this, TreeDataSelectionEvent.type);
	}

	public Result add_on_result1;
	public Result add_on_result2;

	public void showActiveResult()
	{
		try
		{

		Result r;
		String res_id = "";
		add_on_result1 = null;
		add_on_result2 = null;

		displayed_test = null;
		displayed_evaluation = null;
		displayed_analysis = null;
		displayed_modeling = null;
		title = null;

		if (user_action.equals("alarm_selected"))
		{
			Alarm alarm = (Alarm )Pool.get(Alarm.typ, id);
			title = "Результат: " + alarm.getName();
			SystemEvent event = (SystemEvent )Pool.get("event", alarm.event_id);

			displayed_alarm = alarm;
			user_action = "result_selected";
			id = event.descriptor;
		}
		if (user_action.equals("test_selected"))
		{
//			res_id = dataSource.GetTestResult(id);
			res_id = new SurveyDataSourceImage(dataSource).GetTestResult(id);
			Result re = (Result )Pool.get(Result.typ, res_id);
			displayed_test = (Test )Pool.get(Test.typ, id);
			title = "Результат: " + re.getName();
		}
		if (user_action.equals("path_selected"))
		{
//			res_id = dataSource.GetLastResult(id);
			res_id = new SurveyDataSourceImage(dataSource).GetLastResult(id);
			Result re = (Result )Pool.get(Result.typ, res_id);
			displayed_test = (Test )Pool.get(Test.typ, re.action_id);
			title = "Результат: " + re.getName();
		}
		if (user_action.equals("analysis_selected"))
		{
//			res_id = dataSource.GetAnalysisResult(id);
			res_id = new SurveyDataSourceImage(dataSource).GetAnalysisResult(id);
			Result re = (Result )Pool.get(Result.typ, res_id);
			displayed_analysis = (Analysis )Pool.get(Analysis.typ, id);
			title = "Результат: " + re.getName();
		}
		if (user_action.equals("modeling_selected"))
		{
//			res_id = dataSource.GetModelingResult(id);
			res_id = new SurveyDataSourceImage(dataSource).GetModelingResult(id);
			displayed_modeling = (Modeling )Pool.get(Modeling.typ, id);
			title = "Результат: " + displayed_modeling.getName();
		}
		if (user_action.equals("evaluation_selected"))
		{
//			res_id = dataSource.GetEvaluationResult(id);
			res_id = new SurveyDataSourceImage(dataSource).GetEvaluationResult(id);
			Result re = (Result )Pool.get(Result.typ, res_id);
			displayed_evaluation = (Evaluation )Pool.get(Evaluation.typ, id);
			title = "Результат: " + re.getName();
		}
		if (user_action.equals("result_selected"))
		{
//			new SurveyDataSourceImage(dataSource).GetResult(id);
			res_id = id;

			Result rrr = (Result )Pool.get("result", res_id);
			if(rrr.result_type.equals("test"))
			{
				new SurveyDataSourceImage(dataSource).GetTests(new String[] {rrr.action_id} );
				Test test = (Test )Pool.get("test", rrr.action_id);
				displayed_test = test;
				if(title == null)
					title = "Результат: " + rrr.getName();

				Analysis anal = null;
				if(test.analysis_id != null && !test.analysis_id.equals(""))
				{
					new SurveyDataSourceImage(dataSource).GetAnalysis(test.analysis_id);
					anal = (Analysis )Pool.get("analysis", test.analysis_id);
					if(anal != null)
					{
						displayed_analysis = anal;
//						dataSource.GetAnalysisResult(anal.getId());
//						new SurveyDataSourceImage(dataSource).GetAnalysisResult(anal.getId());
					}
				}

				Evaluation eval = null;
				if(test.evaluation_id != null && !test.evaluation_id.equals(""))
				{
					new SurveyDataSourceImage(dataSource).GetEvaluation(test.evaluation_id);
					eval = (Evaluation )Pool.get("evaluation", test.evaluation_id);
					if(eval != null)
					{
						displayed_evaluation = eval;
//						dataSource.GetEvaluationResult(eval.getId());
//						new SurveyDataSourceImage(dataSource).GetEvaluationResult(eval.getId());
					}
				}

				for(int i = 0; i < test.result_ids.length; i++)
					if(test.result_ids[i].equals(id))
					{
						if(anal != null)
							if(anal.result_ids.length > i)
						{
							String ri = anal.result_ids[i];
							new SurveyDataSourceImage(dataSource).GetResult(ri);
							add_on_result1 = (Result )Pool.get("result", ri);
						}

						if(eval != null)
							if(eval.result_ids.length > i)
						{
							String ri = eval.result_ids[i];
							new SurveyDataSourceImage(dataSource).GetResult(ri);
							add_on_result2 = (Result )Pool.get("result", ri);
						}
					}
			}
			else
			if(rrr.result_type.equals("analysis"))
			{
				new SurveyDataSourceImage(dataSource).GetAnalysis(rrr.action_id);
				Analysis anal = (Analysis )Pool.get("analysis", rrr.action_id);
				displayed_analysis = anal;
				if(title == null)
					title = "Результат: " + rrr.getName();

				Test test = (Test )Pool.get(Test.typ, new SurveyDataSourceImage(dataSource).GetTestForAnalysis(anal.getId()));
				if(test != null)
					for(int i = 0; i < anal.result_ids.length; i++)
						if(anal.result_ids[i].equals(id))
						{
							displayed_test = test;
							if(test.result_ids.length - 1 < i)
								return;
							String ri = test.result_ids[i];
//							dataSource.GetTestResult(test.getId());
//							new SurveyDataSourceImage(dataSource).GetTestResult(test.getId());
							new SurveyDataSourceImage(dataSource).GetResult(ri);
							add_on_result1 = rrr;
							res_id = ri;
//							add_on_result1 = (Result )Pool.get("result", ri);
						}
			}
			else
			if(rrr.result_type.equals("evaluation"))
			{
				new SurveyDataSourceImage(dataSource).GetEvaluation(rrr.action_id);
				Evaluation eval = (Evaluation )Pool.get("evaluation", rrr.action_id);
				displayed_evaluation = eval;
				if(title == null)
					title = "Результат: " + rrr.getName();

				Test test = (Test )Pool.get(Test.typ, new SurveyDataSourceImage(dataSource).GetTestForEvaluation(eval.getId()));
				if(test != null)
					for(int i = 0; i < eval.result_ids.length; i++)
						if(eval.result_ids[i].equals(id))
						{
							displayed_test = test;
							if(test.result_ids.length - 1 < i)
								return;
							String ri = test.result_ids[i];
							new SurveyDataSourceImage(dataSource).GetResult(ri);
							add_on_result1 = rrr;
							res_id = ri;

							Analysis anal = null;
							if(test.analysis_id != null && !test.analysis_id.equals(""))
							{
								new SurveyDataSourceImage(dataSource).GetAnalysis(test.analysis_id);
								anal = (Analysis )Pool.get("analysis", test.analysis_id);
								if(anal != null)
									if(anal.result_ids.length > i)
									{
										displayed_analysis = anal;
										String ri2 = anal.result_ids[i];
										new SurveyDataSourceImage(dataSource).GetResult(ri2);
										add_on_result2 = (Result )Pool.get("result", ri2);
									}
							}
							break;
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

	void showResult(Result r)
	{
		tModelPar.clearTable();
		tModelRes.clearTable();
		deleteReflectogramTab();

		if (r == null)
			return;

		Enumeration parameters;
		Enumeration arguments;

		String monitored_element_id = null;
		String map_path_id = null;

	try
	{
		if (r.result_type.equals("analysis"))
		{
			Analysis a = (Analysis)Pool.get("analysis", r.action_id);
			arguments = a.arguments.elements();
			monitored_element_id = a.monitored_element_id;
		}
		else
		if (r.result_type.equals("modeling"))
		{
			Modeling m = (Modeling )Pool.get("modeling", r.action_id);
			arguments = m.arguments.elements();
//			monitored_element_id = m.path_id;
			map_path_id = m.scheme_path_id;
		}
		else
		if (r.result_type.equals("evaluation"))
		{
			Evaluation e = (Evaluation )Pool.get("evaluation", r.action_id);
			arguments = e.arguments.elements();
			monitored_element_id = e.monitored_element_id;
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
		if (r.result_type.equals("test"))
		{
			Test t = (Test )Pool.get("test", r.action_id);
			dataSource.LoadTestArgumentSets(new String[] {t.test_argument_set_id});
			TestArgumentSet tas = (TestArgumentSet )Pool.get(TestArgumentSet.typ, t.test_argument_set_id);
			tas.updateTestArgumentSet(t.test_type_id);
			arguments = tas.arguments.elements();
			monitored_element_id = t.monitored_element_id;
		}
		else
			arguments = new Vector().elements();
	}
	catch(Exception ex)
	{
			arguments = new Vector().elements();
	}


		Vector vec = new Vector();
		vec.addAll(r.parameters);

		if(add_on_result1 != null)
		{
			add_on_result1.updateLocalFromTransferable();
			vec.addAll(add_on_result1.parameters);
		}

		if(add_on_result2 != null)
		{
			add_on_result2.updateLocalFromTransferable();
			vec.addAll(add_on_result2.parameters);
		}
		parameters = vec.elements();

		while (parameters.hasMoreElements())
		{
			Parameter param = (Parameter )parameters.nextElement();
			String codename = "";
			String name = "";
			String data_type = param.gpt.value_type;
			if(param.apt != null)
			{
				codename = param.apt.codename;
				name = param.apt.name;
			}
			else
			if(param.gpt != null)
			{
				codename = param.gpt.codename;
				name = param.gpt.name;
			}

			if (codename.equals("traceevents"))
			{
				ByteArrayCollector bac = new ByteArrayCollector();
				byte[][] bevents =  bac.decode(param.value);
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
				ReflectogramAlarm[] alarms = ReflectogramAlarm.fromByteArray(param.value);
				if (map_markers_panel != null)
					map_markers_panel.updateAlarms(alarms);
			}
			if (codename.equals("dadara_event_array"))
			{
				ReflectogramEvent[] ep = ReflectogramEvent.fromByteArray(param.value);
				jbInitReflectogram(ep, displayed_test, monitored_element_id, map_path_id);
			}
			else
			if (codename.equals("concavities"))
			{}
			else
	//			if (param.code_name.equals("reflectogramm"))
			if (codename.equals("reflectogramm"))
			{
				BellcoreStructure bs = new BellcoreReader().getData(param.value);
				jbInitReflectogram(bs, displayed_test, monitored_element_id, map_path_id);
			}
			else
			{
				Object val = new String("");//param.value.toString();
				try
				{
					if(data_type.equals("string"))
					{
						val = String.valueOf(new ByteArray(param.value).toUTFString());
					}
					else
					if(data_type.equals("integer"))
					{
						val = String.valueOf(new ByteArray(param.value).toInt());
					}
					else if(data_type.equals("long"))
					{
						val = String.valueOf(new ByteArray(param.value).toLong());
					}
					else
					if(data_type.equals("double"))
					{
						val = String.valueOf(new ByteArray(param.value).toDouble());
					}
					else
					if(data_type.equals("traceeventarray"))
					{
						val = String.valueOf(new ByteArray(param.value).toUTFString());
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

		while (arguments.hasMoreElements())
		{
			Parameter arg = (Parameter)arguments.nextElement();
	//				if (arg.code_name.equals("reflectogramm"))
			String codename = "";
			String name = "";
			String data_type = arg.gpt.value_type;
			if(arg.apt != null)
			{
				codename = arg.apt.codename;
				name = arg.apt.name;
			}
			else
			if(arg.gpt != null)
			{
				codename = arg.gpt.codename;
				name = arg.gpt.name;
			}

			if (codename.equals("reflectogramm"))
			{
				BellcoreStructure bs = new BellcoreReader().getData(arg.value);
				jbInitReflectogram(bs, displayed_test, monitored_element_id, map_path_id);
			}
			else
			{
				Object val = new String("");// = arg.value.toString();
				try
				{
					if(data_type.equals("string"))
					{
						val = String.valueOf(new ByteArray(arg.value).toString());
					}
					else if(data_type.equals("integer"))
					{
						val = String.valueOf(new ByteArray(arg.value).toInt());
					}
					else if(data_type.equals("long"))
					{
						val = String.valueOf(new ByteArray(arg.value).toLong());
					}
					else if(data_type.equals("double"))
					{
						val = String.valueOf(new ByteArray(arg.value).toDouble());
					}
					else if(data_type.equals("tracechangescoordinatesarray"))
					{
						val = String.valueOf(new ByteArray(arg.value).toString());
					}
				}
				catch(Exception ex)
				{
				}
				Vector vect = new Vector();
				vect.add(name);
				vect.add(val);
				tModelPar.insertRow(vect);
			}
		}

		if(displayed_test != null)
		{
			Vector vect = new Vector();
			vect.add("Шаблон исследования");
			vect.add(Pool.getName(TestSetup.typ, displayed_test.test_setup_id));
			tModelPar.insertRow(vect);
		}


		paramTable.updateUI();
		resultTable.updateUI();
	}

	private void jbInitReflectogram(ReflectogramEvent[] ep, Test test, String monitored_element_id, String map_path_id)
	{
		if (ep == null || test == null)
			return;

		TestArgumentSet metas = (TestArgumentSet)Pool.get(TestArgumentSet.typ, test.test_argument_set_id);
		if (metas == null)
			return;

		int len = 0;
		double delta_x = 0;

		try
		{
			for (int i = 0; i <metas.arguments.size(); i++)
			{
				Parameter p = (Parameter)metas.arguments.get(i);
				if (p.codename.equals("ref_res"))
				{
					delta_x = new ByteArray(p.value).toDouble();
				}
				if (p.codename.equals("ref_trclen"))
				{
					len = (int)new ByteArray(p.value).toDouble();
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
				dataSource.loadTestSetup(test.test_setup_id);
				TestSetup ts = (TestSetup)Pool.get(TestSetup.typ, test.test_setup_id);
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
			dataSource.loadTestSetup(test.test_setup_id);
			TestSetup ts = (TestSetup)Pool.get(TestSetup.typ, test.test_setup_id);
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
					DataSet data = ev.getDataSet();
					ObjectResource resource = data.get(ev.getSelectionNumber());

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

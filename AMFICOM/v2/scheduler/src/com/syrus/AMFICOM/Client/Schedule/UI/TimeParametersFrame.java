package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.util.HashMap;

import javax.swing.*;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.TestType;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.util.*;

public class TimeParametersFrame
	extends JInternalFrame
	implements OperationListener {

	public static final String COMMAND_CREATE_TEST = "CreateTest";
	public static final String COMMAND_APPLY_TEST = "ApplyTest";
	public static final String COMMAND_DATA_REQUEST = "DataRequest";
	public static final String COMMAND_SEND_DATA = "SendData";

	private ApplicationContext aContext;
	private Dispatcher dispatcher;
	private TimeParametersPanel panel;

	private int receiveDataCount = 0;

	private HashMap new_parameters = new HashMap();

	private static final int FLAG_CREATE = 1 << 0;
	private static final int FLAG_APPLY = 1 << 1;
	private int flag = 0;

	public TimeParametersFrame(ApplicationContext aContext) {
		this.aContext = aContext;
		initModule(aContext.getDispatcher());
		setTitle("Временные параметры");
		setFrameIcon(
			new ImageIcon(
				Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		panel = new TimeParametersPanel(aContext);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.typ);
		this.dispatcher.register(this, COMMAND_CREATE_TEST);
		this.dispatcher.register(this, COMMAND_APPLY_TEST);
		this.dispatcher.register(this, COMMAND_SEND_DATA);
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		System.out.println(getClass().getName() +" commandName: " + commandName);
		if (commandName.equals(TestUpdateEvent.typ)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			Test test = tue.test;
			if (tue.TEST_SELECTED) {
				TestRequest treq =
					(TestRequest) Pool.get(TestRequest.typ, test.request_id);
				if (treq != null)
					panel.setTestRequest(treq);
			} else {}
		} else if (commandName.equalsIgnoreCase(COMMAND_CREATE_TEST)) {
			// creating test
			if (flag == 0) {
				flag = FLAG_CREATE;
				receiveDataCount = 0;
				dispatcher.notify(
					new OperationEvent(
						"",
						0,
						TimeParametersFrame.COMMAND_DATA_REQUEST));
			}
		} else if (commandName.equalsIgnoreCase(COMMAND_APPLY_TEST)) {
			// apply test
			if (flag == 0) {
				flag = FLAG_APPLY;
				receiveDataCount = 0;
				dispatcher.notify(
					new OperationEvent(
						"",
						0,
						TimeParametersFrame.COMMAND_DATA_REQUEST));
			}
		} else if (commandName.equalsIgnoreCase(COMMAND_SEND_DATA)) {
			receiveDataCount++;
			System.out.println("receiveDataCount:" + receiveDataCount);
			if (4 == receiveDataCount) {
				if ((flag & FLAG_CREATE) != 0) {
					System.out.println("createTest");
				} else if ((flag & FLAG_APPLY) != 0) {
					System.out.println("applyTest");
				}
				flag = 0;
			}
		}
	}

	private void createTest(String kisId, String portId, String meId) {
		Test temp_test = new Test(""); //текущий тест
		//TestTemporalType ttt = new TestTemporalType("");         //временной тип тестирования		
		//размер кнопок на тулбаре
		String test_type_id = ""; //id текущего типа теста
		String kis_id = ""; //id текущего КИСа
		String port_id = ""; //id текущего порта
		String me_id = ""; //id текущего MonitoredElement
		String test_setup_id = ""; //id текущего TestSetup
		String analysis_type_id = ""; //id текущего типа анализа
		String evaluation_type_id = ""; //id текущего типа оценки

		DataSourceInterface dsi = aContext.getDataSourceInterface();
		temp_test.test_setup_id = test_setup_id;
		TestArgumentSet as = new TestArgumentSet();
		if (!test_setup_id.equals("")) {
			TestSetup ts = (TestSetup) Pool.get(TestSetup.typ, test_setup_id);
			temp_test.test_argument_set_id = ts.test_argument_set_id;
			as =
				(TestArgumentSet) Pool.get(
					TestArgumentSet.typ,
					ts.test_argument_set_id);
			if (!analysis_type_id.equals("")) {
				Analysis anal = new Analysis(dsi.GetUId(Analysis.typ));
				anal.monitored_element_id = meId;
				anal.type_id = analysis_type_id;
				anal.criteria_set_id = ts.criteria_set_id;
				temp_test.analysis_id = anal.getId();
				Pool.put(Analysis.typ, anal.getId(), anal);
			} else {
				temp_test.analysis_id = "";
			}

			if (!evaluation_type_id.equals("")) {
				Evaluation eval = new Evaluation(dsi.GetUId(Evaluation.typ));
				eval.monitored_element_id = meId;
				eval.type_id = evaluation_type_id;
				eval.threshold_set_id = ts.threshold_set_id;
				eval.etalon_id = ts.etalon_id;
				temp_test.evaluation_id = eval.getId();
				Pool.put(Evaluation.typ, eval.getId(), eval);
			} else {
				temp_test.evaluation_id = "";
			}
		}
		temp_test.kis_id = kisId;
		temp_test.monitored_element_id = meId;
		temp_test.test_type_id = test_type_id;
		//temp_test.temporal_type = ttt;
		String aa = temp_test.temporal_type.toString();
		temp_test.status = TestStatus.TEST_STATUS_SCHEDULED;

		//---------------------------------------------------------------------
		/*
		 temp_test.start_time = start_time;
		temp_test.return_type = returntype;
		TestTimeStamps tts = new TestTimeStamps();
		switch (temp_test.temporal_type.value()) {
			case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME :
				tts._default();
				break;
			case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL :
				int temp_ti = (int) ((end_time - start_time) / interval);
				PeriodicalTestParameters ptp =
					new PeriodicalTestParameters(
						interval,
						start_time + temp_ti * interval);
				tts.ptpars(ptp);
				break;
			case TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE :
				long[] times = new long[temp_times.size()];
				for (int i = 0; i < temp_times.size(); i++)
					times[i] =
						Long.parseLong(temp_times.elementAt(i).toString());
				tts.ti(times);
				break;
			default :
				System.out.println(
					"ERROR: Unknown temporal type: "
						+ temp_test.temporal_type.value());
		}
		temp_test.time_stamps = tts;
		*/

		if (test_setup_id.equals("")) {
			as.id = dsi.GetUId(TestArgumentSet.typ);
			Pool.put(TestArgumentSet.typ, as.getId(), as);
			as.name = as.id;
			as.created = 0;
			as.created_by = "";
			as.test_type_id = test_type_id;

			Parameter par_trans;
			if (temp_test.test_type_id.equals("trace_and_analyse")) {
				ByteArray ref_ior;
				ByteArray ref_wvlen;
				ByteArray ref_scans;
				ByteArray ref_trclen;
				ByteArray ref_res;
				ByteArray ref_pulswd;
				ActionParameterType apt;
				TestType tttt =
					(TestType) Pool.get("testtype", temp_test.test_type_id);
				try {
					apt =
						(ActionParameterType) tttt.sorted_arguments.get(
							TestParametersPanel.PARAMETER_REFLECTION);
					ref_ior =
						new ByteArray(
							Double.parseDouble(
								new_parameters
									.get(
										TestParametersPanel
											.PARAMETER_REFLECTION)
									.toString()));
					par_trans =
						new Parameter(
							dsi.GetUId("testargument"),
							apt.getId(),
							ref_ior.getBytes(),
							TestParametersPanel.PARAMETER_REFLECTION,
							"double");
					as.addArgument(par_trans);

					apt =
						(ActionParameterType) tttt.sorted_arguments.get(
							TestParametersPanel.PARAMETER_WAVELENGHT);
					ref_wvlen =
						new ByteArray(
							Integer.parseInt(
								new_parameters
									.get(
										TestParametersPanel
											.PARAMETER_WAVELENGHT)
									.toString()));
					par_trans =
						new Parameter(
							dsi.GetUId("testargument"),
							apt.getId(),
							ref_wvlen.getBytes(),
							TestParametersPanel.PARAMETER_WAVELENGHT,
							"int");
					as.addArgument(par_trans);

					apt =
						(ActionParameterType) tttt.sorted_arguments.get(
							TestParametersPanel.PARAMETER_AVERAGEOUT_COUNT);
					ref_scans =
						new ByteArray(
							Double.parseDouble(
								new_parameters
									.get(
										TestParametersPanel
											.PARAMETER_AVERAGEOUT_COUNT)
									.toString()));
					par_trans =
						new Parameter(
							dsi.GetUId("testargument"),
							apt.getId(),
							ref_scans.getBytes(),
							TestParametersPanel.PARAMETER_AVERAGEOUT_COUNT,
							"double");
					as.addArgument(par_trans);

					apt =
						(ActionParameterType) tttt.sorted_arguments.get(
							TestParametersPanel.PARAMETER_PULSE_WIDTH);
					ref_trclen =
						new ByteArray(
							Double.parseDouble(
								new_parameters
									.get(
										TestParametersPanel
											.PARAMETER_PULSE_WIDTH)
									.toString()));
					par_trans =
						new Parameter(
							dsi.GetUId("testargument"),
							apt.getId(),
							ref_trclen.getBytes(),
							TestParametersPanel.PARAMETER_PULSE_WIDTH,
							"double");
					as.addArgument(par_trans);

					apt =
						(ActionParameterType) tttt.sorted_arguments.get(
							TestParametersPanel.PARAMETER_RESOLUTION);
					ref_res =
						new ByteArray(
							Double.parseDouble(
								new_parameters
									.get(
										TestParametersPanel
											.PARAMETER_RESOLUTION)
									.toString()));
					par_trans =
						new Parameter(
							dsi.GetUId("testargument"),
							apt.getId(),
							ref_res.getBytes(),
							TestParametersPanel.PARAMETER_RESOLUTION,
							"double");
					as.addArgument(par_trans);

					apt =
						(ActionParameterType) tttt.sorted_arguments.get(
							TestParametersPanel.PARAMETER_MAX_DISTANCE);
					ref_pulswd =
						new ByteArray(
							Long.parseLong(
								new_parameters
									.get(
										TestParametersPanel
											.PARAMETER_MAX_DISTANCE)
									.toString()));
					par_trans =
						new Parameter(
							dsi.GetUId("testargument"),
							apt.getId(),
							ref_pulswd.getBytes(),
							TestParametersPanel.PARAMETER_MAX_DISTANCE,
							"double");
					as.addArgument(par_trans);
				} catch (java.io.IOException ex) {}
			} else if (temp_test.test_type_id.equals("voice_analyse")) {
				ByteArray ref_characterizationidentity;
				ActionParameterType apt;
				TestType tttt =
					(TestType) Pool.get("testtype", temp_test.test_type_id);
				try {
					apt =
						(ActionParameterType) tttt.sorted_arguments.get(
							TestParametersPanel.PARAMETER_CHAR_IDENTITY);
					ref_characterizationidentity =
						new ByteArray(
							new_parameters
								.get(
									TestParametersPanel
										.PARAMETER_CHAR_IDENTITY)
								.toString());
					par_trans =
						new Parameter(
							dsi.GetUId("testargument"),
							apt.getId(),
							ref_characterizationidentity.getBytes(),
							TestParametersPanel.PARAMETER_CHAR_IDENTITY,
							"string");
					as.addArgument(par_trans);
				} catch (java.io.IOException ex) {}
			}
			dsi.saveTestArgumentSet(as.getId());
			temp_test.test_argument_set_id = as.getId();
		}

		//	 Длительность теста
		if (temp_test.test_type_id.equals("trace_and_analyse")) {
			java.util.Vector arg = as.arguments;
			double param1 = 0, param2 = 0, param3 = 0;

			try {
				for (int i = 0; i < arg.size(); i++) {
					Parameter par = (Parameter) arg.elementAt(i);
					if (par.codename.equals("ref_ior")) {
						param1 = (new ByteArray(par.value)).toDouble();
					} else if (par.codename.equals("ref_trclen")) {
						param2 = (new ByteArray(par.value)).toDouble();
					} else if (par.codename.equals("ref_scans")) {
						param3 = (new ByteArray(par.value)).toDouble();
					}
					//else if (par.codename.equals("ref_wvlen"))
					//else if (par.codename.equals("ref_trclen"))
					//else if (par.codename.equals("ref_res"))
				}
			} catch (java.io.IOException ex) {}

			long t_warm = 0;
			long t_cool = 0;
			/*
			AccessPort ap = (AccessPort) Pool.get(AccessPort.typ, portid);
			Hashtable ht = ap.characteristics;
			if (ht.size() != 0) {
				t_warm =
					Long.parseLong(
						((Characteristic) ht.get("warm_up_time")).value);
				t_cool =
					Long.parseLong(
						((Characteristic) ht.get("cool_down_time")).value);
			}
			
			double wave_speed = 300000000 / param1;
			double izmer_time = (param2 * 1000) / wave_speed;
			long t_work = (long) (izmer_time * param3 * 1000);
			
			long all_time = t_sys + t_warm + t_cool + t_work;
			temp_test.duration = all_time;
			*/
		}
		// Окончание счета длительности теста

		//Test clone_test = temp_test.myclone();
		//baza_test_new.add(clone_test);
	}

}

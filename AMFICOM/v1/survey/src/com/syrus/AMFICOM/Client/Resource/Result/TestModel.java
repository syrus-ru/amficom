package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.CORBA.General.TestTemporalType;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Test.TestType;

import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

import java.awt.Component;
import java.util.Date;
import java.util.Enumeration;

public class TestModel extends ObjectResourceModel {

	protected Test	test;

	public TestModel(Test test) {
		this.test = test;
	}

	public String getColumnValue(String colId) {
		String s = null;
		try {
			if (colId.equals(ConstStorage.COLUMN_NAME_STATUS)) {
				switch (test.status.value()) {
					case TestStatus._TEST_STATUS_PROCESSING:
						s = LangModelSurvey.getString("labelDoing");
						break;
					case TestStatus._TEST_STATUS_COMPLETED:
						s = LangModelSurvey.getString("labelDone");
						break;
					case TestStatus._TEST_STATUS_SCHEDULED:
						s = LangModelSurvey.getString("labelReadyToDo");
						break;
				}
			} else if (colId.equals(ConstStorage.COLUMN_NAME_LOCAL_ID)) {
				TransmissionPath path;
				for (Enumeration e = Pool.getHash(TransmissionPath.typ)
						.elements(); e.hasMoreElements();) {
					path = (TransmissionPath) e.nextElement();
					if (path.monitored_element_id
							.equals(test.monitored_element_id)) {
						s = path.getName();
						break;
					}
				}
				//				s = test.local_id;
			} else if (colId.equals(ConstStorage.COLUMN_NAME_PORT_ID)) {
				TransmissionPath path;
				for (Enumeration e = Pool.getHash(TransmissionPath.typ)
						.elements(); e.hasMoreElements();) {
					path = (TransmissionPath) e.nextElement();
					if (path.monitored_element_id
							.equals(test.monitored_element_id)) {
						s = Pool.getName(AccessPort.typ, path.access_port_id);
						break;
					}
				}
				//				s = test.local_id;
			} else if (colId.equals(ConstStorage.COLUMN_NAME_KIS_ID))
				s = Pool.getName(KIS.typ, test.kis_id);
			else if (colId.equals(ConstStorage.COLUMN_NAME_START_TIME))
				s = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(
						test.start_time));
			else if (colId.equals(ConstStorage.COLUMN_NAME_TEMPORAL_TYPE)) {
				switch (test.temporal_type.value()) {
					case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
						s = LangModelSurvey.getString("labelOnetime");
						break;
					case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
						s = LangModelSurvey.getString("labelPeriod");
						break;
					case TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE:
						s = LangModelSurvey.getString("labelTimeTable");
						break;
				}
			} else if (colId.equals(ConstStorage.COLUMN_NAME_TEST_TYPE_ID))
				s = Pool.getName(TestType.typ, test.test_type_id);
			else if (colId.equals(ConstStorage.COLUMN_NAME_REQUEST_ID))
					s = Pool.getName(TestRequest.typ, test.request_id);
		} catch (Exception e) {
			System.out.println("error gettin field value - Test");
			s = "";
		}
		return s;
	}

	public Component getColumnRenderer(String colId) {
		if (colId.equals(ConstStorage.COLUMN_NAME_STATUS))
				return new TextFieldEditor(
						getColumnValue(ConstStorage.COLUMN_NAME_STATUS));
		if (colId.equals(ConstStorage.COLUMN_NAME_KIS_ID))
				return new TextFieldEditor(test.kis_id);
		if (colId.equals(ConstStorage.COLUMN_NAME_START_TIME))
				return new TextFieldEditor(ConstStorage.SIMPLE_DATE_FORMAT
						.format(new Date(test.start_time)));
		if (colId.equals(ConstStorage.COLUMN_NAME_TEMPORAL_TYPE))
				return new TextFieldEditor(
						getColumnValue(ConstStorage.COLUMN_NAME_TEMPORAL_TYPE));
		if (colId.equals(ConstStorage.COLUMN_NAME_TEST_TYPE_ID))
				return new ObjectResourceComboBox(TestType.typ,
						test.test_type_id);
		if (colId.equals(ConstStorage.COLUMN_NAME_REQUEST_ID))
				return new ObjectResourceComboBox(TestRequest.typ,
						test.request_id);
		return null;
	}

	public Component getColumnEditor(String colId) {
		return getColumnRenderer(colId);
	}	
}
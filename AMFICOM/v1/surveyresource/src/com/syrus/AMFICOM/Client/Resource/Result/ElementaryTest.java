package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.CORBA.General.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ElementaryTest extends StubResource {
	public static final String TYPE = "ElementaryTest";
	/**
	 * @deprecated use TYPE
	 */
	public static final String typ = TYPE;
	public ClientTest_Transferable transferable;

	public Test test = new Test("");
	public long et_time = 0;
	public TestStatus status = null;
	public int count = 0;

	public String id;

	public ElementaryTest(Test test, long et_time) {
		this.test = test;
		this.et_time = et_time;
		this.id = test.getId() + String.valueOf(et_time);
		this.status = test.getStatus();
		if (this.status.equals(TestStatus.TEST_STATUS_PROCESSING))
		{
			String[] res_ids = test.getResultIds();
			for (int i = 0; i < res_ids.length; i++)
			{
				Result res = (Result )Pool.get(Result.TYPE, res_ids[i]);
				if ( res != null && (et_time == res.getElementaryStartTime()) )
				{
					this.status = TestStatus.TEST_STATUS_COMPLETED;
					break;
				}
			}
		}
	}

	/*public boolean isColumnEditable(String col_id)
	{
		if(col_id.equals("status"))
			return false;
		if(col_id.equals("local_id"))
			return false;
		if(col_id.equals("kis_id"))
			return false;
		if(col_id.equals("start_time"))
			return false;
		if(col_id.equals("temporal_type"))
			return false;
		if(col_id.equals("test_type_id"))
			return false;
		if(col_id.equals("request_id"))
			return false;
		return false;
	}

	public Component getColumnRenderer(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

		if(col_id.equals("status"))
			return new TextFieldEditor(getColumnValue("status"));
		if(col_id.equals("local_id"))
			return new TextFieldEditor(test.local_id);
		if(col_id.equals("kis_id"))
			return new TextFieldEditor(test.kis_id);
		if(col_id.equals("start_time"))
			return new TextFieldEditor(sdf.format(new Date(et_time)));
		if(col_id.equals("temporal_type"))
			return new TextFieldEditor(getColumnValue("temporal_type"));
		if(col_id.equals("test_type_id"))
			return new ObjectResourceComboBox("testtype", test.test_type_id);
		if(col_id.equals("request_id"))
			return new ObjectResourceComboBox("testrequest", test.request_id);
		return null;
	}

	public String getColumnName (String col_id)
	{
		String s = "";
		if(col_id.equals("status"))
			s = "Статус";
		if(col_id.equals("local_id"))
			s = "Волокно";
		if(col_id.equals("kis_id"))
			s = "КИС";
		if(col_id.equals("start_time"))
			s = "Время первого теста";
		if(col_id.equals("temporal_type"))
			s = "Временной тип";
		if(col_id.equals("test_type_id"))
			s = "Тип измерений";
		if(col_id.equals("request_id"))
			s = "Запрос";
		return s;
	}

	public Component getColumnEditor(String col_id)
	{
		return getColumnRenderer(col_id);
	}*/

	public ObjectResourceModel getModel()
	{
		return new ElementaryTestModel(this);
	}
}


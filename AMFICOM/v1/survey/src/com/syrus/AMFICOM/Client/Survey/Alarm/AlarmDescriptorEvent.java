package com.syrus.AMFICOM.Client.Survey.Alarm;

import java.awt.Color;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;

public class AlarmDescriptorEvent extends StubResource
{
	public static final int INFO = 0;
	public static final int DIAGNOSTICS = 1;
	public static final int WARNING = 2;
	public static final int ALARM = 3;
	public static final int ERROR = 4;
	public static final int CRITICAL = 5;

	public String id;
	public String title;
	public String text;
	public String me_id = "";
	public int severity = 0;

	TransmissionPathDecompositor tpd = null;
	
	public AlarmDescriptorEvent(String me_id, String title, String text)
	{
		this.id = "ade" + String.valueOf(System.currentTimeMillis());
		this.title = title;
		this.text = text;
		this.setME(me_id);
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return title;
	}

	public int GetSeverity()
	{
		return severity;
	}

	public Color getColor()
	{
		switch(severity)
		{
			case INFO:
				return Color.white;
			case DIAGNOSTICS:
				return Color.lightGray;
			case WARNING:
				return Color.orange;
			case ALARM:
				return Color.red;
			case ERROR:
				return Color.cyan;
			case CRITICAL:
				return Color.pink;
		}
		return Color.white;
	}

	public Color getTextColor()
	{
		switch(severity)
		{
			case INFO:
				return Color.black;
			case DIAGNOSTICS:
				return Color.black;
			case WARNING:
				return Color.black;
			case ALARM:
				return Color.white;
			case ERROR:
				return Color.black;
			case CRITICAL:
				return Color.white;
		}
		return Color.black;
	}

	public void showAlarmMessage(int x, int y)
	{
		MessageBox mb = new MessageBox(this.text, x, y);
		mb.setVisible(true);
	}

//----------------------------------
	public void showAlarmMessage()
	{
		MessageBox mb = new MessageBox(this.text);
		mb.setVisible(true);
	}

	void setME(String me_id)
	{
		this.me_id = me_id;
		MonitoredElement me = (MonitoredElement )Pool.get(MonitoredElement.typ, me_id);
		if(!me.element_type.equals("path"))
			return;
		TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, me.element_id);
		if(tp == null)
			return;
		tpd = new TransmissionPathDecompositor(tp);
	}

	public TransmissionPathDecompositor getPathDecompositor()
	{
		return tpd;
	}
}


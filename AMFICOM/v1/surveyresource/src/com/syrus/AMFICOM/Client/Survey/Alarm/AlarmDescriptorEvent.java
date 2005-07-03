package com.syrus.AMFICOM.Client.Survey.Alarm;

import java.util.*;
import java.util.List;

import java.awt.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;

public class AlarmDescriptorEvent extends StubResource {

	public static final int INFO = 0;
	public static final int DIAGNOSTICS = 1;
	public static final int WARNING = 2;
	public static final int ALARM = 3;
	public static final int ERROR = 4;
	public static final int CRITICAL = 5;

	protected String id;
	protected String title;
	protected String text;
	protected Identifier meId;
	protected int severity = 0;

	PathDecompositor decompositor = null;

	public AlarmDescriptorEvent(Identifier meId, String title, String text) {
		this.id = "ade" + String.valueOf(System.currentTimeMillis());
		this.title = title;
		this.text = text;

		setME(meId);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return title;
	}

	public int getSeverity() {
		return severity;
	}

	public Color getColor() {
		switch (severity) {
			case INFO:
				return Color.WHITE;
			case DIAGNOSTICS:
				return Color.LIGHT_GRAY;
			case WARNING:
				return Color.ORANGE;
			case ALARM:
				return Color.RED;
			case ERROR:
				return Color.CYAN;
			case CRITICAL:
				return Color.PINK;
		}
		return Color.WHITE;
	}

	public Color getTextColor() {
		switch (severity) {
			case INFO:
				return Color.BLACK;
			case DIAGNOSTICS:
				return Color.BLACK;
			case WARNING:
				return Color.BLACK;
			case ALARM:
				return Color.WHITE;
			case ERROR:
				return Color.BLACK;
			case CRITICAL:
				return Color.WHITE;
		}
		return Color.BLACK;
	}

	public void showAlarmMessage(int x, int y) {
		MessageBox mb = new MessageBox(this.text, x, y);
		mb.setVisible(true);
	}

	public void showAlarmMessage() {
		MessageBox mb = new MessageBox(this.text);
		mb.setVisible(true);
	}

	void setME(Identifier meId) {
		try {
			this.meId = meId;
			MonitoredElement me = (MonitoredElement) MeasurementStorableObjectPool.
					getStorableObject(meId, true);
			if (!me.getSort().equals(MonitoredElementSort.
															 MONITOREDELEMENT_SORT_TRANSMISSION_PATH)) {
				return;
			}

			List ids = me.getMonitoredDomainMemberIds();
			if (ids.size() == 0) {
				return;
			}

			TransmissionPath tp = (TransmissionPath) MeasurementStorableObjectPool.
					getStorableObject((Identifier)ids.get(0), true);
			Map paths = Pool.getMap(SchemePath.typ);
			for (Iterator it = paths.values().iterator(); it.hasNext(); ) {
				SchemePath path = (SchemePath) it.next();
				/**
				 * @todo remove comment when SchemePath moves to new TransmissionPath
				 */

//				if (path.path.equals(tp))
				{
					decompositor = new PathDecompositor(path);
					break;
				}
			}
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}

	public PathDecompositor getPathDecompositor() {
		return decompositor;
	}

	public String getText() {
		return text;
	}
}

/*
 * $Id: AlarmFilter.java,v 1.1.2.4 2004/09/14 12:40:34 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.object;

import com.syrus.AMFICOM.CORBA.General.AlarmStatus;
import com.syrus.AMFICOM.filter.*;
import com.syrus.AMFICOM.server.event.*;
import com.syrus.AMFICOM.server.measurement.*;
import java.sql.*;
import java.util.*;

/**
 * @version $Revision: 1.1.2.4 $, $Date: 2004/09/14 12:40:34 $
 * @author $Author: bass $
 * @module server_v1
 */
final class AlarmFilter implements Filter {
	public boolean expression(FilterExpressionInterface filterExpressionInterface, Object obj) {
		try {
			boolean result = false;

			FilterExpressionBase filterExpressionBase = (FilterExpressionBase) filterExpressionInterface;
			String filterExpressionBaseId = filterExpressionBase.getId();
			List list = filterExpressionBase.getVec();
			String type = (String) list.get(0);

			Alarm alarm = (Alarm) obj;
			long alarmGeneratedTime = alarm.getGenerated().getTime();

			if (type.equals("numeric")) {
				if (filterExpressionBaseId.equals("time")) {
					String operator = (String) list.get(1);
					long operand = Long.parseLong((String) (list.get(2)));
					if ((operator.equals("=") && (alarmGeneratedTime == operand))
						|| (operator.equals(">") && (alarmGeneratedTime > operand))
						|| (operator.equals("<") && (alarmGeneratedTime < operand)))
							result = true;
				}
			} else if (type.equals("time") || type.equals("range")) {
				if (filterExpressionBaseId.equals("time")
						&& (alarmGeneratedTime > Long.parseLong((String)list.get(1)))
						&& (alarmGeneratedTime < Long.parseLong((String)list.get(2))))
					result = true;
			} else if (type.equals("string")) {
				String substring = (String)list.get(1);
				String eventId = alarm.getEventId();
				if (filterExpressionBaseId.equals("source"))
					return ((new EventSource((new Event(eventId)).getSourceId())).getTransferable().object_source_name.indexOf(substring) != -1);
				else if (filterExpressionBaseId.equals("monitoredelement")) {
					/**
					 * @todo A kindda tricky block of code.
					 *       Do I actually need a new
					 *       instance of MonitoredElement
					 *       in order to evaluate a boolean
					 *       expression?!
					 */
					Connection conn = null;
					try {
						conn = AmficomImpl.DATA_SOURCE.getConnection();
						conn.setAutoCommit(false);
						return ((new MonitoredElement(conn, Test.retrieveTestForEvaluation(((Evaluation) (new Result((new Event(eventId)).getDescriptor())).getAction()).getId()).getTransferable().monitored_element_id)).getName().indexOf(substring) != -1);
					} finally {
						if (conn != null)
							conn.close();
					}
				}
			} else if (type.equals("list")) {
				Hashtable tree = (Hashtable )list.get(1);
				if (filterExpressionBaseId.equals("monitoredelement")) {
					FilterTreeNodeHolder mmtn = (FilterTreeNodeHolder) tree.get("root");
					if (mmtn.state == 2) {
						result = true;
					} else if (mmtn.state == 1) {
						for(int i = 0; i < mmtn.children_ids.length; i++) {
							FilterTreeNodeHolder down_mte = (FilterTreeNodeHolder )tree.get(mmtn.children_ids[i]);
							for(int j = 0; j < down_mte.children_ids.length; j++) {
								FilterTreeNodeHolder down_mte2 = (FilterTreeNodeHolder) tree.get(down_mte.children_ids[j]);
								
								/**
								 * @todo See comment above.
								 */
								Connection conn = null;
								try {
									conn = AmficomImpl.DATA_SOURCE.getConnection();
									conn.setAutoCommit(false);
									if ((new MonitoredElement(conn, Test.retrieveTestForEvaluation(((Evaluation) ((new Result((new Event(alarm.getEventId())).getDescriptor())).getAction())).getId()).getTransferable().monitored_element_id)).getId().equals(down_mte2.id) && (down_mte2.state == 2))
										result = true;
								} finally {
									if (conn != null)
										conn.close();
								}
							}
						}
					}
				} else if (filterExpressionBaseId.equals("source")) {
					Event ev = new Event(alarm.getEventId());
					FilterTreeNodeHolder mmtn = (FilterTreeNodeHolder) tree.get("root");
					if (mmtn.state == 2) {
						result = true;
					} else if (mmtn.state == 1) {
						for(int i = 0; i < mmtn.children_ids.length; i++) {
							FilterTreeNodeHolder down_mte = (FilterTreeNodeHolder )tree.get(mmtn.children_ids[i]);
							if (ev.getSourceId().equals(down_mte.id) && (down_mte.state == 2))
								result = true;
						}
					}
				} else if (filterExpressionBaseId.equals("type")) {
					FilterTreeNodeHolder mmtn = (FilterTreeNodeHolder )tree.get("root");
					if (mmtn.state == 2) {
						result = true;
					} else if (mmtn.state == 1) {
						for (int i = 0; i < mmtn.children_ids.length; i++) {
							FilterTreeNodeHolder down_mte = (FilterTreeNodeHolder )tree.get(mmtn.children_ids[i]);
							if (alarm.getTypeId().equals(down_mte.id) && (down_mte.state == 2))
								result = true;
						}
					}
				} else if (filterExpressionBaseId.equals("status")) {
					FilterTreeNodeHolder mmtn = (FilterTreeNodeHolder )tree.get("root");
					if (mmtn.state == 2) {
						result = true;
					} else if (mmtn.state == 1) {
						for (int i = 0; i < mmtn.children_ids.length; i++) {
							FilterTreeNodeHolder down_mte = (FilterTreeNodeHolder )tree.get(mmtn.children_ids[i]);
							String stat = "";
							if (alarm.getStatus() == AlarmStatus._ALARM_STATUS_GENERATED)
								stat = "GENERATED";
							else if (alarm.getStatus() == AlarmStatus._ALARM_STATUS_ASSIGNED)
								stat = "ASSIGNED";
							else if (alarm.getStatus() == AlarmStatus._ALARM_STATUS_FIXED)
								stat = "FIXED";
							if (down_mte.id.equals(stat) && (down_mte.state == 2))
								result = true;
						}
					}
				}
			}
			return result;
		} catch (SQLException sqle) {
			return false;
		}
	}
}

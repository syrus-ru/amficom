/*
 * $Id: AlarmFilter.java,v 1.1.2.1 2004/08/20 17:12:08 bass Exp $
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
import java.sql.SQLException;
import java.util.*;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2004/08/20 17:12:08 $
 * @author $Author: bass $
 * @module server_v1
 */
final class AlarmFilter implements Filter {
	public boolean expression(FilterExpressionInterface filterExpressionInterface, Object obj) {
		try {
			FilterExpressionBase expr = (FilterExpressionBase) filterExpressionInterface;
			boolean result = false;
			Alarm a = (Alarm) obj;
			Vector vec = expr.getVec();
			String type = (String) vec.elementAt(0);
			if (type.equals("numeric")) {
				if (expr.getId().equals("time")) {
					if (((String)vec.elementAt(1)).equals("=")) {
						if (a.getGenerated().getTime() == Long.parseLong((String)vec.elementAt(2)))
							result = true;
					} else if (((String)vec.elementAt(1)).equals(">")) {
						if (a.getGenerated().getTime() > Long.parseLong((String)vec.elementAt(2)))
							result = true;
					} else if (((String)vec.elementAt(1)).equals("<")) {
						if (a.getGenerated().getTime() < Long.parseLong((String)vec.elementAt(2)))
							result = true;
					}
				}
			} else if (type.equals("time")) {
				if (expr.getId().equals("time")) {
					if ( a.getGenerated().getTime() > Long.parseLong((String)vec.elementAt(1)) &&  a.getGenerated().getTime() < Long.parseLong((String)vec.elementAt(2))) {
						result = true;
					}
				}
			} else if (type.equals("range")) {
				if (expr.getId().equals("time")) {
					if ( a.getGenerated().getTime() > Long.parseLong((String)vec.elementAt(1)) &&  a.getGenerated().getTime() < Long.parseLong((String)vec.elementAt(2)))
						result = true;
				}
			} else if (type.equals("string")) {
				String substring = (String)vec.elementAt(1);
				if (expr.getId().equals("source"))
					return ((new EventSource((new Event(a.getEventId())).getSourceId())).getTransferable().object_source_name.indexOf(substring) != -1);
				else if (expr.getId().equals("monitoredelement"))
					return ((new MonitoredElement(Test.retrieveTestForEvaluation(((Evaluation) (new Result((new Event(a.getEventId())).getDescriptor())).getAction()).getId()).getTransferable().monitored_element_id)).getName().indexOf(substring) != -1);
			} else if (type.equals("list")) {
				Hashtable tree = (Hashtable )vec.elementAt(1);
				if (expr.getId().equals("monitoredelement")) {
					Event ev = new Event(a.getEventId());
					Result res = new Result(ev.getDescriptor());
					Evaluation eval = (Evaluation )res.getAction();
					Test test = Test.retrieveTestForEvaluation(eval.getId());
					MonitoredElement me = new MonitoredElement(test.getTransferable().monitored_element_id);
					FilterTreeNodeHolder mmtn = (FilterTreeNodeHolder )tree.get("root");
					if (mmtn.state == 2) {
						result = true;
					} else if (mmtn.state == 1) {
						for(int i = 0; i < mmtn.children_ids.length; i++) {
							FilterTreeNodeHolder down_mte = (FilterTreeNodeHolder )tree.get(mmtn.children_ids[i]);
							for(int j = 0; j < down_mte.children_ids.length; j++) {
								FilterTreeNodeHolder down_mte2 = (FilterTreeNodeHolder )tree.get(down_mte.children_ids[j]);
								if (me.getId().equals(down_mte2.id) && (down_mte2.state == 2))
									result = true;
							}
						}
					}
				}
				if (expr.getId().equals("source")) {
					Event ev = new Event(a.getEventId());
					FilterTreeNodeHolder mmtn = (FilterTreeNodeHolder )tree.get("root");
					if (mmtn.state == 2) {
						result = true;
					} else if (mmtn.state == 1) {
						for(int i = 0; i < mmtn.children_ids.length; i++) {
							FilterTreeNodeHolder down_mte = (FilterTreeNodeHolder )tree.get(mmtn.children_ids[i]);
							if (ev.getSourceId().equals(down_mte.id) && (down_mte.state == 2))
								result = true;
						}
					}
				}
				if (expr.getId().equals("type")) {
					FilterTreeNodeHolder mmtn = (FilterTreeNodeHolder )tree.get("root");
					if (mmtn.state == 2) {
						result = true;
					} else if (mmtn.state == 1) {
						for(int i = 0; i < mmtn.children_ids.length; i++) {
							FilterTreeNodeHolder down_mte = (FilterTreeNodeHolder )tree.get(mmtn.children_ids[i]);
							if (a.getTypeId().equals(down_mte.id) && (down_mte.state == 2))
								result = true;
						}
					}
				}
				if (expr.getId().equals("status")) {
					FilterTreeNodeHolder mmtn = (FilterTreeNodeHolder )tree.get("root");
					if (mmtn.state == 2) {
						result = true;
					} else if (mmtn.state == 1) {
						for(int i = 0; i < mmtn.children_ids.length; i++) {
							FilterTreeNodeHolder down_mte = (FilterTreeNodeHolder )tree.get(mmtn.children_ids[i]);
							String stat = "";
							if (a.getStatus() == AlarmStatus._ALARM_STATUS_GENERATED)
								stat = "GENERATED";
							else if (a.getStatus() == AlarmStatus._ALARM_STATUS_ASSIGNED)
								stat = "ASSIGNED";
							else if (a.getStatus() == AlarmStatus._ALARM_STATUS_FIXED)
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

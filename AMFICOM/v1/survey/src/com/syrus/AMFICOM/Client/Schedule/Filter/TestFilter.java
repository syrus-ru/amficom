package com.syrus.AMFICOM.Client.Schedule.Filter;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import java.util.*;

import com.syrus.AMFICOM.filter.FilterExpressionInterface;

public class TestFilter extends ObjectResourceFilter {

	public static final String	COLUMN_KIS				= "kis";
	public static final String	COLUMN_ME				= "mone";
	public static final String	COLUMN_TEST_TYPE		= "test_type";
	public static final String	COLUMN_TEMPORAL_TYPE	= "temp_type";
	public static final String	COLUMN_STATUS			= "status";
	public static final String	COLUMN_TIME				= "time";
	public static final String	COLUMN_ALARM			= "alarm";

	private Vector				filterColumns;

	private Map					columnFilterTypes;
	private Map					columnFilterPanel;

	public TestFilter() {
		this.filterColumns = new Vector();
		this.filterColumns.add(COLUMN_KIS);
		this.filterColumns.add(COLUMN_ME);
		this.filterColumns.add(COLUMN_TEST_TYPE);
		this.filterColumns.add(COLUMN_TEMPORAL_TYPE);
		this.filterColumns.add(COLUMN_STATUS);
		this.filterColumns.add(COLUMN_TIME);
		this.filterColumns.add(COLUMN_ALARM);

		this.columnFilterTypes = new HashMap();
		this.columnFilterTypes.put(COLUMN_KIS, new String[] { COLUMN_FILTER_TYPE_STRING, COLUMN_FILTER_TYPE_LIST});
		this.columnFilterTypes.put(COLUMN_ME, new String[] { COLUMN_FILTER_TYPE_STRING, COLUMN_FILTER_TYPE_LIST});
		this.columnFilterTypes.put(COLUMN_TEST_TYPE, new String[] { COLUMN_FILTER_TYPE_LIST});
		this.columnFilterTypes.put(COLUMN_TEMPORAL_TYPE, new String[] { COLUMN_FILTER_TYPE_LIST});
		this.columnFilterTypes.put(COLUMN_STATUS, new String[] { COLUMN_FILTER_TYPE_LIST});
		this.columnFilterTypes.put(COLUMN_TIME, new String[] { COLUMN_FILTER_TYPE_TIME});
		this.columnFilterTypes.put(COLUMN_ALARM, new String[] { COLUMN_FILTER_TYPE_LIST});

		this.columnFilterPanel = new HashMap();
		this.columnFilterPanel.put(COLUMN_FILTER_TYPE_NUMERIC, new GeneralEquationFilterPanel());
		this.columnFilterPanel.put(COLUMN_FILTER_TYPE_TIME, new GeneralTimeFilterPanel());
		this.columnFilterPanel.put(COLUMN_FILTER_TYPE_STRING, new GeneralStringFilterPanel());
		this.columnFilterPanel.put(COLUMN_FILTER_TYPE_RANGE, new GeneralRangeFilterPanel());

		Map columnListFilterPanel = new HashMap();
		columnListFilterPanel.put(COLUMN_KIS, new GeneralTreeFilterPanel(new KISTree()));
		columnListFilterPanel.put(COLUMN_ME, new GeneralTreeFilterPanel(new MoneTree()));
		columnListFilterPanel.put(COLUMN_TEST_TYPE, new GeneralTreeFilterPanel(new TestTypeTree()));
		columnListFilterPanel.put(COLUMN_TEMPORAL_TYPE, new GeneralTreeFilterPanel(new TempTypeTree()));
		columnListFilterPanel.put(COLUMN_STATUS, new GeneralTreeFilterPanel(new StatusTree()));
		columnListFilterPanel.put(COLUMN_ALARM, new GeneralTreeFilterPanel(new AlarmTree()));

		this.columnFilterPanel.put(COLUMN_FILTER_TYPE_LIST, columnListFilterPanel);

	}

	public Vector getFilterColumns() {
		return this.filterColumns;
	}

	public String getFilterColumnName(String colId) {
		if (colId == null)
			return "";
		if (colId.equals(COLUMN_KIS))
			return LangModelSchedule.getString("RTU");
		if (colId.equals(COLUMN_ME))
			return LangModelSchedule.getString("TestObject");
		if (colId.equals(COLUMN_TEST_TYPE))
			return LangModelSchedule.getString("TestType");
		if (colId.equals(COLUMN_TEMPORAL_TYPE))
			return LangModelSchedule.getString("TemporalType");
		if (colId.equals(COLUMN_STATUS))
			return LangModelSchedule.getString("Status");
		if (colId.equals(COLUMN_TIME))
			return LangModelSchedule.getString("Test_time");
		if (colId.equals(COLUMN_ALARM))
			return LangModelSchedule.getString("Alarm");
		return "";
	}

	public String[] getColumnFilterTypes(String colId) {
		return (colId == null ? null : (String[]) this.columnFilterTypes.get(colId));
	}

	public FilterPanel getColumnFilterPanel(String colId, String type) {
		Object obj = this.columnFilterPanel.get(type);
		FilterPanel panel = null;
		if (obj instanceof FilterPanel)
			panel = (FilterPanel) obj;
		else if (obj instanceof Map)
			panel = (FilterPanel) ((Map) obj).get(colId);
		return panel;
	}

	public boolean expression(FilterExpressionInterface exp, ObjectResource or) {
		boolean result = false;
		Test test = (Test) or;
		Vector vec = exp.getVec();
		String type = (String) vec.elementAt(0);
		String expId = exp.getId();

		System.out.println("test:" + test.getId());
		System.out.println("type:" + type);
		System.out.println("expId:" + expId);

		if (type.equals(COLUMN_FILTER_TYPE_NUMERIC)) {
			if (expId.equals(COLUMN_FILTER_TYPE_TIME)) {
				if (((String) vec.elementAt(1)).equals("=")) {
					if (test.getStartTime() == Long.parseLong((String) vec.elementAt(2))) {
						result = true;
					}
				} else if (((String) vec.elementAt(1)).equals(">")) {
					if (test.getStartTime() > Long.parseLong((String) vec.elementAt(2))) {
						result = true;
					}
				} else if (((String) vec.elementAt(1)).equals("<")) {
					if (test.getStartTime() < Long.parseLong((String) vec.elementAt(2))) {
						result = true;
					}
				}
			}
		} else if (type.equals(COLUMN_FILTER_TYPE_TIME)) {
			if (expId.equals(COLUMN_FILTER_TYPE_TIME)) {
				if (test.getStartTime() > Long.parseLong((String) vec.elementAt(1))
						&& test.getStartTime() < Long.parseLong((String) vec.elementAt(2))) {
					result = true;
				}
			}
		} else if (type.equals(COLUMN_FILTER_TYPE_RANGE)) {
			if (expId.equals(COLUMN_FILTER_TYPE_TIME)) {
				if (test.getStartTime() > Long.parseLong((String) vec.elementAt(1))
						&& test.getStartTime() < Long.parseLong((String) vec.elementAt(2))) {
					result = true;
				}
			}
		} else if (type.equals(COLUMN_FILTER_TYPE_STRING)) {
			String substring = (String) vec.elementAt(1);
			if (expId.equals(COLUMN_KIS)) {
				String name = Pool.getName(KIS.typ, test.getKisId());
				result = SearchSubstring(name, substring);
			} else if (exp.getId().equals(COLUMN_ME)) {
				MonitoredElement me;
				for (Enumeration e = Pool.getHash(MonitoredElement.typ).elements(); e.hasMoreElements();) {
					me = (MonitoredElement) e.nextElement();
					if (me.getId().equals(test.getMonitoredElementId())) {
						result = SearchSubstring(me.getName(), substring);
					}
				}
			}
		} else if (type.equals(COLUMN_FILTER_TYPE_LIST)) {
			TreeModelClone tree = (TreeModelClone) vec.elementAt(1);
			if (expId.equals(COLUMN_KIS)) {
				FilterTreeNode mmtn = (FilterTreeNode) tree.getRoot();
				String portid = "";
				String meid = "";
				String testtypeid = "";
				MonitoredElement me;
				boolean flag1 = false;
				boolean flag2 = false;
				for (Enumeration e = Pool.getHash(MonitoredElement.typ).elements(); e.hasMoreElements();) {
					me = (MonitoredElement) e.nextElement();
					if (me.getId().equals(test.getMonitoredElementId())) {
						portid = me.access_port_id;
						meid = me.getId();
						break;
					}
				}
				AccessPort ap = (AccessPort) Pool.get(AccessPort.typ, portid);
				AccessPortType apt = (AccessPortType) Pool.get(AccessPortType.typ, ap.type_id);
				for (Enumeration enum = Pool.getHash(TestType.typ).elements(); enum.hasMoreElements();) {
					TestType tt = (TestType) enum.nextElement();
					if (apt.test_type_ids.contains(tt.getId())) {
						testtypeid = tt.getId();
					}
				}

				if (mmtn.state == 2) {
					result = true;
				} else if (mmtn.state == 1) {
					for (Enumeration enum = mmtn.children(); enum.hasMoreElements();) {
						FilterTreeNode down_mte1 = (FilterTreeNode) enum.nextElement();
						if (test.getKisId().equals(down_mte1.id) && (down_mte1.state != 0)) {
							for (Enumeration enu = down_mte1.children(); enu.hasMoreElements();) {
								FilterTreeNode down_mte2 = (FilterTreeNode) enu.nextElement();
								if (portid.equals(down_mte2.id) && (down_mte2.state != 0)) {
									for (Enumeration en = down_mte2.children(); en.hasMoreElements();) {
										FilterTreeNode down_mte3 = (FilterTreeNode) en.nextElement();
										if (down_mte3.getId().equals(COLUMN_ME)) {
											for (Enumeration e = down_mte3.children(); e.hasMoreElements();) {
												FilterTreeNode down_mte4_1 = (FilterTreeNode) e.nextElement();
												if (meid.equals(down_mte4_1.id) && (down_mte4_1.state == 2)) {
													flag1 = true;
													break;
												}
											}
										}
										if (down_mte3.getId().equals("testtypes")) {
											for (Enumeration e = down_mte3.children(); e.hasMoreElements();) {
												FilterTreeNode down_mte4_2 = (FilterTreeNode) e.nextElement();
												if (testtypeid.equals(down_mte4_2.id) && (down_mte4_2.state == 2)) {
													flag2 = true;
													break;
												}
											}
										}
										if (flag1 == true && flag2 == true) {
											result = true;
											break;
										}
									}
								}
								if (flag1 == true && flag2 == true) {
									break;
								}
							}
						}
						if (flag1 == true && flag2 == true) {
							break;
						}
					}
				}
			}
			if (expId.equals(COLUMN_ME)) {
				FilterTreeNode mmtn = (FilterTreeNode) tree.getRoot();
				if (mmtn.state == 2) {
					result = true;
				} else if (mmtn.state == 1) {
					for (Enumeration enum = mmtn.children(); enum.hasMoreElements();) {
						FilterTreeNode down_mte = (FilterTreeNode) enum.nextElement();
						MonitoredElement me = null;
						for (Enumeration e = Pool.getHash(MonitoredElement.typ).elements(); e.hasMoreElements();) {
							me = (MonitoredElement) e.nextElement();
							if (me.getId().equals(test.getMonitoredElementId())) {
								break;
							}
						}

						if (me.id.equals(down_mte.id) && (down_mte.state == 2))
							result = true;
					}
				}
			}
			if (expId.equals(COLUMN_TEST_TYPE)) {
				FilterTreeNode mmtn = (FilterTreeNode) tree.getRoot();
				if (mmtn.state == 2) {
					result = true;
				} else if (mmtn.state == 1) {
					for (Enumeration enum = mmtn.children(); enum.hasMoreElements();) {
						FilterTreeNode down_mte = (FilterTreeNode) enum.nextElement();
						if (test.getTestTypeId().equals(down_mte.id) && (down_mte.state == 2))
							result = true;
					}
				}
			}
			if (expId.equals(COLUMN_TEMPORAL_TYPE)) {
				FilterTreeNode mmtn = (FilterTreeNode) tree.getRoot();
				if (mmtn.state == 2) {
					result = true;
				} else if (mmtn.state == 1) {
					for (Enumeration enum = mmtn.children(); enum.hasMoreElements();) {
						FilterTreeNode down_mte = (FilterTreeNode) enum.nextElement();
						int downMteStatus = Integer.parseInt(down_mte.getId());
						int status = test.getTemporalType().value();
						if ((downMteStatus == status) && (down_mte.state == 2))
							result = true;
					}
				}
			}
			if (expId.equals(COLUMN_ALARM)) {
				FilterTreeNode mmtn = (FilterTreeNode) tree.getRoot();
				if (mmtn.state == 2) {
					result = true;
				} else if (mmtn.state == 1) {
					for (Enumeration enum = mmtn.children(); enum.hasMoreElements();) {
						FilterTreeNode down_mte = (FilterTreeNode) enum.nextElement();
						String stat = "";
						if (test.getElementaryTestAlarms().length != 0)
							stat = "alarm";
						else
							stat = "noalarm";
						if (down_mte.getId().equals(stat) && (down_mte.state == 2))
							result = true;
					}
				}
			}
			if (expId.equals(COLUMN_STATUS)) {
				FilterTreeNode mmtn = (FilterTreeNode) tree.getRoot();
				if (mmtn.state == 2) {
					result = true;
				} else if (mmtn.state == 1) {
					for (Enumeration enum = mmtn.children(); enum.hasMoreElements();) {
						FilterTreeNode down_mte = (FilterTreeNode) enum.nextElement();
						String stat = test.getStatus().toString();
						if (down_mte.getId().equals(stat) && (down_mte.state == 2))
							result = true;
					}
				}
			}
		}
		return result;
	}

	public Object clone() {
		TestFilter af = new TestFilter();
		af.clearCriteria();
		for (int i = 0; i < getCriteria().size(); i++) {
			FilterExpression fe = (FilterExpression) getCriteria().get(i);
			af.addCriterium((FilterExpression) fe.clone());
		}
		return af;
	}
}
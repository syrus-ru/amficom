package com.syrus.AMFICOM.Client.Survey.Alarm.Filter;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

import java.util.*;

public class DefaultAlarmFilter extends AlarmFilter
{
	

	// Фильтр по умолчанию производит фильтрацию по времени (сигналы тревоги
	// за последние сутки) и по статусу (новые сигналы тревоги)
	public DefaultAlarmFilter()
	{
		super();

		FilterExpression fe = new FilterExpression();
		Vector vec = new Vector();
		String col_name = "";

		Calendar hc = Calendar.getInstance();
		hc.set(Calendar.MILLISECOND, 0);
		hc.set(Calendar.SECOND, 0);
		hc.set(Calendar.MINUTE, 0);
		hc.set(Calendar.HOUR_OF_DAY, 0);

		long loval = hc.getTimeInMillis();
//		long loval = System.currentTimeMillis();
		long hival = System.currentTimeMillis();

		vec.add("time");
		vec.add(String.valueOf(loval));
		vec.add(String.valueOf(hival));

		col_name = getFilterColumnName("time");
		fe.setName(LangModel.getString("labelFiltration") +
				" \'" + col_name +
				"\' " + LangModel.getString("labelTimeOt") +
				" " + ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(loval)) +
				" " + LangModel.getString("labelTimeDo") +
				" " + ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(hival)));
		fe.setVec(vec);
		fe.setId("time");
		addCriterium(fe);

		fe = new FilterExpression();
		vec = new Vector();

		AlarmStatusTree ast = new AlarmStatusTree();
		ast.setTree(null);
		ast.a1.state = 2;// set list value "GENERATED" to 'checked'
		ast.root.state = 1;// set root status to 'has checked nodes'
		TreeModelClone tm = (TreeModelClone )ast.tree.getModel();
		TreeModelClone new_tm = tm.myclone();

		vec.add("list");
		vec.add(new_tm);

		col_name = getFilterColumnName("Status");
		fe.setVec(vec);
		fe.setName(LangModel.getString("labelFiltration") +
				" \'" + col_name +
				"\' " + LangModel.getString("labelPoSpisku"));
		fe.setId("Status");
		addCriterium(fe);

		this.logicScheme.organizeStandartScheme();
//		this.logicScheme.restoreSchemeByText(String expression, Vector filterExpressions)

	}
}

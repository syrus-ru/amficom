package com.syrus.AMFICOM.Client.Survey.Alarm;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;

public class AlarmTableRenderer extends DefaultTableCellRenderer
{
	GeneralListPane listPane;

	public AlarmTableRenderer(GeneralListPane listPane)
	{
		super();
		this.listPane = listPane;
	}

		public Component getTableCellRendererComponent(
			JTable table,
			Object value,
						boolean isSelected,
			boolean hasFocus,
			int row,
			int column)
	{
		Alarm alarm = (Alarm )table.getValueAt(row, column);
		ObjectResourceTableModel tableModel = (ObjectResourceTableModel )table.getModel();
		String col_id = tableModel.getColumnByNumber(column);
		String text = alarm.getColumnValue(col_id);

		Component component = super.getTableCellRendererComponent(
				table,
				text,
				isSelected,
				hasFocus,
				row,
				column);

		Color bkgndCol = table.getBackground();

		if (!isSelected)
		{
			if(alarm.status == Constants.ALARM_GENERATED)
				bkgndCol = new Color(255, 100, 100);
			else
			if(alarm.status == Constants.ALARM_ASSIGNED)
				bkgndCol = new Color(200, 255, 200);
			component.setBackground(bkgndCol);
		}
		if(alarm.status == Constants.ALARM_DELETED)
			return null;
		return component;
		}
}
package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class AddToPathFrame extends JDialog
{
	FixedSizeEditableTableModel tm;
	Vector v = new Vector();

	String[] names = new String[]
	{
		"#",
		"схема",
		"элемент",
		"кабель",
		"линия",
		};

	ATable table = new ATable();

	public AddToPathFrame(Frame parent, String title)
	{
		super(parent, title);

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		tm = new FixedSizeEditableTableModel(
				names,
				new String[] {"", "", "", "", ""},
				null,
				null);
		table.setModel(tm);

		setLayout(new BorderLayout());
		add(table, BorderLayout.CENTER);
	}

	public void init(ObjectResource[] resources)
	{
		for (int i = 0; i < resources.length; i++)
		{
			;
			if (resources[i] instanceof SchemeLink)
			{
				v.add(resources[i]);
				tm.addRow(
						String.valueOf(tm.getRowCount() + 1),
						new String[] {"текущая", "",  "", ((SchemeLink)resources[i]).getName()}
						);
			}
			if (resources[i] instanceof SchemeCableLink)
			{
				v.add(resources[i]);
				tm.addRow(
						String.valueOf(tm.getRowCount() + 1),
						new String[] {"текущая", "",
						((SchemeCableLink)resources[i]).getName(),
						((SchemeCableThread)((SchemeCableLink)resources[i]).cable_threads.get(0)).getId()}
						);
			}
		}
	}
}

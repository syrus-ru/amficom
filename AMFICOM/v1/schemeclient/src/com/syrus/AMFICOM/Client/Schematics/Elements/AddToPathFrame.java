package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.*;
import java.util.List;

import java.awt.*;
import javax.swing.JDialog;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.scheme.corba.*;

public class AddToPathFrame extends JDialog
{
	FixedSizeEditableTableModel tm;
	List v = new ArrayList(5);

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

	public void init(Object[] resources)
	{
		for (int i = 0; i < resources.length; i++)
		{
			if (resources[i] instanceof SchemeLink)
			{
				v.add(resources[i]);
				tm.addRow(
						String.valueOf(tm.getRowCount() + 1),
						new String[] {"текущая", "",  "", ((SchemeLink)resources[i]).name()}
						);
			}
			if (resources[i] instanceof SchemeCableLink)
			{
				v.add(resources[i]);
				tm.addRow(
						String.valueOf(tm.getRowCount() + 1),
						new String[] {"текущая", "",
						((SchemeCableLink)resources[i]).name(),
						(((SchemeCableLink)resources[i]).schemeCableThreads()[0]).name()}
						);
			}
		}
	}
}

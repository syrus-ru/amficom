package com.syrus.AMFICOM.Client.General.Filter;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class FilterDialog extends JDialog 
{
	public int retcode = 0;
	private ObjectResourceFilterPane filterPanel = new ObjectResourceFilterPane();
	private JPanel buttonPanel = new JPanel();
	
	private JButton okButton = new JButton();
	private JButton cancelButton = new JButton();
	private JButton clearButton = new JButton();

	public ObjectResourceFilter filter;
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	
	public FilterDialog(ObjectResourceFilter filter)
	{
		super(Environment.getActiveWindow());
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		pack();
		this.filter = filter;
		filterPanel.setFilter(filter);
		filterPanel.setContext(null);
	}

	public FilterDialog(ObjectResourceFilter filter, ApplicationContext aContext)
	{
		this(filter);
		filterPanel.setContext(aContext);
	}

	private void jbInit() throws Exception
	{
//		this.setResizable(false);
		setTitle("Фильтрация");// сигналов тревоги
		this.getContentPane().setLayout(verticalFlowLayout1);
		okButton.setText("Применить");
		okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					okButton_actionPerformed(e);
				}
			});
		cancelButton.setText("Отменить");
		cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					cancelButton_actionPerformed(e);
				}
			});
		clearButton.setText("Очистить");
		clearButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					clearButton_actionPerformed(e);
				}
			});
		this.getContentPane().add(filterPanel, null);
		buttonPanel.add(okButton, null);
		buttonPanel.add(cancelButton, null);
		buttonPanel.add(clearButton, null);
		this.getContentPane().add(buttonPanel, null);
	}

	private void clearButton_actionPerformed(ActionEvent e)
	{
		filter.clearCriteria();
		filterPanel.setFilter(filter);
	}

	private void cancelButton_actionPerformed(ActionEvent e)
	{
		retcode = 2;
		dispose();
	}

	private void okButton_actionPerformed(ActionEvent e)
	{
		retcode = 1;
		dispose();
	}
}
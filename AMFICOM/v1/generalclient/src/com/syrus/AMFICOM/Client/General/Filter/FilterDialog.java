package com.syrus.AMFICOM.Client.General.Filter;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.syrus.AMFICOM.Client.General.Model.*;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class FilterDialog extends JDialog 
{
	public static final int RETURN_CODE_OK = 1;
	public static final int RETURN_CODE_CANCEL = 2;
	
	public int retcode = 0;
	
	ObjectResourceFilterPane filterPanel = new ObjectResourceFilterPane();
	
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
		this.setFilter(filter);
	}

	public FilterDialog(ObjectResourceFilter filter, ApplicationContext aContext)
	{
		this(filter);
		this.filterPanel.setContext(aContext);
	}

	private void jbInit() throws Exception
	{
//		this.setResizable(false);
		setTitle("Фильтрация");// сигналов тревоги

		this.okButton.setText("Применить");
		this.okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					FilterDialog.this.retcode = RETURN_CODE_OK;
					FilterDialog.this.dispose();
				}
			});
		this.cancelButton.setText("Отменить");
		this.cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{					
					FilterDialog.this.retcode = RETURN_CODE_CANCEL;
					FilterDialog.this.dispose();
				}
			});
		this.clearButton.setText("Очистить");
		this.clearButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					FilterDialog.this.filter.clearCriteria();
					FilterDialog.this.filterPanel.setFilter(FilterDialog.this.filter);
				}
			});
		this.buttonPanel.add(this.okButton, null);
		this.buttonPanel.add(this.cancelButton, null);
		this.buttonPanel.add(this.clearButton, null);

		this.getContentPane().setLayout(this.verticalFlowLayout1);
		this.getContentPane().add(this.filterPanel, null);
		this.getContentPane().add(this.buttonPanel, null);
	}

	public void setFilter(ObjectResourceFilter filter){
		this.filter = filter;
		this.filterPanel.setFilter(filter);
		this.filterPanel.setContext(null);

	}
}

/*package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.Map;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.*;

public class CatalogElementsDialog extends JDialog
{
	CatalogElementsPanel panel;
	private JButton okButton = new JButton();
	private JButton cancelButton = new JButton();

	public static final int OK = 1;
	public static final int CANCEL = 0;
	int status = CANCEL;

	ApplicationContext aContext;

	public CatalogElementsDialog(ApplicationContext aContext)
	{
		super(Environment.getActiveWindow());
		this.aContext = aContext;

		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
		setTitle("Связь схемы с каталогом");
		getContentPane().setLayout(new BorderLayout());

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = screenSize.width / 3;
		int h = screenSize.height / 2;
		setSize(w, h);
		setLocation(w, h / 2);
		setModal(true);

		panel = new CatalogElementsPanel(aContext);
		getContentPane().add(panel, BorderLayout.CENTER);

		okButton.setText("OK");
		cancelButton.setText("Отмена");

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(okButton, FlowLayout.LEFT);
		buttonPanel.add(cancelButton, FlowLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				okButton_actionPerformed();
			}
		});

		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelButton_actionPerformed();
			}
		});
	}

	public int init (Map d)
	{
		panel.init(d);
		setVisible(true);

		return status;
	}

	public Map getMapping()
	{
		return panel.selected_ors;
	}

	void okButton_actionPerformed()
	{
		status = OK;
		dispose();
	}

	void cancelButton_actionPerformed()
	{
		dispose();
	}
}

*/
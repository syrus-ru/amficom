package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

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

	public int init (DataSet d)
	{
		panel.init(d);
		setVisible(true);

		return status;
	}

	public Hashtable getMapping()
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


/*package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.Client.Configure.UI.CableLinkFibrePanel;

public class CableThreadSelectionDialog extends JDialog
{
	ApplicationContext aContext;
	CableLinkFibrePanel panel;
	SchemeCableThread selected_thread = null;

	public CableThreadSelectionDialog(ApplicationContext aContext)
	{
		super(Environment.getActiveWindow());
		this.aContext = aContext;
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (400, 600);

		setSize(frameSize);
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height - 30) / 2);

		getContentPane().setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				selected_thread = panel.getSelectedThread();
				if (selected_thread != null)
					dispose();
			}
		});
		JButton cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	public SchemeCableThread init (SchemeCableLink link)
	{
		setTitle("Волокна кабеля " + link.name());

		panel = new CableLinkFibrePanel(link);
		getContentPane().add(panel, BorderLayout.CENTER);
		setVisible(true);
		return selected_thread;
	}


}

*/
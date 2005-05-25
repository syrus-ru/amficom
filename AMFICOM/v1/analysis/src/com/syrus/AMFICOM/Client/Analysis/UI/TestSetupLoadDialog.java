package com.syrus.AMFICOM.Client.Analysis.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.UniTreePanel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.measurement.MeasurementSetup;

public class TestSetupLoadDialog extends JDialog implements OperationListener
{
	public int ret_code = 0;
	public MeasurementSetup resource;

	private Dispatcher dispatcher = new Dispatcher();
	private ApplicationContext aContext = new ApplicationContext();

	JButton okButton;
	JButton cancelButton;
	List data;
	JScrollPane scrollPane = new JScrollPane();

	public TestSetupLoadDialog()
	{
		super(Environment.getActiveWindow());
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		aContext.setDispatcher(dispatcher);
		dispatcher.register(this, "treedataselectionevent");
	}

	private void jbInit() throws Exception
	{
		setModal(true);
		setTitle("����� �������");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (350, 600);
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height - 34;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width - 10;
		}
		setSize(frameSize);
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height - 30) / 2);

		setResizable(true);

		TestSetupTreeModel lrtm = new TestSetupTreeModel();

		UniTreePanel utp = new UniTreePanel(dispatcher, aContext, lrtm);

		JPanel ocPanel = new JPanel();
		ocPanel.setPreferredSize(new Dimension(350, 30));
		ocPanel.setLayout(new FlowLayout());

		okButton = new JButton();
		okButton.setText("OK");
		okButton.setEnabled(false);
		okButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				okButton_actionPerformed(e);
			}
		});
		cancelButton = new JButton();
		cancelButton.setText("������");
		cancelButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelButton_actionPerformed(e);
			}
		});
		ocPanel.add(okButton);
		ocPanel.add(cancelButton);
		getContentPane().setLayout(new BorderLayout());

		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(utp, null);
		utp.setVisible(true);
		scrollPane.setVisible(true);

		this.getContentPane().add(ocPanel, BorderLayout.SOUTH);
	}

	public void operationPerformed(OperationEvent oe)
	{
		if (oe instanceof TreeDataSelectionEvent)
		{
			TreeDataSelectionEvent ev = (TreeDataSelectionEvent)oe;
			
//		    System.out.println("oe instanceof TreeDataSelectionEvent, A1 " + (ev.getDataClass() != null));
//		    if (ev.getDataClass() != null)
//		        System.out.println("   A2 " + ev.getDataClass().equals(MeasurementSetup.class) + " A3 " + (ev.getSelectionNumber() != -1));
		    
			if (ev.getDataClass() != null && ev.getDataClass().equals(MeasurementSetup.class)
					&& ev.getSelectionNumber() != -1)
			{
				okButton.setEnabled(true);
				data = ev.getList();
				resource = (MeasurementSetup )data.get(ev.getSelectionNumber());
			}
			else
			{
				okButton.setEnabled(false);
			}
		}
	}

	void okButton_actionPerformed(ActionEvent e)
	{
		ret_code = 1;
		dispose();
	}

	void cancelButton_actionPerformed(ActionEvent e)
	{
		dispose();
	}
}


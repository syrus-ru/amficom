package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;

public class TestRequestFrame extends JInternalFrame implements OperationListener
{
	ApplicationContext aContext;
	TestRequestPanel panel;

	public TestRequestFrame(ApplicationContext aContext)
	{
		this.aContext = aContext;
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		init_module();
	}

	private void jbInit() throws Exception
	{
		setTitle("Свойства теста");
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);

		panel = new TestRequestPanel(aContext);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}

	void init_module()
	{
		aContext.getDispatcher().register(this, TestUpdateEvent.typ);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(TestUpdateEvent.typ))
		{
			TestUpdateEvent tue = (TestUpdateEvent)ae;
			Test test = tue.test;
			if (tue.TEST_SELECTED)
			{
				//this.getContentPane().removeAll();
				//this.getContentPane().add(panel, BorderLayout.CENTER);
				//updateUI();

				TestRequest treq = (TestRequest)Pool.get(TestRequest.typ, test.request_id);
				if (treq == null)
					System.err.println("TestRequestFrame.operationPerformed() treq not found id " + test.request_id);
				else
					panel.setTestRequest(treq);

			}
			else
			{
				//this.getContentPane().removeAll();
				//JList emptyList = new JList();
				//emptyList.setBorder(BorderFactory.createLoweredBevelBorder());
				//this.getContentPane().add(emptyList, BorderLayout.CENTER);
				//updateUI();
			}
		}
	}

}

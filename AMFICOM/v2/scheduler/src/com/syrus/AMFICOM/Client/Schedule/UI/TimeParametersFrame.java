package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class TimeParametersFrame extends JInternalFrame implements OperationListener
{
	ApplicationContext aContext;
	TimeParametersPanel panel;

	public TimeParametersFrame (ApplicationContext aContext)
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
		setTitle("Временные параметры");
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);

		panel = new TimeParametersPanel();
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
				TestRequest treq = (TestRequest)Pool.get(TestRequest.typ, test.request_id);
				if (treq != null)
					panel.setTestRequest(treq);
			}
			else
			{

			}
		}
	}

}

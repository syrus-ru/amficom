package com.syrus.AMFICOM.Client.General.Command.Survey;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Vector;
import java.util.Date;
import java.text.*;

import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
//import com.syrus.AMFICOM.Client.Resource.Survey.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
//import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Test.*;
import com.syrus.AMFICOM.Client.Survey.*;
import com.syrus.AMFICOM.Client.Survey.Test.*;
import com.syrus.AMFICOM.Client.General.Command.*;

public class TestRequestCommand extends VoidCommand
{
	ApplicationContext aContext;
	TestDialog td;

	public TestRequestCommand()
	{
	}

	public TestRequestCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new TestRequestCommand(aContext);
	}

	public void execute()
	{
        System.out.println("Creating new test request");

		td = new TestDialog(null, "Testing this shit", false);
/*    
		td.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
        td.dispose();
//				System.exit(0);
			}      
		});
*/
		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize =  td.getSize();

        if (frameSize.height > screenSize.height)
        {
            frameSize.height = screenSize.height;
        }

        if (frameSize.width > screenSize.width)
        {
            frameSize.width = screenSize.width;
        }

        td.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);

		td.pack();
		td.setModal(true);
        td.setVisible(true);

		if ( td.retcode == 1)
		{
			DataSourceInterface dataSource = aContext.getDataSourceInterface();

			TestRequest treq = new TestRequest(dataSource.GetUId("testrequest"));
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
			treq.user_id = aContext.getSessionInterface().getUserId();
			treq.name = treq.user_id + " at " + sdf.format(new Date(System.currentTimeMillis()));
      
			Pool.put("testrequest", treq.id, treq);

			Test t = td.tt;
			t.id = dataSource.GetUId("test");
			t.request_id = treq.getId();
			t.user_id = aContext.getSessionInterface().getUserId();
			t.name = sdf.format(new Date(System.currentTimeMillis()));
/*
      new Test(dataSource.GetUId("test"));
			byte[] value = new byte[0];
			Parameter_Transferable arg1 = new Parameter_Transferable (
					"0",
					"נופכוךעמדנאללא",
					"reflectogramm",
					"נופכוךעמדנאללא",
					"reflectogramm",
					t.id,
					value );
			t.addArgument(new Parameter(arg1));
*/
			Pool.put("test", t.id, t);

// test!!!!!!
			treq.test_ids.add(t.getId());
			treq.updateLocalFromTransferable();

			dataSource.RequestTest(treq.id, t.id);
		}

	}

}
package com.syrus.AMFICOM.Client.General.Filter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JInternalFrame;
import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import javax.swing.event.InternalFrameEvent;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilterPane;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;

import com.syrus.AMFICOM.Client.Resource.DataSet;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class SetRestrictionsWindow extends JInternalFrame
{
  public static String ev_lsWindowCreated = "ev_lsWindowCreated";

  public ObjectResourceFilterPane orfp = null;

  private ObjectResourceFilter orf = null;
  private DataSet dataset = null;
  private ApplicationContext aContext = null;
//  private JFrame ownerWindow = null;

  public SetRestrictionsWindow(
		ObjectResourceFilter orf,
		DataSet data,
		ApplicationContext aC,
		JFrame ownerWindow)
  {
	 try
	 {
		this.orf = orf;
		this.aContext = aC;
		this.dataset = data;
		jbInit();
	 }
	 catch(Exception e)
	 {
		e.printStackTrace();
	 }
  }
  private void jbInit() throws Exception
  {
	 //this.setSize(new Dimension(280, 452));
		this.setSize(new Dimension(275, 400));
	 this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter()
	 {
		public void internalFrameClosing(InternalFrameEvent e)
		{
		  aContext.getDispatcher().notify(
			 new OperationEvent(orfp.getFilter(),0,com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilterPane.state_filterClosed));

		  closeSchemeWindow();
		}
	 });

	 orfp = new ObjectResourceFilterPane(orf, this.dataset, this, this.aContext);

	 this.setClosable(true);
	 this.setResizable(true);
	 this.setTitle(LangModel.String("label_filter"));
	 this.getContentPane().add(orfp, BorderLayout.CENTER);
  }

  public void closeSchemeWindow()
  {
	 this.orfp.logicSchemeWindow.dispose();
  }
}
package com.syrus.AMFICOM.Client.General.Filter;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Image;

import java.util.List;
import javax.swing.JInternalFrame;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.event.InternalFrameEvent;


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
  private List dataset = null;
  private ApplicationContext aContext = null;
//  private JFrame ownerWindow = null;

  public SetRestrictionsWindow(
		ObjectResourceFilter orf,
		List data,
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
	 this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
		 "images/main/report_mini.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));

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
	 this.setTitle(LangModel.getString("label_filter"));
	 this.getContentPane().add(orfp, BorderLayout.CENTER);
  }

  public void closeSchemeWindow()
  {
	 this.orfp.logicSchemeWindow.dispose();
  }
}

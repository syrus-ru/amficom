/*
 * $Id: SetRestrictionsWindow.java,v 1.9 2004/09/27 10:06:55 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;

/**
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2004/09/27 10:06:55 $
 * @module generalclient_v1
 */
public class SetRestrictionsWindow extends JInternalFrame
{
	public static String ev_lsWindowCreated = "ev_lsWindowCreated";

	public ObjectResourceFilterPane orfp = null;

	private ObjectResourceFilter orf = null;

	private List dataset = null;

	private ApplicationContext aContext = null;

	public SetRestrictionsWindow(
		ObjectResourceFilter orf,
		List data,
		ApplicationContext aC,
		JFrame ownerWindow)
	{
		this.orf = orf;
		this.aContext = aC;
		this.dataset = data;
		jbInit();
	}

	private void jbInit()
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

/*
 * $Id: TextPanel.java,v 1.4 2004/09/27 07:47:07 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Report;

import java.awt.Graphics;
import javax.swing.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/09/27 07:47:07 $
 * @module generalclient_v1
 */
public class TextPanel extends JScrollPane
{
	public JTextPane dataPane = null;

	public TextPanel(ObjectsReport rp)
			throws CreateReportException
	{
		try
		{
      this.dataPane = (JTextPane)rp.getReserve();

			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new CreateReportException("",CreateReportException.generalError);
		}
	}
	private void jbInit() throws Exception
	{
		this.setPreferredSize(dataPane.getPreferredSize());
//    this.getViewport().add(dataPane);    

		this.setVisible(true);
	}
  
  public void paint(Graphics g)
  {
    super.paint(g);
    
    this.dataPane.paint(g);
  }
}

package com.syrus.AMFICOM.Client.General.Report;

import java.awt.Dimension;
import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * <p>Title: </p>
 * <p>Description: Испольуется для реализации всех таблиц -
 * элементов шаблона</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class TextPanel extends JScrollPane
{
	private RenderingObject ro = null;
	private JTextPane dataPane = null;

	public TextPanel(ObjectsReport rp)
			throws CreateReportException
	{
		try
		{
			this.ro = ro;
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
    this.getViewport().add(dataPane);    

		this.setVisible(true);
	}
}

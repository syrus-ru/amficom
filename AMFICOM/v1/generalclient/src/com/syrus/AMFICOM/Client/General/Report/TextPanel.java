package com.syrus.AMFICOM.Client.General.Report;

import java.awt.Dimension;
import java.awt.BorderLayout;

import java.awt.Graphics;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * <p>Title: </p>
 * <p>Description: ����������� ��� ���������� ���� ������ -
 * ��������� �������</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
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

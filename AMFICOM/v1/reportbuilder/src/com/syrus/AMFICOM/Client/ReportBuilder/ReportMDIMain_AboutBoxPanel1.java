package com.syrus.AMFICOM.Client.ReportBuilder;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JLabel;

import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * <p>Description: Панель для размещения схемы элементов шаблона</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class ReportMDIMain_AboutBoxPanel1 extends JPanel
{
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JLabel jLabel4 = new JLabel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	Border border1 = new EtchedBorder();

	public ReportMDIMain_AboutBoxPanel1()
	{
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		jLabel1.setText("ISM");
		jLabel2.setText("Peskovsky Peter");
		jLabel3.setText("Copyright (c) Syrus Systems 2003");
		jLabel4.setText("Syrus Systems");
		this.setLayout(gridBagLayout1);
		this.setBorder(border1);
		this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		this.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.add(jLabel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
	}
}

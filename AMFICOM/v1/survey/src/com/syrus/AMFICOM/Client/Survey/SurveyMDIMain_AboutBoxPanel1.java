
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Survey;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import oracle.jdeveloper.layout.*;

/**
 * A Swing-based AboutBox class.
 * <P>
 * @author Andrei Kroupennikov
 */
public class SurveyMDIMain_AboutBoxPanel1 extends JPanel
{
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JLabel jLabel4 = new JLabel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	Border border1 = new EtchedBorder();

	public SurveyMDIMain_AboutBoxPanel1()
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
		jLabel1.setText("АМФИКОМ");
		jLabel2.setText("Версия 1.0");
		jLabel3.setText("Ограниченная демо-версия");
		jLabel4.setText("Syrus Systems 2003");
		this.setLayout(gridBagLayout1);
		this.setBorder(border1);
		this.add(jLabel1, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0,
		        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		this.add(jLabel2, new GridBagConstraints2(0, 1, 1, 1, 0.0, 0.0,
		        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.add(jLabel3, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0,
		        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.add(jLabel4, new GridBagConstraints2(0, 3, 1, 1, 0.0, 0.0,
		        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
	}
}


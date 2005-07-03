// Copyright (c) Syrus Systems 2000-2003 Syrus Systems
package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

/*
 * A Swing-based AboutBox class.
 * <P>
 * @author Vitaliy Shiriaev
 */
public class OptimizeMDIMain_AboutBoxPanel1 extends JPanel
{
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JLabel jLabel4 = new JLabel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	Border border1 = new EtchedBorder();

	public OptimizeMDIMain_AboutBoxPanel1()
	{	try
		{	jbInit();
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{ jLabel1.setText("Topology optimization module");
		jLabel2.setText("Written by Vitaliy Shiriaev");
		jLabel3.setText("Copyright (c) Syrus Systems 2003");
		jLabel4.setText("Syrus Systems, Russia");
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

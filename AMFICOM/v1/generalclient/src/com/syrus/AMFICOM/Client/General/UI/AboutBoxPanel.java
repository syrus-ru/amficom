package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import oracle.jdeveloper.layout.*;

import com.syrus.AMFICOM.Client.General.Model.*;

public class AboutBoxPanel extends JPanel
{
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JLabel jLabel4 = new JLabel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	Border border1 = new EtchedBorder();

	public AboutBoxPanel()
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
		jLabel2.setText("Версия " + Version.getVersionNumber() + " обновление " + Patch.getVersion());
		jLabel3.setText(Version.getVersionText());
		jLabel4.setText(Version.getVersionCopyright());
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

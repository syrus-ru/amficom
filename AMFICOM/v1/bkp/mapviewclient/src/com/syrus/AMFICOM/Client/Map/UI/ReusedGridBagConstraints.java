package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class ReusedGridBagConstraints extends GridBagConstraints 
{
	protected static GridBagConstraints constraints = new GridBagConstraints();

	protected static final Insets DEFAULT_INSETS = new Insets(0, 0, 0, 0);

	private ReusedGridBagConstraints()
	{
	}
	
	public static GridBagConstraints get(
			int gridx, 
			int gridy,
			int gridwidth, 
			int gridheight,
			double weightx, 
			double weighty,
			int anchor, 
			int fill,
			Insets insets, 
			int ipadx, 
			int ipady) 
	
	{
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridwidth;
		constraints.gridheight = gridheight;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.anchor = anchor;
		constraints.fill = fill;
		if(insets != null)
			constraints.insets = insets;
		else
			constraints.insets = DEFAULT_INSETS;
		constraints.ipadx = ipadx;
		constraints.ipady = ipady;
		
		return constraints;
	}
}

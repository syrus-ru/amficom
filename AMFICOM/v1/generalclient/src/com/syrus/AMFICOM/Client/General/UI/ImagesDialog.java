/*
 * $Id: ImagesDialog.java,v 1.4 2004/09/27 06:20:52 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JDialog;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/09/27 06:20:52 $
 * @module generalclient_v1
 */
public class ImagesDialog extends JDialog 
{
	private ImagesPanel jPanel1;
	private BorderLayout borderLayout1 = new BorderLayout();

	ApplicationContext aContext;

	public int ret_code = 0;

	public ImagesDialog(ApplicationContext aContext)
	{
		this(aContext, null, "", false);
	}

	public ImagesDialog(ApplicationContext aContext, Frame parent, String title, boolean modal)
	{
		super(parent, title, modal);
		this.aContext = aContext;
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		jPanel1 = new ImagesPanel(aContext.getDataSource());
		this.setSize(new Dimension(400, 300));
		this.setResizable(false);
		this.getContentPane().setLayout(borderLayout1);
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);

		jPanel1.chooseButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					chooseButton_actionPerformed(e);
				}
			});
		jPanel1.cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					cancelButton_actionPerformed(e);
				}
			});
	}

	public ImageResource getImageResource()
	{
		return jPanel1.getImageResource();
	}

	public void setImageResource(ImageResource ir)
	{
		jPanel1.setImageResource(ir);
	}
	
	private void cancelButton_actionPerformed(ActionEvent e)
	{
		ret_code = 2;
		dispose();
	}

	private void chooseButton_actionPerformed(ActionEvent e)
	{
		ret_code = 1;
		dispose();
	}
}

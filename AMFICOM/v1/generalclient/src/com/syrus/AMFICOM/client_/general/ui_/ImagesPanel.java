/*
 * $Id: ImagesPanel.java,v 1.4 2005/02/08 11:27:38 bob Exp $
 *
 * Copyright ї 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.ImageResourceWrapper;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;

/**
 * @author $Author: bob $
 * @version $Revision: 1.4 $, $Date: 2005/02/08 11:27:38 $
 * @module generalclient_v1
 */
public class ImagesPanel extends JPanel
		implements OperationListener
{
	public JPanel buttonsPanel = new JPanel();
	public JButton cancelButton = new JButton();
	public JButton chooseButton = new JButton();
	public BitmapImageResource ir = null;

	ApplicationContext applicationContext;

	Dispatcher disp = new Dispatcher();
	private JButton addButton = new JButton();
	private BorderLayout borderLayout1 = new BorderLayout();
	private FlowLayout flowLayout1 = new FlowLayout();
	private JPanel imagesPanel = new JPanel();
	private JScrollPane jScrollPane1 = new JScrollPane();

	public ImagesPanel(ApplicationContext aContext)
	{
		this.applicationContext = aContext;
		jbInit();
		initImages();
	}

	public ImagesPanel(ApplicationContext aContext, BitmapImageResource ir)
	{
		this(aContext);
		setImageResource(ir);
	}

	public BitmapImageResource getImageResource()
	{
		return ir;
	}

	public void initImages()
	{
		disp.register(this, "select");
		disp.register(this, "selectir");

		try {
			TypicalCondition condition = new TypicalCondition(
				ImageResourceSort._BITMAP,
				ImageResourceSort._BITMAP,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE,
				ImageResourceWrapper.COLUMN_SORT);
			
			List bitMaps = ResourceStorableObjectPool.getStorableObjectsByCondition(condition, true);

			for (Iterator it = bitMaps.iterator(); it.hasNext(); ) {
				BitmapImageResource ir = (BitmapImageResource)it.next();
				ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(ir.getImage()).getScaledInstance(30, 30, Image.SCALE_SMOOTH));
				ImagesPanelLabel ipl = new ImagesPanelLabel(disp, icon, ir);
				imagesPanel.add(ipl);
			}
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}

	public void operationPerformed(OperationEvent oe)
	{
		if(oe.getActionCommand().equals("select"))
		{
			ImagesPanelLabel ipl = (ImagesPanelLabel )oe.getSource();
			ir = (BitmapImageResource)ipl.ir;
			chooseButton.setEnabled(true);
		}
		else
		if(oe.getActionCommand().equals("selectir"))
		{
			ir = (BitmapImageResource)oe.getSource();
			chooseButton.setEnabled(true);
		}
	}

	public void setImageResource(BitmapImageResource ir)
	{
		disp.notify(new OperationEvent(ir, 0, "selectir"));
	}

	private void addButton_actionPerformed(ActionEvent e)
	{
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new ChoosableFileFilter("gif", "Picture"));
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				File file = chooser.getSelectedFile();
				FileInputStream in = new FileInputStream(file);
				byte[] data = new byte[(int) file.length()];
				in.read(data);
				in.close();

				/**
				 * @todo Fill in your own codename.
				 */
				BitmapImageResource bitmapImageResource = BitmapImageResource.createInstance(
						((RISDSessionInfo) applicationContext.getSessionInterface()).getUserIdentifier(),
						"Sample Codename",
						data);

				try
				{
					ResourceStorableObjectPool.putStorableObject(bitmapImageResource);
				}
				catch (IllegalObjectEntityException ioee)
				{
					ioee.printStackTrace();
				}

				imagesPanel.add(new ImagesPanelLabel(
						disp,
						new ImageIcon(
							Toolkit
							.getDefaultToolkit()
							.createImage(bitmapImageResource.getImage())
							.getScaledInstance(30, 30, Image.SCALE_SMOOTH)),
						bitmapImageResource));
				if (disp != null)
					disp.notify(new OperationEvent(bitmapImageResource, 0, "selectir"));
				imagesPanel.revalidate();
			} catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
			catch (CreateObjectException coe)
			{
				coe.printStackTrace();
			}
		}
	}

	private void jbInit()
	{
		this.setLayout(borderLayout1);

		flowLayout1.setAlignment(FlowLayout.LEFT);
		imagesPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		imagesPanel.setBackground(Color.white);
		imagesPanel.setLayout(flowLayout1);
		imagesPanel.setAutoscrolls(false);
		imagesPanel.setMaximumSize(new Dimension(0, 1300));
		imagesPanel.setMinimumSize(new Dimension(0, 100));
		imagesPanel.setPreferredSize(new Dimension(0, 100));

		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//		jScrollPane1.getViewport().setLayout();
		jScrollPane1.getViewport().add(imagesPanel);
		jScrollPane1.setWheelScrollingEnabled(true);
		this.add(jScrollPane1, BorderLayout.CENTER);

		chooseButton.setText("Выбрать");
		chooseButton.setEnabled(false);
		addButton.setText("Добавить");
		cancelButton.setText("Отменить");
		buttonsPanel.add(chooseButton, null);
		buttonsPanel.add(addButton, null);
		buttonsPanel.add(cancelButton, null);
		this.add(buttonsPanel, BorderLayout.SOUTH);

		addButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addButton_actionPerformed(e);
				}
			});
	}

}

package com.syrus.AMFICOM.Client.General.UI;
import com.syrus.AMFICOM.configuration.DomainCondition;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import java.util.List;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import java.awt.Image;

import javax.swing.JFileChooser;
import java.util.Enumeration;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import javax.swing.JScrollPane;
import java.util.Iterator;

public class ImagesPanel extends JPanel
		implements OperationListener
{
	private FlowLayout flowLayout1 = new FlowLayout();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel imagesPanel = new JPanel();
	public JPanel buttonsPanel = new JPanel();
	public JButton chooseButton = new JButton();
	private JButton addButton = new JButton();
	public JButton cancelButton = new JButton();

	Dispatcher disp = new Dispatcher();
	public AbstractImageResource ir = null;
	DataSourceInterface dsi = null;
	private JScrollPane jScrollPane1 = new JScrollPane();

	public ImagesPanel(DataSourceInterface dsi)
	{
		this.dsi = dsi;
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		initImages();
	}

	public ImagesPanel(DataSourceInterface dsi, AbstractImageResource ir)
	{
		this(dsi);
		setImageResource(ir);
	}

	private void jbInit() throws Exception
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

	public void initImages()
	{
		disp.register(this, "select");
		disp.register(this, "selectir");
		
		StorableObjectCondition condition = 
			new DomainCondition(null, ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE);
		
		List irs = null;

		try
		{
			irs = ResourceStorableObjectPool.getStorableObjectsByCondition(condition, true);
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
			return;
		}

		for(Iterator it = irs.iterator(); it.hasNext();)
		{
			AbstractImageResource ir = (AbstractImageResource )it.next();
			ImageIcon icon = new ImageIcon(ir.getImage());
			Image im = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			ImagesPanelLabel ipl = new ImagesPanelLabel(disp, new ImageIcon(im), ir);
			imagesPanel.add(ipl);
		}
	}

	public void operationPerformed(OperationEvent oe)
	{
		if(oe.getActionCommand().equals("select"))
		{
			ImagesPanelLabel ipl = (ImagesPanelLabel )oe.getSource();
			ir = (AbstractImageResource )ipl.ir;
			chooseButton.setEnabled(true);
		}
		else
		if(oe.getActionCommand().equals("selectir"))
		{
			ir = (AbstractImageResource )oe.getSource();
			chooseButton.setEnabled(true);
		}
	}

	public AbstractImageResource getImageResource()
	{
		return ir;
	}

	public void setImageResource(AbstractImageResource ir)
	{
		disp.notify(new OperationEvent(ir, 0, "selectir"));
	}

	private void addButton_actionPerformed(ActionEvent ae)
	{
		JFileChooser chooser = new JFileChooser();
		  chooser.addChoosableFileFilter(new ChoosableFileFilter("gif", "Picture"));
		  int returnVal = chooser.showOpenDialog(null);
		  if(returnVal == JFileChooser.APPROVE_OPTION)
		  {
			try
			{
				ir = new FileImageResource(
					IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE));
			}
			catch (ObjectNotFoundException e)
			{
				e.printStackTrace();
				return;
			}
			catch (RetrieveObjectException e)
			{
				e.printStackTrace();
				return;
			}
			catch (IllegalObjectEntityException e)
			{
				e.printStackTrace();
				return;
			}
			((FileImageResource )ir).setFileName(chooser.getSelectedFile().getName());
			try
			{
				ResourceStorableObjectPool.putStorableObject(ir);
			}
			catch (IllegalObjectEntityException e)
			{
				e.printStackTrace();
				return;
			}
//					chooser.getSelectedFile().getAbsolutePath());
			ImageIcon icon = new ImageIcon(ir.getImage());
			Image im = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			ImagesPanelLabel ipl = new ImagesPanelLabel(disp, new ImageIcon(im), ir);

			imagesPanel.add(ipl);
			if(disp != null)
				disp.notify(new OperationEvent(ir, 0, "selectir"));
			imagesPanel.revalidate();
		  }
	}

}

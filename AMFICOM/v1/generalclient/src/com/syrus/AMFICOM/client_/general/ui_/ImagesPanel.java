package com.syrus.AMFICOM.client_.general.ui_;

import java.io.*;
import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.corba.ImageResourceSort;

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
	public BitmapImageResource ir = null;
	private JScrollPane jScrollPane1 = new JScrollPane();

	ApplicationContext aContext;

	public ImagesPanel(ApplicationContext aContext)
	{
		this.aContext = aContext;
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

	public ImagesPanel(ApplicationContext aContext, BitmapImageResource ir)
	{
		this(aContext);
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

		try {
			StringFieldCondition condition = new StringFieldCondition(
					String.valueOf(ImageResourceSort._BYTES),
					ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE,
					StringFieldSort.STRINGSORT_INTEGER);
			List bitMaps = ResourceStorableObjectPool.getStorableObjectsByCondition(condition, true);

			for (Iterator it = bitMaps.iterator(); it.hasNext(); ) {
				BitmapImageResource ir = (BitmapImageResource)it.next();
				ImageIcon icon = new ImageIcon(ir.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
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

	public BitmapImageResource getImageResource()
	{
		return ir;
	}

	public void setImageResource(BitmapImageResource ir)
	{
		disp.notify(new OperationEvent(ir, 0, "selectir"));
	}

	private void addButton_actionPerformed(ActionEvent e)
	{
		JFileChooser chooser = new JFileChooser();
			chooser.addChoosableFileFilter(new ChoosableFileFilter("gif", "Picture"));
			int returnVal = chooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				try {
					FileInputStream in = new FileInputStream(chooser.getSelectedFile());
					byte[] data = new byte[(int)chooser.getSelectedFile().length()];
					in.read(data);

					Identifier user_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
							getAccessIdentifier().domain_id);
					Date date = new Date(System.currentTimeMillis());
					BitmapImageResource ir = BitmapImageResource.createInstance(
							date, date, user_id, user_id, new ImageIcon(data).getImage());

					try {
						ResourceStorableObjectPool.putStorableObject(ir);
					}
					catch (IllegalObjectEntityException ex1) {
						ex1.printStackTrace();
					}

					ImageIcon icon = new ImageIcon(ir.getImage().getScaledInstance(30, 30,
							Image.SCALE_SMOOTH));
					ImagesPanelLabel ipl = new ImagesPanelLabel(disp, icon, ir);
					imagesPanel.add(ipl);
					if (disp != null) {
						disp.notify(new OperationEvent(ir, 0, "selectir"));
					}
					imagesPanel.revalidate();
				}
				catch (IOException ex) {
				}
			}
	}

}

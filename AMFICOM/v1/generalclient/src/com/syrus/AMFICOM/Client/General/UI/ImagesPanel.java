package com.syrus.AMFICOM.Client.General.UI;
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
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
import com.syrus.AMFICOM.Client.General.Event.*;
import javax.swing.JScrollPane;

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
	public ImageResource ir = null;
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

	public ImagesPanel(DataSourceInterface dsi, ImageResource ir)
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
		for(Enumeration enum = ImageCatalogue.getAll(); enum.hasMoreElements();)
		{
			ImageResource ir = (ImageResource )enum.nextElement();
			ImageIcon icon = new ImageIcon(ir.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
			ImagesPanelLabel ipl = new ImagesPanelLabel(disp, icon, ir);
			imagesPanel.add(ipl);
		}
	}

	public void operationPerformed(OperationEvent oe)
	{
		if(oe.getActionCommand().equals("select"))
		{
			ImagesPanelLabel ipl = (ImagesPanelLabel )oe.getSource();
			ir = (ImageResource )ipl.ir;
			chooseButton.setEnabled(true);
		}
		else
		if(oe.getActionCommand().equals("selectir"))
		{
			ir = (ImageResource )oe.getSource();
			chooseButton.setEnabled(true);
		}
	}

	public ImageResource getImageResource()
	{
		return ir;
	}

	public void setImageResource(ImageResource ir)
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
			ImageResource ir = new ImageResource(
					dsi.GetUId(ImageResource.typ),
					chooser.getSelectedFile().getName(),
					chooser.getSelectedFile().getAbsolutePath());
			ImageCatalogue.add(ir.getId(), ir);
			ImageIcon icon = new ImageIcon(ir.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
			ImagesPanelLabel ipl = new ImagesPanelLabel(disp, icon, ir);
			imagesPanel.add(ipl);
			if(disp != null)
				disp.notify(new OperationEvent(ir, 0, "selectir"));
			imagesPanel.revalidate();
		  }
	}

}

package com.syrus.AMFICOM.Client.Schematics.UI;

import java.text.SimpleDateFormat;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class SchemeCableLinkFibrePanel extends GeneralPanel
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	SchemeCableLink cl;
	private JPanel linksPanel = new JPanel();
	private JPanel labelsPanel = new JPanel();
	private JPanel controlsPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
	private JLabel linksTypeLabel = new JLabel();
	private JLabel linksNameLabel = new JLabel();
	private JLabel linksIdLabel = new JLabel();
	private ObjectResourceComboBox linksTypeBox = new ObjectResourceComboBox(LinkType.typ, true);
	private JTextField linksNameField = new JTextField();
	private JTextField linksIdField = new JTextField();
	private JPanel listPanel = new JPanel();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private BorderLayout borderLayout1 = new BorderLayout();
	private ObjectResourceListBox LinksList = new ObjectResourceListBox(SchemeCableThread.typ);
	private BorderLayout borderLayout2 = new BorderLayout();
	private BorderLayout borderLayout3 = new BorderLayout();

	public SchemeCableLinkFibrePanel()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public SchemeCableLinkFibrePanel(SchemeCableLink cl)
	{
		this();
		setObjectResource(cl);
	}

	private void jbInit() throws Exception
	{
		setName("Волокна");

//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));
		this.setLayout(borderLayout2);

		linksPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		linksPanel.setLayout(borderLayout3);
		labelsPanel.setLayout(verticalFlowLayout2);
		labelsPanel.setMinimumSize(new Dimension(145, 125));
		controlsPanel.setLayout(verticalFlowLayout1);
		linksTypeLabel.setText("Тип");
		linksTypeLabel.setPreferredSize(new Dimension(140, 24));
		linksNameLabel.setText("Название");
		linksNameLabel.setPreferredSize(new Dimension(140, 24));
		linksIdLabel.setText("Идентификатор");
		linksIdLabel.setPreferredSize(new Dimension(140, 24));
		listPanel.setLayout(borderLayout1);
//		listPanel.setMinimumSize(new Dimension(145, 125));
		jScrollPane1.setPreferredSize(new Dimension(145, 125));
		jScrollPane1.setMinimumSize(new Dimension(145, 125));
		jScrollPane1.setMaximumSize(new Dimension(145, 125));
		jScrollPane1.setSize(new Dimension(145, 125));
		LinksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		LinksList.setBorder(BorderFactory.createLoweredBevelBorder());
		LinksList.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					LinksList_valueChanged(e);
				}
			});

		jScrollPane1.getViewport();
		jScrollPane1.getViewport().add(LinksList, null);
		listPanel.add(jScrollPane1, BorderLayout.CENTER);
		this.add(listPanel, BorderLayout.WEST);
		this.add(linksPanel, BorderLayout.CENTER);
		controlsPanel.add(linksIdField, null);
		controlsPanel.add(linksNameField, null);
		controlsPanel.add(linksTypeBox, null);
		linksPanel.add(controlsPanel, BorderLayout.CENTER);
		labelsPanel.add(linksIdLabel, null);
		labelsPanel.add(linksNameLabel, null);
		labelsPanel.add(linksTypeLabel, null);
		linksPanel.add(labelsPanel, BorderLayout.WEST);
	}

	public SchemeCableThread getSelectedThread()
	{
		return (SchemeCableThread)LinksList.getSelectedObjectResource();
	}

	public ObjectResource getObjectResource()
	{
		return cl;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.cl = (SchemeCableLink )or;

		this.LinksList.setContents("");
		this.linksIdField.setText("");
		this.linksNameField.setText("");
		this.linksTypeBox.setSelected("");

		if(cl != null)
		{
			ObjectResourceSorter sorter = SchemeCableThread.getDefaultSorter();
			sorter.setDataSet(cl.cable_threads);
			LinksList.setContents(sorter.default_sort());
		}
	}

	public void setEditable(boolean b)
	{
		linksTypeBox.setEnabled(b);
		linksNameField.setEditable(b);
		linksIdField.setEditable(b);
	}

	public boolean modify()
	{
		try
		{
			SchemeCableThread clt = (SchemeCableThread)LinksList.getSelectedObjectResource();

			clt.id = this.linksIdField.getText();
			clt.name = this.linksNameField.getText();
			clt.link_type_id = (String )this.linksTypeBox.getSelected();
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}

	private void LinksList_valueChanged(ListSelectionEvent e)
	{
		if(e.getValueIsAdjusting())
			return;
		SchemeCableThread clt = (SchemeCableThread)LinksList.getSelectedObjectResource();
		if (clt != null)
		{
			this.linksIdField.setText(clt.getId());
			linksNameField.setCaretPosition(0);
			this.linksNameField.setText(clt.getName());
			linksNameField.setCaretPosition(0);
			this.linksTypeBox.setSelected(clt.link_type_id);
		}
	}
}

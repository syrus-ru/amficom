package com.syrus.AMFICOM.Client.General.UI;

import oracle.jdeveloper.layout.*;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ObjectResourceCatalogButtonsPanel extends JPanel
{
	public ApplicationContext aContext;
	Dispatcher dispatcher = new Dispatcher();


	public JButton buttonAdd = new JButton();
	public JButton buttonCancel = new JButton();
	public JButton buttonDelete = new JButton();
	public JButton buttonProperties = new JButton();
	public JButton buttonSave = new JButton();

	PropertiesPanel pp;

	public ObjectResourceCatalogButtonsPanel(PropertiesPanel pp)
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
		setPropertiesPanel(pp);
	}

	public void setPropertiesPanel(PropertiesPanel pp)
	{
		this.pp = pp;
	}

	public void setActionModel(ObjectResourceCatalogActionModel orcam)
	{
		removeAll();
		buttonAdd.setVisible(orcam.add_button);
		buttonCancel.setVisible(orcam.cancel_button);
		buttonDelete.setVisible(orcam.remove_button);
		buttonProperties.setVisible(orcam.props_button);
		buttonSave.setVisible(orcam.save_button);

		if(orcam.add_button)
			add(buttonAdd);
		if(orcam.save_button)
			add(buttonSave);
		if(orcam.remove_button)
			add(buttonDelete);
		if(orcam.props_button)
			add(buttonProperties);
		if(orcam.cancel_button)
			add(buttonCancel);
	}
	
	private void jbInit() throws Exception
	{
		buttonAdd.setText("Новый");
//		buttonAdd.setSize(new Dimension (50, 8));
//		buttonAdd.setFocusable(false);
		buttonAdd.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonAdd.setBorderPainted(false);

		buttonAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonAdd_actionPerformed(e);
			}
		});
		buttonCancel.setText("Отменить");
//		buttonCancel.setSize(new Dimension (50, 8));
//		buttonCancel.setFocusable(false);
		buttonCancel.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonCancel.setBorderPainted(false);
		buttonCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonCancel_actionPerformed(e);
			}
		});
		buttonDelete.setText("Удалить");
//		buttonDelete.setSize(new Dimension (50, 8));
//		buttonDelete.setFocusable(false);
		buttonDelete.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonDelete.setBorderPainted(false);
		buttonDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonDelete_actionPerformed(e);
			}
		});
		buttonProperties.setText("Свойства");
//		buttonProperties.setSize(new Dimension (50, 8));
//		buttonProperties.setFocusable(false);
		buttonProperties.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonProperties.setBorderPainted(false);
		buttonProperties.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonProperties_actionPerformed(e);
			}
		});

		buttonSave.setText("Сохранить");
//		buttonSave.setSize(new Dimension (50, 8));
//		buttonSave.setFocusable(false);
		buttonSave.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
		buttonSave.setBorderPainted(false);
		buttonSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttonSave_actionPerformed(e);
			}
		});

		setBorder(BorderFactory.createEtchedBorder());
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		fl.setHgap(1);
		fl.setVgap(0);
		setLayout(fl);
		setPreferredSize(new Dimension(575, 24));
		add(buttonAdd, new XYConstraints(10, 0, -1, 20));
		add(buttonSave, new XYConstraints(60, 0, -1, 20));
		add(buttonDelete, new XYConstraints(125, 0, -1, 20));
		add(buttonProperties, new XYConstraints(180, 0, -1, 20));
		add(buttonCancel, new XYConstraints(240, 0, -1, 20));

		buttonCancel.setEnabled(false);
		buttonSave.setEnabled(false);
		buttonDelete.setEnabled(false);
		buttonProperties.setEnabled(false);
	}

	void buttonProperties_actionPerformed(ActionEvent e)
	{
/*
		ObjectResource or  = (ObjectResource)listPane.getSelectedObject();
		if(or != null)
		{
			if(!propPane.setObjectResource(or))
				return;
				jTabbedPane.setSelectedComponent(propScrollPane);
		}
*/
	}

	void buttonDelete_actionPerformed(ActionEvent e)
	{
/*
		ObjectResource or = (ObjectResource)listPane.getSelectedObject();

		if(!propPane.delete())
			return;

		dataSet.remove(or);
		listPane.tableModel.fireTableDataChanged();

		dispatcher.notify(new OperationEvent(or.getTyp(), 0, "treelistrefreshevent"));
		buttonCancel.setEnabled(true);
		buttonSave.setEnabled(true);
		jTabbedPane.setSelectedComponent(listPane);
		if (dataSet.size() == 0)
		{
			jTabbedPane.setDisabledIconAt(1, new TextIcon(((JComponent)propPane).getName(), jTabbedPane, false));
			jTabbedPane.setEnabledAt(1, false);
			buttonProperties.setEnabled(false);
			buttonDelete.setEnabled(false);
		}
		else
		{
			ObjectResource obj = (ObjectResource )dataSet.elements().nextElement();
			listPane.setSelected(obj);
			send_event = true;
			dispatcher.notify(new OperationEvent(obj, 0, "treelistselectionevent"));
			send_event = false;
		}
*/
	}

	void buttonCancel_actionPerformed(ActionEvent e)
	{
	}

	void buttonAdd_actionPerformed(ActionEvent e)
	{
/*
		if(!propPane.create())
			return;

		ObjectResource res = propPane.getObjectResource();
		dataSet.add(res);

		listPane.tableModel.fireTableDataChanged();
		dispatcher.notify(new OperationEvent(res.getTyp(), 0, "treelistrefreshevent"));

		send_event = true;
		dispatcher.notify(new OperationEvent(res, 0, "treelistselectionevent"));
		send_event = false;

//		listPane.tableModel.fireTableDataChanged();
		listPane.setSelected(res);
		jTabbedPane.setSelectedComponent(propScrollPane);
		buttonSave.setEnabled(true);
*/
	}

	void buttonSave_actionPerformed(ActionEvent e)
	{
/*
		if(!propPane.save())
			return;
		
		dispatcher.notify(new OperationEvent(propPane.getObjectResource().getTyp(), 0, "treelistrefreshevent"));

		buttonSave.setEnabled(false);
		buttonCancel.setEnabled(false);
*/
	}
}

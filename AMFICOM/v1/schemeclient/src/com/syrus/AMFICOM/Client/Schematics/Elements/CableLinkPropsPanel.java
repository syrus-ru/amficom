package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.UI.PopupNameFrame;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.*;

public class CableLinkPropsPanel extends JPanel
{
	private AComboBox sortComboBox = new AComboBox();

	private ObjComboBox typeComboBox = new ObjComboBox(
			 LinkTypeController.getInstance(),
			 LinkTypeController.KEY_NAME);
	private JButton addTypeButton = new JButton("...");
	private JTextArea descriptionTextArea = new JTextArea();
	private JTextField manufacturerTextField = new JTextField();
	private JTextField nameText = new JTextField();
	private JTextField optLen = new JTextField();
	private JTextField strLen = new JTextField();
	private String undoDescr;
	private String undoManufacturer;
	ApplicationContext aContext;
	private boolean skip_changes = false;

	private boolean smooth_length = false;

	SchemeCableLink[] links;
	List cablelinkTypes;
	CableLinkType lt;

	private static LinkTypeSort[] linkTypeSorts = new LinkTypeSort[] {
		LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER,
		LinkTypeSort.LINKTYPESORT_ETHERNET,
		LinkTypeSort.LINKTYPESORT_GSM
	};

	public CableLinkPropsPanel(ApplicationContext aContext)
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
	}
	private void jbInit() throws Exception
	{
		JPanel p1 = new JPanel(new BorderLayout());
		JPanel p2 = new JPanel(new BorderLayout());

		JPanel p11 = new JPanel(new BorderLayout());
		JPanel p12 = new JPanel(new BorderLayout());
		JPanel p13 = new JPanel(new BorderLayout());
		JPanel p21 = new JPanel(new BorderLayout());
		JPanel p22 = new JPanel(new BorderLayout());
		JPanel p221 = new JPanel(new BorderLayout());
		JPanel p222 = new JPanel(new BorderLayout());

		setLayout(new BorderLayout());
		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);

		JPanel clLabelPanel = new JPanel();
		clLabelPanel.setPreferredSize(new Dimension (60, 10));
		clLabelPanel.add(new JLabel("Класс"));

		JPanel typeLabelPanel = new JPanel();
		typeLabelPanel.setPreferredSize(new Dimension (60, 10));
		typeLabelPanel.add(new JLabel("Тип"));

		JPanel nameLabelPanel = new JPanel();
		nameLabelPanel.setPreferredSize(new Dimension (60, 10));
		nameLabelPanel.add(new JLabel("Название"));

		JPanel descrLabelPanel = new JPanel();
		descrLabelPanel.setPreferredSize(new Dimension (60, 10));
		descrLabelPanel.add(new JLabel("Описание"));

		JPanel manLabelPanel = new JPanel();
		manLabelPanel.setPreferredSize(new Dimension (60, 10));
		manLabelPanel.add(new JLabel("Произв."));

		JPanel lengthLabelPanel = new JPanel();
		lengthLabelPanel.setPreferredSize(new Dimension (60, 10));
		lengthLabelPanel.add(new JLabel("Длина(м)"));

		JPanel lenPanel1 = new JPanel(new BorderLayout());
		JPanel lenPanel2 = new JPanel(new BorderLayout());

		lenPanel1.add(new JLabel(" Строит. "), BorderLayout.WEST);
		lenPanel1.add(strLen, BorderLayout.CENTER);
		lenPanel2.add(new JLabel(" Оптич. "), BorderLayout.WEST);
		lenPanel2.add(optLen, BorderLayout.CENTER);

		JSplitPane lenPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, lenPanel1, lenPanel2);
		lenPanel.setBorder(BorderFactory.createEmptyBorder());
		lenPanel.setResizeWeight(.5);
		lenPanel.setDividerSize(0);

		p11.add(clLabelPanel, BorderLayout.WEST);
		p12.add(typeLabelPanel, BorderLayout.WEST);
		p13.add(nameLabelPanel, BorderLayout.WEST);
		p21.add(descrLabelPanel, BorderLayout.WEST);
		p221.add(manLabelPanel, BorderLayout.WEST);
		p222.add(lengthLabelPanel, BorderLayout.WEST);

		p11.add(sortComboBox, BorderLayout.CENTER);
		p12.add(typeComboBox, BorderLayout.CENTER);
		p13.add(nameText, BorderLayout.CENTER);
		JScrollPane scroll = new JScrollPane(descriptionTextArea);
		p21.add(scroll, BorderLayout.CENTER);
		p221.add(manufacturerTextField, BorderLayout.CENTER);
		p222.add(lenPanel, BorderLayout.CENTER);

		p12.add(addTypeButton, BorderLayout.EAST);

		typeComboBox.setPreferredSize(sortComboBox.getPreferredSize());

		p1.add(p11, BorderLayout.NORTH);
		p1.add(p12, BorderLayout.CENTER);
		p1.add(p13, BorderLayout.SOUTH);
		p2.add(p21, BorderLayout.CENTER);
		p2.add(p22, BorderLayout.SOUTH);
		p22.add(p221, BorderLayout.CENTER);
		p22.add(p222, BorderLayout.SOUTH);

		addTypeButton.setPreferredSize(new Dimension(25, 7));
		addTypeButton.setBorder(BorderFactory.createEtchedBorder());
		addTypeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				addTypeButton_actionPerformed();
			}
		});
		sortComboBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ie)
			{
				if (ie.getStateChange() == ItemEvent.SELECTED)
					sortComboBox_stateChanged();
			}
		});

		typeComboBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ie)
			{
				if (ie.getStateChange() == ItemEvent.SELECTED)
					typeComboBox_stateChanged();
			}
		});
		descriptionTextArea.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (lt == null)
					return;
				lt.setDescription(descriptionTextArea.getText());
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		manufacturerTextField.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (lt == null)
					return;
				lt.setManufacturer(manufacturerTextField.getText());
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		nameText.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (links == null || links.length != 1)
					return;
				links[0].name(nameText.getText());
				aContext.getDispatcher().notify(new SchemeElementsEvent(links[0].id(), links[0].name(), SchemeElementsEvent.CABLE_LINK_NAME_UPDATE_EVENT));
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		optLen.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (links == null || links.length != 1)
					return;
				try
				{
					double d = Double.parseDouble(optLen.getText());
					links[0].opticalLength(d);
					optLen.setForeground(nameText.getForeground());
					if (smooth_length)
					{
						links[0].physicalLength(d);
						strLen.setText(optLen.getText());
						strLen.setForeground(nameText.getForeground());
					}
				}
				catch (NumberFormatException e)
				{
					optLen.setForeground(Color.red);
				}
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		strLen.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (links == null || links.length != 1)
					return;
				try
				{
					double d = Double.parseDouble(strLen.getText());
					links[0].physicalLength(d);
					strLen.setForeground(nameText.getForeground());
					if (smooth_length)
					{
						links[0].opticalLength(d);
						optLen.setText(strLen.getText());
						optLen.setForeground(nameText.getForeground());
					}
				}
				catch (NumberFormatException e)
				{
					strLen.setForeground(Color.red);
				}
			}
			public void keyPressed(KeyEvent ae)
					{}
		});

		descriptionTextArea.setPreferredSize(new Dimension (300, 80));
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setAutoscrolls(true);

		try {
			DomainCondition condition = new DomainCondition(null, ObjectEntities.CABLE_LINKTYPE_ENTITY_CODE);
			cablelinkTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

			for (int i = 0; i < linkTypeSorts.length; i++) {
				sortComboBox.addItem(linkTypeSorts[i]);
			}
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}

	public void setEditable(boolean b)
	{
		sortComboBox.setEnabled(b);
		addTypeButton.setEnabled(b);
		typeComboBox.setEnabled(b);
		typeComboBox.setEnabled(b);
		if (links != null && links.length != 1)
		{
			nameText.setEnabled(false);
			optLen.setEnabled(false);
			strLen.setEnabled(false);
		}
		else
		{
			nameText.setEnabled(b);
			optLen.setEnabled(b);
			strLen.setEnabled(b);
		}
		descriptionTextArea.setEnabled(b);
		manufacturerTextField.setEnabled(b);
	}

	public void init(SchemeCableLink[] links)
	{
		this.links = links;
		lt = links[0].cableLinkTypeImpl();

		if (lt != null)
		{
			sortComboBox.setSelectedItem(lt.getSort());
			typeComboBox.setSelectedItem(lt);
			descriptionTextArea.setText(lt.getDescription());

			undoManufacturer = lt.getManufacturer();
			undoDescr = lt.getDescription();
		}
		else
			typeComboBox_stateChanged();

		if (links.length == 1)
		{
			if (links[0].opticalLength() == 0 || links[0].physicalLength() == 0)
				smooth_length = true;

			nameText.setText(links[0].name());
			nameText.setCaretPosition(0);
			optLen.setText(String.valueOf(links[0].opticalLength()));
			strLen.setText(String.valueOf(links[0].physicalLength()));
		}
		else
		{
			nameText.setText("<множественный выбор>");
			nameText.setCaretPosition(0);
			optLen.setText("<...>");
			strLen.setText("<...>");
			nameText.setEnabled(false);
			optLen.setEnabled(false);
			strLen.setEnabled(false);
		}
		updateUI();
	}

	public void undo()
	{
		if (lt != null)
		{
			lt.setDescription(undoDescr);
			lt.setManufacturer(undoManufacturer);
		}
	}

	public CableLinkType getSelectedLinkType()
	{
		return (CableLinkType)typeComboBox.getSelectedItem();
	}

	void sortComboBox_stateChanged()
	{
		typeComboBox.removeAll();
		LinkTypeSort sort = (LinkTypeSort)sortComboBox.getSelectedItem();

		for (Iterator it = cablelinkTypes.iterator(); it.hasNext();)
		{
			CableLinkType type = (CableLinkType)it.next();
			if (type.getSort().equals(sort))
				typeComboBox.addItem(type);
		}
		if (lt != null)
			typeComboBox.setSelectedItem(lt);

		typeComboBox_stateChanged();
	}

	void typeComboBox_stateChanged()
	{
		if (skip_changes)
			return;
		undo();
		if (typeComboBox.getItemCount() == 0)
			return;

		int old_num = lt.getCableThreadTypes().size();
		lt = (CableLinkType)typeComboBox.getSelectedItem();
		int num = lt.getCableThreadTypes().size();

		CableThreadType ctt = (CableThreadType)lt.getCableThreadTypes().get(0);
		String codename = ctt.getCodename();
		Identifier user_id = new Identifier(((RISDSessionInfo)aContext.
							getSessionInterface()).getAccessIdentifier().user_id);

		if (num > old_num)
		{
			for (int i = 0; i < links.length; i++)
			{
				links[i].cableLinkTypeImpl(lt);
				Iterator it = lt.getCableThreadTypes().iterator();
				for (int j = 0; j < old_num; j++)
					links[i].schemeCableThreads()[j].cableThreadTypeImpl((CableThreadType)it.next());
				for (int j = old_num; j < num; j++) {
					SchemeCableThread newct = SchemeFactory.createSchemeCableThread();
					CableThreadType type = (CableThreadType)it.next();
					newct.cableThreadTypeImpl(type);
					newct.name(String.valueOf(j));
					newct.schemeCablelink(links[0]);
				}
			}
		}
		else
		{
			for (int i = 0; i < links.length; i++)
			{
				links[i].cableLinkTypeImpl(lt);
				Iterator it = lt.getCableThreadTypes().iterator();
				for (int j = 0; j < num; j++)
					links[i].schemeCableThreads()[j].cableThreadTypeImpl((CableThreadType)it.next());

					List toDelete = new LinkedList();
					List threads = Arrays.asList(links[i].schemeCableThreads());
					for (int j = old_num-1; j >= num; j--)
					{
						SchemeCableThread thread = (SchemeCableThread)threads.get(j);
						threads.remove(thread);
						//CableTypeThread ctt = (CableTypeThread)clt.cable_threads.get(i);
						toDelete.add(thread.id());
					}
					try {
						SchemeStorableObjectPool.delete(toDelete);
					}
					catch (ApplicationException ex) {
						ex.printStackTrace();
					}
			}
		}
		descriptionTextArea.setText(lt.getDescription());
		manufacturerTextField.setText(lt.getManufacturer());
		manufacturerTextField.setCaretPosition(0);
		descriptionTextArea.setCaretPosition(0);
		//aContext.getDispatcher().notify(new OperationEvent(clt, 1, "elementslistvaluechanged"));
	 // aContext.getDispatcher().notify(new SchemeElementsEvent(links, clt, SchemeElementsEvent.OBJECT_TYPE_UPDATE_EVENT));
	}

	void addTypeButton_actionPerformed()
	{
	}
}

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;

/**
 * Панель "Свойства теста" 
 * @author ??? , Vladimir Dolzhenko
 */

public class TestRequestPanel extends JPanel implements OperationListener {
	TestRequest treq;

	private JTextField nameTextField = new JTextField();
	private JTextField ownerTextField = new JTextField();
	private JTextField typeTextField = new JTextField();
	private ObjectResourceListBox testList = new ObjectResourceListBox();

	private Hashtable tests;
	private ApplicationContext aContext;
	private Dispatcher dispatcher;
	private boolean skip = false;

	public TestRequestPanel(ApplicationContext aContext) {
		this.aContext = aContext;
		initModule(aContext.getDispatcher());
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(
			this,
			TimeParametersFrame.COMMAND_DATA_REQUEST);
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		System.out.println(getClass().getName() +" commandName: " + commandName);
		if (commandName
			.equalsIgnoreCase(TimeParametersFrame.COMMAND_DATA_REQUEST)) {
			/**
			 * @todo must send data edit in this form 
			 */
			dispatcher.notify(
				new OperationEvent(
					"",
					0,
					TimeParametersFrame.COMMAND_SEND_DATA));
		}
	}

	public void setTestRequest(TestRequest treq) {
		if (skip)
			return;

		this.treq = treq;
		nameTextField.setText(treq.getName());
		ownerTextField.setText(treq.user_id);
		nameTextField.setCaretPosition(0);
		ownerTextField.setCaretPosition(0);

		tests = new Hashtable();
		Hashtable ht = new Hashtable();
		String type = "";
		//for (Enumeration enum = treq.test_ids.elements(); enum.hasMoreElements();)
		for (int i = 0; i < treq.test_ids.size(); i++) {
			String test_id = (String) treq.test_ids.get(i);
			Test test = (Test) Pool.get(Test.typ, test_id);
			MonitoredElement me =
				(MonitoredElement) Pool.get(
					MonitoredElement.typ,
					test.monitored_element_id);
			ht.put(me.getId(), me);
			tests.put(me.getId(), test);

			type = Pool.getName(TestType.typ, test.test_type_id);
		}
		testList.setContents(ht);
		typeTextField.setText(type);
		typeTextField.setCaretPosition(0);
	}

	public void addTest(Test test) {
		if (treq != null)
			treq.test_ids.add(test.getId());
	}

	public void addTest(String test_id) {
		if (treq != null)
			treq.test_ids.add(test_id);
	}

	public void removeTest(Test test) {
		if (treq != null)
			treq.test_ids.remove(test.getId());
	}

	public void removeTest(String test_id) {
		if (treq != null)
			treq.test_ids.remove(test_id);
	}

	private void jbInit() throws Exception {
		/*
		setLayout(new BorderLayout());
		
		JPanel namePanel = new JPanel(new BorderLayout());
		JPanel nameLabelPanel = new JPanel();
		nameLabelPanel.add(new JLabel("Название:"));
		nameLabelPanel.setPreferredSize(new Dimension(label_width, 0));
		namePanel.add(nameLabelPanel, BorderLayout.WEST);
		namePanel.add(nameTextField, BorderLayout.CENTER);
		
		JPanel ownerPanel = new JPanel(new BorderLayout());
		JPanel ownerLabelPanel = new JPanel();
		ownerLabelPanel.add(new JLabel("Владелец:"));
		ownerLabelPanel.setPreferredSize(new Dimension(label_width, 0));
		ownerPanel.add(ownerLabelPanel, BorderLayout.WEST);
		ownerPanel.add(ownerTextField, BorderLayout.CENTER);
		
		JPanel typePanel = new JPanel(new BorderLayout());
		JPanel typeLabelPanel = new JPanel();
		typeLabelPanel.add(new JLabel("Тип:"));
		typeLabelPanel.setPreferredSize(new Dimension(label_width, 0));
		typePanel.add(typeLabelPanel, BorderLayout.WEST);
		typePanel.add(typeTextField, BorderLayout.CENTER);
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(namePanel, BorderLayout.NORTH);
		northPanel.add(ownerPanel, BorderLayout.CENTER);
		northPanel.add(typePanel, BorderLayout.SOUTH);
		
		JScrollPane listPanel = new JScrollPane(testList);
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel listLabelPanel = new JPanel();
		listLabelPanel.add(new JLabel("Объект:"));
		listLabelPanel.setPreferredSize(new Dimension(label_width, 0));
		centerPanel.add(listLabelPanel, BorderLayout.WEST);
		centerPanel.add(listPanel, BorderLayout.CENTER);
		
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		*/
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		this.setLayout(gridbag);
		JLabel titleLabel = new JLabel("Название:");
		gbc.weightx = 0.0;
		gbc.gridheight = 1;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(titleLabel, gbc);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(nameTextField, gbc);

		JLabel ownerLabel = new JLabel("Владелец:");
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(ownerLabel, gbc);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(ownerTextField, gbc);

		JLabel typeLabel = new JLabel("Тип:");
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.gridheight = GridBagConstraints.RELATIVE;
		gbc.weightx = 0.0;
		add(typeLabel, gbc);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(typeTextField, gbc);

		JLabel objectLabel = new JLabel("Объект:");
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		add(objectLabel, gbc);
		gbc.weightx = 4.0;
		gbc.weighty = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		JScrollPane listPanel = new JScrollPane(testList);
		add(listPanel, gbc);

		testList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting())
					return;
				MonitoredElement me =
					(MonitoredElement) testList.getSelectedObjectResource();
				if (me == null)
					aContext.getDispatcher().notify(
						new TestUpdateEvent(
							this,
							null,
							TestUpdateEvent.TEST_DESELECTED_EVENT));
				else {
					skip = true;
					Test test = (Test) tests.get(me.getId());
					aContext.getDispatcher().notify(
						new TestUpdateEvent(
							this,
							test,
							TestUpdateEvent.TEST_SELECTED_EVENT));
					skip = false;
				}
			}
		});
	}
}

/*
 * SaveParametersFrame.java Created on 17.05.2004 18:23:26
 *  
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestController;

/**
 * @author Vladimir Dolzhenko
 */
public class TableFrame extends JInternalFrame implements OperationListener {

	Dispatcher			dispatcher;
	SchedulerModel				schedulerModel;
	ObjectResourceTable	listTable;
	ApplicationContext	aContext;
	private JPanel		panel;
	private Test		test;
	java.util.List		rowToRemove;
	private Command		command;

	public TableFrame(ApplicationContext aContext) {
		this.aContext = aContext;
		if (aContext != null) {
			initModule(aContext.getDispatcher());
			this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		}
		init();
		this.command = new WindowCommand(this);
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(SchedulerModel.COMMAND_REFRESH_TEST)) {
			Test test = this.schedulerModel.getSelectedTest();
			if ((this.test == null) || (!this.test.getId().equals(test.getId()))) {
				Collection savedTests = ((SchedulerModel) this.aContext.getApplicationModel()).getTests();
				this.test = test;

				ObjectResourceTableModel tableModel = (ObjectResourceTableModel) this.listTable.getModel();
				int rowIndex = tableModel.getIndexOfObject(test);

				if (rowIndex<0) {
					if (test.isChanged()) {
						tableModel.getContents().add(test);
						this.listTable.repaint();
						this.listTable.revalidate();
					} else
						savedTests.add(test);
				}
				rowIndex = ((ObjectResourceTableModel) this.listTable.getModel()).getIndexOfObject(test);
				this.listTable.setRowSelectionInterval(rowIndex, rowIndex);

			}

		} else if (commandName.equals(SchedulerModel.COMMAND_REFRESH_TESTS)) {
			this.listTable.removeAll();
			this.setSavedTests();
			this.listTable.revalidate();
			this.listTable.repaint();

		} 
	}

	public void setSavedTests() {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				ObjectResourceTableModel model = (ObjectResourceTableModel) TableFrame.this.listTable.getModel();
				model.clear();
				Collection tests = ((SchedulerModel) TableFrame.this.aContext.getApplicationModel()).getTests();
				//System.out.println("tests.size:"+tests.size());
				for (Iterator it = tests.iterator(); it.hasNext();) {
					Test test = (Test) it.next();
					if (model.getIndexOfObject(test) < 0) {
						model.getContents().add(test);
					}
				}
			}
		});
	}

	private JPanel getPanel() {
		if (this.panel == null) {
			this.panel = new JPanel(new BorderLayout());

			this.listTable = new ObjectResourceTable(TestController.getInstance());
			this.listTable.setDefaultTableCellRenderer();
			ListSelectionModel rowSM = this.listTable.getSelectionModel();
			rowSM.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting())
						return;

					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (!lsm.isSelectionEmpty()) {
						int selectedRow = lsm.getMinSelectionIndex();
						TableFrame.this.schedulerModel.setSelectedTest((Test) ((ObjectResourceTableModel) TableFrame.this.listTable.getModel())
							.getObject(selectedRow));
					}
				}

			});

			this.listTable.addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					mouseClicked(e);
				}

				public void mouseClicked(MouseEvent evt) {
					final JTable table = ((JTable) evt.getSource());
					//					if (SwingUtilities.isLeftMouseButton(evt)) {
					//						int rowIndex = table.getSelectedRow();
					//						TestTableModel model = (TestTableModel) table.getModel();
					//						TestTableRow line = (TestTableRow)
					// model.getRow(rowIndex);
					//						if (model != null) {
					//							//System.out.println("test:" + line.getTest());
					//							Test test = (Test) line.getObjectResource();
					//							//TableFrame.this.skipTestUpdate = true;
					//							TableFrame.this.dispatcher
					//									.notify(new TestUpdateEvent(this, test,
					// TestUpdateEvent.TEST_SELECTED_EVENT));
					//							//System.out.println("send test:"+test.getId());
					//							//TableFrame.this.skipTestUpdate = false;
					//						}
					//					} else
					if (SwingUtilities.isRightMouseButton(evt)) {
						System.out.println("RightMouseButton");
						final int[] rowIndices = table.getSelectedRows();
						if ((rowIndices != null) && (rowIndices.length > 0)) {
							final ObjectResourceTableModel model = (ObjectResourceTableModel) table.getModel();
							JMenuItem deleteTestMenuItem = new JMenuItem(LangModelSchedule.getString("delete_tests")); //$NON-NLS-1$
							deleteTestMenuItem.addActionListener(new ActionListener() {

								public void actionPerformed(ActionEvent e) {

									int temp = JOptionPane
											.showConfirmDialog(Environment.getActiveWindow(), LangModelSchedule
													.getString("AreYouShureDeleteTests"), LangModelSchedule
													.getString("ConfirmDeleting"), JOptionPane.YES_NO_OPTION);
									if (temp == JOptionPane.YES_OPTION) {
										if (TableFrame.this.rowToRemove == null)
											TableFrame.this.rowToRemove = new LinkedList();
										else
											TableFrame.this.rowToRemove.clear();
										for (int i = 0; i < rowIndices.length; i++) {											
											Test test = (Test) model.getObject(rowIndices[i]);
											TableFrame.this.rowToRemove.add(test);
										}
										for (Iterator it = TableFrame.this.rowToRemove.iterator(); it.hasNext();) {
											Test test = (Test) it.next();											
											TableFrame.this.schedulerModel.removeTest(test);
											model.getContents().remove(test);
										}
										table.revalidate();
										table.repaint();
									}
								}
							});							
							JPopupMenu popup = new JPopupMenu();
							popup.add(deleteTestMenuItem);
							popup.show(table, evt.getX(), evt.getY());
						}
					}

				}
			});
			/**
			 * TODO set custom renderer
			 */
			{
//				//int vColIndex = 0;
//				TestTableCellRenderer tableCellRenderer = new TestTableCellRenderer();
//				for (int vColIndex = 0; vColIndex < tableModel.getColumnCount(); vColIndex++) {
//					TableColumn col = this.listTable.getColumnModel().getColumn(vColIndex);
//					col.setCellRenderer(tableCellRenderer);
//				}
			}
			JTableHeader header = this.listTable.getTableHeader();

			this.panel.add(header, BorderLayout.NORTH);
			this.panel.add(new JScrollPane(this.listTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
											ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		}
		return this.panel;

	}

	private void init() {
		setTitle(LangModelSchedule.getString("Tests_status_and_characters")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		this.panel = getPanel();
		setContentPane(this.panel);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, SchedulerModel.COMMAND_REFRESH_TEST);
		this.dispatcher.register(this, SchedulerModel.COMMAND_REFRESH_TESTS);
	}

	public void unregisterDispatcher() {
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_REFRESH_TEST);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_REFRESH_TESTS);
	}

	/**
	 * @return Returns the command.
	 */
	public Command getCommand() {
		return this.command;
	}
}
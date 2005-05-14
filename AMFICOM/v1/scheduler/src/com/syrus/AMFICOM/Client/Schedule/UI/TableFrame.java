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
import java.util.Set;

import javax.swing.Icon;
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
import javax.swing.UIManager;
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
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Schedule.Commandable;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestController;
import com.syrus.util.Log;

/**
 * @author Vladimir Dolzhenko
 */
public class TableFrame extends JInternalFrame implements 
//TestsEditor, TestEditor , 
Commandable {

	private static final long	serialVersionUID	= 3761405313630156343L;
	Dispatcher			dispatcher;
	SchedulerModel		schedulerModel;
	ObjectResourceTable	listTable;
	ApplicationContext	aContext;
	private JPanel		panel;
//	private Test		test;
	java.util.List		rowToRemove;
	private Command		command;
	boolean skip = false;

	public TableFrame(ApplicationContext aContext) {
		this.aContext = aContext;
		if (aContext != null) {
			// initModule(aContext.getDispatcher());
			this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
//			this.schedulerModel.addTestsEditor(this);
//			this.schedulerModel.addTestEditor(this);
			this.dispatcher = aContext.getDispatcher();
			OperationListener operationListener = new OperationListener() {
				public void operationPerformed(OperationEvent e) {
					String actionCommand = e.getActionCommand();
					if (actionCommand.equals(SchedulerModel.COMMAND_REFRESH_TESTS)) {
						updateTests();
					} else if (actionCommand.equals(SchedulerModel.COMMAND_REFRESH_TEST)) {
						updateTest();
					}
				}
			};
			
			this.dispatcher.register(operationListener, SchedulerModel.COMMAND_REFRESH_TESTS);
			this.dispatcher.register(operationListener, SchedulerModel.COMMAND_REFRESH_TEST);
		}
		init();
		this.command = new WindowCommand(this);
	}

	public void updateTest() {
		this.skip = true;
		Set selectedTestIds = this.schedulerModel.getSelectedTestIds();
		if (selectedTestIds == null || selectedTestIds.isEmpty()) {

			int[] selectedRows = this.listTable.getSelectedRows();
			if (selectedRows.length > 0) {
				int maxRowIndex = Integer.MIN_VALUE;
				int minRowIndex = Integer.MAX_VALUE;
				for (int i = 0; i < selectedRows.length; i++) {
					maxRowIndex = maxRowIndex > selectedRows[i] ? maxRowIndex : selectedRows[i];
					minRowIndex = minRowIndex < selectedRows[i] ? minRowIndex : selectedRows[i];
				}
				this.listTable.removeRowSelectionInterval(minRowIndex, maxRowIndex);
			}
		
		} else {
//			int[] selectedRows = new int[selectedTestIds.size()];
//			int j = 0;
			ObjectResourceTableModel tableModel = (ObjectResourceTableModel) this.listTable.getModel();
			for (Iterator iterator = selectedTestIds.iterator(); iterator.hasNext();) {
				Identifier identifier = (Identifier) iterator.next();
				try {
					Test test1 = (Test)MeasurementStorableObjectPool.getStorableObject(identifier, true);
					Identifier groupTestId = test1.getGroupTestId();
					if (groupTestId != null) {
						identifier = groupTestId;
					}
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(int i=0;i<tableModel.getRowCount();i++) {
					Test test = (Test)tableModel.getObject(i);
					Identifier testId = test.getGroupTestId();
					testId = testId != null ? testId : test.getId();
					if (testId.equals(identifier)) {
//						selectedRows[j++] = i;
						this.listTable.setRowSelectionInterval(i, i);
						break;
					}
				}
			}
//			listTable.setRow
		}
//		ObjectResourceTableModel tableModel = (ObjectResourceTableModel) this.listTable.getModel();
//		int rowIndex = tableModel.getIndexOfObject(this.test);
//		if (rowIndex >= 0) {
//			this.listTable.setRowSelectionInterval(rowIndex, rowIndex);
//		} else {}
		this.skip = false;
	}

	public void updateTests() {
		this.setTests();
		this.updateTest();
	}

	private void setTests() {
		this.listTable.removeAll();
		ObjectResourceTableModel model = (ObjectResourceTableModel) TableFrame.this.listTable.getModel();
		model.clear();
		Collection tests = this.schedulerModel.getTests();
		for (Iterator it = tests.iterator(); it.hasNext();) {
			Test test1 = (Test) it.next();			
			Identifier groupTestId = test1.getGroupTestId();
			assert Log.debugMessage("TableFrame.setTests | test1 is " + test1.getId() + ", groupTestId is " + groupTestId, Log.FINEST);
			if (groupTestId != null && !groupTestId.equals(test1.getId())) {
				continue;
			}			
			if (model.getIndexOfObject(test1) < 0) {
				Log.debugMessage("TableFrame.setTests | added ", Log.FINEST);
				model.getContents().add(test1);
			}
		}
		this.listTable.revalidate();
		this.listTable.repaint();
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
					if (!lsm.isSelectionEmpty() && !TableFrame.this.skip) {
						int selectedRow = lsm.getMinSelectionIndex();
						try {
							TableFrame.this.schedulerModel.unselectTests();
							TableFrame.this.schedulerModel
									.addSelectedTest((Test) ((ObjectResourceTableModel) TableFrame.this.listTable
											.getModel()).getObject(selectedRow));
						} catch (ApplicationException e1) {
							SchedulerModel.showErrorMessage(TableFrame.this, e1);
						}
					}
				}

			});

			this.listTable.addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					mouseClicked(e);
				}

				public void mouseClicked(MouseEvent evt) {
					final JTable table = ((JTable) evt.getSource());
					// if (SwingUtilities.isLeftMouseButton(evt)) {
					// int rowIndex = table.getSelectedRow();
					// TestTableModel model = (TestTableModel) table.getModel();
					// TestTableRow line = (TestTableRow)
					// model.getRow(rowIndex);
					// if (model != null) {
					// //System.out.println("test:" + line.getTest());
					// Test test = (Test) line.getObjectResource();
					// //TableFrame.this.skipTestUpdate = true;
					// TableFrame.this.dispatcher
					// .notify(new TestUpdateEvent(this, test,
					// TestUpdateEvent.TEST_SELECTED_EVENT));
					// //System.out.println("send test:"+test.getId());
					// //TableFrame.this.skipTestUpdate = false;
					// }
					// } else
					if (SwingUtilities.isRightMouseButton(evt)) {
						System.out.println("RightMouseButton");
						final int[] rowIndices = table.getSelectedRows();
						if ((rowIndices != null) && (rowIndices.length > 0)) {
							final ObjectResourceTableModel model = (ObjectResourceTableModel) table.getModel();
							JMenuItem deleteTestMenuItem = new JMenuItem(LangModelSchedule.getString("delete_tests")); //$NON-NLS-1$
							deleteTestMenuItem.addActionListener(new ActionListener() {

								public void actionPerformed(ActionEvent e) {

									int temp = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
										LangModelSchedule.getString("Are_you_shure_delete_tests"), LangModelSchedule
												.getString("Confirm_deleting"), JOptionPane.YES_NO_OPTION);
									if (temp == JOptionPane.YES_OPTION) {
										if (TableFrame.this.rowToRemove == null)
											TableFrame.this.rowToRemove = new LinkedList();
										else
											TableFrame.this.rowToRemove.clear();
										for (int i = 0; i < rowIndices.length; i++) {
											Test test1 = (Test) model.getObject(rowIndices[i]);
											TableFrame.this.rowToRemove.add(test1);
										}
										for (Iterator it = TableFrame.this.rowToRemove.iterator(); it.hasNext();) {
											Test test1 = (Test) it.next();
											TableFrame.this.schedulerModel.removeTest(test1);
											model.getContents().remove(test1);

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
				// //int vColIndex = 0;
				// TestTableCellRenderer tableCellRenderer = new
				// TestTableCellRenderer();
				// for (int vColIndex = 0; vColIndex <
				// tableModel.getColumnCount(); vColIndex++) {
				// TableColumn col =
				// this.listTable.getColumnModel().getColumn(vColIndex);
				// col.setCellRenderer(tableCellRenderer);
				// }
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
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		this.panel = getPanel();
		setContentPane(this.panel);
	}

	/**
	 * @return Returns the command.
	 */
	public Command getCommand() {
		return this.command;
	}
}

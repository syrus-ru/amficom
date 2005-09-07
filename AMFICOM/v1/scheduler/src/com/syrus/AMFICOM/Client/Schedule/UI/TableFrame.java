/*-
 * $Id: TableFrame.java,v 1.31 2005/09/07 02:56:04 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
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

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestController;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;

/**
 * @version $Revision: 1.31 $, $Date: 2005/09/07 02:56:04 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
public class TableFrame extends JInternalFrame implements PropertyChangeListener {

	private static final long serialVersionUID = 3761405313630156343L;
	Dispatcher dispatcher;
	SchedulerModel schedulerModel;
	WrapperedTable<Test> listTable;
	ApplicationContext aContext;
	private JPanel panel;
	List<Test> rowToRemove;
	PropertyChangeEvent propertyChangeEvent;

	public TableFrame(final ApplicationContext aContext) {
		this.aContext = aContext;
		if (aContext != null) {
			this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
			this.dispatcher = aContext.getDispatcher();
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TESTS, this);
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TEST, this);
		}
		this.init();
	}

	private void updateTest() {
		final Set<Identifier> selectedTestIds = this.schedulerModel.getSelectedTestIds();
		if (selectedTestIds == null || selectedTestIds.isEmpty()) {

			final int[] selectedRows = this.listTable.getSelectedRows();
			if (selectedRows.length > 0) {
				int maxRowIndex = Integer.MIN_VALUE;
				int minRowIndex = Integer.MAX_VALUE;
				for (int i = 0; i < selectedRows.length; i++) {
					maxRowIndex = maxRowIndex > selectedRows[i] ? maxRowIndex : selectedRows[i];
					minRowIndex = minRowIndex < selectedRows[i] ? minRowIndex : selectedRows[i];
				}
				if (maxRowIndex != Integer.MIN_VALUE && minRowIndex != Integer.MAX_VALUE) {
					this.listTable.removeRowSelectionInterval(minRowIndex, maxRowIndex);
				}
			}

		} else {
			// int[] selectedRows = new int[selectedTestIds.size()];
			// int j = 0;
			final WrapperedTableModel<Test> tableModel = this.listTable.getModel();
			for (Identifier identifier : selectedTestIds) {
				try {
					final Test test1 = (Test) StorableObjectPool.getStorableObject(identifier, true);
					final Identifier groupTestId = test1.getGroupTestId();
					if (!groupTestId.isVoid()) {
						identifier = groupTestId;
					}
				} catch (ApplicationException e) {
					AbstractMainFrame.showErrorMessage(this, e);
				}
				for (int i = 0; i < tableModel.getRowCount(); i++) {
					final Test test = tableModel.getObject(i);
					Identifier testId = test.getGroupTestId();
					testId = !testId.isVoid() ? testId : test.getId();
					if (testId.equals(identifier)) {
						// selectedRows[j++] = i;
						this.listTable.setRowSelectionInterval(i, i);
						break;
					}
				}
			}
		}
	}

	private void updateTests() {
		this.setTests();
		this.updateTest();
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		this.propertyChangeEvent = evt;
		final String propertyName = evt.getPropertyName();
		if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_TESTS)) {
			this.updateTests();
		} else if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_TEST)) {
			this.updateTest();
		}
		this.propertyChangeEvent = null;
	}

	private void setTests() {
		this.listTable.removeAll();
		final WrapperedTableModel<Test> model = this.listTable.getModel();
		model.clear();
		try {
			final Set<Test> tests = StorableObjectPool.getStorableObjects(this.schedulerModel.getTestIds(), true);
			for (final Test test : tests) {
				final Identifier groupTestId = test.getGroupTestId();
				if (!groupTestId.isVoid() && !groupTestId.equals(test.getId())) {
					continue;
				}
				if (model.getIndexOfObject(test) < 0) {
					model.addObject(test);
				}
			}
		} catch (final ApplicationException e) {
			AbstractMainFrame.showErrorMessage(this, e);
		}
		this.listTable.revalidate();
		this.listTable.repaint();
	}

	private JPanel getPanel() {
		if (this.panel == null) {
			this.panel = new JPanel(new BorderLayout());

			this.listTable = new WrapperedTable<Test>(TestController.getInstance(), new String[] {
					TestController.KEY_TEMPORAL_TYPE, TestController.KEY_MONITORED_ELEMENT,
					TestController.KEY_TEST_OBJECT, TestController.KEY_MEASUREMENT_TYPE, TestController.KEY_START_TIME,
					TestController.KEY_STATUS});
			this.listTable.setDefaultTableCellRenderer();
			final ListSelectionModel rowSM = this.listTable.getSelectionModel();
			rowSM.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(final ListSelectionEvent e) {
					if (e.getValueIsAdjusting()) {
						return;
					}

					final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (!lsm.isSelectionEmpty() && TableFrame.this.propertyChangeEvent == null) {
						final int selectedRow = lsm.getMinSelectionIndex();
						CommonUIUtilities.invokeAsynchronously(new Runnable() {

							public void run() {
								try {
									TableFrame.this.schedulerModel.unselectTests();
									final WrapperedTableModel<Test> model = TableFrame.this.listTable.getModel();
									TableFrame.this.schedulerModel.addSelectedTest(model.getObject(selectedRow));

								} catch (ApplicationException ae) {
									AbstractMainFrame.showErrorMessage(TableFrame.this, ae);
								}

							}
						}, LangModelGeneral.getString("Message.Information.PlsWait"));
					}
				}

			});

			this.listTable.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(final MouseEvent evt) {
					final JTable table = ((JTable) evt.getSource());
					if (SwingUtilities.isRightMouseButton(evt)) {
						
						final int[] rowIndices = table.getSelectedRows();
						if (rowIndices == null || rowIndices.length == 0) {
							return;
						}

						final WrapperedTableModel<Test> model = (WrapperedTableModel<Test>) table.getModel();

						for (final int index : rowIndices) {
							final Test test = model.getObject(index);
							final int status = test.getStatus().value();
							if (status != TestStatus._TEST_STATUS_NEW &&
								status != TestStatus._TEST_STATUS_PROCESSING &&
								status != TestStatus._TEST_STATUS_SCHEDULED) {
								return;
							}
						}

						final JPopupMenu popup = new JPopupMenu();

						boolean enableDeleting = true;
						boolean enableStopping = true;

						for (int i = 0; i < rowIndices.length; i++) {
							final Test test1 = model.getObject(rowIndices[i]);
							final int status = test1.getStatus().value();
							if (status != TestStatus._TEST_STATUS_NEW) {
								enableDeleting = false;
							}
							if (status != TestStatus._TEST_STATUS_PROCESSING ||
								status != TestStatus._TEST_STATUS_SCHEDULED) {
								enableStopping = false;
							}

							if (!enableDeleting && !enableStopping) {
								break;
							}
						}

						if (enableDeleting) {
							final JMenuItem deleteTestMenuItem = new JMenuItem(LangModelSchedule.getString(rowIndices.length == 1
									? "Text.Table.DeleteTest"
										: "Text.Table.DeleteTests"));
							deleteTestMenuItem.addActionListener(new ActionListener() {

								public void actionPerformed(final ActionEvent e) {
									final int temp = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
											LangModelSchedule.getString("Text.Table.DeleteTest.ConfirmMessage"),
											LangModelSchedule.getString("Text.Table.DeleteTest.ConfirmTitle"),
											JOptionPane.YES_NO_OPTION);
									if (temp == JOptionPane.YES_OPTION) {
										if (TableFrame.this.rowToRemove == null) {
											TableFrame.this.rowToRemove = new LinkedList<Test>();
										} else {
											TableFrame.this.rowToRemove.clear();
										}
										for (int i = 0; i < rowIndices.length; i++) {
											final Test test = model.getObject(rowIndices[i]);
											TableFrame.this.rowToRemove.add(test);
										}
										for (final Test test : TableFrame.this.rowToRemove) {
											TableFrame.this.schedulerModel.removeTest(test);
											model.removeObject(test);
										}
										table.revalidate();
										table.repaint();
									}
								}
							});
							popup.add(deleteTestMenuItem);
						}

						if (enableStopping) {
							final JMenuItem stopTestMenuItem = new JMenuItem(LangModelSchedule.getString("Text.Table.StopTesting"));
							stopTestMenuItem.addActionListener(new ActionListener() {

								public void actionPerformed(final ActionEvent e) {
									final int temp = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
											LangModelSchedule.getString("Text.Table.StopTesting.ConfirmMessage"),
											LangModelSchedule.getString("Text.Table.StopTesting.ConfirmTitle"),
											JOptionPane.YES_NO_OPTION);
									if (temp == JOptionPane.YES_OPTION) {
										for (int i = 0; i < rowIndices.length; i++) {
											final Test test = model.getObject(rowIndices[i]);
											test.setStatus(TestStatus.TEST_STATUS_STOPPING);
										}
										TableFrame.this.dispatcher.firePropertyChange(new PropertyChangeEvent(TableFrame.this,
												SchedulerModel.COMMAND_REFRESH_TESTS,
												null,
												null));
										table.revalidate();
										table.repaint();
									}
								}
							});

							if (enableDeleting) {
								popup.addSeparator();
							}
							popup.add(stopTestMenuItem);
						}
						popup.show(table, evt.getX(), evt.getY());
					}

				}
			});

			final JTableHeader header = this.listTable.getTableHeader();

			this.listTable.sortColumn(4);

			this.panel.add(header, BorderLayout.NORTH);
			this.panel.add(new JScrollPane(this.listTable,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		}
		return this.panel;

	}

	private void init() {
		super.setTitle(LangModelSchedule.getString("Text.Table.Title")); //$NON-NLS-1$
		super.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		super.setResizable(true);
		super.setClosable(true);
		super.setIconifiable(true);
		this.panel = getPanel();
		super.setContentPane(this.panel);
	}

}

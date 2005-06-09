/*-
 * $Id: StatusBar.java,v 1.2 2005/06/09 13:01:45 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/09 13:01:45 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
 */
public class StatusBar implements PropertyChangeListener {

	public static final String		FIELD_PREFIX	= "field";
	public static final String		FIELD_DOMAIN	= "domain";
	public static final String		FIELD_STATUS	= "status";
	public static final String		FIELD_SERVER	= "server";
	public static final String		FIELD_SESSION	= "session";
	public static final String		FIELD_USER		= "user";
	public static final String		FIELD_TIME		= "time";

	private JPanel					panel;

	private DefaultTableColumnModel	model;
	private DefaultTableModel		tableModel;

	private Timer					timeTimer;
	private Timer					progressBarTimer;

	private JTable					table;

	int								number			= 5;

	private class TableHeaderCellRenderer extends DefaultTableCellRenderer {

		private static final long	serialVersionUID	= 1416487175210085862L;
		
		//private final SimpleDateFormat	sdf	= new SimpleDateFormat("HH:mm:ss");

		public TableHeaderCellRenderer() {
			this.setHorizontalAlignment(SwingConstants.CENTER);
			this.setBorder(BorderFactory.createBevelBorder(
				BevelBorder.LOWERED,
				Color.white,
				Color.white,
				new Color(142, 142, 142),
				new Color(99, 99, 99)));
		}

		public Component getTableCellRendererComponent(	JTable jtable,
														Object value,
														boolean isSelected,
														boolean hasFocus,
														int row,
														int column) {
			if (jtable != null) {
				JTableHeader header = jtable.getTableHeader();
				if (header != null) {
					this.setForeground(header.getForeground());
					this.setBackground(header.getBackground());
					this.setFont(header.getFont());
				}
			}

			if (value instanceof Date) {
				SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.HOURS_MINUTES_SECONDS_DATE_FORMAT);
				this.setText(sdf.format((Date) value));
			} else if (value instanceof Component) {
				return (Component) value;
			} else {
				this.setText((value == null) ? "" : value.toString());
			}
//			this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			return this;
		}

	}

	public StatusBar() {
		this.createModels();
		this.createUI();
	}

	private void createModels() {
		this.tableModel = new DefaultTableModel(1, 1);
		this.model = new DefaultTableColumnModel();
	}

	private void createUI() {
		this.table = new JTable(this.tableModel, this.model);
		this.table.setRowHeight(1);

		JTableHeader tableHeader = this.table.getTableHeader();
		tableHeader.setDefaultRenderer(new TableHeaderCellRenderer());

		this.panel = new JPanel(new GridBagLayout());
		this.panel.setBorder(BorderFactory.createEtchedBorder());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;

		this.panel.add(this.table, gbc);
		this.panel.add(tableHeader, gbc);
		// this.createAddRemoveButtons();
//		this.createStartStopButtons();
	}

//	private void createAddRemoveButtons() {
//		JButton addButton = new JButton("add");
//		GridBagConstraints gbc = new GridBagConstraints();
//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		gbc.gridwidth = GridBagConstraints.RELATIVE;
//		this.panel.add(addButton, gbc);
//		addButton.addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				StatusBar.this.add(Integer.toString(++StatusBar.this.number));
//
//			}
//		});
//
//		JButton removeButton = new JButton("remove");
//		gbc.gridwidth = GridBagConstraints.REMAINDER;
//		this.panel.add(removeButton, gbc);
//		removeButton.addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				StatusBar.this.remove(Integer.toString(StatusBar.this.number--));
//
//			}
//		});
//	}
//
//	private void createStartStopButtons() {
//		JButton startButton = new JButton("Start");
//		GridBagConstraints gbc = new GridBagConstraints();
//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		gbc.gridwidth = GridBagConstraints.RELATIVE;
//		this.panel.add(startButton, gbc);
//		startButton.addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				StatusBar.this.setProgressBarEnable(true);
//
//			}
//		});
//
//		JButton stopButton = new JButton("Stop");
//		gbc.gridwidth = GridBagConstraints.REMAINDER;
//		this.panel.add(stopButton, gbc);
//		stopButton.addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				StatusBar.this.setProgressBarEnable(false);
//
//			}
//		});
//	}

	public JPanel getPanel() {
		return this.panel;
	}

	public void add(final String fieldId) {
		this.add(-1, fieldId);
	}

	public void add(final int index,
					final String fieldId) {
		assert fieldId != null : ErrorMessages.NON_NULL_EXPECTED;
		TableColumn column = new TableColumn();
		column.setHeaderValue(" " + fieldId);
		column.setIdentifier(fieldId);
		this.model.addColumn(column);
		int index2 = this.model.getColumnIndex(fieldId);
		if (index >= 0) {
			this.model.moveColumn(index2, index);
		}

		if (fieldId.equals(FIELD_TIME)) {
			this.addTimerField(fieldId);
		}
	}

	public void remove(final String fieldId) {
		assert fieldId != null : ErrorMessages.NON_NULL_EXPECTED;

		for (int i = 0; i < this.model.getColumnCount(); i++) {
			TableColumn column = this.model.getColumn(i);
			if (fieldId.equals(column.getIdentifier())) {
				this.model.removeColumn(column);
				break;
			}
		}

	}

	private void setValue(final 	String fieldId,
	                      final Object value) {
		final JTableHeader tableHeader = this.table.getTableHeader();
		TableColumn column = null;
		for (int i = 0; i < this.model.getColumnCount(); i++) {
			column = this.model.getColumn(i);
			Object identifier = column.getIdentifier();
			if (fieldId.equals(identifier)) { break; }
		}
		if (column != null) {
			column.setHeaderValue(value);
			if (this.progressBarTimer == null) {
				tableHeader.repaint();
			}
		} else {
			Log.debugMessage("StatusBar.addTimerField | fieldId '" + fieldId + "' not found.", Log.FINEST);
		}
	}
	
	public void setText(final String fieldId, final String text) {
		this.setValue(fieldId, text);
	}
	
	public void setWidth(final String fieldId, final int width) {
		TableColumn column = this.model.getColumn(this.model.getColumnIndex(fieldId));
		column.setWidth(width);
	}

	private void addStatusBarField(final String fieldId) {

		JProgressBar progressBar = new JProgressBar();
		progressBar.setString(LangModel.getString("Loading_Please_wait"));
		progressBar.setIndeterminate(true);
		progressBar.setStringPainted(true);

		final JTableHeader tableHeader = this.table.getTableHeader();
		progressBar.setPreferredSize(new Dimension(progressBar.getPreferredSize().width, tableHeader.getHeight()));

		this.progressBarTimer = new Timer(UIManager.getInt("ProgressBar.repaintInterval"), new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				tableHeader.repaint();

			}
		});
		this.progressBarTimer.start();

		this.setValue(fieldId, progressBar);
	}

	private void removeStatusBar(final String fieldId) {
		if (this.progressBarTimer != null) {
			this.progressBarTimer.stop();
			this.progressBarTimer = null;
		}

		this.remove(fieldId);
	}

	private void addTimerField(final String fieldId) {

		if (this.timeTimer == null) {
			this.timeTimer = new Timer(1000, new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					StatusBar.this.setValue(fieldId, new Date());
				}
			});

			this.timeTimer.start();
		}
	}

	private void setProgressBarEnable(boolean enable) {
		Log.debugMessage("StatusBar.setProgressBarEnable | enable is " + enable, Log.FINEST);
		final String field = FIELD_STATUS;
		if (enable) {
			/* is there status bar ? */
			for (int i = 0; i < this.model.getColumnCount(); i++) {
				Object identifier = this.model.getColumn(i).getIdentifier();
				if (field.equals(identifier)) { return; }
			}
			/* there is no status bar, add it */
			this.add(0, field);
			this.addStatusBarField(field);
		} else {
			this.remove(field);
			this.removeStatusBar(field);
		}
	}

	public void removeDispatcher(Dispatcher dispatcher) {
		if (dispatcher != null) {
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_MESSAGE, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_USER, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_SERVER, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_SESSION, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_DOMAIN, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_PROGRESS_BAR, this);
		}
	}

	public void addDispatcher(Dispatcher dispatcher) {
		if (dispatcher != null) {
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_MESSAGE, this);
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_USER, this);
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_SERVER, this);
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_SESSION, this);
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_DOMAIN, this);
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_PROGRESS_BAR, this);
		}
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		Log.debugMessage("StatusBar.propertyChange | propertyName : " + propertyName + " ["+ evt.getClass().getName() + ']', Log.FINEST);
		if (evt instanceof StatusMessageEvent) {
			StatusMessageEvent sme = (StatusMessageEvent) evt;
			if (propertyName.equals(StatusMessageEvent.STATUS_MESSAGE)) {
				this.setValue(FIELD_STATUS, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_SESSION)) {
				this.setValue(FIELD_SESSION, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_SERVER)) {
				this.setValue(FIELD_SERVER, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_USER)) {
				this.setValue(FIELD_USER, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_DOMAIN)) {
				this.setValue(FIELD_DOMAIN, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_PROGRESS_BAR)) {
				this.setProgressBarEnable(sme.isShowProgressBar());
			}

		}

	}

//	public static void main(String[] args) {
//		JFrame frame = new JFrame();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		StatusBar statusBar = new StatusBar();
//		statusBar.add("первонахЪ");
//		statusBar.add("2");
//		statusBar.add("3");
//		statusBar.add("4");
//		statusBar.remove("2");
//		statusBar.setProgressBarEnable(true);
//		statusBar.add(FIELD_TIME);
//		frame.getContentPane().add(statusBar.getPanel());
//		frame.pack();
//		frame.setVisible(true);
//	}

}

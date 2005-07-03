/*-
 * $Id: StatusBar.java,v 1.5 2005/06/24 08:50:11 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
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
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/24 08:50:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
 */
public class StatusBar implements PropertyChangeListener {

	public static final String		FIELD_PREFIX	= "field";
	public static final String		FIELD_DOMAIN	= "domain";
	public static final String		FIELD_PROGRESS	= "progress";
	public static final String		FIELD_STATUS	= "status";
	public static final String		FIELD_SERVER	= "server";
	public static final String		FIELD_SESSION	= "session";
	public static final String		FIELD_USER		= "user";
	public static final String		FIELD_TIME		= "time";

	private JPanel					panel;

	private DefaultTableColumnModel	model;
	private DefaultTableModel		tableModel;

	private Timer					timeTimer;
//	private Timer					progressBarTimer;

	private JTable					table;

	int								number			= 5;

	private class TableHeaderCellRenderer extends DefaultTableCellRenderer {

		private static final long	serialVersionUID	= 1416487175210085862L;

		public TableHeaderCellRenderer() {
			this.setHorizontalAlignment(SwingConstants.CENTER);
			this.setBorder(BorderFactory.createBevelBorder(
				BevelBorder.LOWERED,
				Color.WHITE,
				Color.WHITE,
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
			// this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
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

		this.table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

			private static final long	serialVersionUID	= 4968210937248459683L;

			public Component getTableCellRendererComponent(	JTable jtable,
															Object value,
															boolean isSelected,
															boolean hasFocus,
															int row,
															int column) {
				super.getTableCellRendererComponent(jtable, value, isSelected, hasFocus, row, column);
				this.setBorder(null);
				return this;
			}
		});

		JTableHeader tableHeader = this.table.getTableHeader();
		tableHeader.setDefaultRenderer(new TableHeaderCellRenderer());

		this.panel = new JPanel(new GridBagLayout());
//		this.panel.setBorder(BorderFactory.createEtchedBorder());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;

		this.panel.add(tableHeader, gbc);
		this.panel.add(this.table, gbc);
	}


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

	public void setValue(	final String fieldId,
							final Object value) {
		final JTableHeader tableHeader = this.table.getTableHeader();
		TableColumn column = null;
		int index;
		for (index = 0; index < this.model.getColumnCount(); index++) {
			column = this.model.getColumn(index);
			Object identifier = column.getIdentifier();
			if (fieldId.equals(identifier)) {
				break;
			}
		}
		if (column != null) {
			column.setHeaderValue(value);
			//tableHeader.repaint();
			Rectangle headerRect = tableHeader.getHeaderRect(index);
				if (headerRect != null) {
					Component componentAt = tableHeader.getComponentAt(
							headerRect.x, 
							headerRect.y);
					if (componentAt != null) {
						componentAt.repaint();		
					}
			}
		} else {
			Log.debugMessage("StatusBar.addTimerField | fieldId '" + fieldId + "' not found.", Log.FINEST);
		}
	}

	public void setText(final String fieldId,
						final String text) {
		this.setValue(fieldId, text);
	}

	public void setWidth(	final String fieldId,
							final int width) {
		this.table.getColumn(fieldId).setPreferredWidth(width);
	}

//	private void addStatusBarField(final String fieldId) {
//
//		JProgressBar progressBar = new JProgressBar();
//		progressBar.setString(LangModelGeneral.getString("StatusBar.PleaseWait"));
//		progressBar.setIndeterminate(true);
//		progressBar.setStringPainted(true);
//
//		final JTableHeader tableHeader = this.table.getTableHeader();
//		progressBar.setPreferredSize(new Dimension(progressBar.getPreferredSize().width, tableHeader.getHeight()));
//
//		this.progressBarTimer = new Timer(UIManager.getInt("ProgressBar.repaintInterval"), new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				tableHeader.repaint();
//
//			}
//		});
//		this.progressBarTimer.start();
//
//		this.setValue(fieldId, progressBar);
//	}
//
//	private void removeStatusBar(final String fieldId) {
//		if (this.progressBarTimer != null) {
//			this.progressBarTimer.stop();
//			this.progressBarTimer = null;
//		}
//
//		this.remove(fieldId);
//	}

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

//	private void setProgressBarEnable(boolean enable) {
//		final String field = FIELD_PROGRESS;
//		if (enable) {
//			/* is there status bar ? */
//			for (int i = 0; i < this.model.getColumnCount(); i++) {
//				Object identifier = this.model.getColumn(i).getIdentifier();
//				if (field.equals(identifier)) { return; }
//			}
//			/* there is no status bar, add it */
//			this.add(0, field);
//			this.addStatusBarField(field);
//		} else {
//			this.remove(field);
//			this.removeStatusBar(field);
//		}
//	}

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
//				this.setProgressBarEnable(sme.isShowProgressBar());
			}
		}
	}
}

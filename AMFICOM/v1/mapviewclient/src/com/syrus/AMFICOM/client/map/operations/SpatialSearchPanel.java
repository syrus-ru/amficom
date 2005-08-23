/*
 * Название: $Id: SpatialSearchPanel.java,v 1.14 2005/08/23 10:08:29 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.map.operations;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.resource.LangModelMap;

/**
 * панель поиска географических объектов
 * @version $Revision: 1.14 $, $Date: 2005/08/23 10:08:29 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
 public class SpatialSearchPanel extends JPanel {
	private static final long serialVersionUID = 181586229719837247L;

	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JScrollPane jScrollPane = new JScrollPane();

	JTextField searchField = new JTextField();
	JButton searchButton = new JButton();

	/**
	 * список найденных объектов
	 */
	JList foundList = new JList();
	JButton centerButton = new JButton();

	String searchText = "";
	private boolean searching = false;
	
	private MapFrame mapFrame;

	private static Dimension buttonSize = new Dimension(24, 24);

	public void setMapFrame(final MapFrame mapFrame) {
		this.mapFrame = mapFrame;
		if (mapFrame == null) {
			this.foundList.removeAll();
		}
	}

	public MapFrame getMapFrame() {
		return this.mapFrame;
	}

	/**
	 * По умолчанию
	 */
	public SpatialSearchPanel(final MapFrame mapFrame) {
		this.jbInit();
		this.setMapFrame(mapFrame);
	}

	private void jbInit() {
		this.setToolTipText(LangModelMap.getString("SearchTopologicalObjects"));
		this.setLayout(this.gridBagLayout1);
		this.setSize(new Dimension(370, 629));
		this.searchButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/map_search.gif")));
		this.searchButton.setPreferredSize(buttonSize);
		this.searchButton.setMaximumSize(buttonSize);
		this.searchButton.setMinimumSize(buttonSize);
		this.searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				SpatialSearchPanel.this.doSearch();
			}
		});
		this.jScrollPane.getViewport().add(this.foundList);
		this.jScrollPane.setAutoscrolls(true);
		this.centerButton.setText(LangModelMap.getString("DoCenter"));
		this.centerButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				SpatialSearchPanel.this.doCenter();
			}
		});

		this.searchField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					SpatialSearchPanel.this.doSearch();
				}
			}

			public void keyReleased(final KeyEvent e) {/* empty */
			}

			public void keyTyped(final KeyEvent e) {/* empty */
			}
		});

		final GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.add(this.searchField, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.add(this.searchButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.add(this.jScrollPane, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.add(this.centerButton, constraints);

		this.foundList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.foundList.setCellRenderer(new SpatialSearchPanel.SpatialObjectRenderer());
		this.foundList.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(final java.awt.event.MouseEvent e) {
				if (e.getClickCount() > 1)
					SpatialSearchPanel.this.doCenter();
			}
		});
	}

	/**
	 * обработка нажатия на кнопку поиска. сам поиск запускается в отдельном
	 * thread'е
	 */
	void doSearch() {
		if (this.searching) {
			return;
		}
		if (this.mapFrame == null) {
			return;
		}

		this.searchText = this.searchField.getText();
		if (this.searchText.length() == 0) {
			return;
		}
		this.searching = true;
		this.foundList.removeAll();

		final Thread t = new Thread(new Runnable() {
			public void run() {
				search();
			}
		});
		t.start();
	}

	/**
	 * выполнение поиска географических объектов по шаблону найденные объекты
	 * помещаются в список foundList
	 */
	public void search() {
		this.searchButton.setEnabled(false);
//		try {
//			final List<SpatialObject> found = this.mapFrame.getMapViewer().getRenderer().getLoader().findSpatialObjects(this.searchText);
//			this.foundList.setListData(found.toArray());
//		} catch (MapConnectionException e) {
//			this.foundList.setListData(new String[] { e.getMessage() });
//			e.printStackTrace();
//		} catch (MapDataException e) {
//			this.foundList.setListData(new String[] { e.getMessage() });
//			e.printStackTrace();
//		}
		this.searchButton.setEnabled(true);
		this.searching = false;
	}

	/**
	 * обработка нажатия кнопки Центрировать. вид карты центрируется по 
	 * среднему геометрическому центров выделенных в списке объектов
	 */
	void doCenter() {
		try {
			final SpatialObject so = (SpatialObject) this.foundList.getSelectedValue();
			this.mapFrame.getMapViewer().centerSpatialObject(so);
			this.mapFrame.getMapViewer().repaint(true);
		} catch (MapException e) {
			this.mapFrame.getContext().getDispatcher().firePropertyChange(new StatusMessageEvent(this,
					StatusMessageEvent.STATUS_MESSAGE,
					"Ошибка соединения с картографическими данными"));
			e.printStackTrace();
		}
	}

	/**
	 * отрисовка географических объектов в списке - отображение их заголовков
	 */
	protected class SpatialObjectRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = -218545524571005590L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			final JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			final SpatialObject so = (SpatialObject) value;
			lbl.setText(so.getLabel());
			return lbl;
		}
	}
}

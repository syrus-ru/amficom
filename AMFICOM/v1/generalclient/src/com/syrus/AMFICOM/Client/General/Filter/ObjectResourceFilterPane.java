//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Реализация серверной части интерфейса прототипа РИСД       * //
// *           (включает реализацию пакета pmServer и класса pmRISDImpl)  * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\Panels\GeneralListPane.java                    * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.General.UI.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;

import java.util.Vector;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;
import oracle.jdeveloper.layout.VerticalFlowLayout;

import javax.swing.border.EtchedBorder;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Filter.*;
import java.awt.*;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.*;

public class ObjectResourceFilterPane extends JScrollPane
{
	public static String state_filterChanged = "filterChanged";

	public static String state_filterClosed = "filterClosed";

	ApplicationContext aContext = null;

	ObjectResourceFilter filter = null;

	DataSet dataSet = new DataSet();

	private JInternalFrame ownerWindow = null;

	JPanel mainPanel = new JPanel();

	FilterPanel fp = null;

	JLabel jLabel1 = new JLabel();

	AComboBox columnComboBox = new AComboBox();

	FilterExpression criteriaListEditObject = null;

	JPanel typePanel = new JPanel();

	JRadioButton eqRadioButton = new JRadioButton();

	JRadioButton rangeRadioButton = new JRadioButton();

	JRadioButton timeRadioButton = new JRadioButton();

	JRadioButton subRadioButton = new JRadioButton();

	JRadioButton listRadioButton = new JRadioButton();

	JRadioButton emptyRadioButton = new JRadioButton();

	ButtonGroup radio;

	MouseListener ml;

	JScrollPane filterPanel = new JScrollPane();

	JButton addButton = new JButton();

	JButton changeButton = new JButton();

	VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();

	private JScrollPane criteriaListPanel = new JScrollPane();

	private ObjectResourceListBox criteriaList = new ObjectResourceListBox();

	private JLabel jLabel2 = new JLabel();

	public JTextField generalExpressionTextField = new JTextField();

	public LogicSchemeWindow logicSchemeWindow = null;

	private JButton editLogicTreeButton = new JButton();

	GridBagLayout gridBagLayout1 = new GridBagLayout();

	JCheckBox templateCheckBox = new JCheckBox ("задать параметры условия при реализации отчёта");


//	private JButton applyButton = new JButton();

	public ObjectResourceFilterPane()
	{
		super();
		try
		{
			logicSchemeWindow = new LogicSchemeWindow(this);
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ObjectResourceFilterPane(ObjectResourceFilter filter)
	{
		super();

		try
		{
			logicSchemeWindow = new LogicSchemeWindow(filter, this);
			setFilter(filter);
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ObjectResourceFilterPane(ObjectResourceFilter filter, DataSet dataSet)
	{
		this(filter);
		this.dataSet = dataSet;
	}

	public ObjectResourceFilterPane(
		ObjectResourceFilter filter,
		DataSet dataSet,
		JInternalFrame oW,
		ApplicationContext aC)
	{
		this(filter);
		this.dataSet = dataSet;
		this.ownerWindow = oW;
		this.aContext = aC;
	}

	public ObjectResourceFilter getFilter()
	{
		return filter;
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	void this_mousePressed(MouseEvent e)
	{
		criteriaListEditObject = (FilterExpression) criteriaList.
										 getSelectedObjectResource();

		if (criteriaListEditObject == null)
			return;

		columnComboBox.setSelectedItem(criteriaListEditObject.getId());
		Vector vec = criteriaListEditObject.getVec();
		String type = (String) vec.elementAt(0);

		if (type.equals("numeric"))
		{
			eqRadioButton.setSelected(true);
			fp.setExpression(criteriaListEditObject);
		}
		else if (type.equals("range"))
		{
			rangeRadioButton.setSelected(true);
			fp.setExpression(criteriaListEditObject);
		}
		else if (type.equals("time"))
		{
			timeRadioButton.setSelected(true);
			fp.setExpression(criteriaListEditObject);
		}
		else if (type.equals("string"))
		{
			subRadioButton.setSelected(true);
			fp.setExpression(criteriaListEditObject);
		}
		else if (type.equals("list"))
		{
			listRadioButton.setSelected(true);
			fp.setExpression(criteriaListEditObject);
		}
		changeButton.setEnabled(true);

		if (SwingUtilities.isRightMouseButton(e))
		{
			JPopupMenu popup = new JPopupMenu();
			JMenuItem delete = new JMenuItem(LangModel.String(
				"label_deleteCriteria"));
			delete.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					delete_actionPerformed(e);
repaint();
				}
			});
			popup.add(delete);
			popup.show(criteriaList, e.getX(), e.getY());
		}
	}

	void delete_actionPerformed(ActionEvent e)
	{
		FilterExpression fe = (FilterExpression) criteriaList.
									 getSelectedObjectResource();

		if (fe == null)
			return;

		criteriaList.remove(fe);
		filter.removeCriterium(fe);
		fp.removeAll();
		fp.repaint();
		changeButton.setEnabled(false);

		int removedIndex = fe.getListID();
		updateExpressionIDs(removedIndex);

		logicSchemeWindow.logicSchemePanel.repaint();
		this.generalExpressionTextField.setText(filter.logicScheme.getTextValue());

		tryToSaveChanges();
	}

	private void updateExpressionIDs(int removedIndex)
	{
		Vector schemeEls = this.filter.logicScheme.schemeElements;
		for (int i = 0; i < schemeEls.size(); i++)
		{
			LogicSchemeElement se = (LogicSchemeElement) schemeEls.get(i);
			if (se.type.equals(LogicSchemeElement.t_condition))
			{
				int thisID = Integer.parseInt(se.operandType);
				if (thisID > removedIndex)
				{
					se.operandType = (new Integer(--thisID)).toString();
					se.filterExpression.setListID(Integer.parseInt(se.operandType));
				}
			}
		}
	}

	private void jbInit() throws Exception
	{
		setName(LangModel.String("labelTabbedFilter"));
		ml = new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				this_mousePressed(e);
			}
		};
		this.setSize(new Dimension(443, 462));
		mainPanel.setLayout(gridBagLayout1);
		jLabel1.setText(LangModel.String("label_filterCriteria"));
		columnComboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				columnComboBox_actionPerformed(e);
			}
		});

		eqRadioButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/filter_equal.gif").getScaledInstance(16, 16,
			Image.SCALE_SMOOTH)));
		eqRadioButton.setToolTipText(LangModel.String("label_equality"));
		eqRadioButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				eqRadioButton_actionPerformed(e);
			}
		});

		rangeRadioButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
															getImage(
			"images/filter_diapazon.gif").getScaledInstance(16, 16,
			Image.SCALE_SMOOTH)));
		rangeRadioButton.setToolTipText(LangModel.String("label_diapason"));
		rangeRadioButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				rangeRadioButton_actionPerformed(e);
			}
		});

		timeRadioButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
														  getImage("images/filter_time.gif").
														  getScaledInstance(16, 16,
			Image.SCALE_SMOOTH)));
		timeRadioButton.setToolTipText(LangModel.String("label_time"));
		timeRadioButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				timeRadioButton_actionPerformed(e);
			}
		});

		subRadioButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/filter_substring.gif").getScaledInstance(16, 16,
			Image.SCALE_SMOOTH)));
		subRadioButton.setToolTipText(LangModel.String("label_substring"));
		subRadioButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				subRadioButton_actionPerformed(e);
			}
		});

		listRadioButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
														  getImage("images/filter_list.gif").
														  getScaledInstance(16, 16,
			Image.SCALE_SMOOTH)));
		listRadioButton.setToolTipText(LangModel.String("label_list"));
		listRadioButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				listRadioButton_actionPerformed(e);
			}
		});

		typePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		typePanel.setLayout(verticalFlowLayout1);

		changeButton.setEnabled(false);
		changeButton.setText(LangModel.String("label_change"));
		changeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				changeButton_actionPerformed(e);
			}
		});
		criteriaList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.
			RAISED));
		criteriaListPanel.setBorder(null);
		editLogicTreeButton.setPreferredSize(new Dimension(27, 24));
		editLogicTreeButton.setMargin(new Insets(2, 2, 2, 2));
		editLogicTreeButton.setText(">>");
		editLogicTreeButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				editLogicTreeButton_actionPerformed(e);
			}
		});
		jLabel2.setText(LangModel.String("label_summaryExpression"));
		/*		applyButton.setText(LangModel.String("label_apply"));
		  applyButton.addActionListener(new java.awt.event.ActionListener()
		  {
			public void actionPerformed(ActionEvent e)
			{
			 applyButton_actionPerformed(e);
			}
		  });*/
		templateCheckBox.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				templateCheckBox_actionPerformed(e);
			}
		});
		typePanel.add(eqRadioButton, null);
		typePanel.add(rangeRadioButton, null);
		typePanel.add(timeRadioButton, null);
		typePanel.add(subRadioButton, null);
		typePanel.add(listRadioButton, null);
		criteriaListPanel.getViewport().add(criteriaList, null);
		listRadioButton.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				filterRadioButton_itemStateChanged(e);
			}
		});
		subRadioButton.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				filterRadioButton_itemStateChanged(e);
			}
		});
		rangeRadioButton.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				filterRadioButton_itemStateChanged(e);
			}
		});
		timeRadioButton.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				filterRadioButton_itemStateChanged(e);
			}
		});
		eqRadioButton.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				filterRadioButton_itemStateChanged(e);
			}
		});
		radio = new ButtonGroup();
		addButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addButton_actionPerformed();
			}
		});
		radio.add(eqRadioButton);
		radio.add(rangeRadioButton);
		radio.add(timeRadioButton);
		radio.add(subRadioButton);
		radio.add(listRadioButton);
		radio.add(emptyRadioButton);

		filterPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.
			RAISED));

		addButton.setText(LangModel.String("label_add"));

		mainPanel.add(jLabel1,
							new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 2, 0, 0), 0, 0));
		mainPanel.add(jLabel2,
							new GridBagConstraints(0, 5, 5, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		mainPanel.add(columnComboBox,
							new GridBagConstraints(2, 0, 4, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 2), 0, 0));
		mainPanel.add(generalExpressionTextField,
							new GridBagConstraints(0, 6, 4, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
		mainPanel.add(typePanel,
							new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(14, 2, 0, 0), 0, 0));
		mainPanel.add(filterPanel,
							new GridBagConstraints(1, 1, 5, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(14, 0, 0, 1), 0, 0));
		mainPanel.add(addButton,
							 new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 20, 0, 0), 0, 0));
		mainPanel.add(changeButton,
							 new GridBagConstraints(3, 2, 1, 1, 1.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 0, 0), 0, 0));
		mainPanel.add(criteriaListPanel,
							new GridBagConstraints(0, 4, 6, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 0, 2), 0, 0));
		mainPanel.add(editLogicTreeButton,
							new GridBagConstraints(4, 6, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 0, 0));

		mainPanel.add(templateCheckBox,
							new GridBagConstraints(0, 3, 8, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 0, 0));

//		jPanel.add(applyButton, new XYConstraints(68, 395, 125, 26));

		this.getViewport().add(mainPanel, null);

		addButton.setEnabled(false);
		changeButton.setEnabled(false);
		eqRadioButton.setEnabled(false);
		subRadioButton.setEnabled(false);
		timeRadioButton.setEnabled(false);
		rangeRadioButton.setEnabled(false);
		listRadioButton.setEnabled(false);
		criteriaList.addMouseListener(ml);

		columnComboBox_actionPerformed(new ActionEvent(this, 0, ""));
	}

	public void setFilter(ObjectResourceFilter filter)
	{
		this.filter = filter;
		logicSchemeWindow.setFilter(filter);

		generalExpressionTextField.setText("");

		columnComboBox.setModel(new DefaultComboBoxModel(new Vector()));

		if (filter != null)
		{
			criteriaList.setContents(filter.getCriteria().elements());

			columnComboBox.setRenderer(new MyCriteriaListRenderer(filter));
			Vector vec = filter.getFilterColumns();
			columnComboBox.setModel(new DefaultComboBoxModel(vec));
			String col_id = (String) columnComboBox.getSelectedItem();
			String types[] = filter.getColumnFilterTypes(col_id);
			vec = new Vector();
			MyUtil.addToVector(vec, types);
			eqRadioButton.setEnabled(vec.contains("numeric"));
			timeRadioButton.setEnabled(vec.contains("time"));
			subRadioButton.setEnabled(vec.contains("string"));
			rangeRadioButton.setEnabled(vec.contains("range"));
			listRadioButton.setEnabled(vec.contains("list"));
			emptyRadioButton.setSelected(true);
			setFilterPanel();
			generalExpressionTextField.setText(filter.logicScheme.getTextValue());
		}
	}

	void columnComboBox_actionPerformed(ActionEvent e)
	{
		changeButton.setEnabled(false);
		String col_id = (String) columnComboBox.getSelectedItem();
		filterPanel.getViewport().removeAll();
		filterPanel.repaint();
		addButton.setEnabled(false);

		if (filter == null)
		{
			eqRadioButton.setEnabled(false);
			subRadioButton.setEnabled(false);
			timeRadioButton.setEnabled(false);
			rangeRadioButton.setEnabled(false);
			listRadioButton.setEnabled(false);
			emptyRadioButton.setSelected(true);
			return;
		}

		String types[] = filter.getColumnFilterTypes(col_id);
		Vector vec = new Vector();
		MyUtil.addToVector(vec, types);
		eqRadioButton.setEnabled(vec.contains("numeric"));
		timeRadioButton.setEnabled(vec.contains("time"));
		subRadioButton.setEnabled(vec.contains("string"));
		rangeRadioButton.setEnabled(vec.contains("range"));
		listRadioButton.setEnabled(vec.contains("list"));
		emptyRadioButton.setSelected(true);

		setFilterPanel();
	}

	void filterRadioButton_itemStateChanged(ItemEvent e)
	{
		setFilterPanel();
	}

	public void setFilterPanel()
	{
		String col_id = (String) columnComboBox.getSelectedItem();
		filterPanel.getViewport().removeAll();
		addButton.setEnabled(false);

		if (filter == null)
			return;

		String type = "";

		if (eqRadioButton.isSelected())
			type = "numeric";
		else
		if (subRadioButton.isSelected())
			type = "string";
		else
		if (timeRadioButton.isSelected())
			type = "time";
		else
		if (rangeRadioButton.isSelected())
			type = "range";
		else
		if (listRadioButton.isSelected())
			type = "list";

		fp = filter.getColumnFilterPanel(col_id, type);

		if (fp != null)
		{
			fp.setContext(this.aContext);
			filterPanel.getViewport().add((JComponent) fp);
			addButton.setEnabled(true);
		}
	}

	void addButton_actionPerformed()
	{
		String col_id = (String) columnComboBox.getSelectedItem();
		String col_name = filter.getFilterColumnName(col_id);

		FilterExpression expr = fp.getExpression(col_id, col_name);
		expr.setTemplate(templateCheckBox.isSelected());
		filter.addCriterium(expr);
		criteriaList.add(expr);

		if (logicSchemeWindow.useStandartScheme)
			filter.logicScheme.organizeStandartScheme();

		this.generalExpressionTextField.setText(filter.logicScheme.getTextValue());

		logicSchemeWindow.logicSchemePanel.repaint();

		tryToSaveChanges();
	}

	void eqRadioButton_actionPerformed(ActionEvent e)
	{
		changeButton.setEnabled(false);
	}

	void rangeRadioButton_actionPerformed(ActionEvent e)
	{
		changeButton.setEnabled(false);
	}

	void timeRadioButton_actionPerformed(ActionEvent e)
	{
		changeButton.setEnabled(false);
	}

	void subRadioButton_actionPerformed(ActionEvent e)
	{
		changeButton.setEnabled(false);
	}

	void listRadioButton_actionPerformed(ActionEvent e)
	{
		changeButton.setEnabled(false);
	}

	void changeButton_actionPerformed(ActionEvent e)
	{
		criteriaList.remove(criteriaListEditObject);
//		filter.removeCriterium(criteriaListEditObject);
		String col_id = (String) columnComboBox.getSelectedItem();
		String col_name = filter.getFilterColumnName(col_id);
		FilterExpression expr = fp.getExpression(col_id, col_name);
		filter.replaceCriterium(criteriaListEditObject, expr);
//		filter.addCriterium(expr);
		criteriaList.add(expr);

		tryToSaveChanges();
	}

	void editLogicTreeButton_actionPerformed(ActionEvent e)
	{
		if (this.filter.logicScheme.getRestrictionsNumber() == 0)
		{
			JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModel.String("label_emptyScheme"),
				LangModel.String("label_error"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (aContext != null)
			if (aContext.getDispatcher() != null)
				aContext.getDispatcher().notify(
					new OperationEvent(
					this.logicSchemeWindow,
					0,
					SetRestrictionsWindow.ev_lsWindowCreated));

		this.logicSchemeWindow.setModal(true);
		this.logicSchemeWindow.setVisible(true);
		tryToSaveChanges();
	}

	void tryToSaveChanges()
	{
		if (aContext != null)
			if (aContext.getDispatcher() != null)
				aContext.getDispatcher().notify(
					new OperationEvent(
					filter,
					0,
					ObjectResourceFilterPane.state_filterChanged));

//    applyButton.setEnabled(false);
	}

	void templateCheckBox_actionPerformed(ActionEvent e)
	{
		if (fp == null)
			return;

		boolean curState = templateCheckBox.isSelected();
		for (int i = 0; i < fp.getComponentCount(); i++)
		{
			Component curComponent = fp.getComponent(i);
			curComponent.setEnabled(!curState);
		}
	}
}

class MyCriteriaListRenderer extends DefaultListCellRenderer
{
	ObjectResourceFilter filter;

	public MyCriteriaListRenderer(ObjectResourceFilter filter)
	{
		this.filter = filter;
	}

	public Component getListCellRendererComponent(
		JList list,
		Object value,
		int index,
		boolean isSelected,
		boolean cellHasFocus)
	{
		String text = filter.getFilterColumnName((String) value);
		return super.getListCellRendererComponent(list, text, index, isSelected,
																cellHasFocus);
	}
}
package com.syrus.AMFICOM.Client.Schedule.UI;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.AMFICOM.filter.FilterExpressionInterface;
import com.syrus.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class FiltrationFrame extends JInternalFrame
		implements OperationListener
{
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Dispatcher internal_dispatcher;	
	ApplicationContext aContext;
	ObjectResourceFilter filter = Test.getFilter();
	DataSet dataSet;
	FilterPanel fp;
	FilterExpression CriteriaListEditObject = null;

	ButtonGroup radio;
	MouseListener ml;

	boolean initial_init = true;

	static String iniFileName = "My.properties";
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel jPanel1 = new JPanel();
	private BorderLayout borderLayout2 = new BorderLayout();
	private JPanel jPanel2 = new JPanel();
	private BorderLayout borderLayout4 = new BorderLayout();
	private AComboBox columnComboBox = new AComboBox();
	private JPanel jPanel3 = new JPanel();
	private BorderLayout borderLayout5 = new BorderLayout();
	private JPanel jPanel4 = new JPanel();
	private JPanel jPanel5 = new JPanel();
	private JPanel jPanel6 = new JPanel();
	private BorderLayout borderLayout7 = new BorderLayout();
	private JButton changeButton = new JButton();
	private JButton addButton = new JButton();
	private JRadioButton emptyRadioButton = new JRadioButton();
	private JPanel jPanel8 = new JPanel();
	private JButton doFilterButton = new JButton();
	private JPanel typePanel = new JPanel();
	private JRadioButton subRadioButton = new JRadioButton();
	private JRadioButton rangeRadioButton = new JRadioButton();
	private BorderLayout borderLayout8 = new BorderLayout();
	private JRadioButton eqRadioButton = new JRadioButton();
	private JRadioButton listRadioButton = new JRadioButton();
	private JRadioButton timeRadioButton = new JRadioButton();
	private BorderLayout borderLayout3 = new BorderLayout();
	private JPanel jPanel7 = new JPanel();
	private JScrollPane filterPanel = new JScrollPane();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jPanel9 = new JPanel();
	private JLabel jLabel1 = new JLabel();
	private ObjectResourceListBox criteriaList = new ObjectResourceListBox();
	private JScrollPane criteriaListPanel = new JScrollPane();

	public FiltrationFrame(ApplicationContext aContext, DataSet ds)
	{
		this.dataSet = ds;
		this.aContext=aContext;
		fp = new FilterPanel();
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		setContext(aContext);
	}

	private void jbInit() throws Exception
	{
		filterPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		jPanel7.setLayout(borderLayout8);
		timeRadioButton.setText(LangModelSchedule.String("labelTime"));
		timeRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeRadioButton_actionPerformed(e);
			}
		});
		timeRadioButton.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					filterRadioButton_itemStateChanged(e);
				}
			});
		timeRadioButton.setEnabled(false);
		listRadioButton.setText(LangModelSchedule.String("labelList"));
		listRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listRadioButton_actionPerformed(e);
			}
		});
		listRadioButton.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					filterRadioButton_itemStateChanged(e);
				}
			});
		listRadioButton.setEnabled(false);
		eqRadioButton.setText(LangModelSchedule.String("labelEqual"));
		eqRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eqRadioButton_actionPerformed(e);
			}
		});
		eqRadioButton.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					filterRadioButton_itemStateChanged(e);
				}
			});
		eqRadioButton.setEnabled(false);
		rangeRadioButton.setText(LangModelSchedule.String("labelRange"));
		rangeRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rangeRadioButton_actionPerformed(e);
			}
		});
		rangeRadioButton.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					filterRadioButton_itemStateChanged(e);
				}
			});
		rangeRadioButton.setEnabled(false);
		subRadioButton.setText(LangModelSchedule.String("labelSubstring"));
		subRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subRadioButton_actionPerformed(e);
			}
		});
		subRadioButton.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					filterRadioButton_itemStateChanged(e);
				}
			});
		subRadioButton.setEnabled(false);
		typePanel.setLayout(borderLayout3);
		typePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		addButton.setEnabled(false);
		addButton.setText(LangModelSchedule.String("labelAdd"));
		addButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addButton_actionPerformed(e);
				}
			});
		changeButton.setEnabled(false);
		changeButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					changeButton_actionPerformed(e);
				}
			});
		changeButton.setText(LangModelSchedule.String("labelChange"));
		changeButton.setEnabled(false);
		jPanel5.setLayout(borderLayout7);
		jPanel4.setLayout(gridBagLayout1);
		jPanel3.setLayout(borderLayout5);
		columnComboBox.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					columnComboBox_actionPerformed(e);
				}
			});
		jPanel2.setLayout(borderLayout4);
		jPanel1.setLayout(borderLayout2);
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		internal_dispatcher=aContext.getDispatcher();
		this.setClosable(true);
		this.setIconifiable(true);
//		this.setMaximizable(true);
		this.setResizable(true);
		this.setTitle(LangModelSchedule.String("MyFiltrationTitle"));
		this.setEnabled(true);
		this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
			public void internalFrameActivated(InternalFrameEvent e) {
				this_internalFrameActivated(e);
			}
			public void internalFrameOpened(InternalFrameEvent e) {
				this_internalFrameOpened(e);
			}
		});
		this.addComponentListener(new java.awt.event.ComponentAdapter()
			{
				public void componentShown(ComponentEvent e)
				{
					this_componentShown(e);
				}
			});
		this.getContentPane().setLayout(borderLayout1);
		ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			this_mousePressed (e);
			}
		};
		//this.setPreferredSize(new Dimension(510, 410));


		radio = new ButtonGroup();
		jLabel1.setText(LangModelSchedule.String("labelCriteriaFilt"));
		criteriaListPanel.setBorder(null);
		criteriaList.setBorder(BorderFactory.createEtchedBorder());
		radio.add(emptyRadioButton);
		radio.add(eqRadioButton);
		radio.add(rangeRadioButton);
		radio.add(listRadioButton);
		radio.add(subRadioButton);
		radio.add(timeRadioButton);

		internal_dispatcher.register(this,"RemoveFiltrationFrame");
		setFilter(filter);
		doFilterButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doFilterButton_actionPerformed(e);
			}
		});
		doFilterButton.setText(LangModelSchedule.String("labelFilter"));
		this.getContentPane().add(jPanel1,  BorderLayout.CENTER);
		jPanel1.add(jPanel2, BorderLayout.CENTER);
		jPanel2.add(columnComboBox, BorderLayout.NORTH);
		jPanel2.add(jPanel3, BorderLayout.CENTER);
		jPanel3.add(jPanel4,  BorderLayout.CENTER);
		jPanel4.add(jPanel5,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
			,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 131, 0));
		jPanel5.add(jPanel6,  BorderLayout.NORTH);
		jPanel6.add(changeButton, null);
		jPanel6.add(addButton, null);
		jPanel5.add(criteriaListPanel, BorderLayout.CENTER);
		criteriaListPanel.getViewport().add(criteriaList);
		jPanel4.add(filterPanel,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
			,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 466, 265));
		jPanel3.add(typePanel, BorderLayout.NORTH);
		typePanel.add(listRadioButton, BorderLayout.SOUTH);
		typePanel.add(eqRadioButton, BorderLayout.NORTH);
		typePanel.add(jPanel7, BorderLayout.CENTER);
		jPanel7.add(subRadioButton, BorderLayout.SOUTH);
		jPanel7.add(rangeRadioButton, BorderLayout.NORTH);
		jPanel7.add(timeRadioButton, BorderLayout.CENTER);
		jPanel1.add(jPanel8,  BorderLayout.SOUTH);
		jPanel8.add(doFilterButton, null);
		this.getContentPane().add(jPanel9, BorderLayout.NORTH);
		jPanel9.add(jLabel1, null);
		criteriaList.addMouseListener(ml);
	}

	public void init_module()
	{
		initial_init = false;
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setCommand("myEntry", new VoidCommand());

		aModel.setEnabled("myEntry", true);
		aModel.fireModelChanged("");
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		if(aContext == null)
			return;
		if(aContext.getApplicationModel() == null)
			aContext.setApplicationModel(new ApplicationModel());
		setModel(aContext.getApplicationModel());

		if(aContext.getDispatcher() != null)
			aContext.getDispatcher().register(this, "myevent");
	}

	public ApplicationContext getContext()
	{
		return aContext;
	}

	public void setModel(ApplicationModel aModel)
	{
//		aModel.addListener(toolBar);
//		toolBar.setModel(aModel);

		aModel.fireModelChanged("");
	}

	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}

	public Dispatcher getInternalDispatcher()
	{
		return internal_dispatcher;
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("RemoveFiltrationFrame"))
		{
			this.dispose();
		}
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.fireModelChanged("");
	}

	void this_componentShown(ComponentEvent e)
	{
		if(initial_init)
			init_module();
	}

	void this_internalFrameActivated(InternalFrameEvent e)
	{
		this.grabFocus();
	}

	void this_internalFrameOpened(InternalFrameEvent e)
	{
		this.grabFocus();
	}

	void this_mousePressed (MouseEvent e)
	{
		CriteriaListEditObject = (FilterExpression )criteriaList.getSelectedObjectResource();
		if (CriteriaListEditObject != null)
		{
			columnComboBox.setSelectedItem(CriteriaListEditObject.getId());
			Vector vec = CriteriaListEditObject.getVec();
			String type = (String )vec.elementAt(0);

			if (type.equals("numeric"))
			{
				eqRadioButton.setSelected(true);
				fp.setExpression(CriteriaListEditObject);
			}
			else if (type.equals("range"))
			{
				rangeRadioButton.setSelected(true);
				fp.setExpression(CriteriaListEditObject);
			}
			else if (type.equals("time"))
			{
				timeRadioButton.setSelected(true);
				fp.setExpression(CriteriaListEditObject);
			}
			else if (type.equals("string"))
			{
				subRadioButton.setSelected(true);
				fp.setExpression(CriteriaListEditObject);
			}
			else if (type.equals("list"))
			{
				listRadioButton.setSelected(true);
				fp.setExpression(CriteriaListEditObject);
			}
			changeButton.setEnabled(true);
		}

		if (SwingUtilities.isRightMouseButton(e))
		{
			JPopupMenu popup = new JPopupMenu();
			JMenuItem delete = new JMenuItem(LangModelSchedule.String("labelCriteriaDel"));
			delete.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					delete_actionPerformed(e);
				}
			});
			popup.add(delete);
			popup.show(criteriaList,e.getX(),e.getY());
		}
	}

	void delete_actionPerformed(ActionEvent e)
	{
		FilterExpression or = (FilterExpression) criteriaList.getSelectedObjectResource();
		criteriaList.remove(or);
		filter.removeCriterium(or);
		fp.removeAll();
		fp.repaint();
		changeButton.setEnabled(false);
	}

	public void setFilter(ObjectResourceFilter filter)
	{
		columnComboBox.setModel(new DefaultComboBoxModel(new Vector()));
		if(filter != null)
		{
			columnComboBox.setRenderer(new MyCriteriaListRenderer(filter));
			Vector vec = filter.getFilterColumns();
			columnComboBox.setModel(new DefaultComboBoxModel(vec));
			String col_id = (String )columnComboBox.getSelectedItem();
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
		}
	}

	void columnComboBox_actionPerformed(ActionEvent e)
	{
		changeButton.setEnabled(false);
		String col_id = (String )columnComboBox.getSelectedItem();
		filterPanel.getViewport().removeAll();
		filterPanel.repaint();
		addButton.setEnabled(false);

//		if(col_id == null)
//			return;

		if(filter == null)
		{
			eqRadioButton.setEnabled(false);
			timeRadioButton.setEnabled(false);
			subRadioButton.setEnabled(false);
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
		String col_id = (String )columnComboBox.getSelectedItem();
		filterPanel.getViewport().removeAll();
		addButton.setEnabled(false);

		if(filter == null)
			return;

		String type = "";

		if(eqRadioButton.isSelected())
			type = "numeric";
		else
		if(timeRadioButton.isSelected())
			type = "time";
		else
		if(subRadioButton.isSelected())
			type = "string";
		else
		if(rangeRadioButton.isSelected())
			type = "range";
		else
		if(listRadioButton.isSelected())
			type = "list";

		fp = filter.getColumnFilterPanel(col_id, type);

		if(fp != null)
		{
			fp.setContext(aContext);
			filterPanel.getViewport().add((JComponent)fp);
			addButton.setEnabled(true);
		}
	}

	void addButton_actionPerformed(ActionEvent e)
	{
		String col_id = (String )columnComboBox.getSelectedItem();
		String col_name = filter.getFilterColumnName(col_id);
		/**
		 * FIXME
		 */
//		FilterExpressionInterface expr = fp.getExpression(col_id, col_name,true);
//		filter.addCriterium(expr);
//		criteriaList.add(expr);
	}

	void doFilterButton_actionPerformed(ActionEvent e)
	{
		DataSet dset = filter.filter(dataSet);
		internal_dispatcher.notify(new OperationEvent(dset,0,"Filtration_parameters"));
	}

	void eqRadioButton_actionPerformed(ActionEvent e)
	{
		changeButton.setEnabled(false);
	}

	void rangeRadioButton_actionPerformed(ActionEvent e)
	{
		changeButton.setEnabled(false);
	}

	void subRadioButton_actionPerformed(ActionEvent e)
	{
		changeButton.setEnabled(false);
	}

	void timeRadioButton_actionPerformed(ActionEvent e)
	{
		changeButton.setEnabled(false);
	}

	void listRadioButton_actionPerformed(ActionEvent e)
	{
		changeButton.setEnabled(false);
	}
	void changeButton_actionPerformed(ActionEvent e)
	{
		criteriaList.remove(CriteriaListEditObject);
		filter.removeCriterium(CriteriaListEditObject);
		String col_id = (String )columnComboBox.getSelectedItem();
		String col_name = filter.getFilterColumnName(col_id);
		/**
		 * FIXME
		 */
//		FilterExpression expr = fp.getExpression(col_id, col_name);
//		filter.addCriterium(expr);
//		criteriaList.add(expr);
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
		String text = filter.getFilterColumnName((String )value);
		return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
	}
}

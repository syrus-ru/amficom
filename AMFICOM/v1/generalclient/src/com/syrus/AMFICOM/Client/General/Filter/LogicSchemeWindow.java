package com.syrus.AMFICOM.Client.General.Filter;

import javax.swing.*;
import oracle.jdeveloper.layout.*;
import java.awt.*;
import java.awt.event.*;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilterPane;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Filter.LogicSchemePanel;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LogicSchemeWindow extends JDialog//JInternalFrame
{
	public ObjectResourceFilter filter = null;
	public ObjectResourceFilterPane filterPane = null;

	public String lsWindowButtonPressed = "";

	private XYLayout xYLayout1 = new XYLayout();
	private JButton deleteButton = new JButton();
	private JToggleButton andToggleButton = new JToggleButton();
	private JToggleButton orToggleButton = new JToggleButton();
	private JScrollPane jScrollPane1 = new JScrollPane();

	public LogicSchemePanel logicSchemePanel = null;

	private JCheckBox useStandartSchemeCheckBox = new JCheckBox();
	public boolean useStandartScheme = true;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JButton buttonClose = new JButton();

	public LogicSchemeWindow(
			Dialog parent,
			ObjectResourceFilter filter,
			ObjectResourceFilterPane filterPane)
	{
		super(parent);
		try
		{
			this.filter = filter;
			this.filterPane = filterPane;
			logicSchemePanel = new LogicSchemePanel(this);

			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public LogicSchemeWindow(
			Frame parent,
			ObjectResourceFilter filter,
			ObjectResourceFilterPane filterPane)
	{
		super(parent);
		try
		{
			this.filter = filter;
			this.filterPane = filterPane;
			logicSchemePanel = new LogicSchemePanel(this);

			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public LogicSchemeWindow(
			ObjectResourceFilter filter,
			ObjectResourceFilterPane filterPane)
	{
		this((Frame )Environment.getActiveWindow(), filter, filterPane);
	}

	public LogicSchemeWindow(ObjectResourceFilterPane filterPane)
	{
		this(null, filterPane);
	}

	public void setFilter (ObjectResourceFilter orf)
	{
		this.filter = orf;
		logicSchemePanel.setFilter(orf);
	}

	private void jbInit() throws Exception
	{
//    setClosable(true);
//    this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.setTitle(LangModel.getString("label_lswTitle"));
		JPanel contentPane = (JPanel) this.getContentPane();
		buttonClose.setText(LangModel.getString("label_close"));
		deleteButton.setText(LangModel.getString("label_delete"));
		deleteButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				deleteButton_actionPerformed(e);
			}
		});
		contentPane.setLayout(gridBagLayout1);
		this.setSize(new Dimension(610, 300));

		andToggleButton.setMargin(new Insets(2, 2, 2, 2));
		andToggleButton.setText(LangModel.getString("label_and"));
		andToggleButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				andToggleButton_actionPerformed(e);
			}
		});
		orToggleButton.setText(LangModel.getString("label_or"));
		orToggleButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				orToggleButton_actionPerformed(e);
			}
		});
		orToggleButton.setMargin(new Insets(2, 2, 2, 2));
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane1.setAutoscrolls(true);
		useStandartSchemeCheckBox.setSelected(true);
		useStandartSchemeCheckBox.setText(LangModel.getString("label_createStandartScheme"));
		contentPane.setBorder(BorderFactory.createEtchedBorder());
		contentPane.add(useStandartSchemeCheckBox, new GridBagConstraints(0, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
		contentPane.add(jScrollPane1, new GridBagConstraints(0, 1, 5, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 0, 10), 0, 0));
		contentPane.add(andToggleButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
		contentPane.add(orToggleButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
		contentPane.add(deleteButton, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
		contentPane.add(Box.createVerticalGlue(), new GridBagConstraints(3, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		contentPane.add(buttonClose, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jScrollPane1.getViewport().add(logicSchemePanel);

		useStandartSchemeCheckBox.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				useStandartScheme = useStandartSchemeCheckBox.isSelected();
			}
		});

		this.addComponentListener(new ComponentAdapter()
		{
			public void componentHidden(ComponentEvent e)
			{
				if (!filter.logicScheme.checkScheme())
				{
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							LangModel.getString("label_schemeNotCompleted"),
							LangModel.getString("label_error"),
							JOptionPane.ERROR_MESSAGE);
					filter.logicScheme.organizeStandartScheme();
					refreshLSTextValue();
				}
			}
			public void componentMoved(ComponentEvent e){}
			public void componentResized(ComponentEvent e){}
			public void componentShown(ComponentEvent e){}
		});

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		buttonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonClose_actionPerformed(e);
				}
			});
		contentPane.setSize(new Dimension(666, 276));
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		setLocation(
				(screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
	}

	void deleteButton_actionPerformed(ActionEvent e)
	{
		this.logicSchemePanel.deleteSelectedElementWithWarning();
		refreshLSTextValue();
	}

	void andToggleButton_actionPerformed(ActionEvent e)
	{
		if (andToggleButton.isSelected())
		{
			lsWindowButtonPressed = LogicSchemeElement.ot_and;
			orToggleButton.setSelected(false);
		}
		else
			lsWindowButtonPressed = "";
	}

	void orToggleButton_actionPerformed(ActionEvent e)
	{
		if (orToggleButton.isSelected())
		{
			lsWindowButtonPressed = LogicSchemeElement.ot_or;
			andToggleButton.setSelected(false);
		}
		else
			lsWindowButtonPressed = "";
	}

	public void clearAllToggles()
	{
		andToggleButton.setSelected(false);
		orToggleButton.setSelected(false);
	}

	public void refreshLSTextValue()
	{
		filterPane.generalExpressionTextField.setText(
			filter.logicScheme.getTextValue());
	}

	protected void processWindowEvent(WindowEvent e)
	{
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			hide();
			return;
		}
		super.processWindowEvent(e);
	}

	private void buttonClose_actionPerformed(ActionEvent e)
	{
		filterPane.tryToSaveChanges();
		hide();
	}
}

package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.filter.FilterExpressionInterface;

import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class GeneralEquationFilterPanel extends FilterPanel
{


	JTextField textField = new JTextField();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JRadioButton eqButton = new JRadioButton();
	JRadioButton lessButton = new JRadioButton();
	JRadioButton moreButton = new JRadioButton();
	ButtonGroup radio;

	public GeneralEquationFilterPanel()
	{
		super();
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private void jbInit() throws Exception
	{
		this.setLayout(new GridBagLayout());
		jLabel1.setText(LangModel.String("labelUslovie"));
		jLabel2.setText(LangModel.String("labelZnachenie"));
		eqButton.setText("=");
		lessButton.setText("<");
		moreButton.setText(">");
		radio = new ButtonGroup();
		radio.add(lessButton);
		radio.add(eqButton);
		radio.add(moreButton);

		this.add(jLabel1,  new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
		this.add(jLabel2,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));

		this.add(lessButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
		this.add(eqButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
		this.add(moreButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));

		this.add(textField,  new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
//		this.setLayout(null);
	}

	public FilterExpressionInterface getExpression(String col_id, String col_name, boolean conditionsRequested)
	{
		Vector vec = new Vector();
		String temp = "";
		vec.add("numeric");
		if(eqButton.isSelected())
			temp = "=";
		else if(lessButton.isSelected())
			temp = "<";
		else if(moreButton.isSelected())
			temp = ">";
		vec.add(temp);
		vec.add(textField.getText());
		FilterExpression fexp = new FilterExpression();

		String expName = LangModel.String("labelFiltration") + " \'" + col_name + "\' " + LangModel.String("labelPoZnach");
		if (conditionsRequested)
			expName += (" " + temp + " " + textField.getText());

		fexp.setName(expName);
		fexp.setColumnName(col_name);
		fexp.setVec(vec);
		fexp.setId(col_id);

		return fexp;
	}

	public void setExpression(FilterExpression expr)
	{
		Vector vec = expr.getVec();
		if(((String )vec.elementAt(1)).equals("="))
			eqButton.setSelected(true);
		else if(((String )vec.elementAt(1)).equals("<"))
			lessButton.setSelected(true);
		else if(((String )vec.elementAt(1)).equals(">"))
			moreButton.setSelected(true);
		textField.setText((String) vec.elementAt(2));
	}
}

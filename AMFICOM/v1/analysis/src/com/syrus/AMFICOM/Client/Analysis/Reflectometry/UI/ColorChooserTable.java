package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;

import com.syrus.AMFICOM.Client.General.UI.ATable;

public class ColorChooserTable extends ATable
{

	public ColorChooserTable()
	{
		this(new DefaultTableModel());
	}

	public ColorChooserTable(AbstractTableModel tModel)
	{
		super(tModel);

		this.setPreferredScrollableViewportSize(new Dimension(500, 70));

		//Set up renderer and editor for the Favorite Color column.
		setUpColorRenderer(this);
		setUpColorEditor(this);
	}

		class ColorRenderer extends JLabel
												implements TableCellRenderer {
				Border unselectedBorder = null;
				Border selectedBorder = null;
				boolean isBordered = true;

				public ColorRenderer(boolean isBordered) {
						super();
						this.isBordered = isBordered;
						setOpaque(true); //MUST do this for background to show up.
				}

				public Component getTableCellRendererComponent(
																JTable table, Object color,
																boolean isSelected, boolean hasFocus,
																int row, int column) {
						setBackground((Color)color);
						if (isBordered) {
								if (isSelected) {
										if (selectedBorder == null) {
												selectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
																									table.getSelectionBackground());
										}
										setBorder(selectedBorder);
								} else {
										if (unselectedBorder == null) {
												unselectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
																									table.getBackground());
										}
										setBorder(unselectedBorder);
								}
						}
						return this;
				}
		}

		private void setUpColorRenderer(JTable table) {
				table.setDefaultRenderer(Color.class,
																 new ColorRenderer(true));
		}

		//Set up the editor for the Color cells.
		private void setUpColorEditor(JTable table) {
				//First, set up the button that brings up the dialog.
				final JButton button = new JButton("") {
						public void setText(String s) {
								//Button never shows text -- only color.
						}
				};
				button.setBackground(Color.white);
				button.setBorderPainted(false);
				button.setMargin(new Insets(0,0,0,0));

				//Now create an editor to encapsulate the button, and
				//set it up as the editor for all Color cells.
				final ColorEditor colorEditor = new ColorEditor(button);
				table.setDefaultEditor(Color.class, colorEditor);

				//Set up the dialog that the button brings up.
				final JColorChooser colorChooser = new JColorChooser();
				ActionListener okListener = new ActionListener() {
						public void actionPerformed(ActionEvent e) {
								colorEditor.currentColor = colorChooser.getColor();
						}
				};
				final JDialog dialog = JColorChooser.createDialog(button,
																				"Pick a Color",
																				true,
																				colorChooser,
																				okListener,
																				null); //XXXDoublecheck this is OK

				//Here's the code that brings up the dialog.
				button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
								button.setBackground(colorEditor.currentColor);
								colorChooser.setColor(colorEditor.currentColor);
								//Without the following line, the dialog comes up
								//in the middle of the screen.
								//dialog.setLocationRelativeTo(button);
								dialog.show();
						}
				});
		}
		/*
		 * The editor button that brings up the dialog.
		 * We extend DefaultCellEditor for convenience,
		 * even though it mean we have to create a dummy
		 * check box.  Another approach would be to copy
		 * the implementation of TableCellEditor methods
		 * from the source code for DefaultCellEditor.
		 */
		class ColorEditor extends DefaultCellEditor {
				Color currentColor = null;

				public ColorEditor(JButton b) {
								super(new JCheckBox()); //Unfortunately, the constructor
																				//expects a check box, combo box,
																				//or text field.
						editorComponent = b;
						setClickCountToStart(1); //This is usually 1 or 2.

						//Must do this so that editing stops when appropriate.
						b.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
										fireEditingStopped();
								}
						});
				}

				protected void fireEditingStopped() {
						super.fireEditingStopped();
				}

				public Object getCellEditorValue() {
						return currentColor;
				}

				public Component getTableCellEditorComponent(JTable table,
																										 Object value,
																										 boolean isSelected,
																										 int row,
																										 int column) {
						((JButton)editorComponent).setText(value.toString());
						currentColor = (Color)value;
						return editorComponent;
				}
		}
}

		class DefaultTableModel extends AbstractTableModel {
				final String[] columnNames = {"Element",
																			"Color"};
				final Object[][] data = {
						{"DefaultElement", new Color(255, 255, 255)}
				};

				public int getColumnCount() {
						return columnNames.length;
				}

				public int getRowCount() {
						return data.length;
				}

				public String getColumnName(int col) {
						return columnNames[col];
				}

				public Object getValueAt(int row, int col) {
						return data[row][col];
				}

				/*
				 * JTable uses this method to determine the default renderer/
				 * editor for each cell.  If we didn't implement this method,
				 * then the last column would contain text ("true"/"false"),
				 * rather than a check box.
				 */
				public Class getColumnClass(int c) {
						return getValueAt(0, c).getClass();
				}

				/*
				 * Don't need to implement this method unless your table's
				 * editable.
				 */
				public boolean isCellEditable(int row, int col) {
						//Note that the data/cell address is constant,
						//no matter where the cell appears onscreen.
						if (col < 1) {
								return false;
						} else {
								return true;
						}
				}

				public void setValueAt(Object value, int row, int col) {
						data[row][col] = value;
						fireTableCellUpdated(row, col);
				}
		}

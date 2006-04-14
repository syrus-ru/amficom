/*-
 * $Id: ReflectometryParametersPanel.java,v 1.1.2.8 2006/04/14 14:26:53 saa Exp $
 * 
 * Copyright � 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Schedule.UI.MeasurementParameters.Property;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.measurement.ActionTemplate;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind;
import com.syrus.util.Log;

/**
 * @todo ���� ����� ������ ����� ����������� �������� � ��������.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1.2.8 $, $Date: 2006/04/14 14:26:53 $
 * @module
 */
public class ReflectometryParametersPanel extends MeasurementParametersPanel {
	MeasurementParameters parameters = null;
	Map<JComponent, MeasurementParameters.Property> requirements;
	boolean skip; // ���������� action change �������

	public ReflectometryParametersPanel() {
		this.requirements =
			new HashMap<JComponent, MeasurementParameters.Property>();
		createUI();
	}

	private void createUI() {
		JComboBox refractComboBox = new JComboBox();
		JComboBox waveLengthComboBox = new JComboBox();
		JComboBox maxDistanceComboBox = new JComboBox();
		JComboBox pulseWidthMComboBox = new JComboBox();
		pulseWidthMComboBox.setMaximumRowCount(15);
		JComboBox pulseWidthNsComboBox = new JComboBox();
		pulseWidthNsComboBox.setMaximumRowCount(15);
		JComboBox resolutionComboBox = new JComboBox();
		JComboBox averagesComboBox = new JComboBox();
		JFormattedTextField averagesField = new JFormattedTextField(); // XXX: ��� ��� ����� ��������?

//		JLabel refractLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.IndexOfRefraction"));
//		JLabel waveLengthLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.WaveLength"));
//		JLabel averagesCBLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.AverageQuantity"));
//		JLabel averagesFLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.AverageQuantity"));
//		JLabel resolutionLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.Resolution"));
//		JLabel maxDistanceLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.Distance"));
//		JLabel pulseWidthMLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.PulseWidth"));
//		JLabel pulseWidthNsLabel = new JLabel(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.PulseWidth"));
//
//		JCheckBox gsOptionBox = new JCheckBox(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.GainSplice"));
//		JCheckBox smoothOptionBox = new JCheckBox(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.BoxCar"));
//		JCheckBox lfdOptionBox = new JCheckBox(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.LiveFiberDetect"));

//		JCheckBox highResolutionCheckBox = new JCheckBox(I18N.getString("Scheduler.Text.MeasurementParameter.Reflectomety.HighResolution"));

		JLabel refractLabel = new JLabel();
		JLabel waveLengthLabel = new JLabel();
		JLabel averagesCBLabel = new JLabel();
		JLabel averagesFLabel = new JLabel();
		JLabel resolutionLabel = new JLabel();
		JLabel maxDistanceLabel = new JLabel();
		JLabel pulseWidthMLabel = new JLabel();
		JLabel pulseWidthNsLabel = new JLabel();

		JCheckBox gsOptionBox = new JCheckBox();
		JCheckBox smoothOptionBox = new JCheckBox();
		JCheckBox lfdOptionBox = new JCheckBox();

		/* Note: �������� ��� ���� ����� ��� ����� ����� ����� �� I18N (see MeasurementParameters) */
		JCheckBox highResolutionCheckBox = new JCheckBox();

		this.requirements.put(refractComboBox, MeasurementParameters.Property.E_REFRACTION_INDEX);
		this.requirements.put(waveLengthComboBox, MeasurementParameters.Property.E_WAVELENGTH);
		this.requirements.put(maxDistanceComboBox, MeasurementParameters.Property.E_TRACELENGTH);
		this.requirements.put(highResolutionCheckBox, MeasurementParameters.Property.FLAG_HIRES);

		this.requirements.put(pulseWidthMComboBox, MeasurementParameters.Property.E_PULSE_WIDTH_M);
		this.requirements.put(pulseWidthNsComboBox, MeasurementParameters.Property.E_PULSE_WIDTH_NS);
		this.requirements.put(resolutionComboBox, MeasurementParameters.Property.E_RESOLUTION);
		this.requirements.put(averagesComboBox, MeasurementParameters.Property.E_AVERAGES);
		this.requirements.put(averagesField, MeasurementParameters.Property.I_AVERAGES);

		this.requirements.put(refractLabel, MeasurementParameters.Property.E_REFRACTION_INDEX);
		this.requirements.put(waveLengthLabel, MeasurementParameters.Property.E_WAVELENGTH);
		this.requirements.put(averagesCBLabel, MeasurementParameters.Property.E_AVERAGES);
		this.requirements.put(averagesFLabel, MeasurementParameters.Property.I_AVERAGES);
		this.requirements.put(resolutionLabel, MeasurementParameters.Property.E_RESOLUTION);
		this.requirements.put(maxDistanceLabel, MeasurementParameters.Property.E_TRACELENGTH);
		this.requirements.put(pulseWidthMLabel, MeasurementParameters.Property.E_PULSE_WIDTH_M);
		this.requirements.put(pulseWidthNsLabel, MeasurementParameters.Property.E_PULSE_WIDTH_NS);

		this.requirements.put(gsOptionBox, MeasurementParameters.Property.FLAG_GAIN_SPLICE);
		this.requirements.put(smoothOptionBox, MeasurementParameters.Property.FLAG_SMOOTH);
		this.requirements.put(lfdOptionBox, MeasurementParameters.Property.FLAG_LFD);

		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ReflectometryParametersPanel.this.skip) {
					return;
				}
				final Object source = e.getSource();
				ReflectometryParametersPanel.this.processorUiChange(source);
			}
		};

		FocusListener focusListener = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (ReflectometryParametersPanel.this.skip) {
					return;
				}
				final Object source = e.getSource();
				ReflectometryParametersPanel.this.processorUiChange(source);
			}
		};
		for (JComponent component : this.requirements.keySet()) {
			if (component instanceof JCheckBox) {
				((JCheckBox) component).addActionListener(actionListener);
			} else if (component instanceof JComboBox) {
				((JComboBox) component).addActionListener(actionListener);
			} else if (component instanceof JTextField) {
				((JTextField) component).addActionListener(actionListener);
				((JTextField) component).addFocusListener(focusListener);
			}
		}

		this.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;

		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(refractLabel, gbc);
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(refractComboBox, gbc);

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(waveLengthLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		add(waveLengthComboBox, gbc);

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(maxDistanceLabel, gbc);
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(maxDistanceComboBox, gbc);

		gbc.weightx = 1.0;
		add(highResolutionCheckBox, gbc);

		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(pulseWidthMLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		add(pulseWidthMComboBox, gbc);

		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(pulseWidthNsLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		add(pulseWidthNsComboBox, gbc);

		highResolutionCheckBox.doClick();

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(averagesCBLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		add(averagesComboBox, gbc);

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(averagesFLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		add(averagesField, gbc);

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(resolutionLabel, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		add(resolutionComboBox, gbc);

		gbc.weightx = 1.0;
//		gbc.gridwidth = GridBagConstraints.RELATIVE;
//		add(new JLabel(I18N.getString("Scheduler.Text.GainSplice")), gbc); //$NON-NLS-1$
//		gbc.weightx = 0.0;
//		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(gsOptionBox, gbc);

		gbc.weightx = 1.0;
//		add(bcOptionBox, gbc);
		smoothOptionBox.setSelected(false);
		smoothOptionBox.setEnabled(false);


		gbc.weightx = 1.0;
//		gbc.gridwidth = GridBagConstraints.RELATIVE;
//		add(new JLabel(I18N.getString("Scheduler.Text.LiveFiberDetect")), gbc); //$NON-NLS-1$
//		gbc.weightx = 0.0;
//		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(lfdOptionBox, gbc);

		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.SOUTH;
		add(new JLabel(), gbc);
	}

	void processorUiChange(Object source) {
		final Property property = this.requirements.get(source);
		if (property == null) {
			Log.errorMessage("RPP: action listner: unknown event source " + source);
			return;
		}
		final MeasurementParameters pars = this.parameters;
		if (pars == null) {
			Log.debugMessage("RPP: action listner: parameters are not present yet", Level.FINE);
			return; // ignore
		}
		if (source instanceof JCheckBox) {
			JCheckBox box = (JCheckBox) source;
			pars.setPropertyAsBoolean(property, box.isSelected());
		} else if (source instanceof JComboBox) {
			JComboBox box = (JComboBox) source;
			pars.setPropertyStringValue(property, (String)box.getSelectedItem());
		} else if (source instanceof JTextField) {
			JTextField field = (JTextField) source;
			try {
				pars.setPropertyStringValue(property, field.getText());
			} catch (NumberFormatException e) {
				// ��������� ������ �� ���������� ��� �����
				// �������� ����: ������������� ������ ��������
				this.skip = true;
				field.setText(pars.getPropertyStringValue(property));
				this.skip = false;
			}
		} else {
			throw new InternalError("Unknown source type: " + source);
		}
	}

	private void updateVisibilityAndValues() {

		this.skip = true;

		for (JComponent component : this.requirements.keySet()) {
			// ���������� ���������
			final Property property = this.requirements.get(component);
			final boolean visible = this.parameters != null && this.parameters.hasProperty(property);
			component.setVisible(visible);

			// ���������� ��������
			if (visible) {
				if (component instanceof JLabel
						|| component instanceof JCheckBox) {
					String description =
						this.parameters.getPropertyDescription(property);

					if (component instanceof JLabel) {
						((JLabel)component).setText(description);
					} else {
						((JCheckBox)component).setText(description);
					}
				}
			}

			// ������������� ����� ���������� �������� � ������� ��������
			if (visible) {
				final ParameterValueKind valueKind = this.parameters.getPropertyValueKind(property);
				if (component instanceof JLabel) {
					// JLabel is not a control, skip
				} else if (component instanceof JComboBox) {
					JComboBox comboBox = (JComboBox) component;
					// ��������� ����� ��������
					if (valueKind != ParameterValueKind.ENUMERATED) {
						throw new InternalError("Unsupported value kind");
					}
					comboBox.removeAllItems();
					for (String value: this.parameters.valuesPropertyStringValue(property)) {
						comboBox.addItem(value);
					}
					// �������� ������� ��������
					final String currentValue = this.parameters.getPropertyStringValue(property);
					comboBox.setSelectedItem(currentValue);
				} else if (component instanceof JCheckBox) {
					JCheckBox checkBox = (JCheckBox) component;
					// ����� �������� ��������� �� ����
					checkBox.setSelected(this.parameters.getPropertyAsBoolean(property));
				} else if (component instanceof JTextField) {
					JTextField textField = (JTextField) component;
					if (valueKind != ParameterValueKind.CONTINUOUS) {
						throw new InternalError("Unsupported value kind");
					}
					// ����� �������� ��������� �� ����
					// ������������� ������� ��������
					textField.setText(this.parameters.getPropertyStringValue(property));
				} else {
					assert false; // did we add component of any other type?
				}
			}
		}

		this.skip = false;
	}

//	private String getUnit(final ParameterType parameterType) {
//		String name = parameterType.getMeasurementUnit().getName();
//		return name.trim().length() > 0 ? ',' + name : "";
//	}

	@Override
	public final ActionTemplate<Measurement> getMeasurementTemplate()
			throws CreateObjectException {
		try {
			return parameters.createMeasurementTemplate();
		} catch (CreateObjectException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}
	}

	@Override
	public void setMeasurementTemplate(ActionTemplate<Measurement> template) {
		try {
//			this.parameters = new MeasurementParameters(template);
			this.parameters.setTemplate(template);
		} catch (ApplicationException e) {
			/* XXX: ApplicationException processing */
			Log.errorMessage(e);
			throw new InternalError(e.getMessage());
		}

		updateVisibilityAndValues();
	}

	@Override
	public void setMonitoredElement(MonitoredElement me) {
		if (this.parameters != null && this.parameters.getMe().equals(me)) {
			return; // same ME
		}
		try {
			this.parameters = new MeasurementParameters(me);
		} catch (ApplicationException e) {
			/* XXX: ApplicationException processing */
			Log.errorMessage(e);
			throw new InternalError(e.getMessage());
		}

		updateVisibilityAndValues();
	}

	/**
	 * @param b
	 * @see com.syrus.AMFICOM.Client.Schedule.UI.MeasurementParametersPanel#setEnableEditing(boolean)
	 */
	@Override
	public void setEnableEditing(boolean b) {
		for (JComponent component: this.requirements.keySet()) {
			component.setEnabled(b);
		}
	}
}

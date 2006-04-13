/*-
 * $Id: ReflectometryParametersPanel.java,v 1.1.2.3 2006/04/13 12:21:04 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Schedule.UI.MeasurementParameters.Property;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.measurement.ActionTemplate;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind;
import com.syrus.util.Log;

/**
 * @todo дать этому классу более осмысленное название и описание.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1.2.3 $, $Date: 2006/04/13 12:21:04 $
 * @module
 */
public class ReflectometryParametersPanel extends MeasurementParametersPanel {
	private MeasurementParameters parameters = null;
	private Map<JComponent, MeasurementParameters.Property> requirements;

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
		JFormattedTextField averagesField = new JFormattedTextField(); // XXX: как оно будет работать?

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

		/* Note: название для этой опции все равно будет взято из I18N (see MeasurementParameters) */
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

	private void updateVisibilityAndValues() {
		for (JComponent component : this.requirements.keySet()) {
			// определяем видимость
			final Property property = this.requirements.get(component);
			final boolean visible = this.parameters != null && this.parameters.hasProperty(property);
			component.setVisible(visible);

			// определяем названия
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

			// устанавливаем набор допустимых значений и текущее значение
			if (visible) {
				final ParameterValueKind valueKind = this.parameters.getPropertyValueKind(property);
				if (component instanceof JLabel) {
					// JLabel is not a control, skip
				} else if (component instanceof JComboBox) {
					JComboBox comboBox = (JComboBox) component;
					// загружаем набор значений
					if (valueKind != ParameterValueKind.ENUMERATED) {
						throw new InternalError("Unsupported value kind");
					}
					comboBox.removeAllItems();
					for (String value: this.parameters.valuesPropertyStringValue(property)) {
						comboBox.addItem(value);
					}
					// выбираем текущее значение
					final String currentValue = this.parameters.getPropertyStringValue(property);
					comboBox.setSelectedItem(currentValue);
				} else if (component instanceof JCheckBox) {
					JCheckBox checkBox = (JCheckBox) component;
					// набор значений загружать не надо
					checkBox.setSelected(this.parameters.getPropertyAsBoolean(property));
				} else if (component instanceof JTextField) {
					JTextField textField = (JTextField) component;
					if (valueKind != ParameterValueKind.CONTINUOUS) {
						throw new InternalError("Unsupported value kind");
					}
					// набор значений загружать не надо
					// устанавливаем текущее значение
					textField.setText(this.parameters.getPropertyStringValue(property));
				} else {
					assert false; // did we add component of any other type?
				}
			}
		}
	}

//	private String getUnit(final ParameterType parameterType) {
//		String name = parameterType.getMeasurementUnit().getName();
//		return name.trim().length() > 0 ? ',' + name : "";
//	}

	@Override
	public final ActionTemplate<Measurement> getMeasurementTemplate()
			throws CreateObjectException {
		try {
			return parameters.createMeasurementTemplate(); // FIXME: сначала проверить, достаточно ли данных
		} catch (CreateObjectException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}
	}

	@Override
	public void setMeasurementTemplate(ActionTemplate<Measurement> template) {
		try {
			this.parameters = new MeasurementParameters(template);
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

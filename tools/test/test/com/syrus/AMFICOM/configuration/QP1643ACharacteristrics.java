/*
* $Id: QP1643ACharacteristrics.java,v 1.2 2004/12/29 09:18:08 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package test.com.syrus.AMFICOM.configuration;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.AMFICOM.configuration.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.measurement.ParameterTypeCodenames;


/**
 * @version $Revision: 1.2 $, $Date: 2004/12/29 09:18:08 $
 * @author $Author: bob $
 * @module tools
 */
public class QP1643ACharacteristrics extends ConfigureTestCase{

	
	public QP1643ACharacteristrics(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = QP1643ACharacteristrics.class;
		junit.awtui.TestRunner.run(clazz);
//		junit.swingui.TestRunner.run(clazz);
//		junit.textui.TestRunner.run(clazz);

	}

	public static Test suite() {
		return suiteWrapper(QP1643ACharacteristrics.class);
	}
	public void testCreateCharacteristics() throws CreateObjectException, ObjectNotFoundException, RetrieveObjectException{
		CharacteristicTypeDatabase characteristicTypeDatabase = (CharacteristicTypeDatabase)ConfigurationDatabaseContext.getCharacteristicTypeDatabase();
		
		
		
		CharacteristicType waveLengthType;
		try{
			waveLengthType = characteristicTypeDatabase.retrieveForCodename(ParameterTypeCodenames.TRACE_WAVELENGTH);
		}catch(ObjectNotFoundException onfe){
			waveLengthType = CharacteristicType.createInstance(
				creatorId,
				ParameterTypeCodenames.TRACE_WAVELENGTH,
				"reflectometer wavelength",
				DataType._DATA_TYPE_DOUBLE,									  
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				waveLengthType.insert();
		}
		
		
		CharacteristicType traceLengthType;
		try{
			traceLengthType = characteristicTypeDatabase.retrieveForCodename(ParameterTypeCodenames.TRACE_LENGTH);
		}catch(ObjectNotFoundException onfe){
			traceLengthType = CharacteristicType.createInstance(
				creatorId,
				ParameterTypeCodenames.TRACE_LENGTH,
				"reflectometer trace length",
				DataType._DATA_TYPE_DOUBLE,									  
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				traceLengthType.insert();

		}
		
		CharacteristicType pulseWidthType;
		try{
			pulseWidthType = characteristicTypeDatabase.retrieveForCodename(ParameterTypeCodenames.TRACE_PULSE_WIDTH);
		}catch(ObjectNotFoundException onfe){
			pulseWidthType = CharacteristicType.createInstance(
				creatorId,
				ParameterTypeCodenames.TRACE_PULSE_WIDTH,
				"reflectometer pulse width",
				DataType._DATA_TYPE_LONG,									  
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				pulseWidthType.insert();
		}

		CharacteristicType indexOfRefractionType; 
		
		try{
			indexOfRefractionType = characteristicTypeDatabase.retrieveForCodename(ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION);
		}catch(ObjectNotFoundException onfe){
			indexOfRefractionType = CharacteristicType.createInstance(
				creatorId,
				ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION,
				"reflectometer index of refraction",
				DataType._DATA_TYPE_DOUBLE,									  
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				indexOfRefractionType.insert();
		}
		
		CharacteristicType averageCountType;
		
		try{
			averageCountType = characteristicTypeDatabase.retrieveForCodename(ParameterTypeCodenames.TRACE_AVERAGE_COUNT);
		}catch(ObjectNotFoundException onfe){
			averageCountType = CharacteristicType.createInstance(
				creatorId,
				ParameterTypeCodenames.TRACE_AVERAGE_COUNT,
				"reflectometer average count",
				DataType._DATA_TYPE_INTEGER,									  
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL); 
			
			averageCountType.insert();
		}

		CharacteristicType resoulutionType;
		
		try{
			resoulutionType = characteristicTypeDatabase.retrieveForCodename(ParameterTypeCodenames.TRACE_RESOLUTION);
		}catch(ObjectNotFoundException onfe){
			resoulutionType = CharacteristicType.createInstance(
				creatorId,
				ParameterTypeCodenames.TRACE_RESOLUTION,
				"reflectometer resolution",
				DataType._DATA_TYPE_DOUBLE,									  
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL); 
			
			resoulutionType.insert();
		}

		MeasurementPort measurementPort = new MeasurementPort(new Identifier("MeasurementPort_19"));
		
		Characteristic wlCharacteristic = Characteristic.createInstance(
			  creatorId,
			  waveLengthType,
			  ParameterTypeCodenames.TRACE_WAVELENGTH,
			  "QP1643A wavelength",
			  CharacteristicSort._CHARACTERISTIC_SORT_KIS,
			  "1625.00",
			  measurementPort.getId(),
			  false,
			  true);
		
		wlCharacteristic.insert();
		
		Characteristic traceLengthCharacteristic = Characteristic.createInstance(
			  creatorId,
			  traceLengthType,
			  ParameterTypeCodenames.TRACE_WAVELENGTH + "_1625_" + ParameterTypeCodenames.TRACE_LENGTH,
			  "QP1643A trace length at wavelength 1625.00 nm",
			  CharacteristicSort._CHARACTERISTIC_SORT_KIS,
			  "5.00 20.00 50.00 75.00 125.00 250.00 300.00",
			  measurementPort.getId(),
			  false,
			  true);
		
		traceLengthCharacteristic.insert();
		
		Characteristic pulseWidthCharacteristic = Characteristic.createInstance(
			  creatorId,
			  pulseWidthType,
			  ParameterTypeCodenames.TRACE_WAVELENGTH + "_1625_" + ParameterTypeCodenames.TRACE_PULSE_WIDTH,
			  "QP1643A pulse width at wavelength 1625.00 nm",
			  CharacteristicSort._CHARACTERISTIC_SORT_KIS,
			  "327680 655360 1310720 3276800 6553606 13107206 32768006 65536006 131072006 327680006 655360007 1310720007 1966080007",
			  measurementPort.getId(),
			  false,
			  true);
		
		pulseWidthCharacteristic.insert();
		
		Characteristic indexOfRefractionCharacteristic = Characteristic.createInstance(
			  creatorId,
			  indexOfRefractionType,
			  ParameterTypeCodenames.TRACE_WAVELENGTH + "_1625_" + ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION,
			  "QP1643A index of refraction at wavelength 1625.00 nm",
			  CharacteristicSort._CHARACTERISTIC_SORT_KIS,
			  "1.468200",
			  measurementPort.getId(),
			  false,
			  true);
		
		indexOfRefractionCharacteristic.insert();
		
		Characteristic averageCountCharacteristic = Characteristic.createInstance(
			  creatorId,
			  averageCountType,
			  ParameterTypeCodenames.TRACE_WAVELENGTH + "_1625_" + ParameterTypeCodenames.TRACE_AVERAGE_COUNT,
			  "QP1643A average count at wavelength 1625.00 nm",
			  CharacteristicSort._CHARACTERISTIC_SORT_KIS,
			  "4096 45312 262144",
			  measurementPort.getId(),
			  false,
			  true);
		
		averageCountCharacteristic.insert();
		
		Characteristic resolutionCharacteristic = Characteristic.createInstance(
			  creatorId,
			  resoulutionType,
			  ParameterTypeCodenames.TRACE_RESOLUTION,
			  "QP1643A resolution",
			  CharacteristicSort._CHARACTERISTIC_SORT_KIS,
			  "16.00 8.00 4.00 2.00 1.00 0.50 0.25",
			  measurementPort.getId(),
			  false,
			  true);	
		
		resolutionCharacteristic.insert();
		
	}
}

package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.Iterator;
import java.math.BigDecimal;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OracleResultSet;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.ora.TemporalTemplate;
import com.syrus.AMFICOM.measurement.ora.TimeQuantum;
import com.syrus.AMFICOM.measurement.ora.TimeQuantumArray;
import com.syrus.AMFICOM.measurement.ora.DayTime;
import com.syrus.AMFICOM.measurement.ora.DayTimeArray;

public class PTTemporalTemplate_Database extends StorableObject_Database {

	public void retrieve(StorableObject storableObject) throws Exception {
		PTTemporalTemplate pttt = null;
		if (storableObject instanceof PTTemporalTemplate)
			pttt = (PTTemporalTemplate)storableObject;
		else
			throw new Exception("PTTemporalTemplate_Database.retrieve | Illegal Storable Object: " + storableObject.getClass().getName());

		String pttt_id_str = pttt.getId().toString();
		String sql = "SELECT description, " + DatabaseDate.toQuerySubString("created") + ", value FROM " + ObjectEntities.PTTEMPORALTEMPLATE_ENTITY + " WHERE id = " + pttt_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("PTTemporalTemplate_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				TemporalTemplate temporalTemplate = (TemporalTemplate)((OracleResultSet)resultSet).getORAData("value", TemporalTemplate.getORADataFactory());
				TimeQuantum period = temporalTemplate.getPeriod();

				DayTime[] dayTimesArray = temporalTemplate.getDayTimes().getArray();
				LinkedList dayTimes = new LinkedList();
				for (int i = 0; i < dayTimesArray.length; i++)
					dayTimes.add(new PTTemporalTemplate.DayTime(dayTimesArray[i].getHour().intValue(),
																											dayTimesArray[i].getMinute().intValue(),
																											dayTimesArray[i].getSecond().intValue()));

				TimeQuantum[] datesArray = temporalTemplate.getDates().getArray();
				LinkedList dates = new LinkedList();
				for (int i = 0; i < datesArray.length; i++)
					dates.add(new PTTemporalTemplate.TimeQuantum(datesArray[i].getScale().intValue(),
																											 datesArray[i].getValue().intValue()));

				pttt.setAttributes(resultSet.getString("description"),
													 DatabaseDate.fromQuerySubString(resultSet, "created"),
													 new PTTemporalTemplate.TimeQuantum(period.getScale().intValue(), period.getValue().intValue()),
													 dayTimes,
													 dates);
			}
			else
				throw new Exception("No such temporal template: " + pttt_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "PTTemporalTemplate_Database.retrieve | Cannot retrieve temporal template " + pttt_id_str;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {}
		}
	}
	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		PTTemporalTemplate pttt = null;
		if (storableObject instanceof PTTemporalTemplate)
			pttt = (PTTemporalTemplate)storableObject;
		else
			throw new Exception("PTTemporalTemplate_Database.retrieveObject | Illegal Storable Object: " + storableObject.getClass().getName());

		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		PTTemporalTemplate pttt = null;
		if (storableObject instanceof PTTemporalTemplate)
			pttt = (PTTemporalTemplate)storableObject;
		else
			throw new Exception("PTTemporalTemplate_Database.insert | Illegal Storable Object: " + storableObject.getClass().getName());

		long pttt_id_code = pttt.getId().getCode();
		String sql = "INSERT INTO " + ObjectEntities.PTTEMPORALTEMPLATE_ENTITY + " (id, description, created, value) VALUES (?, ?, ?, ?)";
		PreparedStatement preparedStatement = null;
		try {
				PTTemporalTemplate.TimeQuantum period = pttt.getPeriod();

				LinkedList dayTimes = pttt.getDayTimes();
				DayTime[] dayTimesArray = new DayTime[dayTimes.size()];
				PTTemporalTemplate.DayTime dayTime;
				int i = 0;
				for (Iterator iterator = dayTimes.iterator(); iterator.hasNext();) {
					dayTime = (PTTemporalTemplate.DayTime)iterator.next();
					dayTimesArray[i++] = new DayTime(new BigDecimal((double)dayTime.hour),
																					 new BigDecimal((double)dayTime.minute),
																					 new BigDecimal((double)dayTime.second));
				}

				LinkedList dates = pttt.getDates();
				TimeQuantum[] datesArray = new TimeQuantum[dates.size()];
				PTTemporalTemplate.TimeQuantum date;
				i = 0;
				for (Iterator iterator = dates.iterator(); iterator.hasNext();) {
					date = (PTTemporalTemplate.TimeQuantum)iterator.next();
					datesArray[i++] = new TimeQuantum(new BigDecimal((double)date.scale),
																						new BigDecimal((double)date.value));
				}

				TemporalTemplate temporalTemplate = new TemporalTemplate(new TimeQuantum(new BigDecimal((double)period.scale), new BigDecimal((double)period.value)),
																																 new DayTimeArray(dayTimesArray),
																																 new TimeQuantumArray(datesArray));
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setLong(1, pttt_id_code);
				preparedStatement.setString(2, pttt.getDescription());
				preparedStatement.setDate(3, new java.sql.Date(pttt.getCreated().getTime()));
				((OraclePreparedStatement)preparedStatement).setORAData(4, temporalTemplate);
				Log.debugMessage("PTTemporalTemplate_Database.insert | Inserting template " + pttt_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
				connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "PTTemporalTemplate_Database.insert | Cannot insert temporal template " + pttt_id_code;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {}
		}
	}

	public void update(StorableObject storableObject, int update_kind, Object obj) throws Exception {
		PTTemporalTemplate pttt = null;
		if (storableObject instanceof PTTemporalTemplate)
			pttt = (PTTemporalTemplate)storableObject;
		else
			throw new Exception("PTTemporalTemplate_Database.update | Illegal Storable Object: " + storableObject.getClass().getName());
	}
}
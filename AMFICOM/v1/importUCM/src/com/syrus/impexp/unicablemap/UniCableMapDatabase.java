package com.syrus.impexp.unicablemap;

import com.syrus.impexp.ImportExportException;
import java.sql.*;
import java.util.*;
import javax.naming.*;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.1.1.1 $, $Date: 2005/03/30 07:31:39 $
 * @module mapviewclient_v1
 */
public class UniCableMapDatabase {
	String driverName = "jdbc:firebirdsql";

	Connection connection = null;
	
	static String BUF_DELIMITER = "\\[Дуська\\]";

	public IntHashtable objects = new IntHashtable();
	public IntHashtable links = new IntHashtable();

	public IntHashtable objectTypes = new IntHashtable();
	public IntHashtable linkTypes = new IntHashtable();
	public IntHashtable parameterTypes = new IntHashtable();

	public IntTreeMap type_elements = new IntTreeMap();

	public UniCableMapDatabase(
		String username,
		String password,
		String host,
		String database)
		throws ImportExportException
	{
		try {
			String url = this.driverName + ":" + host + ":" + database;
			this.connection = getDriverConnection(url, username, password);

			checkForWarning (connection.getWarnings());
	
			DatabaseMetaData dma = connection.getMetaData();
	
			System.out.println("\nConnected to " + dma.getURL());
			System.out.println("Driver       " + 
				dma.getDriverName());
			System.out.println("Version      " +
				dma.getDriverVersion());
			System.out.println("");
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new ImportExportException("Cannot load driver " + this.driverName);
		} catch (SQLException e) {
			printSQLException(e);
			throw new ImportExportException("Cannot connect to " + database + " at " + host);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new ImportExportException("Cannot load driver " + this.driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ImportExportException("Cannot load driver " + this.driverName);
		}

		try {
			UniCableMapObject.init(this.connection);
			UniCableMapType.init(this.connection);
			UniCableMapLink.init(this.connection);
			
			scanTypes();

			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_ABSTRACT));
			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_REAL));

			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_BOOLEAN));
			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_BUTTON));
			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_DATE));
			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_DEPENDENCY));
			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_DROPDOWN_LIST));
			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_FLOAT));
			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_GRAPHICAL_LINK));
			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_IMAGE));
			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_INTEGER));
			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_PASSWORD));
			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_PICTURE));
			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_REFERENCE));
			scanParameterTypes(getType(UniCableMapType.UCM_PARAMETER_STRING));

			scanLinkTypes(getType(UniCableMapType.UCM_LINK_TYPE_GENERAL));
			scanLinkTypes(getType(UniCableMapType.UCM_LINK_TYPE_REAL));
			scanObjects();
		}
		catch (java.sql.SQLException ex) {
			printSQLException(ex);
			throw new ImportExportException("Wrong format of database: " + ex.getMessage());
		}
	}

	public void scanObjects() throws SQLException
	{
		if(this.connection == null)
			return;

		int counter = 0;
		
		try
		{
			ResultSet resultSet = UniCableMapObject.objectStatement.executeQuery();
			
			boolean more = resultSet.next();
			while (more) {
				UniCableMapObject ucmObject = new UniCableMapObject();
				parseUniCableMapObject(resultSet, ucmObject);

				UniCableMapType ucmType = ucmObject.typ;

				if(ucmType == null)
					System.out.println("NULL! " + ucmObject);

				IntTreeMap elements = (IntTreeMap )type_elements.get(ucmObject.typ.un);
				elements.put(ucmObject.un, ucmObject);

				more = resultSet.next();
				counter++;
			}

			resultSet.close();
		}
		catch (java.sql.SQLException e)
		{
			printSQLException(e);
		}

		for(Iterator it = type_elements.keySet().iterator(); it.hasNext();)
		{
			Integer key = (Integer )it.next();
			UniCableMapType ucmType = (UniCableMapType )
				objectTypes.get(key.intValue());
			IntTreeMap elements = (IntTreeMap )type_elements.get(key.intValue());
			if(ucmType.text != null 
					&& UniCableMapType.map_objects.contains(ucmType.text)
					&& elements.size() != 0) {
				System.out.println("TYPE: " + ucmType.text + " (" + ucmType.un + ") COUNT: " + 
					elements.size());
//				for(Iterator iter = elements.values().iterator(); iter.hasNext();)
//				{
//					System.out.println(iter.next());
//				}
			}
		}

		System.out.println("Total: " + counter);
	}
	
	void scanTypes() {
		if(this.connection == null)
			return;

		try
		{
			UniCableMapType.typedStatement.clearParameters();
			UniCableMapType.typedStatement.setInt(1, 1000);
			ResultSet resultSet = UniCableMapType.typedStatement.executeQuery();
			
			boolean more = resultSet.next();
			int counter = 0;
			while (more) {
				UniCableMapType ucmType = new UniCableMapType();
				parseUniCableMapObject(resultSet, ucmType);

				if(objectTypes.get(ucmType.un) == null) {
					objectTypes.put(ucmType.un, ucmType);
					type_elements.put(ucmType.un, new IntTreeMap());
//				System.out.println("	public static final String UCM_" + ucmType.text + " = \"" + ucmType.text + "\";");
				}		
				more = resultSet.next();
				counter++;
			}

			resultSet.close();
			System.out.println("Total " + counter + " types");
		}
		catch (java.sql.SQLException e)
		{
			printSQLException(e);
		}
	}
	
	void scanLinkTypes(UniCableMapType ucmType) {
		if(this.connection == null)
			return;
		
		try
		{
			UniCableMapType.typedStatement.clearParameters();
			UniCableMapType.typedStatement.setInt(1, ucmType.un);
			ResultSet resultSet = UniCableMapType.typedStatement.executeQuery();
			
			boolean more = resultSet.next();
			int counter = 0;
			while (more) {
				UniCableMapLinkType ucmLinkType = new UniCableMapLinkType();
				parseUniCableMapObject(resultSet, ucmLinkType);

				if(linkTypes.get(ucmLinkType.un) == null) {
					linkTypes.put(ucmLinkType.un, ucmLinkType);
				}		
//				System.out.println("	public static final String UCM_" + ucmLinkType.text + " = \"" + ucmLinkType.text + "\";");
				more = resultSet.next();
				counter++;
			}

			resultSet.close();
			System.out.println("Total " + counter + " link types (" + ucmType.text + ")");
		}
		catch (SQLException e)
		{
			printSQLException(e);
		}
	}

	void scanParameterTypes(UniCableMapType ucmType) {
		if(this.connection == null)
			return;
		
		try
		{
			UniCableMapType.typedStatement.clearParameters();
			UniCableMapType.typedStatement.setInt(1, ucmType.un);
			ResultSet resultSet = UniCableMapType.typedStatement.executeQuery();
			
			boolean more = resultSet.next();
			int counter = 0;
			while (more) {
				UniCableMapObject ucmParameterType = new UniCableMapObject();
				parseUniCableMapObject(resultSet, ucmParameterType);

				if(parameterTypes.get(ucmParameterType.un) == null) {
					parameterTypes.put(ucmParameterType.un, ucmParameterType);
				}		
//				System.out.println("	public static final String UCM_" + ucmParameterType.text + " = \"" + ucmParameterType.text + "\"; //" + ucmParameterType.un);
				more = resultSet.next();
				counter++;
			}

			resultSet.close();
			System.out.println("Total " + counter + " parameter types (" + ucmType.text + ")");
		}
		catch (SQLException e)
		{
			printSQLException(e);
		}
	}
/*
	void scanTypesPre() {
		if(this.connection == null)
			return;
		Statement statement;
		String query = "select " +
				"UN, " +
				"FLAG, " +
				"LAY, " +
				"UN_REQ, " +
				"UN_MIN, " +
				"UN_MAX, " +
				"UN_DEF, " +
				"STD_SIZE, " +
				"ABR " +
			"from MODEL_TYPES";

		try
		{
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			boolean more = resultSet.next();
			while (more) {
				UniCableMapType ucmType = new UniCableMapType();
				ucmType.un = resultSet.getInt("UN");
				ucmType.flag = resultSet.getInt("FLAG");
				ucmType.lay = resultSet.getString("LAY");
				ucmType.un_req = resultSet.getInt("UN_REQ");
				ucmType.un_min = resultSet.getInt("UN_MIN");
				ucmType.un_max = resultSet.getInt("UN_MAX");
				ucmType.un_def = resultSet.getInt("UN_DEF");
				ucmType.std_size = resultSet.getInt("STD_SIZE");
				ucmType.abr = resultSet.getString("ABR");

				types.put(ucmType.un, ucmType);
				type_elements.put(ucmType.un, new IntTreeMap());
		
				more = resultSet.next();
			}

			resultSet.close();
			statement.close();
		}
		catch (java.sql.SQLException e)
		{
			printSQLException(e);
		}
	}
*/
	private void parseUniCableMapObject(ResultSet resultSet, UniCableMapObject ucmObject) 
			throws java.sql.SQLException {
		ucmObject.un = resultSet.getInt("UN");
		int typ = resultSet.getInt("TYP");
		ucmObject.typ = getType(typ);
		ucmObject.text = resultSet.getString("TEXT");
		String buf = resultSet.getString("BUF");
		ucmObject.buf = parseBuf(buf);
		ucmObject.msk = resultSet.getInt("MSK");
		ucmObject.ord = resultSet.getInt("ORD");
		ucmObject.dx = resultSet.getDouble("DX");
		ucmObject.dy = resultSet.getDouble("DY");
		ucmObject.x0 = resultSet.getDouble("X0");
		ucmObject.y0 = resultSet.getDouble("Y0");
		ucmObject.x1 = resultSet.getDouble("X1");
		ucmObject.y1 = resultSet.getDouble("Y1");
		ucmObject.state = resultSet.getInt("STATE");
		ucmObject.elid = resultSet.getString("ELID");
	}

	private void parseUniCableMapLink(ResultSet resultSet, UniCableMapLink ucmLink) 
			throws java.sql.SQLException {
		int un_parent = resultSet.getInt("UN_PARENT");
		int un_child = resultSet.getInt("UN_CHILD");
		int mod = resultSet.getInt("MOD");

		ucmLink.msk = resultSet.getInt("MSK");
		ucmLink.lord = resultSet.getInt("LORD");
		ucmLink.sys = resultSet.getInt("SYS");
		ucmLink.parent = getObject(un_parent);
		ucmLink.child = getObject(un_child);
		ucmLink.mod = getLinkType(mod);
	}

	public Collection getObjects(UniCableMapType ucmType) 
			throws SQLException {
		Collection objects = new LinkedList();
		IntTreeMap elements = (IntTreeMap )type_elements.get(ucmType.un);
		objects.addAll(elements.values());
		return objects;
	}

	public Collection getChildren(UniCableMapObject ucmParent)
			throws SQLException {
		Collection objects = new LinkedList();
		UniCableMapLink.byParentStatement.clearParameters();
		UniCableMapLink.byParentStatement.setInt(1, ucmParent.un);
		ResultSet resultSet = UniCableMapLink.byParentStatement.executeQuery();
		boolean more = resultSet.next();
		while(more) {
			UniCableMapLink ucmLink = new UniCableMapLink();
			parseUniCableMapLink(resultSet, ucmLink);
			objects.add(ucmLink);
			more = resultSet.next();
		}
		return objects;
	}

	public Collection getChildren(UniCableMapObject ucmParent, UniCableMapLinkType ucmLinkType)
			throws SQLException {
		Collection objects = new LinkedList();
		UniCableMapLink.byParentTypedStatement.clearParameters();
		UniCableMapLink.byParentTypedStatement.setInt(1, ucmParent.un);
		UniCableMapLink.byParentTypedStatement.setInt(2, ucmLinkType.un);
		ResultSet resultSet = UniCableMapLink.byParentTypedStatement.executeQuery();
		boolean more = resultSet.next();
		while(more) {
			UniCableMapLink ucmLink = new UniCableMapLink();
			parseUniCableMapLink(resultSet, ucmLink);
			objects.add(ucmLink);
			more = resultSet.next();
		}
		return objects;
	}

	public Collection getParents(UniCableMapObject ucmChild) 
			throws SQLException {
		Collection objects = new LinkedList();
		UniCableMapLink.byChildStatement.clearParameters();
		UniCableMapLink.byChildStatement.setInt(1, ucmChild.un);
		ResultSet resultSet = UniCableMapLink.byChildStatement.executeQuery();
		boolean more = resultSet.next();
		while(more) {
			UniCableMapLink ucmLink = new UniCableMapLink();
			parseUniCableMapLink(resultSet, ucmLink);
			objects.add(ucmLink);
			more = resultSet.next();
		}
		return objects;
	}

	public Collection getParents(UniCableMapObject ucmChild, UniCableMapLinkType ucmLinkType) 
			throws SQLException {
		Collection objects = new LinkedList();
		UniCableMapLink.byChildTypedStatement.clearParameters();
		UniCableMapLink.byChildTypedStatement.setInt(1, ucmChild.un);
		UniCableMapLink.byChildTypedStatement.setInt(2, ucmLinkType.un);
		ResultSet resultSet = UniCableMapLink.byChildTypedStatement.executeQuery();
		boolean more = resultSet.next();
		while(more) {
			UniCableMapLink ucmLink = new UniCableMapLink();
			parseUniCableMapLink(resultSet, ucmLink);
			objects.add(ucmLink);
			more = resultSet.next();
		}
		return objects;
	}

	public UniCableMapObject getObject(int objectUn) 
			throws SQLException {
		UniCableMapObject ucmObject = (UniCableMapObject )objects.get(objectUn);
		if(ucmObject == null)
			ucmObject = retreiveObject(objectUn);
		return ucmObject;
	}
	
	public UniCableMapObject retreiveObject(int objectUn) 
			throws SQLException {
		UniCableMapObject ucmObject = null;
		UniCableMapObject.unObjectStatement.clearParameters();
		UniCableMapObject.unObjectStatement.setInt(1, objectUn);
		ResultSet resultSet = UniCableMapObject.unObjectStatement.executeQuery();
		if(resultSet.next())
		{
			ucmObject = new UniCableMapObject();
			objects.put(objectUn, ucmObject);
			parseUniCableMapObject(resultSet, ucmObject);
		}
		return ucmObject;
	}
	
	public UniCableMapLinkType getLinkType(String typeText)
			throws SQLException {
		for(Enumeration en = linkTypes.elements(); en.hasMoreElements();)
		{
			UniCableMapLinkType ucmLinkType = (UniCableMapLinkType )en.nextElement();
			if(ucmLinkType.text.equals(typeText))
				return ucmLinkType;
		}
		return null;
	}

	public UniCableMapLinkType getLinkType(int typeUn) 
			throws SQLException {
		UniCableMapLinkType ucmType = (UniCableMapLinkType )linkTypes.get(typeUn);
		if(ucmType == null)
			ucmType = retreiveLinkType(typeUn);
		return ucmType;
	}

	public UniCableMapLinkType retreiveLinkType(int typeUn) 
			throws SQLException {
		UniCableMapLinkType ucmLinkType = null;
		UniCableMapObject.unObjectStatement.clearParameters();
		UniCableMapObject.unObjectStatement.setInt(1, typeUn);
		ResultSet resultSet = UniCableMapObject.unObjectStatement.executeQuery();
		if(resultSet.next())
		{
			ucmLinkType = new UniCableMapLinkType();
			linkTypes.put(typeUn, ucmLinkType);
			parseUniCableMapObject(resultSet, ucmLinkType);
		}
		return ucmLinkType;
	}

	public UniCableMapType getType(String typeText)
			throws SQLException {
		for(Enumeration en = objectTypes.elements(); en.hasMoreElements();)
		{
			UniCableMapType ucmObjectType = (UniCableMapType )en.nextElement();
			if(ucmObjectType.text.equals(typeText))
				return ucmObjectType;
		}
		return null;
	}

	public UniCableMapType getType(int typeUn)
			throws SQLException {
		UniCableMapType ucmObjectType = (UniCableMapType )objectTypes.get(typeUn);
		if(ucmObjectType == null)
			ucmObjectType = retreiveType(typeUn);
		return ucmObjectType;
	}
	
	public UniCableMapType retreiveType(int typeUn) 
			throws SQLException {
		UniCableMapType ucmObjectType = null;
		UniCableMapObject.unObjectStatement.clearParameters();
		UniCableMapObject.unObjectStatement.setInt(1, typeUn);
		ResultSet resultSet = UniCableMapObject.unObjectStatement.executeQuery();
		if(resultSet.next())
		{
			ucmObjectType = new UniCableMapType();
			objectTypes.put(typeUn, ucmObjectType);
			type_elements.put(typeUn, new IntTreeMap());
			parseUniCableMapObject(resultSet, ucmObjectType);
		}
		return ucmObjectType;
	}
	
	public UniCableMapBuf parseBuf(String buf) 
			throws SQLException {
		UniCableMapBuf ucmBuf = new UniCableMapBuf();
		if(buf != null) {
			String tokens[] = buf.split(BUF_DELIMITER);
			for (int i = 0; i < tokens.length; i++) {
				if(tokens[i].length() != 0) {
					UniCableMapParameter ucmParam = new UniCableMapParameter();
					int paramUn = Integer.parseInt(tokens[i].substring(0, tokens[i].indexOf(" ")));
					ucmParam.realParameter = getObject(paramUn);
					ucmParam.value = tokens[i].substring(tokens[i].indexOf(" ") + 1, tokens[i].length());
					
					ucmBuf.params.add(ucmParam);
				}
			}
		}
		return ucmBuf;
		
	}
	
	private Connection getDriverConnection(
			String url, 
			String username,
			String password) 
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// Load the jdbc-odbc bridge driver
		Class.forName ("org.firebirdsql.jdbc.FBDriver");

		java.sql.DriverManager.registerDriver (
			(java.sql.Driver) Class.forName ("org.firebirdsql.jdbc.FBDriver").newInstance() );

		Properties sysProps = System.getProperties ();
		StringBuffer drivers = new StringBuffer (this.driverName);
		String oldDrivers = sysProps.getProperty ("jdbc.drivers");
		if (oldDrivers != null)
			drivers.append (":" + oldDrivers);
		sysProps.put ("jdbc.drivers", drivers.toString ());
		System.setProperties (sysProps);

		DriverManager.setLogStream(System.out);
		
		// Параметры соединения с базой
		Properties connInfo = new Properties();
		
		connInfo.put("user", username);
		connInfo.put("password", password);
		connInfo.put("charSet", "Cp1251");
		
		// Устанавливаем соединение
		// Attempt to connect to a driver.  Each one
		// of the registered drivers will be loaded until
		// one is found that can process this URL
		return DriverManager.getConnection (url, connInfo);
	}

	public void close()
	{
		try
		{
			this.connection.close();
		}
		catch (java.sql.SQLException e)
		{
			printSQLException(e);
		}
	}
	
	private static boolean checkForWarning (java.sql.SQLWarning warn) 	
			throws java.sql.SQLException  {
		boolean rc = false;
	
		// If a SQLWarning object was given, display the
		// warning messages.  Note that there could be
		// multiple warnings chained together
	
		if (warn != null) {
			System.out.println ("\n *** Warning ***\n");
			rc = true;
			while (warn != null) {
				System.out.println("SQLState: " + warn.getSQLState());
				System.out.println("Message:  " + warn.getMessage());
				System.out.println("Vendor:   " + warn.getErrorCode());
				System.out.println("");
				warn = warn.getNextWarning();
			}
		}
		return rc;
	}
	
	private static void printSQLException(java.sql.SQLException ex) {
		// A SQLException was generated.  Catch it and
		// display the error information.  Note that there
		// could be multiple error objects chained
		// together
	
		System.out.println("\n*** SQLException caught ***\n");
	
		while (ex != null) {
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("Message:  " + ex.getMessage());
			System.out.println("Vendor:   " + ex.getErrorCode());
			ex = ex.getNextException();
			System.out.println("");
		}
	}

}








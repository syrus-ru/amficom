package com.syrus.AMFICOM.server.measurement;

import java.util.LinkedList;
import java.sql.*;
import oracle.jdbc.driver.OracleResultSet;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.AMFICOM.CORBA.Survey.ClientParameter_Transferable;
import com.syrus.AMFICOM.CORBA.KIS.Parameter_Transferable;
import com.syrus.AMFICOM.server.ResourcedbInterface;
import sqlj.runtime.ref.DefaultContext;

public class Parameter {
	private static final Connection CONN = DefaultContext.getDefaultContext().getConnection(); 

	private String id;
	private String result_id;
	private String type_id;
	private byte[] value;
	private String holder_sort;

  public Parameter(String id, String holder_sort) throws SQLException {
    this.id = id;
    this.holder_sort = holder_sort;

    Statement st = DatabaseConnection.getConnection().createStatement();
    String table_name = this.holder_sort + "parameters";
    
    String query = "SELECT result_id, type_id, value"
      + " FROM amficom." + table_name
      + " WHERE id = '" + this.id + "'";
//		Log.debugMessage("Parameter.init | Trying: " + query, Log.DEBUGLEVEL05);
    OracleResultSet rs = (OracleResultSet)st.executeQuery(query);
    if (!rs.next()) {
      String mesg = "No record in " + table_name + " with id '" + this.id + "'";
      throw new SQLException(mesg);
    }
    this.result_id = rs.getString("result_id");
    this.type_id = rs.getString("type_id");
    this.value = ByteArrayDatabase.toByteArray(rs.getBLOB("value"));
		rs.close();
		st.close();
  }

  public Parameter(ClientParameter_Transferable parameter_t,
                   String result_id,
                   String holder_sort) throws Exception {
    this(holder_sort,
         parameter_t.id,
         result_id,
         parameter_t.type_id,
         parameter_t.value);
    this.save();
  }

  public Parameter(Parameter_Transferable parameter_t,
                   String result_id,
                   String holder_sort) throws Exception {
    this(holder_sort,
         ResourcedbInterface.getUid(CONN, "parameter"),
         result_id,
         parameter_t.type_id,
         parameter_t.value);
    this.save();
  }

  private Parameter(String holder_sort,
                    String id,
                    String result_id,
                    String type_id,
                    byte[] value) {
    this.holder_sort = holder_sort;
    this.id = id;
    this.result_id = result_id;
    this.type_id = type_id;
    this.value = value;
  }

  public void save() throws Exception {
    Connection conn = DatabaseConnection.getConnection();
    Statement st = conn.createStatement();
    String table_name = this.holder_sort + "parameters";

    String update = "INSERT INTO " + table_name
      + " (id, result_id, type_id, value)"
      + " VALUES ('" + this.id + "', '" + this.result_id + "', '" + this.type_id + "', EMPTY_BLOB())";
//		Log.debugMessage("Parameter.init | Trying: " + update, Log.DEBUGLEVEL05);
    st.executeUpdate(update);
		st.close();
    ByteArrayDatabase.saveAsBlob(this.value, conn, table_name, "value", "id = '" + this.id + "'");
  }

  public String getId() {
    return this.id;
  }

  public String getResultId() {
    return this.result_id;
  }

  public String getTypeId() {
    return this.type_id;
  }

  public byte[] getValue() {
    return this.value;
  }

  public ClientParameter_Transferable getClientTransferable() throws SQLException {
    ParameterType parameterType = new ParameterType(this.type_id, this.holder_sort, "parameter");
    return new ClientParameter_Transferable(this.id,
                                            this.type_id,
                                            this.value,
                                            parameterType.getCodename(),
                                            parameterType.getParameterTypeId());
  }

  public Parameter_Transferable getTransferable() throws SQLException {
    ParameterType parameterType = new ParameterType(this.type_id, this.holder_sort, "parameter");
    return new Parameter_Transferable(parameterType.getCodename(),
                                      this.type_id,
                                      this.value);
  }

  public static Parameter[] retrieveParameters(String result_id, String holder_sort) throws SQLException {
    Statement st = DatabaseConnection.getConnection().createStatement();
    String table_name = holder_sort + "parameters";

    String query = "SELECT id, type_id, value"
      + " FROM amficom." + table_name
      + " WHERE result_id = '" + result_id + "'";
//		Log.debugMessage("Parameter.retrieveParameters | Trying: " + query, Log.DEBUGLEVEL05);
    OracleResultSet rs = (OracleResultSet)st.executeQuery(query);
    LinkedList llp = new LinkedList();
    while (rs.next())
      llp.add(new Parameter(holder_sort,
                            rs.getString("id"),
                            result_id,
                            rs.getString("type_id"),
                            ByteArrayDatabase.toByteArray(rs.getBLOB("value"))));
    rs.close();
		st.close();
//		Log.debugMessage("Parameter.retrieveParameters | Retrieved " + llp.size() + " " + holder_sort + " parameters for result_id = '" + result_id + "'", Log.DEBUGLEVEL05);
    Parameter[] parameters = new Parameter[llp.size()];
    return (Parameter[])llp.toArray(parameters);
  }
}

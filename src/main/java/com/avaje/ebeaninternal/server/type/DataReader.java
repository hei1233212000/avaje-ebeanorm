package com.avaje.ebeaninternal.server.type;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.SQLException;

public interface DataReader {

  void close() throws SQLException;

  boolean next() throws SQLException;

  void resetColumnPosition();

  void incrementPos(int increment);

  byte[] getBinaryBytes() throws SQLException;

  byte[] getBlobBytes() throws SQLException;

  InputStream getBlobInputStream() throws SQLException;

  String getStringFromStream() throws SQLException;

  String getStringClob() throws SQLException;

  Reader getClobReader() throws SQLException;

  String getString() throws SQLException;

  Boolean getBoolean() throws SQLException;

  Byte getByte() throws SQLException;

  Short getShort() throws SQLException;

  Integer getInt() throws SQLException;

  Long getLong() throws SQLException;

  Float getFloat() throws SQLException;

  Double getDouble() throws SQLException;

  byte[] getBytes() throws SQLException;

  java.sql.Date getDate() throws SQLException;

  java.sql.Time getTime() throws SQLException;

  java.sql.Timestamp getTimestamp() throws SQLException;

  BigDecimal getBigDecimal() throws SQLException;

  Array getArray() throws SQLException;

  Object getObject() throws SQLException;

  InputStream getBinaryStream() throws SQLException;
}

package com.satya.Persistence;

import java.sql.Connection;

public interface ConnectionProviderI {
	public Connection getConnection();
	public void releaseConnection(Connection c);
}

/**
 * Copyright 2010 Tim Azzopardi 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.lazyluke.log4jdbc.tools;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.sf.log4jdbc.ConnectionSpy;
import net.sf.log4jdbc.DriverSpy;
import net.sf.log4jdbc.SpyLogFactory;

/**
 * Inspired by http://groups.google.com/group/log4jdbc/browse_thread/thread/0706611d1b85e210
 * 
 * @author tim.azzopardi
 *
 */
public class Log4jdbcProxyDataSource implements DataSource {
    private DataSource realDataSource; 
    public Log4jdbcProxyDataSource(DataSource realDataSource) 
    { 
            this.realDataSource = realDataSource; 
    } 

    /**
     * Convenience constructor to make spring config easier 
     * @param realDataSource
     * @param loggingType
     */
    public Log4jdbcProxyDataSource(DataSource realDataSource, LoggingType loggingType) 
    { 
            this.realDataSource = realDataSource;
            SpyLogFactory.setSpyLogDelegator(new Log4JdbcCustomFormatter(loggingType));
    } 
    @Override
    public Connection getConnection() throws SQLException 
    { 
            final Connection connection = realDataSource.getConnection();
            return new ConnectionSpy(connection,DriverSpy.getRdbmsSpecifics(connection)); 
    } 
    @Override
    public Connection getConnection(String username, String password) throws SQLException 
    { 
            final Connection connection = realDataSource.getConnection(username, password);
            return new ConnectionSpy(connection,DriverSpy.getRdbmsSpecifics(connection)); 
    }
    public int getLoginTimeout() throws SQLException {
        return realDataSource.getLoginTimeout();
    }
    public PrintWriter getLogWriter() throws SQLException {
        return realDataSource.getLogWriter();
    }
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return realDataSource.isWrapperFor(iface);
    }
    public void setLoginTimeout(int seconds) throws SQLException {
        realDataSource.setLoginTimeout(seconds);
    }
    public void setLogWriter(PrintWriter out) throws SQLException {
        realDataSource.setLogWriter(out);
    }
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return realDataSource.unwrap(iface);
    } 
    

}

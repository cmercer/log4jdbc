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
package net.sf.log4jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;


/**
 * A proxy datasource that can be used to wrap a real data source allowing log4jdbc to do its work on the real 
 * data source.
 * Inspired by http://groups.google.com/group/log4jdbc/browse_thread/thread/0706611d1b85e210
 * 
 * This can be useful in a Spring context. Imagine your spring context includes this datasource definition
 * 
 * <code><pre>
 *   <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
 *     <property name="driverClass" value="${datasource.driverClassName}"/>
 *     <property name="jdbcUrl" value="${datasource.url}"/>
 *     <property name="user" value="${datasource.username}"/>
 *     <property name="password" value="${datasource.password}"/>
 *     <property name="initialPoolSize" value="${datasource.initialPoolSize}" />
 *     <property name="minPoolSize" value="${datasource.minPoolSize}" />
 *     <property name="maxPoolSize" value="${datasource.maxPoolSize}" />
 *     <property name="maxStatements" value="${datasource.maxStatements}" />
 *   </bean>
 * </pre></code>
 * 
 * You can get log4jdbc to work on this using the following config changes
 * 
 * <code><pre>
 *  <bean id="dataSourceSpied" class="com.mchange.v2.c3p0.ComboPooledDataSource">
 *      <property name="driverClass" value="${datasource.driverClassName}"/>
 *       <property name="jdbcUrl" value="${datasource.url}"/>
 *       <property name="user" value="${datasource.username}"/>
 *       <property name="password" value="${datasource.password}"/>
 *       <property name="initialPoolSize" value="${datasource.initialPoolSize}" />
 *       <property name="minPoolSize" value="${datasource.minPoolSize}" />
 *       <property name="maxPoolSize" value="${datasource.maxPoolSize}" />
 *       <property name="maxStatements" value="${datasource.maxStatements}" />
 *   </bean>
 *   
 *   <bean id="dataSource" class="net.sf.log4jdbc.tools.Log4jdbcProxyDataSource">
 *     <constructor-arg ref="dataSourceSpied" />
 *     <property name="logFormatter">
 *       <bean class="net.sf.log4jdbc.Log4JdbcCustomFormatter">
 *         <property name="loggingType" value="MULTI_LINE" />
 *         <property name="margin" value="20" />
 *       </bean>
 *    </property>
 *  </bean>
 * </pre></code>
 * 
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

    public SpyLogDelegator getLogFormatter() {
      return SpyLogFactory.getSpyLogDelegator();
    }
    /**
     * Set a custom SpyLogDelegator (default is usually Slf4jSpyLogDelegator)
     * @param spyLogDelegator
     */
    public void setLogFormatter(SpyLogDelegator spyLogDelegator) {
      SpyLogFactory.setSpyLogDelegator(spyLogDelegator);
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

/**
 * Copyright 2010 Tim Azzioardi
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
package org.lazyluke.log4jdbc;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import net.sf.log4jdbc.SpyLogDelegator;
import net.sf.log4jdbc.SpyLogFactory;

import org.springframework.core.io.Resource;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

public abstract class AbstractTest extends AbstractTransactionalDataSourceSpringContextTests {

    public AbstractTest() {
        setAutowireMode(AUTOWIRE_BY_NAME);
    }

    protected static final SpyLogDelegator log = SpyLogFactory.getSpyLogDelegator();


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
    }
    
    @Override
    protected String[] getConfigLocations() {
        return new String[]{"test-application-config.xml"};
    }

    /**
     * Spring provides executeSqlScript() (used in initialiseDatabase() above) to execute a series of sql statements each terminated by a semicolon. The sql
     * statements are executed one at time. This function executes the whole script as a single statement. This allows you to use a script that is a single
     * oracle anonymous block e.g. declare -- variable declarations begin -- pl/sql statements end; To use executeSqlScriptAsSingleStatement to initializing the
     * database in your tests, you need to override AbstractDAOTests#initialiseDatabase in your test subclass e.g.
     */
    protected void executeSqlScriptAsSingleStatement(String sql) {

        if (log.isJdbcLoggingEnabled()) {
        	log.debug("Executing SQL script '" + sql + "'");
        }

        long startTime = System.currentTimeMillis();
        Resource res = getApplicationContext().getResource(sql);
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        LineNumberReader lnr = null;
        try {
            inputStream = res.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            lnr = new LineNumberReader(inputStreamReader);
            String statement = lnr.readLine();
            String line;
            while ((line = lnr.readLine()) != null) {
                statement += "\n" + line;
            }

            jdbcTemplate.update(statement);
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.debug("Finished executing SQL script '" + sql + "' in " + elapsedTime + " ms");
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to open SQL script '" + sql + "'", ex);
        }
        finally {
        	close(inputStream);
            close(inputStreamReader);
            close(lnr);
        }
    }
    
    private void close(Closeable closeable) {
    	if (closeable == null) return;
    	try {
			closeable.close();
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
	}


}
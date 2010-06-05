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
package net.sf.log4jdbc.tools;

import net.sf.log4jdbc.ResultSetCollector;
import net.sf.log4jdbc.Slf4jSpyLogDelegator;
import net.sf.log4jdbc.Spy;
import net.sf.log4jdbc.StatementSpy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4JdbcCustomFormatter extends Slf4jSpyLogDelegator {

    private LoggingType loggingType = LoggingType.DISABLED;

    private String margin = "";

    public int getMargin() {
        return margin.length();
    }

    public void setMargin(int n) {
        margin = String.format("%1$#" + n + "s", "");
    }

    /**
     * Logger that shows the results in a table
     */
    private final Logger resultSetTableLogger = LoggerFactory.getLogger("jdbc.resultsettable");

    public Log4JdbcCustomFormatter() {
    }

    @Override
    public boolean isJdbcLoggingEnabled() {
        return super.isJdbcLoggingEnabled() || resultSetTableLogger.isErrorEnabled();
    }

    @Override
    public boolean isResultSetCollectionEnabled() {
        return resultSetTableLogger.isInfoEnabled();
    }

    @Override
    public boolean isResultSetCollectionEnabledWithUnreadValueFillIn() {
        return resultSetTableLogger.isDebugEnabled();
    }
    
    @Override
    public String sqlOccured(Spy spy, String methodCall, String rawSql) {
        if (loggingType == LoggingType.DISABLED) {
            return "";
        }

        // Remove all existing cr lf, unless MULTI_LINE
        if (loggingType != LoggingType.MULTI_LINE) {
            rawSql = rawSql.replaceAll("\r", "");
            rawSql = rawSql.replaceAll("\n", "");
        }
        final String fromClause = " from ";
        String sql = rawSql;
        if (loggingType == LoggingType.MULTI_LINE) {
            final String whereClause = " where ";
            final String andClause = " and ";
            final String subSelectClauseS = "\\(select";
            final String subSelectClauseR = " (select";
            final String indent = "          ";
            sql = sql.replaceAll(fromClause, "\n" + margin + indent + fromClause);
            sql = sql.replaceAll(whereClause, "\n" + margin + indent + whereClause);
            sql = sql.replaceAll(andClause, "\n" + margin + indent + andClause);
            sql = sql.replaceAll(subSelectClauseS, "\n" + margin + indent + subSelectClauseR);
        }
        if (loggingType == LoggingType.SINGLE_LINE_TWO_COLUMNS) {
            if (sql.startsWith("select")) {
                String from = sql.substring(sql.indexOf(fromClause) + fromClause.length());
                sql = from + "\t" + sql;
            }
        }
        getSqlOnlyLogger().info(margin + "SQL:" + sql);
        return sql;
    }

    @Override
    public String sqlOccured(StatementSpy spy, String methodCall, String[] sqls) {
        String s = "";
        for (int i = 0; i < sqls.length; i++) {
            s += sqlOccured(spy, methodCall, sqls[i]) + String.format("%n");
        }
        return s;
    }

    public LoggingType getLoggingType() {
        return loggingType;
    }

    public void setLoggingType(LoggingType loggingType) {
        this.loggingType = loggingType;
    }

    @Override
    public void resultSetCollected(ResultSetCollector resultSetCollector) {
        new ResultSetCollectorPrinter(resultSetTableLogger, margin).printResultSet(resultSetCollector);
    }

}
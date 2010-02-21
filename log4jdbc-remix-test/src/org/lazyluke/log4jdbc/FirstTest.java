package org.lazyluke.log4jdbc;

import java.io.StringWriter;

import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggerRepository;

public class FirstTest extends AbstractTest {

	//static Logger logger = Logger.getLogger("FirstTest.class");
	static StringWriter logstring = new StringWriter();

	public void testname() throws Exception {

		Layout layout = new PatternLayout("%m%n");
		LoggerRepository repo = Logger.getRootLogger().getLoggerRepository();
		setAppender(repo.getLogger("jdbc.sqlonly"), logstring, layout, Level.INFO);
		setAppender(repo.getLogger("jdbc.resultsettable"), logstring, layout, Level.INFO);
		setAppender(repo.getLogger("jdbc.audit"), logstring, layout, Level.ERROR);
		setAppender(repo.getLogger("jdbc.resultset"), logstring, layout, Level.ERROR);
		setAppender(repo.getLogger("jdbc.connection"), logstring, layout, Level.ERROR);
		setAppender(repo.getLogger("log4jdbc.debug"), logstring, layout, Level.ERROR);
		
		jdbcTemplate.queryForList("select * from EMP");

		System.out.println(logstring);
	}

	private static void setAppender(Logger pkgLogger, StringWriter logstring, Layout layout, Level level) {
		// pkgLogger.removeAllAppenders();
		pkgLogger.setLevel(level);
		pkgLogger.setAdditivity(false);
		pkgLogger.addAppender(new WriterAppender(layout, logstring));
	}
}

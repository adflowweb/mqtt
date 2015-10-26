/*
 * 
 */
package kr.co.adflow.util;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

// TODO: Auto-generated Javadoc
/**
 * The Class LogFormatter.
 */
public final class LogFormatter extends Formatter {

	/* (non-Javadoc)
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(LogRecord record) {
		System.out.println("LogFormatter::ThreadID::"
				+ Thread.currentThread().getId() + "::Thread::"
				+ Thread.currentThread());
		StringBuilder sb = new StringBuilder();

		// Build output the way you want
		sb.append(new Date(record.getMillis())).append(" \t")
				.append(record.getThreadID()).append(" \t")
				.append(record.getSourceMethodName()).append(" \t")
				.append(record.getSourceClassName()).append(" \t")
				.append(record.getLevel().getLocalizedName()).append(": ")
				.append(formatMessage(record))
				.append(System.getProperty("line.separator"));
		return sb.toString();
	}
}
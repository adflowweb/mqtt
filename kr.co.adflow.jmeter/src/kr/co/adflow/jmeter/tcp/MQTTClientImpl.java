package kr.co.adflow.jmeter.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.jmeter.protocol.tcp.sampler.ReadException;
import org.apache.jmeter.protocol.tcp.sampler.TCPClientImpl;

public class MQTTClientImpl extends TCPClientImpl {

	private static int breakCharacter = '0';

	@Override
	public String read(InputStream is) throws ReadException {
		// System.out.println("called read");

		// ByteArrayOutputStream w = new ByteArrayOutputStream();
		// try {
		// byte[] buffer = new byte[4096];
		// int x = 0;
		// while ((x = is.read(buffer)) > -1) {
		// w.write(buffer, 0, x);
		// if (true) {
		// break;
		// }
		// }
		// return w.toString("euc-kr");
		// } catch (IOException e) {
		// throw new ReadException("", e, w.toString());
		// }

		StringBuffer sb = new StringBuffer();
		try {
			int c;
			while ((c = is.read()) != -1) {
				sb.append((char) c);
				if (c == breakCharacter) {
					break;
				}
			}
			return sb.toString();
		} catch (Exception e) {
			throw new ReadException("", e, sb.toString());
		}
	}

	@Override
	public void write(OutputStream os, InputStream is) throws IOException {
		super.write(os, is);
	}

	@Override
	public void write(OutputStream os, String s) throws IOException {
		super.write(os, s);
	}

}

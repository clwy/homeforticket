package com.homeforticket.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	public static String stream2Str(InputStream is) {
		try {
			ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
			byte[] str_b = new byte[1024];
			int i = -1;
			while ((i = is.read(str_b)) > 0) {
				outputstream.write(str_b, 0, i);
			}
			return outputstream.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		return swapStream.toByteArray();
	}
}

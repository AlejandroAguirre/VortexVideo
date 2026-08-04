package com.streaming.vortex.common;

import java.io.IOException;
import java.io.InputStream;

public class LimitedInputStream extends InputStream {

	private final InputStream delegate;
	private long remaining;

	public LimitedInputStream(InputStream delegate, long limit) {
		this.delegate = delegate;
		this.remaining = limit;
	}

	@Override
	public int read() throws IOException {
		if (remaining <= 0)
			return -1;
		int data = delegate.read();
		if (data != -1)
			remaining--;
		return data;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (remaining <= 0)
			return -1;
		len = (int) Math.min(len, remaining);
		int read = delegate.read(b, off, len);
		if (read != -1)
			remaining -= read;
		return read;
	}

	@Override
	public void close() throws IOException {
		delegate.close();
	}
}
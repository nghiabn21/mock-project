package com.example.mockproject.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServletInputStreamWrapper extends ServletInputStream {
    InputStream is;

    ServletInputStreamWrapper(InputStream is) {
        this.is = is;
    }

    @Override
    public int read() throws IOException {
        return is.read();
    }

    @Override
    public int readLine(byte[] b, int off, int len) throws IOException {
        return is.read(b, off, len);
    }

    @Override
    public boolean isFinished() {
        try {
            return is.available() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        return is.read(bytes);
    }

    @Override
    public int read(byte[] bytes, int i, int i1) throws IOException {
        return is.read(bytes, i, i1);
    }

    @Override
    public long skip(long l) throws IOException {
        return is.skip(l);
    }
}



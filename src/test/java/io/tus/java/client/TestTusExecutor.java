package io.tus.java.client;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

public class TestTusExecutor {
    @Test
    public void testSetDelays() {
        CountingExecutor exec = new CountingExecutor();

        assertArrayEquals(exec.getDelays(), new int[]{500, 1000, 2000, 3000});
        exec.setDelays(new int[]{1, 2, 3});
        assertArrayEquals(exec.getDelays(), new int[]{1, 2, 3});
        assertEquals(exec.getCalls(), 0);
    }

    @Test
    public void testInterrupting() throws Exception {
        TusExecutor exec = new TusExecutor() {
            @Override
            protected void makeAttempt() throws ProtocolException, IOException {
                throw new IOException();
            }
        };

        exec.setDelays(new int[]{100000});

        final Thread executorThread = Thread.currentThread();
        Thread waiterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    executorThread.interrupt();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        waiterThread.start();

        assertFalse(exec.makeAttempts());
    }


    @Test
    public void testMakeAttempts() throws Exception {
        CountingExecutor exec = new CountingExecutor();

        exec.setDelays(new int[]{1, 2, 3});
        assertTrue(exec.makeAttempts());
        assertEquals(exec.getCalls(), 1);
    }


    @Test(expected = IOException.class)
    public void testMakeAllAttemptsThrowIOException() throws Exception {
        CountingExecutor exec = new CountingExecutor() {
            @Override
            protected void makeAttempt() throws ProtocolException, IOException {
                super.makeAttempt();
                throw new IOException();
            }
        };

        exec.setDelays(new int[]{1, 2, 3});
        try {
            exec.makeAttempts();
        } finally {
            assertEquals(exec.getCalls(), 4);
        }
    }

    @Test(expected = ProtocolException.class)
    public void testMakeAllAttemptsThrowProtocolException() throws Exception {
        CountingExecutor exec = new CountingExecutor() {
            @Override
            protected void makeAttempt() throws ProtocolException, IOException {
                super.makeAttempt();
                throw new ProtocolException("something happened", new MockHttpURLConnection(500));
            }
        };

        exec.setDelays(new int[]{1, 2, 3});
        try {
            exec.makeAttempts();
        } finally {
            assertEquals(exec.getCalls(), 4);
        }
    }

    @Test(expected = ProtocolException.class)
    public void testMakeOneAttempt() throws Exception {
        CountingExecutor exec = new CountingExecutor() {
            @Override
            protected void makeAttempt() throws ProtocolException, IOException {
                super.makeAttempt();
                throw new ProtocolException("something happened", new MockHttpURLConnection(404));
            }
        };

        exec.setDelays(new int[]{1, 2, 3});
        try {
            exec.makeAttempts();
        } finally {
            assertEquals(exec.getCalls(), 1);
        }
    }

    /**
     * A mocked HttpURLConnection which always returns the specified response code.
     */
    private class MockHttpURLConnection extends HttpURLConnection {
        private int statusCode;

        public MockHttpURLConnection(int statusCode) throws MalformedURLException {
            super(new URL("http://localhost/"));
            this.statusCode = statusCode;
        }

        @Override
        public int getResponseCode() {
            return statusCode;
        }

        @Override
        public boolean usingProxy() {
            return false;
        }

        @Override
        public void disconnect() {}

        @Override
        public void connect() {}
    }

    /**
     * A TusExecutor implementation which counts the calls to makeAttempt().
     */
    private class CountingExecutor extends TusExecutor {
        private int calls;

        @Override
        protected void makeAttempt() throws ProtocolException, IOException {
            calls++;
        }

        public int getCalls() {
            return calls;
        }
    }
}

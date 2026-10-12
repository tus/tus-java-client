package io.tus.java.client;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for a {@link TusExecutor}.
 */
public class TestTusExecutor {

    /**
     * Tests if the delays for connection attempts are set in the right manner.
     */
    @Test
    public void testSetDelays() {
        CountingExecutor exec = new CountingExecutor();

        assertArrayEquals(exec.getDelays(), new int[]{500, 1000, 2000, 3000});
        exec.setDelays(new int[]{1, 2, 3});
        assertArrayEquals(exec.getDelays(), new int[]{1, 2, 3});
        assertEquals(exec.getCalls(), 0);
    }

    /**
     * Tests if a running execution is interuptable.
     * @throws Exception
     */
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        waiterThread.start();

        assertFalse(exec.makeAttempts());
    }


    /**
     * Tests if {@link TusExecutor#makeAttempts()} actually makes attempts.
     * @throws Exception
     */
    @Test
    public void testMakeAttempts() throws Exception {
        CountingExecutor exec = new CountingExecutor();

        exec.setDelays(new int[]{1, 2, 3});
        assertTrue(exec.makeAttempts());
        assertEquals(exec.getCalls(), 1);
    }

    /**
     * Tests if retry decisions and schedules can be observed, and progress can reset retry state.
     * @throws Exception
     */
    @Test
    public void testRetryHooksAndReset() throws Exception {
        final List<String> events = new ArrayList<String>();
        CountingExecutor exec = new CountingExecutor() {
            @Override
            protected void makeAttempt() throws ProtocolException, IOException {
                super.makeAttempt();
                if (getCalls() == 1) {
                    throw new IOException();
                }
                if (getCalls() == 2) {
                    resetRetryAttempts();
                    throw new IOException();
                }
            }

            @Override
            protected boolean shouldRetry(IOException exception, int retryAttempt) {
                events.add("should-retry:" + retryAttempt);
                return true;
            }

            @Override
            protected void onRetryScheduled(int retryAttempt, int delayMillis) {
                events.add("retry-schedule:" + retryAttempt + ":" + delayMillis);
            }
        };

        exec.setDelays(new int[]{1, 2, 3});
        assertTrue(exec.makeAttempts());
        assertEquals(exec.getCalls(), 3);
        assertEquals(events.size(), 4);
        assertEquals(events.get(0), "should-retry:0");
        assertEquals(events.get(1), "retry-schedule:0:1");
        assertEquals(events.get(2), "should-retry:0");
        assertEquals(events.get(3), "retry-schedule:0:1");
    }

    /**
     * Tests if retry hooks can stop retrying I/O failures.
     * @throws Exception
     */
    @Test(expected = IOException.class)
    public void testRetryHookCanStopIoRetry() throws Exception {
        CountingExecutor exec = new CountingExecutor() {
            @Override
            protected void makeAttempt() throws ProtocolException, IOException {
                super.makeAttempt();
                throw new IOException();
            }

            @Override
            protected boolean shouldRetry(IOException exception, int retryAttempt) {
                return false;
            }
        };

        try {
            exec.makeAttempts();
        } finally {
            assertEquals(exec.getCalls(), 1);
        }
    }


    /**
     * Tests if every attempt can throw an IOException.
     * @throws Exception
     */
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

    /**
     * Tests if every attempt can throw a {@link ProtocolException}.
     * @throws Exception
     */
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

    /**
     * Tests if an Exception can be thrown also at single attempts.
     * @throws Exception
     */
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

        MockHttpURLConnection(int statusCode) throws MalformedURLException {
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
        public void disconnect() { }

        @Override
        public void connect() { }
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

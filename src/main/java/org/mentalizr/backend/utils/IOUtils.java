package org.mentalizr.backend.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class IOUtils {

    private static final int DEFAULT_PUMP_BUFFER_SIZE = 10240;

    /**
     * Pumps the specified inputStream to the specified outputStream facilitating NIO {@link Channels} and a NIO
     * {@link ByteBuffer}. Both the input and output streams will implicitly be closed after pumping,
     * regardless of whether an exception has been thrown or not.
     * @param inputStream The input stream.
     * @param outputStream The output stream.
     * @return The length of the written bytes.
     * @throws IOException When an I/O error occurs.
     */
    public static long pump(InputStream inputStream, OutputStream outputStream) throws IOException {

        try (ReadableByteChannel inputChannel = Channels.newChannel(inputStream);
             WritableByteChannel outputChannel = Channels.newChannel(outputStream)) {

            ByteBuffer buffer = ByteBuffer.allocateDirect(DEFAULT_PUMP_BUFFER_SIZE);
            long size = 0;

            while (inputChannel.read(buffer) != -1) {
                buffer.flip();
                size += outputChannel.write(buffer);
                buffer.clear();
            }

            return size;
        }
    }

}

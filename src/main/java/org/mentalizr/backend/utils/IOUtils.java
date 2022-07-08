package org.mentalizr.backend.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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

    /**
     * Pumps a specified range of bytes from the specified file (source) to the specified OutputStream (destination)
     * facilitating NIO {@link Channels} and a NIO {@link ByteBuffer}. The output stream will only implicitly be closed
     * after streaming when the specified range
     * represents the whole file, regardless of whether an exception has been thrown or not.
     * @param file The file.
     * @param outputStream The output stream.
     * @param start The start position (offset).
     * @param length The (intended) length of written bytes.
     * @return The (actual) length of the written bytes. This may be smaller when the given length is too large.
     * @throws IOException When an I/O error occurs.
     */
    public static long pump(File file, OutputStream outputStream, long start, long length) throws IOException {
        if (start == 0 && length >= file.length()) {
            return pump(new FileInputStream(file), outputStream);
        }

        try (FileChannel fileChannel = (FileChannel) Files.newByteChannel(file.toPath(), StandardOpenOption.READ)) {
            WritableByteChannel outputChannel = Channels.newChannel(outputStream);
            ByteBuffer buffer = ByteBuffer.allocateDirect(DEFAULT_PUMP_BUFFER_SIZE);
            long size = 0;

            while (fileChannel.read(buffer, start + size) != -1) {
                buffer.flip();

                if (size + buffer.limit() > length) {
                    buffer.limit((int) (length - size));
                }

                size += outputChannel.write(buffer);

                if (size >= length) {
                    break;
                }

                buffer.clear();
            }

            return size;
        }
    }

}

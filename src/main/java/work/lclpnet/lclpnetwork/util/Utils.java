/*
 * Copyright (c) 2022 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Common utilities.
 */
public class Utils {

    /**
     * Converts all bytes of an {@link InputStream} to a {@link String}.
     *
     * @param in Any input.
     * @param charset The charset of the input string.
     * @return The string read from the input stream's bytes.
     * @throws IOException If there was an error reading the string.
     */
    public static String toString(InputStream in, Charset charset) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        transfer(in, out);
        return new String(out.toByteArray(), charset);
    }

    /**
     * Transfers all bytes from an {@link InputStream} to an {@link OutputStream}.
     *
     * @param in Any input.
     * @param out Any output.
     * @throws IOException If there was an error transferring all bytes.
     */
    public static void transfer(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;

        while((read = in.read(buffer, 0, buffer.length)) != -1)
            out.write(buffer, 0, read);
    }

}

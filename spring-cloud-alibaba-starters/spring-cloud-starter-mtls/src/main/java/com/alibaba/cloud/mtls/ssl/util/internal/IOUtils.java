/*
 * Copyright 2019 Thunderberry.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloud.mtls.ssl.util.internal;

import com.alibaba.cloud.mtls.ssl.exception.GenericIOException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

import static com.alibaba.cloud.mtls.ssl.util.internal.ValidationUtils.GENERIC_EXCEPTION_MESSAGE;
import static com.alibaba.cloud.mtls.ssl.util.internal.ValidationUtils.requireNotNull;

/**
 * <strong>NOTE:</strong>
 * Please don't use this class directly as it is part of the internal API. Class name and methods can be changed any time.
 *
 * @author Hakan Altindag
 */
public final class IOUtils {

    private IOUtils() {}

    public static String getContent(InputStream inputStream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(requireNotNull(inputStream, GENERIC_EXCEPTION_MESSAGE.apply("InputStream")), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            return bufferedReader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            throw new GenericIOException(e);
        }
    }

    public static byte[] copyToByteArray(InputStream inputStream) {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new GenericIOException(e);
        }
    }

    public static void closeSilently(AutoCloseable autoCloseable) {
        try {
            autoCloseable.close();
        } catch (Exception ignored) {
            //ignore exception
        }
    }

    public static InputStream getResourceAsStream(String name) {
        return IOUtils.class.getClassLoader().getResourceAsStream(name);
    }

    public static InputStream getFileAsStream(Path path) {
        try {
            return Files.newInputStream(path, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new GenericIOException(e);
        }
    }

    public static void write(Path path, byte[] data) {
        try {
            createDirectoriesIfAbsent(path);
            Files.write(path, data, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new GenericIOException(e);
        }
    }

    public static void write(Path path, Consumer<OutputStream> consumer) {
        try {
            createDirectoriesIfAbsent(path);
            try (OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.CREATE)) {
                consumer.accept(outputStream);
            }
        } catch (Exception e) {
            throw new GenericIOException(e);
        }
    }

    private static void createDirectoriesIfAbsent(Path absoluteFilePath) throws IOException {
        Path parentDirectories = absoluteFilePath.getParent();
        if (Files.notExists(parentDirectories)) {
            Files.createDirectories(parentDirectories);
        }
    }

    @FunctionalInterface
    public interface Consumer<T> {
        void accept(T t) throws Exception;
    }

}

package cs6250.benchmarkingsuite.imageprocessing.Util;


/**
 * Created by farzon on 11/22/17.
 */

/*
 * Copyright (c) 2015. 2Leaves Workshop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

@SuppressWarnings("WeakerAccess")
public class FileUtils {

    private static final String TAG = "FU";

    public static final String DEFAULT_ENCODING="utf-8";

    public static String readFileToString(String path) throws IOException {
        return readFileToString(path, DEFAULT_ENCODING);
    }

    public static String readFileToString(String path, String encoding) throws IOException {

        return readStreamToString(new FileInputStream(path), encoding);

    }

    public static String readStreamToString(InputStream is) throws IOException {
        return readStreamToString(is, DEFAULT_ENCODING);
    }

    public static String readStreamToString(InputStream is, String encoding) throws IOException {

        Reader r = new InputStreamReader(is, encoding);

        StringBuilder builder = new StringBuilder();

        char[] buffer = new char[4096];
        int count;
        while ((count=r.read(buffer))!=-1) {
            builder.append(buffer, 0, count);
        }

        r.close();

        return builder.toString();

    }

    public static byte[] readFileToByteArray(String path) throws IOException {
        return readStreamToByteArray(new FileInputStream(path));
    }

    public static byte[] readStreamToByteArray(InputStream is) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int count;
        while ((count=is.read(buffer))!=-1) {
            baos.write(buffer, 0, count);
        }

        return baos.toByteArray();

    }

    public static void writeStringToFile(String path, String encoding, String value) throws IOException {

        writeStringToStream(new FileOutputStream(path), encoding, value);

    }

    public static void writeStreamToStream(OutputStream os, InputStream is) throws IOException {
        writeStreamToStream(os, is, true);
    }

    public static void writeStreamToStream(OutputStream os, InputStream is,
                                           @SuppressWarnings("SameParameterValue") boolean closeInputStream) throws IOException {

        int bufferSize = 16384;
        byte[] buffer = new byte[bufferSize];
        int count;
        while ((count=is.read(buffer, 0, bufferSize))!=-1) {
            os.write(buffer, 0, count);
        }

        if (closeInputStream)
            is.close();
        os.close();

    }

    public static void writeStreamToStream(OutputStream os, InputStream is,
                                           @SuppressWarnings("SameParameterValue") boolean closeInputStream,
                                           @SuppressWarnings("SameParameterValue") boolean closeOutputStream) throws IOException {

        int bufferSize = 16384;
        byte[] buffer = new byte[bufferSize];
        int count;
        while ((count=is.read(buffer, 0, bufferSize))!=-1) {
            os.write(buffer, 0, count);
        }

        if (closeInputStream)
            is.close();
        os.close();

    }

    public static void writeStringToStream(OutputStream os, String encoding, String value) throws IOException {

        Writer w = new OutputStreamWriter(os, encoding);
        w.write(value);
        w.close();

    }

    public static void writeBytesToFile(String path, byte[] data) throws IOException {

        FileOutputStream os = new FileOutputStream(path);
        os.write(data);
        os.close();

    }

    public static String getDir(String path) {
        int index = path.lastIndexOf(File.separator);
        if (index==-1) return "";
        else
            return path.substring(0,index);
    }

    public static boolean ensureDirectory(String path) {

        File f = new File(path);
        return ensureDirectory(f);

    }

    public static boolean  ensureDirectory(File path) {
        if (path.exists())
            return false;
        else
            return path.mkdirs();
    }

    private static boolean deleteFolder(File folder, int level, boolean deleteSelf) {

        boolean deleted = false;

        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    if (deleteFolder(f, level+1, true))
                        deleted = true;
                } else {
                    f.delete();
                    deleted = true;
                }
            }
        }
        if (deleteSelf) {
            if (folder.delete()) deleted = true;
        }

        return deleted;

    }

    public static boolean deleteFolder(File folder) {
        return deleteFolder(folder, 0, true);
    }

    public static boolean  deleteFolder(File folder, boolean deleteSelf) {
        return deleteFolder(folder, 0, deleteSelf);
    }


}

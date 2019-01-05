/*
 *  Copyright 2006-2018 WebPKI.org (http://webpki.org).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

import java.io.File;

import org.webpki.util.ArrayUtil;
import org.webpki.util.DebugFormatter;

import org.webpki.jcs.JsonCanonicalizer;

public class CanonicalizerTest {

    static String inputDirectory;
    static String outputDirectory;

    static void performOneTest(String fileName) throws Exception {
        byte[] rawInput = ArrayUtil.readFile(inputDirectory + File.separator + fileName);
        byte[] expected = ArrayUtil.readFile(outputDirectory + File.separator + fileName);
        byte[] actual = new JsonCanonicalizer(rawInput).getEncodedUTF8();
        StringBuilder utf8InHex = new StringBuilder("\nFile: ");
        utf8InHex.append(fileName).append("\n");
        int byteCount = 0;
        boolean next = false;
        for (byte b : actual) {
            if (byteCount++ % 32 == 0) {
                utf8InHex.append('\n');
                next = false;
            }
            if (next) {
                utf8InHex.append(" ");
            }
            next = true;
            utf8InHex.append(DebugFormatter.getHexString(new byte[]{b}));
        }
        System.out.println(utf8InHex.append("\n").toString());
        if (!ArrayUtil.compare(expected, actual)) {
            throw new RuntimeException("Failed for file: " + fileName);
        }
    }

    public static void main(String[] args) throws Exception {
        inputDirectory = args[0] + File.separator + "input";
        outputDirectory = args[0] + File.separator + "output";
        File[] files = new File(inputDirectory).listFiles();
        for (File f : files) {
            performOneTest(f.getName());
        }
    }
}

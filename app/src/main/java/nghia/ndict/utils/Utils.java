package nghia.ndict.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.text.Collator;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 4/15/13
 * Time: 5:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    public static void log(String message) {
        Log.d(Constants.LOG_TAG, message);
    }

    public static void toast(Context context, String mes) {
        if (!isNullOrEmpty(mes)) {
            Toast.makeText(context, mes, Toast.LENGTH_LONG).show();
        }
    }

    public static int BytesToInt(byte[] bytes) {
        return ((bytes[0] & 0xff) << 24) | ((bytes[1] & 0xff) << 16) |
                ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff);
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static int compareTwoStrings(String str1, String str2) {
        Collator collator = Collator.getInstance(new Locale("vi"));
        if (str1.compareToIgnoreCase(str2) == 0) {
            return 0;
        }

        int l1 = str1.length();
        int l2 = str2.length();
        int minLen = l1 > l2 ? l2 : l1;
        int index = 0;

        while (index < minLen) {
            int result = collator.compare(Character.toString(str1.charAt(index)), Character.toString(str2.charAt(index)));
            if (result != 0) {
                return result;
            }
            index++;
        }
        return l1 > l2 ? 1 : -1;
    }

    public byte[] intToBytes(final int i) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        return bb.array();
    }
}

package cn.edu.hebtu.software.sharemateclient.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameUtils {

    public static boolean isName(String inputText){
        /**
         * 限10个字符，支持中英文、数字、减号或下划线
         */
        Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5_a-zA-Z0-9-]{1,10}$");
        Matcher matcher = pattern.matcher(inputText);
        return matcher.matches();
    }
}

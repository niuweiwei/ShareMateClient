package cn.edu.hebtu.software.sharemateclient.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtils {

    public static boolean isPassword(String inputText){
        /**8-16位数字和字母密码
         * 匹配的正则表达式
         *  ^ 匹配一行的开头位置 (?![0-9]+$)
         *  预测该位置后面不全是数字 (?![a-zA-Z]+$)
         *  预测该位置后面不全是字母 [0-9A-Za-z]
         *  {8,16} 由8-16位数字或这字母组成 $ 匹配行结尾位置
         */
        Pattern pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$");
        Matcher matcher = pattern.matcher(inputText);
        return matcher.matches();
    }
}

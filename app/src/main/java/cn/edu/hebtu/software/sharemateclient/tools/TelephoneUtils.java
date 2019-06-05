package cn.edu.hebtu.software.sharemateclient.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelephoneUtils {
    /*
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、184、187、188、147
     * 联通号码段:130、131、132、185、186、145、171/176/175
     * 电信号码段:133、153、180、181、189、173、177
     */
    /**
     * (13[0-9])代表13号段 130-139
     * (14[5|7])代表14号段 145、147
     * (15([0-3]|[5-9]))代表15号段 150-153 155-159
     * (17([1-3][5-8]))代表17号段 171-173 175-179 虚拟运营商170屏蔽
     * (18[0-9]))代表18号段 180-189
     * d{8}代表后面可以是0-9的数字，有8位
     */
    public static boolean isPhone(String inputText){
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17([1-3]|[5-9]))|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(inputText);
        return matcher.matches();
    }
}

package io.github.marktony.espresso.constant;


/**
 * Created by lizhaotailang on 2017/2/9.
 */

public class Api {


    // 基础API
    public static final String API_BASE = "http://www.kuaidi100.com/";

    // 获取特定单号的物流信息
    public static final String PACKAGE_STATE = API_BASE + "query";

    // 自动获取单号对应的快递公司
    public static final String COMPANY_QUERY = "autonumber/autoComNum";

    // 获取所有快递公司信息
    public static final String COMPANIES = "js/share/company_name.js";

    // 测试单号，京东
    public static final String TEST_NUMBER = "47258833029";

}

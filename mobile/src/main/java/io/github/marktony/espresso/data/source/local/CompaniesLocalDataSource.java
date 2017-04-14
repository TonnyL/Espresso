/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.marktony.espresso.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.Callable;

import io.github.marktony.espresso.data.Company;
import io.github.marktony.espresso.data.source.CompaniesDataSource;
import io.github.marktony.espresso.realm.RealmHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.realm.Case;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by lizhaotailang on 2017/3/22.
 */

public class CompaniesLocalDataSource implements CompaniesDataSource {

    @Nullable
    public static CompaniesLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private CompaniesLocalDataSource() {

    }

    public static CompaniesLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CompaniesLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Company>> getCompanies() {
        return Observable.fromCallable(new Callable<List<Company>>() {
            @Override
            public List<Company> call() throws Exception {
                Realm rlm = RealmHelper.newRealmInstance();
                List<Company> companyList = rlm.copyFromRealm(rlm.where(Company.class)
                        .findAllSorted("alphabet", Sort.ASCENDING));
                rlm.close();
                return companyList;
            }
        });
    }

    @Override
    public Observable<Company> getCompany(@NonNull final String companyId) {
        return Observable.fromCallable(new Callable<Company>() {
            @Override
            public Company call() throws Exception {
                Realm rlm = RealmHelper.newRealmInstance();
                Company company = rlm.copyFromRealm(rlm.where(Company.class)
                        .equalTo("id", companyId)
                        .findFirst());
                rlm.close();
                return company;
            }
        });
    }

    @Override
    public void initData() {
        Realm rlm = RealmHelper.newRealmInstance();

        rlm.beginTransaction();
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'申通快递','id':'shentong','tel':'95543','website':'http://www.sto.cn','alphabet':'shentongkuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'EMS','id':'ems','tel':'11183','website':'http://www.ems.com.cn/','alphabet':'ems','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'顺丰速运','id':'shunfeng','tel':'95338','website':'http://www.sf-express.com','alphabet':'shunfengsuyun','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'韵达快递','id':'yunda','tel':'95546','website':'http://www.yundaex.com','alphabet':'yundakuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'圆通速递','id':'yuantong','tel':'95554','website':'http://www.ytoexpress.com/','alphabet':'yuantongsudi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中通快递','id':'zhongtong','tel':'95311','website':'http://www.zto.cn','alphabet':'zhongtongkuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'百世快递','id':'huitongkuaidi','tel':'400-956-5656','website':'http://www.800bestex.com/','alphabet':'baishikuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'天天快递','id':'tiantian','tel':'400-188-8888','website':'http://www.ttkdex.com','alphabet':'tiantiankuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'宅急送','id':'zhaijisong','tel':'400-6789-000','website':'http://www.zjs.com.cn','alphabet':'zhaijisong','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'鑫飞鸿','id':'xinhongyukuaidi','tel':'021-69781999','website':'http://www.xfhex.cn/','alphabet':'xinfeihong','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'CCES/国通快递','id':'cces','tel':'400-111-1123','website':'http://www.gto365.com','alphabet':'cces/guotongkuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'全一快递','id':'quanyikuaidi','tel':'400-663-1111','website':'http://www.unitop-apex.com/','alphabet':'quanyikuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'彪记快递','id':'biaojikuaidi','tel':'+886 (02) 2562-3533','website':'http://www.pewkee.com','alphabet':'biaojikuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'星晨急便','id':'xingchengjibian','tel':'','website':'','alphabet':'xingchenjibian','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'亚风速递','id':'yafengsudi','tel':'4001-000-002','website':'http://www.airfex.net/','alphabet':'yafengsudi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'源伟丰','id':'yuanweifeng','tel':'400-601-2228','website':'http://www.ywfex.com','alphabet':'yuanweifeng','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'全日通','id':'quanritongkuaidi','tel':'020-86298999','website':'http://www.at-express.com/','alphabet':'quanritong','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'安信达','id':'anxindakuaixi','tel':'400-716-1919','website':'http://www.axdkd.com','alphabet':'anxinda','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'民航快递','id':'minghangkuaidi','tel':'400-817-4008','website':'http://www.cae.com.cn','alphabet':'minhangkuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'凤凰快递','id':'fenghuangkuaidi','tel':'010-85826200','website':'http://www.phoenixexp.com','alphabet':'fenghuangkuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'京广速递','id':'jinguangsudikuaijian','tel':'0769-88629888','website':'http://www.szkke.com/','alphabet':'jingguansudi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'配思货运','id':'peisihuoyunkuaidi','tel':'010-65489928,65489571,65489469,65489456','website':'http://www.peisi.cn','alphabet':'peisihuoyun','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中铁物流','id':'ztky','tel':'400-000-5566','website':'http://www.ztky.com','alphabet':'zhongtiewuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'UPS','id':'ups','tel':'400-820-8388','website':'http://www.ups.com/cn','alphabet':'ups','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'FedEx-国际件','id':'fedex','tel':'400-886-1888','website':'http://fedex.com/cn','alphabet':'fedex-guojijian','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'DHL-中国件','id':'dhl','tel':'800-810-8000','website':'http://www.cn.dhl.com','alphabet':'dhl-zhongguojian','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'AAE-中国件','id':'aae','tel':'400-610-0400','website':'http://cn.aaeweb.com','alphabet':'aae-zhongguojian','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'大田物流','id':'datianwuliu','tel':'400-626-1166','website':'http://www.dtw.com.cn','alphabet':'datianwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'德邦物流','id':'debangwuliu','tel':'95353','website':'http://www.deppon.com','alphabet':'debangwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'新邦物流','id':'xinbangwuliu','tel':'4008-000-222','website':'http://www.xbwl.cn','alphabet':'xinbangwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'龙邦速递','id':'longbanwuliu','tel':'021-59218889','website':'http://www.lbex.net','alphabet':'longbangsudi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'一邦速递','id':'yibangwuliu','tel':'400-8000666','website':'http://www.ebon-express.com','alphabet':'yibangsudi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'速尔快递','id':'suer','tel':'400-158-9888','website':'http://www.sure56.com','alphabet':'suerkuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'联昊通','id':'lianhaowuliu','tel':'400-8888887','website':'http://www.lhtex.com.cn','alphabet':'lianhaotong','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'广东邮政','id':'guangdongyouzhengwuliu','tel':'020-38181677','website':'http://www.ep183.cn/','alphabet':'guangdongyouzheng','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中邮物流','id':'zhongyouwuliu','tel':'11183','website':'http://www.cnpl.com.cn','alphabet':'zhongyouwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'天地华宇','id':'tiandihuayu','tel':'400-808-6666','website':'http://www.hoau.net','alphabet':'tiandihuayu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'盛辉物流','id':'shenghuiwuliu','tel':'4008-222-222','website':'http://www.shenghui56.com','alphabet':'shenghuiwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'长宇物流','id':'changyuwuliu','tel':'0755-32809388','website':'http://61.145.121.47/custSearch.jsp','alphabet':'changyuwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'飞康达','id':'feikangda','tel':'010-84223376,84223378','website':'http://www.fkd.com.cn','alphabet':'feikangda','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'元智捷诚','id':'yuanzhijiecheng','tel':'400-081-2345','website':'http://www.yjkd.com','alphabet':'yuanzhijiecheng','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'邮政包裹/平邮','id':'youzhengguonei','tel':'11185','website':'http://yjcx.chinapost.com.cn','alphabet':'youzhengbaoguo/pingyou','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'邮政国际包裹','id':'youzhengguoji','tel':'11185','website':'http://intmail.183.com.cn/','alphabet':'youzhengguoji','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'万家物流','id':'wanjiawuliu','tel':'4001-156-561','website':'http://www.manco-logistics.com/','alphabet':'wanjiawuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'远成物流','id':'yuanchengwuliu','tel':'400-820-1646','website':'http://www.ycgwl.com/','alphabet':'yuanchengwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'信丰物流','id':'xinfengwuliu','tel':'400-830-6333','website':'http://www.xf-express.com.cn','alphabet':'xinfengwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'文捷航空','id':'wenjiesudi','tel':'020-88561502,85871501,31683301','website':'http://www.wjexpress.com','alphabet':'wenjiehangkong','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'全晨快递','id':'quanchenkuaidi','tel':'0769-82026703','website':'http://www.qckd.net/','alphabet':'quanchenkuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'佳怡物流','id':'jiayiwuliu','tel':'400-631-9999','website':'http://www.jiayi56.com/','alphabet':'jiayiwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'优速物流','id':'youshuwuliu','tel':'400-1111-119','website':'http://www.uc56.com','alphabet':'yousuwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'快捷速递','id':'kuaijiesudi','tel':'4008-333-666','website':'http://www.kjkd.com/','alphabet':'kuaijiesudi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'D速快递','id':'dsukuaidi','tel':'0531-88636363','website':'http://www.d-exp.cn','alphabet':'dsusudi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'全际通','id':'quanjitong','tel':'400-0179-888','website':'http://www.quanjt.com','alphabet':'quanjitong','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'港中能达速递','id':'ganzhongnengda','tel':'400-6886-765','website':'https://www.nd56.com/','alphabet':'gangzhongnengdasudi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'安捷快递','id':'anjiekuaidi','tel':'400-056-5656','website':'http://www.anjelex.com','alphabet':'anjiekuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'越丰物流','id':'yuefengwuliu','tel':'852-23909969','website':'http://www.yfexpress.com.hk','alphabet':'yuefengwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'DPEX','id':'dpex','tel':'400-920-7011,800-820-7011','website':'https://www.dpex.com/','alphabet':'dpex','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'急先达','id':'jixianda','tel':'021-59766363','website':'http://www.joust.net.cn/','alphabet':'jixianda','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'百福东方','id':'baifudongfang','tel':'400-706-0609','website':'http://www.ees.com.cn','alphabet':'baifudongfang','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'BHT','id':'bht','tel':'010-58633508','website':'http://www.bht-exp.com/','alphabet':'bht','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'伍圆速递','id':'wuyuansudi','tel':'0592—5050535','website':'http://www.f5xm.com','alphabet':'wuyuansudi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'蓝镖快递','id':'lanbiaokuaidi','tel':'0769-82898999','website':'http://www.bluedart.cn','alphabet':'lanbiaokuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'COE','id':'coe','tel':'0755-83575000','website':'http://www.coe.com.hk','alphabet':'coe','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'南京100','id':'nanjing','tel':'025-84510043','website':'http://www.100cskd.com','alphabet':'nanjing100','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'恒路物流','id':'hengluwuliu','tel':'400-182-6666','website':'http://www.e-henglu.com','alphabet':'hengluwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'金大物流','id':'jindawuliu','tel':'0755-82262209','website':'http://www.szkingdom.com.cn','alphabet':'jindawuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'华夏龙物流','id':'huaxialongwuliu','tel':'400-716-6133','website':'http://www.chinadragon56.com','alphabet':'huaxialongwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'运通中港','id':'yuntongkuaidi','tel':'0769-81156999','website':'http://www.ytkd168.com','alphabet':'yuntongzhonggang','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'佳吉快运','id':'jiajiwuliu','tel':'400-820-5566','website':'http://www.jiaji.com','alphabet':'jiajikuaiyun','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'盛丰物流','id':'shengfengwuliu','tel':'0591-83621111','website':'http://www.sfwl.com.cn','alphabet':'shengfengwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'源安达','id':'yuananda','tel':'0769-85157789','website':'http://www.yadex.com.cn','alphabet':'yuananda','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'加运美','id':'jiayunmeiwuliu','tel':'0769-85515555','website':'http://www.jym56.cn/','alphabet':'jiayunmei','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'万象物流','id':'wanxiangwuliu','tel':'400-820-8088','website':'http://www.ewinshine.com','alphabet':'wanxiangwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'宏品物流','id':'hongpinwuliu','tel':'400-612-1456','website':'http://www.hpexpress.com.cn','alphabet':'hongpinwuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'GLS','id':'gls','tel':'877-914-5465','website':'http://www.gls-group.net','alphabet':'gls','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'上大物流','id':'shangda','tel':'400-021-9122','website':'http://www.sundapost.net','alphabet':'shangdawuliu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中铁快运','id':'zhongtiewuliu','tel':'95572','website':'http://www.cre.cn','alphabet':'zhongtiekuaiyun','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'原飞航','id':'yuanfeihangwuliu','tel':'0769-87001100','website':'http://www.yfhex.com','alphabet':'yuanfeihang','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'海外环球','id':'haiwaihuanqiu','tel':'010-59790107','website':'http://www.haiwaihuanqiu.com/','alphabet':'haiwaihuanqiu','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'三态速递','id':'santaisudi','tel':'400-881-8106','website':'http://www.sfcservice.com/','alphabet':'santaisudi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'晋越快递','id':'jinyuekuaidi','tel':'400-638-9288','website':'http://www.byondex.com','alphabet':'jinyuekuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'联邦快递','id':'lianbangkuaidi','tel':'400-889-1888','website':'http://cndxp.apac.fedex.com/dxp.html','alphabet':'lianbangkuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'飞快达','id':'feikuaida','tel':'400-716-6666','website':'http://www.fkdex.com','alphabet':'feikuaida','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'全峰快递','id':'quanfengkuaidi','tel':'400-100-0001','website':'http://www.qfkd.com.cn','alphabet':'quanfengkuaidi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'如风达','id':'rufengda','tel':'400-010-6660','website':'http://www.rufengda.com','alphabet':'rufengda','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'乐捷递','id':'lejiedi','tel':'400-618-1400','website':'http://www.ljd365.com','alphabet':'lejiedi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'忠信达','id':'zhongxinda','tel':'400-646-6665','website':'http://www.zhongxind.cn/index.asp','alphabet':'zhongxinda','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'芝麻开门','id':'zhimakaimen','tel':'400-105-6056','website':'http://www.zmkmex.com/','alphabet':'zhimakaimen','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'赛澳递','id':'saiaodi','tel':'4000-345-888','website':'http://www.51cod.com','alphabet':'saiaodi','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'海红网送','id':'haihongwangsong','tel':'400-632-9988','website':'http://www.haihongwangsong.com/index.asp','alphabet':'haihongwangsong','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'共速达','id':'gongsuda','tel':'400-111-0005','website':'http://www.gongsuda.com','alphabet':'gongsuda','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'嘉里大通','id':'jialidatong','tel':'400-610-3188','website':'http://www.kerryeas.com','alphabet':'jialidatong','avatar':'#00BCD4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'OCS','id':'ocs','tel':'400-118-8588','website':'http://www.ocschina.com','alphabet':'ocs','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'USPS','id':'usps','tel':'800-275-8777','website':'https://zh.usps.com','alphabet':'usps','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'美国快递','id':'meiguokuaidi','tel':'888-611-1888','website':'http://www.us-ex.com','alphabet':'meiguokuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'成都立即送','id':'lijisong','tel':'400-028-5666','website':'http://www.cdljs.com','alphabet':'chengdulijisong','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'银捷速递','id':'yinjiesudi','tel':'0755-88999000','website':'http://www.sjfd-express.com','alphabet':'yinjiesudi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'门对门','id':'menduimen','tel':'400-700-7676','website':'http://www.szdod.com','alphabet':'menduimen','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'递四方','id':'disifang','tel':'0755-33933895','website':'http://www.4px.com','alphabet':'disifang','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'郑州建华','id':'zhengzhoujianhua','tel':'0371-65995266','website':'http://www.zzjhtd.com/','alphabet':'zhengzhoujianhua','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'河北建华','id':'hebeijianhua','tel':'0311-86123186','website':'http://116.255.133.172/hebeiwebsite/index.jsp','alphabet':'hebeijianhua','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'微特派','id':'weitepai','tel':'400-6363-000','website':'http://www.vtepai.com/','alphabet':'weitepai','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'DHL-德国件(DHL Deutschland)','id':'dhlde','tel':'+49 (0) 180 5 345300-1*','website':'http://www.dhl.de/en.html','alphabet':'dhl-deguojian(DHL Deutschland)','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'通和天下','id':'tonghetianxia','tel':'400-0056-516','website':'http://www.cod56.com','alphabet':'tonghetianxia','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'EMS-国际件','id':'emsguoji','tel':'11183','website':'http://www.ems.com.cn','alphabet':'ems-guojijian','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'FedEx-美国件','id':'fedexus','tel':'800-463-3339','website':'http://www.fedex.com/us/','alphabet':'fedex-meiguojian','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'风行天下','id':'fengxingtianxia','tel':'4000-404-909','website':'http://www.fxtxsy.com','alphabet':'fengxingtianxia','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'康力物流','id':'kangliwuliu','tel':'400-156-5156','website':'http://www.kangliex.com/','alphabet':'kangliwuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'跨越速运','id':'kuayue','tel':'4008-098-098','website':'http://www.ky-express.com/','alphabet':'kuayuewuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'海盟速递','id':'haimengsudi','tel':'400-080-6369','website':'http://www.hm-express.com','alphabet':'haimengsudi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'圣安物流','id':'shenganwuliu','tel':'4006-618-169','website':'http://www.sa56.net','alphabet':'shenganwuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'一统飞鸿','id':'yitongfeihong','tel':'61501533-608','website':'http://218.97.241.58:8080/yitongfeihongweb/common?action=toindex','alphabet':'yitongfeihong','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中速快递','id':'zhongsukuaidi','tel':'11183','website':'http://www.ems.com.cn/mainservice/ems/zhong_su_guo_ji_kuai_jian.html','alphabet':'zhongsukuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'新蛋奥硕','id':'neweggozzo','tel':'400-820-4400','website':'http://www.ozzo.com.cn','alphabet':'xindanaoshuo','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'OnTrac','id':'ontrac','tel':'800-334-5000','website':'http://www.ontrac.com','alphabet':'ontrac','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'七天连锁','id':'sevendays','tel':'400-882-1202','website':'http://www.92856.cn','alphabet':'qitianliansuo','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'明亮物流','id':'mingliangwuliu','tel':'400-035-6568','website':'http://www.szml56.com/','alphabet':'mingliangwuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'华企快运','id':'huaqikuaiyun','tel':'400-806-8111','website':'http://www.hqkd.cn/','alphabet':'huaqikuaiyun','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'城市100','id':'city100','tel':'400-820-0088','website':'http://www.bjcs100.com/','alphabet':'chengshi100','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'山西红马甲物流','id':'sxhongmajia','tel':'0351-5225858','website':'http://www.hmj.com.cn/','alphabet':'shanxihongmajiawuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'穗佳物流','id':'suijiawuliu','tel':'400-880-9771','website':'http://www.suijiawl.com','alphabet':'suijiawuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'飞豹快递','id':'feibaokuaidi','tel':'400-000-5566','website':'http://www.ztky.com/feibao/KJCX.aspx','alphabet':'feibaokuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'传喜物流','id':'chuanxiwuliu','tel':'400-777-5656','website':'http://www.cxcod.com/','alphabet':'chuanxiwuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'捷特快递','id':'jietekuaidi','tel':'400-820-8585','website':'http://www.jet185.com/','alphabet':'jietekuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'隆浪快递','id':'longlangkuaidi','tel':'021-31171576,021-61552015','website':'','alphabet':'longlangkuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'EMS-英文','id':'emsen','tel':'11183','website':'http://www.ems.com.cn/english.html','alphabet':'ems-yingwen','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中天万运','id':'zhongtianwanyun','tel':'400-0056-001','website':'http://www.ztwy56.cn/','alphabet':'zhongtianwanyun','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'HongKong Post(香港邮政)','id':'hkpost','tel':'852-2921-2222','website':'http://www.hongkongpost.hk','alphabet':'hongkong post(xianggangyouzheng)','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'邦送物流','id':'bangsongwuliu','tel':'021-20965696','website':'http://express.banggo.com','alphabet':'bangsongwuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'国通快递','id':'guotongkuaidi','tel':'400-111-1123','website':'http://www.gto365.com','alphabet':'guotongkuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Australia Post(澳大利亚邮政)','id':'auspost','tel':'0061-3-88479045','website':'http://auspost.com.au','alphabet':'australia post(aodaliyayouzheng)','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Canada Post(加拿大邮政)','id':'canpost','tel':'416-979-8822','website':'http://www.canadapost.ca','alphabet':'canada post(jianadayouzheng)','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'加拿大邮政','id':'canpostfr','tel':'','website':'','alphabet':'jianadayouzheng','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'UPS-全球件','id':'upsen','tel':'1-800-742-5877','website':'http://www.ups.com/','alphabet':'ups-quanqiujian','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'TNT-全球件','id':'tnten','tel':'','website':'http://www.tnt.com','alphabet':'tnt-quanqiujian','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'DHL-全球件','id':'dhlen','tel':'','website':'http://www.dhl.com/en.html','alphabet':'dhl-quanqiujian','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'顺丰-美国件','id':'shunfengen','tel':'1-855-901-1133','website':'http://www.sf-express.com/us/en/','alphabet':'shunfeng-meiguojian','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'汇强快递','id':'huiqiangkuaidi','tel':'','website':'','alphabet':'huiqiangkuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'希优特','id':'xiyoutekuaidi','tel':'400-8400-365','website':'http://www.cod365.com/','alphabet':'xiyoute','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'昊盛物流','id':'haoshengwuliu','tel':'400-186-5566','website':'http://www.hs-express.cn/','alphabet':'haoshengwuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'尚橙物流','id':'shangcheng','tel':'400-890-0101','website':'http://www.suncharms.net/','alphabet':'shangchengwuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'亿领速运','id':'yilingsuyun','tel':'400-1056-400','website':'http://www.yelee.com.cn/','alphabet':'yilingsuyun','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'大洋物流','id':'dayangwuliu','tel':'400-820-0088','website':'http://www.dayang365.cn/','alphabet':'dayangwuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'递达速运','id':'didasuyun','tel':'400-687-8123','website':'http://www.dida.hk/','alphabet':'didasuyun','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'易通达','id':'yitongda','tel':'0898-65339299','website':'http://www.etd365.com/','alphabet':'yitongda','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'邮必佳','id':'youbijia','tel':'400-687-8123','website':'http://www.ubjia.com/','alphabet':'youbijia','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'亿顺航','id':'yishunhang','tel':'400-6018-268','website':'http://www.igoex.com/','alphabet':'yishunhang','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'飞狐快递','id':'feihukuaidi','tel':'010-51389299','website':'http://www.feihukuaidi.com/','alphabet':'feihukuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'潇湘晨报','id':'xiaoxiangchenbao','tel':'','website':'','alphabet':'xiaoxiangchenbao','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'巴伦支','id':'balunzhi','tel':'400-885-6561','website':'http://cnbd.hendari.com/','alphabet':'balunzhi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Aramex','id':'aramex','tel':'400-6318-388','website':'http://www.aramex.com/','alphabet':'aramex','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'闽盛快递','id':'minshengkuaidi','tel':'0592-3725988','website':'http://www.xmms-express.com/','alphabet':'minshengkuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'沈阳佳惠尔','id':'syjiahuier','tel':'024-23904138','website':'http://www.jhekd.com/','alphabet':'shenyangjiahuier','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'民邦速递','id':'minbangsudi','tel':'0769-81515303','website':'http://www.mbex168.com/','alphabet':'minbangsudi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'上海快通','id':'shanghaikuaitong','tel':'','website':'','alphabet':'shanghaikuaitong','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'北青小红帽','id':'xiaohongmao','tel':'010-67756666','website':'','alphabet':'beiqingxiaohongmao','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'GSM','id':'gsm','tel':'021-64656011','website':'http://www.gsmnton.com','alphabet':'gsm','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'安能物流','id':'annengwuliu','tel':'400-104-0088','website':'http://www.ane56.com','alphabet':'annengwuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'KCS','id':'kcs','tel':'800-858-5590','website':'http://www.kcs56.com','alphabet':'kcs','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'City-Link','id':'citylink','tel':'603-5565-8399','website':'http://www.citylinkexpress.com/','alphabet':'ccity-link','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'店通快递','id':'diantongkuaidi','tel':'021-20917385,021-66282857','website':'http://www.shdtkd.com.cn/','alphabet':'diantongkuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'凡宇快递','id':'fanyukuaidi','tel':'4006-580-358','website':'http://www.fanyu56.com.cn/','alphabet':'fanyukuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'平安达腾飞','id':'pingandatengfei','tel':'4009-990-998','website':'http://www.padtf.com/','alphabet':'pingandatengfei','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'广东通路','id':'guangdongtonglu','tel':'','website':'','alphabet':'guangdongtonglu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中睿速递','id':'zhongruisudi','tel':'400-0375-888','website':'http://www.zorel.cn/','alphabet':'zhongruisudi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'快达物流','id':'kuaidawuliu','tel':'','website':'','alphabet':'kuaidawuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'佳吉快递','id':'jiajikuaidi','tel':'400-820-5566','website':'http://www.jiaji.com/','alphabet':'jiajikuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'ADP国际快递','id':'adp','tel':'1588-1330','website':'http://www.adpair.co.kr/','alphabet':'adpguojikuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'颿达国际快递','id':'fardarww','tel':'0755-27332618','website':'http://www.fardar.com/','alphabet':'fandaguojikuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'颿达国际快递-英文','id':'fandaguoji','tel':'0755-27332618','website':'http://www.fardar.com/','alphabet':'fandaguojikuaidi-yingwen','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'林道国际快递','id':'shlindao','tel':'4008-200-112','website':'http://www.ldxpress.com/','alphabet':'lindaoguojikuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中外运速递-中文','id':'sinoex','tel':'010-8041 8611','website':'http://www.sinoex.com.cn','alphabet':'zhongwaiyunsudi-zhongwen','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中外运速递','id':'zhongwaiyun','tel':'010-8041 8611','website':'http://www.sinoex.com.cn','alphabet':'zhongwaiyunsudi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'德创物流(深圳)','id':'dechuangwuliu','tel':'4006-989-833','website':'http://www.dc56.cn/','alphabet':'dechuangwuliu(shenzhen)','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'林道国际快递-英文','id':'ldxpres','tel':'800-820-1470','website':'http://www.ldxpress.com/','alphabet':'lindaoguojikuaidi-yingwen','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'瑞典邮政(Sweden Post)','id':'ruidianyouzheng','tel':'+46 8 23 22 20','website':'http://www.posten.se/en','alphabet':'ruidianyouzheng(sweden post)','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'PostNord(Posten AB)','id':'postenab','tel':'+46 771 33 33 10','website':'http://www.posten.se/en','alphabet':'posternord(posten ab)','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'偌亚奥国际快递','id':'nuoyaao','tel':'4008-871-871','website':'http://www.royaleinternational.com/','alphabet':'nuoyaaoguojikuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'城际速递','id':'chengjisudi','tel':'4000-523-525','website':'http://chengji-express.com','alphabet':'chengjisudi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'祥龙运通物流','id':'xianglongyuntong','tel':'4008-908-908','website':'http://www.ldl.com.cn','alphabet':'xianglongyuntongwuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'品速心达快递','id':'pinsuxinda','tel':'400-800-3693','website':'http://www.psxd88.com/','alphabet':'pinsuxindakuaidi','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'宇鑫物流','id':'yuxinwuliu','tel':'0371-66368798','website':'http://www.yx56.cn/','alphabet':'yuxinwuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'陪行物流','id':'peixingwuliu','tel':'400-993-0555','website':'http://www.peixingexpress.com','alphabet':'peixingwuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'户通物流','id':'hutongwuliu','tel':'400-060-1656','website':'http://www.cnhtwl.com','alphabet':'hutongwuliu','avatar':'#4CAF50'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'西安城联速递','id':'xianchengliansudi','tel':'029-89113508','website':'http://www.city-link.net.cn/','alphabet':'xianchengliansudi','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'煜嘉物流','id':'yujiawuliu','tel':'','website':'http://www.yujia56.net/','alphabet':'yujiawuliu','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'一柒国际物流','id':'yiqiguojiwuliu','tel':'001-(971) 238-9990','website':'http://www.17htb.com/','alphabet':'yiqiguojiwuliu','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Fedex-国际件-中文','id':'fedexcn','tel':'400-889-1888','website':'http://www.fedex.com/cn/index.html','alphabet':'fedex-guojijian-zhongwen','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'联邦快递-英文','id':'lianbangkuaidien','tel':'400-889-1888','website':'http://cndxp.apac.fedex.com/tracking/track.html','alphabet':'llianbangkuaidi-yingwen','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中通(带电话)','id':'zhongtongphone','tel':'','website':'','alphabet':'zhongtong(daidianhua)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'赛澳递for买卖宝','id':'saiaodimmb','tel':'','website':'','alphabet':'saiaodiformaimaibao','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'上海无疆for买卖宝','id':'shanghaiwujiangmmb','tel':'','website':'','alphabet':'shanghaiwujiangformaimaibao','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Singapore Post(新加坡小包)','id':'singpost','tel':'','website':'http://www.singpost.com/','alphabet':'singapore post(xinjiapoxiaobao)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'音素快运','id':'yinsu','tel':'400-007-1118','website':'http://www.yskd168.com/','alphabet':'yinsukuaiyun','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'南方传媒物流','id':'ndwl','tel':'','website':'','alphabet':'nanfangchuanmeiwuliu','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'速呈宅配','id':'sucheng','tel':'','website':'','alphabet':'suchengzhaipei','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'创一快递','id':'chuangyi','tel':'400-887-7779','website':'http://www.cyexpress.cn/','alphabet':'chuangyikuaidi','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'云南滇驿物流','id':'dianyi','tel':'','website':'','alphabet':'yunnandianyiwuliu','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'重庆星程快递','id':'cqxingcheng','tel':'','website':'','alphabet':'chongqingxingchengkuaidi','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'四川星程快递','id':'scxingcheng','tel':'','website':'','alphabet':'sichuanxingchengkuaidi','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'贵州星程快递','id':'gzxingcheng','tel':'','website':'','alphabet':'guizhouxingchengkuaidi','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Gati-英文','id':'gatien','tel':'4000-804-284','website':'http://www.gati.com/','alphabet':'gati-yingwen','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Gati-中文','id':'gaticn','tel':'4000-804-284','website':'http://www.gaticn.com/','alphabet':'gati-zhongwen','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'jcex','id':'jcex','tel':'','website':'','alphabet':'jcex','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'派尔快递','id':'peex','tel':'','website':'','alphabet':'paierkuaidi','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'凯信达','id':'kxda','tel':'','website':'','alphabet':'kaixinda','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'安达信','id':'advancing','tel':'','website':'','alphabet':'andaxin','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'汇文','id':'huiwen','tel':'','website':'','alphabet':'huiwen','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'亿翔','id':'yxexpress','tel':'','website':'','alphabet':'yixiang','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'东红物流','id':'donghong','tel':'4000-081-556','website':'http://www.donghong56.com/','alphabet':'donghongwuliu','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'飞远配送','id':'feiyuanvipshop','tel':'4007-031-313','website':'http://www.fyps.cn/','alphabet':'feiyuanpeisong','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'好运来','id':'hlyex','tel':'020-86293333','website':'http://www.hlyex.com/','alphabet':'haoyunlai','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Toll','id':'dpexen','tel':'','website':'http://www.dpex.com/','alphabet':'toll','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'增益速递','id':'zengyisudi','tel':'4008-456-789','website':'http://www.zeny-express.com/','alphabet':'zengyisudi','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'四川快优达速递','id':'kuaiyouda','tel':'4006-068-555','website':'http://www.sckyd.net/','alphabet':'sichuankuaiyoudasudi','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'日昱物流','id':'riyuwuliu','tel':'4008-820-800','website':'http://www.rywl.cn/','alphabet':'riyuwuliu','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'速通物流','id':'sutongwuliu','tel':'','website':'','alphabet':'sutongwuliu','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'晟邦物流','id':'nanjingshengbang','tel':'400-666-6066','website':'http://www.3856.cc/','alphabet':'shengbangwuliu','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'爱尔兰邮政(An Post)','id':'anposten','tel':'01-7057600','website':'http://www.anpost.ie/AnPost/','alphabet':'aierlanyouzheng(an post)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'日本邮政(Japan Post)','id':'japanposten','tel':'+81 0570-046111','website':'http://www.post.japanpost.jp/english/index.html','alphabet':'ribenyouzheng(japan post)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'丹麦邮政(Post Denmark)','id':'postdanmarken','tel':'+45 80 20 70 30','website':'http://www.postdanmark.dk/en/Pages/home.aspx','alphabet':'danmaiyouzheng(post denmark)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'巴西邮政(Brazil Post/Correios)','id':'brazilposten','tel':'+55 61 3003 0100','website':'http://www.correios.com.br/','alphabet':'baxiyouzheng(brazil post/correios)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'荷兰挂号信(PostNL international registered mail)','id':'postnlcn','tel':'34819','website':'http://www.postnl.post','alphabet':'helanguahaoxin(postnl international registered mail)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'荷兰挂号信(PostNL international registered mail)','id':'postnl','tel':'34819','website':'http://www.postnl.post/details/','alphabet':'helanguahaoxin(postnl international registered mail)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'乌克兰EMS-中文(EMS Ukraine)','id':'emsukrainecn','tel':'+38 044 234-73-84','website':'http://dpsz.ua/en','alphabet':'wukelanems-zhongwen(ems ukraine)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'乌克兰EMS(EMS Ukraine)','id':'emsukraine','tel':'+38 044 234-73-84','website':'http://dpsz.ua/en','alphabet':'wukelanems(ems ukraine)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'乌克兰邮政包裹','id':'ukrpostcn','tel':'','website':'','alphabet':'wukelanyouzhengbaoguo','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'乌克兰小包大包(UkrPost)','id':'ukrpost','tel':'+380 (0) 800-500-440','website':'http://www.ukrposhta.com/','alphabet':'wukelanxiaobaodabao(ukrpost)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'海红for买卖宝','id':'haihongmmb','tel':'','website':'','alphabet':'haihongformaimaibao','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'FedEx-英国件（FedEx UK)','id':'fedexuk','tel':'+ 44 2476 706 660','website':'http://www.fedex.com/gb/ukservices/','alphabet':'fedex-yingguojian(fedex uk)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'FedEx-英国件','id':'fedexukcn','tel':'+ 44 2476 706 660','website':'http://www.fedex.com/gb/ukservices/','alphabet':'fedex-yingguojian','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'叮咚快递','id':'dingdong','tel':'','website':'','alphabet':'dingdongkuaidi','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'DPD','id':'dpd','tel':'+31 20 480 2900','website':'http://www.dpd.com/','alphabet':'dpd','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'UPS Freight','id':'upsfreight','tel':'+1 800-333-7400','website':'http://ltl.upsfreight.com/','alphabet':'ups freight','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'ABF','id':'abf','tel':'(479) 785-6486','website':'http://www.abfs.com/','alphabet':'abf','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Purolator','id':'purolator','tel':'-8754','website':'http://www.purolator.com/','alphabet':'purolator','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'比利时(Bpost)','id':'bpost','tel':'+32 (0)2 278 50 90','website':'http://www.bpostinternational.com/','alphabet':'bilishi(bpost)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'比利时国际(Bpost international)','id':'bpostinter','tel':'+32 (0)2 278 50 90','website':'http://www.bpostinternational.com/','alphabet':'bilishiguoji(bpost international)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'LaserShip','id':'lasership','tel':'+1 (800) 527-3764','website':'http://www.lasership.com/','alphabet':'lasership','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'英国大包EMS(Parcel Force)','id':'parcelforce','tel':'08448 00 44 66','website':'http://www.parcelforce.com/','alphabet':'yingguodabaoems(parcel force)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'英国邮政大包EMS','id':'parcelforcecn','tel':'08448 00 44 66','website':'http://www.parcelforce.com/','alphabet':'yingguodabaoems','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'YODEL','id':'yodel','tel':'+44 800 0152 662','website':'http://www.myyodel.co.uk/','alphabet':'yodel','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'DHL-荷兰(DHL Netherlands)','id':'dhlnetherlands','tel':'+31 26-324 6700','website':'http://www.dhl.nl','alphabet':'dhl-helan(dhl netherlands)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'MyHermes','id':'myhermes','tel':'+44 844 543 7000','website':'https://www.myhermes.co.uk/','alphabet':'myhermes','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'DPD Germany','id':'dpdgermany','tel':'+49 01806 373 200','website':'https://www.dpd.com/de/(portal)/de/(rememberCountry)/0','alphabet':'dpd germany','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Fastway Ireland','id':'fastway','tel':'+353 1 4242 900','website':'http://www.fastway.ie/index.php','alphabet':'fastway ireland','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'法国大包EMS-法文(Chronopost France)','id':'chronopostfra','tel':'+33 (0) 969 391 391','website':'http://www.chronopost.fr/','alphabet':'faguodabaoems-fawen(chronopost france)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Selektvracht','id':'selektvracht','tel':'+31 0900-2222120','website':'http://www.selektvracht.nl/','alphabet':'selektvracht','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'蓝弧快递','id':'lanhukuaidi','tel':'4000661646','website':'http://www.lanhukd.com/','alphabet':'lanhukuaidi','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'比利时(Belgium Post)','id':'belgiumpost','tel':'+32 2 276 22 74','website':'http://www.bpost.be/','alphabet':'bilishi(belgium post)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'UPS Mail Innovations','id':'upsmailinno','tel':'+1 800-500-2224','website':'http://www.upsmailinnovations.com/','alphabet':'ups mail innovations','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'挪威(Posten Norge)','id':'postennorge','tel':'+47 21316260','website':'http://www.posten.no/en/','alphabet':'nuowei(posten norge)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'瑞士(Swiss Post)','id':'swisspostcn','tel':'+41848888888','website':'https://www.post.ch/de/privat?wt_shortcut=www-swisspost-com&WT.mc_id=shortcut_www-swisspost-com','alphabet':'ruishi(swiss post)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'瑞士(Swiss Post)','id':'swisspost','tel':'+41 848 888 888','website':'http://www.post.ch/en','alphabet':'ruishi(swiss post)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'英国邮政小包','id':'royalmailcn','tel':'','website':'','alphabet':'yingguoyouzhengxiaobao','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'英国小包(Royal Mail)','id':'royalmail','tel':'+44 1752387112','website':'http://www.royalmail.com/','alphabet':'yingguoxiaobao(royal mail)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'DHL Benelux','id':'dhlbenelux','tel':'+31 26-324 6700','website':'http://www.dhl.nl/nl.html','alphabet':'dhl benelux','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Nova Poshta','id':'novaposhta','tel':'+7 (0) 800 500 609','website':'http://novaposhta.ua/','alphabet':'nova poshta','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'DHL-波兰(DHL Poland)','id':'dhlpoland','tel':'+48 42 6 345 345','website':'http://www.dhl.com.pl/pl.html','alphabet':'dhl-bolan(dhl poland)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Estes','id':'estes','tel':'1-866-378-3748','website':'http://www.estes-express.com/','alphabet':'estes','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'TNT UK','id':'tntuk','tel':'+44 0800 100 600','website':'http://www.tnt.com/portal/location/en.html','alphabet':'tnt uk','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Deltec Courier','id':'deltec','tel':'+44 (0) 20 8569 6767','website':'https://www.deltec-courier.com','alphabet':'deltec courier','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'OPEK','id':'opek','tel':'+48 22 732 79 99','website':'http://www.opek.com.pl/','alphabet':'opek','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'DPD Poland','id':'dpdpoland','tel':'+48 801 400 373','website':'http://www.dpd.com.pl/','alphabet':'dpd poland','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Italy SDA','id':'italysad','tel':'+39 199 113366','website':'http://wwww.sda.it/','alphabet':'italy sda','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'MRW','id':'mrw','tel':'+34 902 300 402','website':'http://www.mrw.es/','alphabet':'mrw','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Chronopost Portugal','id':'chronopostport','tel':'+351 707 20 28 28','website':'http://chronopost.pt/','alphabet':'chronopost portugal','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'西班牙(Correos de España)','id':'correosdees','tel':'+34 902197197','website':'http://www.correos.es','alphabet':'xibanya(correos de espana)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Direct Link','id':'directlink','tel':'+1 (908) 289-0703','website':'http://www.directlink.com','alphabet':'direct link','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'ELTA Hellenic Post','id':'eltahell','tel':'+30 801 11 83000','website':'https://www.elta-courier.gr','alphabet':'elta hellenic post','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'捷克(Česká pošta)','id':'ceskaposta','tel':'+420 840 111 244','website':'http://www.ceskaposta.cz/index','alphabet':'jieke(ceská posta)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Siodemka','id':'siodemka','tel':'+48 22 777 77 77','website':'http://www.siodemka.com/','alphabet':'siodemka','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'International Seur','id':'seur','tel':'+34 93 336 85 85','website':'http://www.seur.com/','alphabet':'international seur','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'久易快递','id':'jiuyicn','tel':'021-64206088','website':'http://www.jiuyicn.com/','alphabet':'jiuyikuaidi','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'克罗地亚(Hrvatska Posta)','id':'hrvatska','tel':'+385 0800 303 304','website':'http://www.posta.hr/','alphabet':'keluodiya(hrvatska posta)','avatar':'#03A9F4'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'保加利亚(Bulgarian Posts)','id':'bulgarian','tel':'+3592/949 3280','website':'http://www.bgpost.bg/','alphabet':'baojialiya(bulgarian posts)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Portugal Seur','id':'portugalseur','tel':'+351 707 50 10 10','website':'http://www.seur.com/','alphabet':'portugal seur','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'EC-Firstclass','id':'ecfirstclass','tel':'+86 4006 988 223','website':'http://www.ec-firstclass.org/Details.aspx','alphabet':'ec-firstclass','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'DTDC India','id':'dtdcindia','tel':'+91 33004444','website':'http://dtdc.com','alphabet':'dtdc india','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Safexpress','id':'safexpress','tel':'+91 11 26783281','website':'http://www.safexpress.com','alphabet':'safexpress','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'韩国(Korea Post)','id':'koreapost','tel':'+82 2 2195 1114','website':'http://www.koreapost.go.kr/kpost/main/index.jsp','alphabet':'hanguo(korea post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'TNT Australia','id':'tntau','tel':'+61 13 11 50','website':'https://www.tntexpress.com.au','alphabet':'tnt australia','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'泰国(Thailand Thai Post)','id':'thailand','tel':'0 2573 5463','website':'http://www.thailandpost.co.th','alphabet':'taiguo(thailand thai post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'SkyNet Malaysia','id':'skynetmalaysia','tel':'+60 3- 56239090','website':'http://www.skynet.com.my/','alphabet':'skynet malaysia','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'马来西亚小包(Malaysia Post(Registered))','id':'malaysiapost','tel':'+603 27279100','website':'http://www.pos.com.my/','alphabet':'malaixiyaxiaobao(malaysia post(registered))','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'马来西亚大包EMS（Malaysia Post(parcel,EMS)）','id':'malaysiaems','tel':'+603 27279100','website':'http://www.pos.com.my/','alphabet':'malaixiyadabaoems(malaysia post(parcel,ems))','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'京东','id':'jd','tel':'400-603-3600','website':'http://www.jd-ex.com','alphabet':'jingdong','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'沙特阿拉伯(Saudi Post)','id':'saudipost','tel':'+966 9200 05700','website':'http://www.sp.com.sa','alphabet':'shatealabo(saudi post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'南非(South African Post Office)','id':'southafrican','tel':'+27 0860 111 502','website':'http://www.postoffice.co.za','alphabet':'nanfei(south african post office)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'OCA Argentina','id':'ocaargen','tel':'+34 800-999-7700','website':'http://www.oca.com.ar/','alphabet':'oca argentina','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'尼日利亚(Nigerian Postal)','id':'nigerianpost','tel':'234-09-3149531','website':'http://www.nipost.gov.ng','alphabet':'niriniya(nigerian postal)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'智利(Correos Chile)','id':'chile','tel':'+562 600 950 2020','website':'http://www.correos.cl','alphabet':'zhili(correos chile)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'以色列(Israel Post)','id':'israelpost','tel':'+972 2 629 0691','website':'http://www.israelpost.co.il','alphabet':'yiselie(israel post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Toll Priority(Toll Online)','id':'tollpriority','tel':'+61 13 15 31','website':'https://online.toll.com.au','alphabet':'toll priority(toll online)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Estafeta','id':'estafeta','tel':'+52 1-800-378-2338','website':'http://rastreo3.estafeta.com','alphabet':'estafeta','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'港快速递','id':'gdkd','tel':'400-11-33333','website':'http://www.gksd.com/','alphabet':'gangkuaisudi','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'墨西哥(Correos de Mexico)','id':'mexico','tel':'+52 01 800 701 7000','website':'http://www.correosdemexico.gob.mx','alphabet':'moxige(correos de mexico)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'罗马尼亚(Posta Romanian)','id':'romanian','tel':'+40 021 9393 111','website':'http://www.posta-romana.ro/posta-romana.html','alphabet':'luomaniya(posta romanian)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'TNT Italy','id':'tntitaly','tel':'+39 199 803 868','website':'http://www.tnt.it','alphabet':'tnt italy','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Mexico Multipack','id':'multipack','tel':'+52 1800 7023200','website':'http://www.multipack.com.mx/','alphabet':'mexico multipack','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'葡萄牙(Portugal CTT)','id':'portugalctt','tel':'+351 707 26 26 26','website':'http://www.ctt.pt','alphabet':'putaoya(portugal ctt)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Interlink Express','id':'interlink','tel':'+44 8702 200 300','website':'http://www.interlinkexpress.com/','alphabet':'interlink express','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'DPD UK','id':'dpduk','tel':'+44 845 9 300 350','website':'http://www.dpd.co.uk/','alphabet':'dpd uk','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'华航快递','id':'hzpl','tel':'400-697-0008','website':'http://www.hz3pl.com','alphabet':'huahangkuaidi','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Gati-KWE','id':'gatikwe','tel':'+91 1800-180-4284','website':'http://www.gatikwe.com/','alphabet':'gati-kwe','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Red Express','id':'redexpress','tel':'+91 1800-123-2400','website':'https://www.getsetred.net','alphabet':'red express','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Mexico Senda Express','id':'mexicodenda','tel':'+52 1800 833 93 00','website':'http://www.sendaexpress.com.mx/rastreo.asp#af','alphabet':'mexico denda express','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'TCI XPS','id':'tcixps','tel':'18002000977','website':'http://www.tcixps.com/','alphabet':'tcixps','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'高铁速递','id':'hre','tel':'400-999-7777','website':'http://www.hre-e.com/','alphabet':'gaotiesudi','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'新加坡EMS大包(Singapore Speedpost)','id':'speedpost','tel':'+65 6222 5777','website':'http://www.speedpost.com.sg/','alphabet':'xinjiapoemsdabao(singapore speedpost)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'EMS-国际件-英文','id':'emsinten','tel':'','website':'http://www.ems.com.cn/','alphabet':'ems-guojijian-yingwen','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Asendia USA','id':'asendiausa','tel':'+1 610 461 3661','website':'http://www.asendiausa.com/','alphabet':'asendia usa','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'法国大包EMS-英文(Chronopost France)','id':'chronopostfren','tel':'+33 (0) 969 391 391','website':'http://www.chronopost.fr/','alphabet':'faguodabaoems-yingwen(chronopost france)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'意大利(Poste Italiane)','id':'italiane','tel':'+39 803 160','website':'http://www.poste.it/','alphabet':'yidali(poste italiane)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'冠达快递','id':'gda','tel':'400-990-0088','website':'http://www.gda-e.com.cn/','alphabet':'guandakuaidi','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'出口易','id':'chukou1','tel':'4006-988-223','website':'http://www.chukou1.com','alphabet':'chukouyi','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'黄马甲','id':'huangmajia','tel':'029-96128','website':'http://www.huangmajia.com','alphabet':'huangmajia','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'新干线快递','id':'anlexpress','tel':'','website':'','alphabet':'xinganxiankuaidi','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'飞洋快递','id':'shipgce','tel':'001-877-387-9799','website':'http://express.shipgce.com/','alphabet':'feiyangkuaidi','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'贝海国际速递','id':'xlobo','tel':'086-400-082-2200','website':'http://www.xlobo.com/','alphabet':'beihaiguojisudi','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'阿联酋(Emirates Post)','id':'emirates','tel':'600-599-999','website':'http://www.epg.gov.ae/','alphabet':'alianqiu(emirates post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'新顺丰(NSF)','id':'nsf','tel':'0064-9-5258288','website':'http://www.nsf.co.nz/','alphabet':'xinshunfeng(nsf)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'巴基斯坦(Pakistan Post)','id':'pakistan','tel':'+92 51 926 00 37','website':'http://ep.gov.pk/','alphabet':'bajisitan(pakistan post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'世运快递','id':'shiyunkuaidi','tel':'400-666-1111','website':'http://www.sehoex.com/','alphabet':'shiyunkuaidi','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'合众速递(UCS)','id':'ucs','tel':'024-31515566','website':'http://www.ucsus.com','alphabet':'hezhongsudi(ucs)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'阿富汗(Afghan Post)','id':'afghan','tel':'+93 20 2104075','website':'http://track.afghanpost.gov.af/','alphabet':'afuhan(afghan post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'白俄罗斯(Belpochta)','id':'belpost','tel':'+375 17 293 59 10','website':'http://www.belpost.by/','alphabet':'baieluosi(belpochta)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'全通快运','id':'quantwl','tel':'','website':'','alphabet':'quantongkuaiyun','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'宅急便','id':'zhaijibian','tel':'','website':'','alphabet':'zhaijibian','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'EFS Post','id':'efs','tel':'0773-2308246','website':'http://www.efspost.com/','alphabet':'efs post','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'TNT Post','id':'tntpostcn','tel':'+31（0）900 0570','website':'http://parcels-uk.tntpost.com/','alphabet':'tnt post','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'英脉物流','id':'gml','tel':'400-880-5088','website':'http://www.gml.cn/','alphabet':'yingmaiwuliu','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'广通速递','id':'gtongsudi','tel':'400-801-5567','website':'http://www.gto56.com','alphabet':'guangtongsudi','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'东瀚物流','id':'donghanwl','tel':'400-092-2229','website':'http://www.donghanwl.com/','alphabet':'donghanwuliu','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'rpx','id':'rpx','tel':'','website':'','alphabet':'rpx','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'日日顺物流','id':'rrs','tel':'400-800-9999','website':'http://www.rrs.com/wl/fwwl','alphabet':'ririshunwuliu','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'黑猫雅玛多','id':'yamato','tel':'','website':'','alphabet':'heimaomayaduo','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'华通快运','id':'htongexpress','tel':'','website':'','alphabet':'huatongkuaiyun','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'吉尔吉斯斯坦(Kyrgyz Post)','id':'kyrgyzpost','tel':'','website':'http://www.posta.kg','alphabet':'jierjisisitan(kyrgyz post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'拉脱维亚(Latvijas Pasts)','id':'latvia','tel':'','website':'http://www.pasts.lv','alphabet':'latuoweiya(latvijas posts)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'黎巴嫩(Liban Post)','id':'libanpost','tel':'+961 1 629628','website':'http://www.libanpost.com.lb','alphabet':'libanen(liban post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'立陶宛(Lietuvos paštas)','id':'lithuania','tel':'+370 700 55 400','website':'http://www.post.lt','alphabet':'litaowan(lietuvos pastas)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'马尔代夫(Maldives Post)','id':'maldives','tel':'+960 331 5555','website':'https://www.maldivespost.com/store/','alphabet':'maerdaifu(maldives post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'马耳他(Malta Post)','id':'malta','tel':'800 7 22 44','website':'http://maltapost.com','alphabet':'maerta(malta post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'马其顿(Macedonian Post)','id':'macedonia','tel':'','website':'http://www.posta.com.mk/','alphabet':'maqidun(macedonian post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'新西兰(New Zealand Post)','id':'newzealand','tel':'','website':'https://www.nzpost.co.nz','alphabet':'xinxilan(new zealand post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'摩尔多瓦(Posta Moldovei)','id':'moldova','tel':'+373 - 22 270 044','website':'http://www.posta.md/','alphabet':'moerduowa(posta moldova)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'孟加拉国(EMS)','id':'bangladesh','tel':'9558006','website':'http://www.bangladeshpost.gov.bd','alphabet':'mengjialaguo(ems)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'塞尔维亚(PE Post of Serbia)','id':'serbia','tel':'0700 100 300','website':'http://www.posta.rs','alphabet':'saierweiya(pe post of serbia)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'塞浦路斯(Cyprus Post)','id':'cypruspost','tel':'77778013','website':'http://www.mcw.gov.cy/mcw/postal/dps.nsf/index_en/index_en','alphabet':'saipulusi(cyprus post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'突尼斯EMS(Rapid-Poste)','id':'tunisia','tel':'(+216) 71 888 888','website':'http://www.e-suivi.poste.tn','alphabet':'tunisi(rapid-poste)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'乌兹别克斯坦(Post of Uzbekistan)','id':'uzbekistan','tel':'(99871) 233 57 47','website':'http://www.pochta.uz/en/','alphabet':'wuzibiekesitan(post of uzbekistan)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'新喀里多尼亚[法国](New Caledonia)','id':'caledonia','tel':'','website':'http://www.opt.nc','alphabet':'xinkaliduoniya[faguo](new caledonia)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'叙利亚(Syrian Post)','id':'republic','tel':'','website':'http://www.syrianpost.gov.sy/','alphabet':'xuliya(syrian post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'亚美尼亚(Haypost-Armenian Postal)','id':'haypost','tel':'','website':'http://www.haypost.am','alphabet':'yameiniya(haypost-armenian)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'也门(Yemen Post)','id':'yemen','tel':'','website':'http://www.post.ye/','alphabet':'yemen(yemen post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'印度(India Post)','id':'india','tel':'1800-11-2011','website':'http://www.indiapost.gov.in','alphabet':'yindu(india post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'英国(大包,EMS)','id':'england','tel':'','website':'','alphabet':'yingguo(dabao,ems)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'约旦(Jordan Post)','id':'jordan','tel':'+962-6-4293000','website':'http://www.jordanpost.com.jo/','alphabet':'yuedan(jordan post)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'越南小包(Vietnam Posts)','id':'vietnam','tel':'(+84) 1900 54 54 81','website':'http://www.vnpost.vn/','alphabet':'yuenanxiaobao(vietnam posts)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'黑山(Pošta Crne Gore)','id':'montenegro','tel':'','website':'http://www.postacg.me','alphabet':'heishan(posta crne gore)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'哥斯达黎加(Correos de Costa Rica)','id':'correos','tel':'','website':'https://www.correos.go.cr','alphabet':'correos de costa rica','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'西安喜来快递','id':'xilaikd','tel':'','website':'','alphabet':'xianxilaikuaidi','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'格陵兰[丹麦](TELE Greenland A/S)','id':'greenland','tel':'','website':'http://sp.post.gl','alphabet':'gelinlan[danmai](tele greenland a/s)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'菲律宾(Philippine Postal)','id':'phlpost','tel':'','website':'https://www.phlpost.gov.ph','alphabet':'feilvbin(philippine postal)','avatar':'#FF9800'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'厄瓜多尔(Correos del Ecuador)','id':'ecuador','tel':'(593-2) 3829210','website':'http://www.correosdelecuador.gob.ec/','alphabet':'eguaduoer(correos del ecuador)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'冰岛(Iceland Post)','id':'iceland','tel':'','website':'http://www.postur.is/','alphabet':'bingdao(iceland post)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'波兰小包(Poczta Polska)','id':'emonitoring','tel':'801 333 444','website':'http://www.poczta-polska.pl/','alphabet':'bolan(poczta polska)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'阿尔巴尼亚(Posta shqipatre)','id':'albania','tel':'','website':'http://www.postashqiptare.al/','alphabet':'aerbaliya(posta shqipatre)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'阿鲁巴[荷兰](Post Aruba)','id':'aruba','tel':'+297 528-7637','website':'http://www.postaruba.com','alphabet':'aluba[helan](post aruba)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'埃及(Egypt Post)','id':'egypt','tel':'23910011','website':'http://www.egyptpost.org/','alphabet':'aiji(egypt post)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'爱尔兰(An Post)','id':'ireland','tel':'+353 1850 57 58 59','website':'http://www.anpost.ie/AnPost/','alphabet':'aierlan(an post)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'爱沙尼亚(Eesti Post)','id':'omniva','tel':'','website':'https://www.omniva.ee/','alphabet':'aishaniya(eesti post)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'云豹国际货运','id':'leopard','tel':'','website':'','alphabet':'yunbaoguojihuoyun','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中外运空运','id':'sinoairinex','tel':'','website':'','alphabet':'zhongwaiyunkongyun','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'上海昊宏国际货物','id':'hyk','tel':'','website':'','alphabet':'shanghaihaohongguojihuowu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'城晓国际快递','id':'ckeex','tel':'','website':'','alphabet':'chengxiaoguojikuaidi','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'匈牙利(Magyar Posta)','id':'hungary','tel':'+36 1 421 7296 Search','website':'http://posta.hu/international','alphabet':'xiongyali(magyar posta)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'澳门(Macau Post)','id':'macao','tel':'','website':'http://www.macaupost.gov.mo/','alphabet':'aomen(macau post)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'台湾(中华邮政)','id':'postserv','tel':'','website':'http://postserv.post.gov.tw','alphabet':'taiwan(zhonghuayouzheng)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'北京EMS','id':'bjemstckj','tel':'010-8417 9386','website':'http://www.bj-cnpl.com/webpage/contactus.asp','alphabet':'beijingems','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'快淘快递','id':'kuaitao','tel':'400-770-3370','website':'http://www.4007703370.com/','alphabet':'kuaitaokuaidi','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'秘鲁(SERPOST)','id':'peru','tel':'511-500','website':'http://www.serpost.com.pe/','alphabet':'bilu(serpost)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'印度尼西亚EMS(Pos Indonesia-EMS)','id':'indonesia','tel':'+62 21 161','website':'http://ems.posindonesia.co.id/','alphabet':'yindunixiyaems(pos indonesia-ems)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'哈萨克斯坦(Kazpost)','id':'kazpost','tel':'8 800 080 08 80','website':'http://www.kazpost.kz/en/','alphabet':'hasakesitan(kazpost)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'立白宝凯物流','id':'lbbk','tel':'020-81258022','website':'http://bkls.liby.com.cn/','alphabet':'libaibaokaiwuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'百千诚物流','id':'bqcwl','tel':'0755-2222 2232','website':'http://www.1001000.com/','alphabet':'baiqianchengwuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'皇家物流','id':'pfcexpress','tel':'0755-29801942','website':'http://www.pfcexpress.com/','alphabet':'huangjiawuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'法国(La Poste)','id':'csuivi','tel':'+33 3631','website':'http://www.colissimo.fr','alphabet':'faguo(la poste)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'奥地利(Austrian Post)','id':'austria','tel':'+43 810 010 100','website':'http://www.post.at','alphabet':'aodili(austrian post)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'乌克兰小包大包(UkrPoshta)','id':'ukraine','tel':'+380 (0) 800-500-440','website':'http://www.ukrposhta.com/www/upost_en.nsf','alphabet':'wukelanxiaobaodabao(ukrposhta)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'乌干达(Posta Uganda)','id':'uganda','tel':'','website':'http://www.ugapost.co.ug/','alphabet':'wuganda(posta uganda)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'阿塞拜疆EMS(EMS AzerExpressPost)','id':'azerbaijan','tel':'','website':'http://www.azems.az/en','alphabet':'asabaijiangems(ems azerexpresspost)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'芬兰(Itella Posti Oy)','id':'finland','tel':'+358 200 71000','website':'http://www.posti.fi/english/','alphabet':'fenlan(itella posti oy)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'斯洛伐克(Slovenská Posta)','id':'slovak','tel':'(+421) 48 437 87 77','website':'http://www.posta.sk/en','alphabet':'siluofake(slovenska posta)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'埃塞俄比亚(Ethiopian postal)','id':'ethiopia','tel':'','website':'http://www.ethiopostal.com/','alphabet':'aisaiebiya(ethiopian postal)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'卢森堡(Luxembourg Post)','id':'luxembourg','tel':'8002 8004','website':'http://www.post.lu/','alphabet':'lusenbao(luxembourg post)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'毛里求斯(Mauritius Post)','id':'mauritius','tel':'208 2851','website':'http://www.mauritiuspost.mu/','alphabet':'maoliqiusi(mauritius post)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'文莱(Brunei Postal)','id':'brunei','tel':'673-2382888','website':'http://www.post.gov.bn/','alphabet':'wenlai(brunei postal)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Quantium','id':'quantium','tel':'','website':'','alphabet':'quantium','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'坦桑尼亚(Tanzania Posts)','id':'tanzania','tel':'','website':'ttp://www.posta.co.tz','alphabet':'tansangniya(tanzania posts)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'阿曼(Oman Post)','id':'oman','tel':'24769925','website':'http://www.omanpost.om','alphabet':'aman(oman post)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'直布罗陀[英国]( Royal Gibraltar Post)','id':'gibraltar','tel':'','website':'http://www.post.gi/','alphabet':'zhibuluotuo[yingguo](royal gibralter post)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'博源恒通','id':'byht','tel':'15834177000','website':'http://www.56soft.com','alphabet':'boyuanhengtong','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'越南EMS(VNPost Express)','id':'vnpost','tel':'','website':'http://www.ems.com.vn/default.aspx','alphabet':'yuenanems(vmpost)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'安迅物流','id':'anxl','tel':'010-59288730','website':'http://www.anxl.com.cn/','alphabet':'anxunwuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'达方物流','id':'dfpost','tel':'400 700 7049','website':'http://www.dfpost.com/','alphabet':'dafangwuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'兰州伙伴物流','id':'huoban','tel':'0931-5345730/32','website':'http://www.lzhbwl.com/','alphabet':'lanzhouhuobanwuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'天纵物流','id':'tianzong','tel':'400-990-8816','website':'http://www.tianzong56.cn/','alphabet':'tianzongwuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'波黑(JP BH Posta)','id':'bohei','tel':'','website':'http://www.posta.ba/pocetna/2/0/0.html','alphabet':'bohei(jp bh posta)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'玻利维亚','id':'bolivia','tel':'','website':'','alphabet':'boliweiya','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'柬埔寨(Cambodia Post)','id':'cambodia','tel':'','website':'','alphabet':'jianpuzhai(combodia post)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'巴林(Bahrain Post)','id':'bahrain','tel':'','website':'http://mot.gov.bh/en','alphabet':'balin(bahrain post)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'纳米比亚(NamPost)','id':'namibia','tel':'+264 61 201 3042','website':'https://www.nampost.com.na/','alphabet':'namibiya(nampost)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'卢旺达(Rwanda i-posita)','id':'rwanda','tel':'','website':'http://i-posita.rw/spip.php?article97','alphabet':'luwangda(rwanda)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'莱索托(Lesotho Post)','id':'lesotho','tel':'','website':'http://lesothopost.org.ls/','alphabet':'laisuotuo(lesotho post)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'肯尼亚(POSTA KENYA)','id':'kenya','tel':'','website':'','alphabet':'kenniya(posta kenya)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'喀麦隆(CAMPOST)','id':'cameroon','tel':'','website':'','alphabet':'kamailong(campost)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'伯利兹(Belize Postal)','id':'belize','tel':'','website':'','alphabet':'bolizi(belize postal)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'巴拉圭(Correo Paraguayo)','id':'paraguay','tel':'','website':'http://www.correoparaguayo.gov.py/','alphabet':'balagui(correo paraguayo)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'十方通物流','id':'sfift','tel':'','website':'','alphabet':'shifangtongwuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'飞鹰物流','id':'hnfy','tel':'400-6291-666','website':'http://www.hnfy56.com/','alphabet':'feiyingwuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'UPS i-parcel','id':'iparcel','tel':'400-078-1183','website':'http://www.i-parcel.com/en/','alphabet':'ups i-parcel','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'鑫锐达','id':'bjxsrd','tel':'','website':'','alphabet':'xinruida','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'麦力快递','id':'mailikuaidi','tel':'400-0000-900','website':'http://www.mailikuaidi.com/','alphabet':'mailikuaidi','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'瑞丰速递','id':'rfsd','tel':'400-063-9000','website':'http://www.rfsd88.com/','alphabet':'ruifengsudi','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'美联快递','id':'letseml','tel':'','website':'','alphabet':'meiliankuaidi','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'CNPEX中邮快递','id':'cnpex','tel':'','website':'','alphabet':'cnpexzhongyoukuaidi','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'鑫世锐达','id':'xsrd','tel':'','website':'','alphabet':'xinshiruida','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'同舟行物流','id':'chinatzx','tel':'18062512813,18062699168','website':'http://www.chinatzx.com/','alphabet':'tongzhouxingwuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'秦邦快运','id':'qbexpress','tel':'','website':'','alphabet':'qinbangkuaiyun','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'大达物流','id':'idada','tel':'400-098-5656','website':'http://www.idada56.com/','alphabet':'dadawuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'skynet','id':'skynet','tel':'','website':'','alphabet':'skynet','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'红马速递','id':'nedahm','tel':'','website':'','alphabet':'hongmasudi','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'云南中诚','id':'czwlyn','tel':'','website':'','alphabet':'yunnanzhongcheng','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'万博快递','id':'wanboex','tel':'','website':'','alphabet':'wanbokuaidi','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'腾达速递','id':'nntengda','tel':'','website':'http://www.nntengda.com/','alphabet':'tengdasudi','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'郑州速捷','id':'sujievip','tel':'','website':'','alphabet':'zhengzhousujie','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'UBI Australia','id':'gotoubi','tel':'','website':'','alphabet':'ubi australia','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'ECMS Express','id':'ecmsglobal','tel':'','website':'','alphabet':'ecms express','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'速派快递(FastGo)','id':'fastgo','tel':'400 886 3278','website':'http://www.fastgo.com.au/','alphabet':'supaikuaidi(fastgo)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'易客满','id':'ecmscn','tel':'86+(400) 086-1756','website':'http://www.trans4e.com/cn/index.html','alphabet':'yikeman','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'俄顺达','id':'eshunda','tel':'0592-5798079','website':'http://www.007ex.com/','alphabet':'eshunda','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'广东速腾物流','id':'suteng','tel':'4001136666','website':'http://www.ste56.com/','alphabet':'guangdongsutengwuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'新鹏快递','id':'gdxp','tel':'','website':'','alphabet':'xinpengkuaidi','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'美国云达','id':'yundaexus','tel':'888-408-3332','website':'http://www.yundaex.us/','alphabet':'meiguoyunda','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'Toll','id':'toll','tel':'','website':'','alphabet':'toll','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'深圳DPEX','id':'szdpex','tel':'','website':'','alphabet':'shenzhendpex','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'百世物流','id':'baishiwuliu','tel':'400-8856-561','website':'http://www.800best.com/','alphabet':'baishiwuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'荷兰包裹(PostNL International Parcels)','id':'postnlpacle','tel':'34819','website':'http://www.postnl.com/','alphabet':'helanbaoguo(postnl international parcels)','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'乐天速递','id':'ltexp','tel':'021-62269059','website':'http://www.ltexp.com.cn','alphabet':'letiansudi','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'智通物流','id':'ztong','tel':'4000561818','website':'http://www.ztong56.com/','alphabet':'zhitongwuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'鑫通宝物流','id':'xtb','tel':'13834168880','website':'http://www.xtb56.com/','alphabet':'xintongbaowuliu','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'airpak expresss','id':'airpak','tel':'','website':'','alphabet':'airpak expresss','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'荷兰邮政-中国件','id':'postnlchina','tel':'34819','website':'http://www.postnl.com/','alphabet':'helanyouzheng-zhongguojian','avatar':'#CDDC39'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'法国小包(colissimo)','id':'colissimo','tel':'+33 3631','website':'http://www.colissimo.fr','alphabet':'faguoxiaobao(colissimo)','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'PCA Express','id':'pcaexpress','tel':'1800 518 000','website':'http://www.pcaexpress.com.au/','alphabet':'pca express','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'韩润','id':'hanrun','tel':'400-636-4311','website':'http://www.hr-sz.com/','alphabet':'hanrun','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'TNT','id':'tnt','tel':'800-820-9868','website':'http://www.tnt.com.cn','alphabet':'tnt','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中远e环球','id':'cosco','tel':'','website':'','alphabet':'zhuanyuanehuanqiu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'顺达快递','id':'sundarexpress','tel':'','website':'','alphabet':'shundakuaidi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'捷记方舟','id':'ajexpress','tel':'','website':'','alphabet':'jiejifangzhou','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'方舟速递','id':'arkexpress','tel':'','website':'','alphabet':'fangzhousudi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'明大快递','id':'adaexpress','tel':'','website':'','alphabet':'mingtaikuaidi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'长江国际速递','id':'changjiang','tel':'','website':'','alphabet':'changjiangguojisudi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'八达通','id':'bdatong','tel':'','website':'','alphabet':'badatong','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'美国申通','id':'stoexpress','tel':'','website':'','alphabet':'meiguoshentong','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'泛捷国际速递','id':'epanex','tel':'','website':'','alphabet':'fanjieguojikuaidi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'顺捷丰达','id':'shunjiefengda','tel':'0755—88999000','website':'http://www.sjfd-express.com','alphabet':'shunjiefengda','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'华赫物流','id':'nmhuahe','tel':'','website':'http://nmhuahe.com','alphabet':'huahewuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'德国(Deutsche Post)','id':'deutschepost','tel':'0180 2 3333*','website':'http://www.dpdhl.com/en.html','alphabet':'deguo(deusche post)','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'百腾物流','id':'baitengwuliu','tel':'400-9989-256','website':'http://www.beteng.com/','alphabet':'baitengwuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'品骏快递','id':'pjbest','tel':'400-9789-888','website':'http://www.pjbest.com/','alphabet':'pinjunkuaidi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'全速通','id':'quansutong','tel':'','website':'','alphabet':'quansutong','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中技物流','id':'zhongjiwuliu','tel':'','website':'','alphabet':'zhongjiwuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'九曳供应链','id':'jiuyescm','tel':'4006-199-939','website':'http://jiuyescm.com','alphabet':'jiuyegongyinglian','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'天翼快递','id':'tykd','tel':'','website':'','alphabet':'tianyikuaidi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'德意思','id':'dabei','tel':'','website':'','alphabet':'deyisi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'城际快递','id':'chengji','tel':'','website':'','alphabet':'chengjikuaidi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'程光快递','id':'chengguangkuaidi','tel':'','website':'','alphabet':'chengguangkuaidi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'佐川急便','id':'sagawa','tel':'','website':'','alphabet':'zuochuanjibian','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'蓝天快递','id':'lantiankuaidi','tel':'','website':'','alphabet':'lantiankuaidi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'永昌物流','id':'yongchangwuliu','tel':'','website':'','alphabet':'yongchangwuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'笨鸟海淘','id':'birdex','tel':'4008-890-788','website':'http://birdex.cn/','alphabet':'benniaohaitao','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'一正达速运','id':'yizhengdasuyun','tel':'','website':'','alphabet':'yizhengdasuyun','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'京东订单','id':'jdorder','tel':'400-606-5500','website':'http://m.jd.com/','alphabet':'jingdongdingdan','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'优配速运','id':'sdyoupei','tel':'0531 89977777','website':'http://www.sdyoupei.com','alphabet':'yousupeiyun','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'TRAKPAK','id':'trakpak','tel':'','website':'','alphabet':'trakpak','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'GTS快递','id':'gts','tel':'4000-159-111','website':'http://www.gto315.com','alphabet':'gtskuaidi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'AOL澳通速递','id':'aolau','tel':'0424047888','website':'http://www.aol-au.com/','alphabet':'aolaotongsudi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'宜送物流','id':'yiex','tel':'4008636658','website':'http://www.yi-express.com','alphabet':'yisongwuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'通达兴物流','id':'tongdaxing','tel':'4001-006-609','website':'http://www.tongdaxing.com/','alphabet':'tongdaxingwuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'香港(HongKong Post)英文','id':'hkposten','tel':'','website':'','alphabet':'xianggang(hongkong post)yingwen','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'苏宁订单','id':'suningorder','tel':'4008-365-365','website':'','alphabet':'suningdingdan','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'飞力士物流','id':'flysman','tel':'86-755-83448000','website':'http://www.flysman.com.cn','alphabet':'feilishiwuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'转运四方','id':'zhuanyunsifang','tel':'','website':'http://www.transrush.com/','alphabet':'zhuanyunsifang','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'logen路坚','id':'ilogen','tel':'','website':'','alphabet':'logenlujian','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'成都东骏物流','id':'dongjun','tel':'028-85538888','website':'http://www.dj56.cc/','alphabet':'chengdudongjunwuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'日本郵便','id':'japanpost','tel':'','website':'','alphabet':'ribenyoubian','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'佳家通货运','id':'jiajiatong56','tel':'4008-056-356','website':'http://www.jiajiatong56.com/','alphabet':'jiajiatongwuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'吉日优派','id':'jrypex','tel':'400-0531-951','website':'http://www.jrypex.com','alphabet':'jiriyoupai','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'西安胜峰','id':'xaetc','tel':'400-029-8171','website':'http://www.xaetc.cn/','alphabet':'jianshengfeng','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'CJ物流','id':'doortodoor','tel':'','website':'','alphabet':'cjwuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'信天捷快递','id':'xintianjie','tel':'400-718-7518','website':'http://www.bjxintianjie.com','alphabet':'xintianjiekuaidi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'泰国138国际物流','id':'sd138','tel':'66880089916','website':'http://www.138sd.net/','alphabet':'taiguo138wuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'猴急送','id':'hjs','tel':'400-8888-798','website':'http://www.hjs777.com','alphabet':'houjisong','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'全信通快递','id':'quanxintong','tel':'400-882-6886','website':'http://www.all-express.com.cn/','alphabet':'quanxintongkuaidi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'amazon-国际订单','id':'amusorder','tel':'400-910-5668','website':'','alphabet':'amazon-guojidingdan','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'骏丰国际速递','id':'junfengguoji','tel':'0773-2218104','website':'http://www.peakmorepost.com/','alphabet':'junfengguojisudi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'货运皇','id':'kingfreight','tel':'','website':'http://www.kingfreight.com.au/','alphabet':'huoyunhuang','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'远成快运','id':'ycexpress','tel':'','website':'http://www.ycgky.com/','alphabet':'yuanchengkuaiyun','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'速必达','id':'subida','tel':'0752-3270594','website':'http://www.speedex.com.cn/','alphabet':'subida','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'特急便物流','id':'sucmj','tel':'','website':'http://www.sucmj.com/','alphabet':'tejibianwuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'亚马逊中国','id':'yamaxunwuliu','tel':'400-910-5669','website':'http://www.z-exp.com/','alphabet':'yamaxunzhongguo','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'锦程物流','id':'jinchengwuliu','tel':'400-020-5556','website':'http://www.jc56.com/','alphabet':'jinchengwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'景光物流','id':'jgwl','tel':'400-700-1682','website':'http://www.jgwl.cn/','alphabet':'jingguangwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'御风速运','id':'yufeng','tel':'400-611-3348','website':'http://www.shyfwl.cn/','alphabet':'yufengsuyun','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'至诚通达快递','id':'zhichengtongda','tel':'400-151-8918','website':'http://www.zctdky.com/','alphabet':'zhichengtongdakuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'日益通速递','id':'rytsd','tel':'400-041-5858','website':'http://www.rytbj.com/','alphabet':'riyitongsudi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'航宇快递','id':'hangyu','tel':'021-54478850','website':'http://www.hyexp.cn/','alphabet':'hangyukuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'急顺通','id':'pzhjst','tel':'0812-6688669','website':'http://www.pzhjst.com/','alphabet':'jishuntong','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'优速通达','id':'yousutongda','tel':'400-651-8331','website':'http://www.yousutongda8.com/','alphabet':'yousutongda','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'秦远物流','id':'qinyuan','tel':'09-8372888','website':'http://www.chinz56.co.nz/','alphabet':'qinyuanwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'澳邮中国快运','id':'auexpress','tel':'130 007 9988,+612 8774 3','website':'http://www.auexpress.com.au/','alphabet':'aoyouzhongguokuaiyun','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'众辉达物流','id':'zhdwl','tel':'400-622-6193','website':'http://www.zhdpt.com/','alphabet':'zhonghuidawuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'飞邦快递','id':'fbkd','tel':'400-016-8756','website':'http://www.fbkd.net/','alphabet':'feibangkuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'华达快运','id':'huada','tel':'400-895-1110','website':'http://www.zz-huada.com/','alphabet':'huadakuaiyun','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'FOX国际快递','id':'fox','tel':'400-965-8885','website':'http://www.foxglobal.nl/','alphabet':'foxguojikuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'环球速运','id':'huanqiu','tel':'139-1076-0364','website':'http://www.pantoscn.com/','alphabet':'huanqiusuyun','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'辉联物流','id':'huilian','tel':'139-1076-0364','website':'http://www.pantoscn.com/','alphabet':'huilianwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'A2U速递','id':'a2u','tel':'03 9877 4330','website':'http://www.a2u.com.au/','alphabet':'a2usudi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'UEQ快递','id':'ueq','tel':'020-37639835','website':'http://www.ueq.com/','alphabet':'ueqkuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中加国际快递','id':'scic','tel':'(604) 207-0338','website':'http://scicglobal.com/','alphabet':'zhongjiaguojikuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'易达通','id':'yidatong','tel':'','website':'','alphabet':'yidatong','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'宜送','id':'yisong','tel':'','website':'http://www.yi-express.com/','alphabet':'yisong','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'捷网俄全通','id':'ruexp','tel':'4007287156','website':'http://www.ruexp.cn/','alphabet':'jiewangequantong','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'华通务达物流','id':'htwd','tel':'0351-5603868','website':'http://www.htwd56.com/','alphabet':'huatongwudawuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'申必达','id':'speedoex','tel':'713-482-1198','website':'http://www.speedoex.com/cn','alphabet':'shenbida','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'联运快递','id':'lianyun','tel':'(02) 8541 8607','website':'http://121.40.93.72/','alphabet':'lianyunkuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'捷安达','id':'jieanda','tel':'03 9544 8304','website':'http://www.giantpost.com.au/','alphabet':'jieanda','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'SHL畅灵国际物流','id':'shlexp','tel':'400-098-5066','website':'http://www.shlexp.com/','alphabet':'shlchanglingguojiwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'EWE全球快递','id':'ewe','tel':'+61 2 9644 2648','website':'https://www.ewe.com.au','alphabet':'ewequanqiukuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'全球快运','id':'abcglobal','tel':'626-363-4161','website':'http://www.abcglobalexpress.com/','alphabet':'quanqiukuaiyun','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'芒果速递','id':'mangguo','tel':'','website':'http://mangoex.cn/','alphabet':'mangguosudi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'金海淘','id':'goldhaitao','tel':'626-330-7733','website':'http://www.goldhaitao.us/','alphabet':'jinhaitao','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'极光转运','id':'jiguang','tel':'0755-86535662','website':'http://www.jiguangus.com/','alphabet':'jiguangzhuanyun','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'富腾达国际货运','id':'ftd','tel':'09-4432342','website':'http://www.ftdlogistics.co.nz/','alphabet':'futengdaguojihuoyun','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'DCS','id':'dcs','tel':'400-678-0856','website':'http://www.dcslogistics.us/','alphabet':'dcs','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'成达国际速递','id':'chengda','tel':'00852-56078835','website':'http://www.chengda-express.com/','alphabet':'chengdaguojisudi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中环快递','id':'zhonghuan','tel':'400-007-9988','website':'http://www.zhexpress.com.au/','alphabet':'zhonghuankuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'顺邦国际物流','id':'shunbang','tel':'95040391701','website':'http://shunbangus.com/','alphabet':'shunbangguojiwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'启辰国际速递','id':'qichen','tel':'','website':'http://www.qichen.hk/','alphabet':'qichenguojisudi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'澳货通','id':'auex','tel':'','website':'','alphabet':'aohuotong','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'澳速物流','id':'aosu','tel':'','website':'','alphabet':'aosuwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'澳世速递','id':'aus','tel':'','website':'','alphabet':'aoshisudi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'当当','id':'dangdang','tel':'','website':'','alphabet':'dangdang','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'天马迅达','id':'tianma','tel':'','website':'http://www.expresstochina.com/','alphabet':'tianmaxunda','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'美龙快递','id':'mjexp','tel':'323-208-9848','website':'http://www.mjexp.com/','alphabet':'meilongkuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'唯品会(vip)','id':'vipshop','tel':'','website':'','alphabet':'weipinhui(vip)','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'香港骏辉物流','id':'chunfai','tel':'','website':'http://www.chunfai.hk/','alphabet':'xianggangjunhuiwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'三三国际物流','id':'zenzen','tel':'','website':'http://zenzen.hk/','alphabet':'sansanguojiwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'淼信快递','id':'mxe56','tel':'','website':'','alphabet':'miaoxinkuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'海派通','id':'hipito','tel':'021-54723815','website':'http://www.hipito.com/','alphabet':'haipaitong','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'国美','id':'gome','tel':'','website':'','alphabet':'guomei','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'鹏程快递','id':'pengcheng','tel':'0800-166-188','website':'http://www.pcexpress.co.nz/','alphabet':'pengchengkuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'冠庭国际物流','id':'guanting','tel':'00852-2318 1213','website':'http://www.quantiumsolutions.com/hk-sc/','alphabet':'guantingguojiwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'1号店','id':'yhdshop','tel':'','website':'','alphabet':'1haodian','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'金岸物流','id':'jinan','tel':'626-818-2750','website':'http://www.gpl-express.com/','alphabet':'jinanwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'海带宝','id':'haidaibao','tel':'400-825-8585','website':'http://www.haidaibao.com/','alphabet':'haidaibao','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'澳通华人物流','id':'cllexpress','tel':'61 8 9457 9339','website':'http://www.cllexpress.com.au/','alphabet':'aotonghuarenwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'斑马物流','id':'banma','tel':'','website':'http://www.360zebra.com/','alphabet':'banmawuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'友家速递','id':'youjia','tel':'','website':'','alphabet':'youjiasudi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'百通物流','id':'buytong','tel':'','website':'http://www.buytong.cn/','alphabet':'baitongwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'新元快递','id':'xingyuankuaidi','tel':'','website':'','alphabet':'xinyuankuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'amazon-国内订单','id':'amcnorder','tel':'','website':'','alphabet':'amazon-guoneidingdan','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'全速物流','id':'quansu','tel':'400-679-3883','website':'http://www.china-quansu.com/','alphabet':'quansuwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'新杰物流','id':'sunjex','tel':'','website':'http://www.sunjex.com/','alphabet':'xinjiewuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'鲁通快运','id':'lutong','tel':'400-055-5656','website':'http://www.lutongky.com/','alphabet':'lutongkuaiyun','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'新元国际','id':'xynyc','tel':'','website':'','alphabet':'xinyuanguoji','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'小C海淘','id':'xiaocex','tel':'400-108-3006','website':'http://www.xiaocex.com','alphabet':'xiaochaitao','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'航空快递','id':'airgtc','tel':'18640151012','website':'http://air-gtc.com/','alphabet':'hangkongkuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'叮咚澳洲转运','id':'dindon','tel':'010-57853244','website':'http://www.dindonexpress.com/','alphabet':'dingdongaozhouzhuanyun','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'环球通达','id':'hqtd','tel':'400-078-1805','website':'http://www.hqtdkd.com/','alphabet':'huanqiutongda','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'小米','id':'xiaomi','tel':'','website':'','alphabet':'xiaomi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'顺丰优选','id':'sfbest','tel':'','website':'','alphabet':'shunfengyouxuan','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'好又快物流','id':'haoyoukuai','tel':'400-800-3838','website':'http://www.ff56.com.cn/','alphabet':'haoyoukuaiwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'永旺达快递','id':'yongwangda','tel':'400-0607-290','website':'http://www.yongwangda8.com/','alphabet':'yongwangdakuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'木春货运','id':'mchy','tel':'400-6359-800','website':'http://www.mcchina-express.com/','alphabet':'muchunhuoyun','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'程光快递','id':'flyway','tel':'6499482780','website':'http://www.flyway.co.nz','alphabet':'chengguangkuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'全之鑫物流','id':'qzx56','tel':'400-080-5658','website':'http://www.qzx56.com/','alphabet':'quanzhixinwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'百事亨通','id':'bsht','tel':'400-185-6666','website':'http://www.bsht-express.com','alphabet':'baishihengtong','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'ILYANG','id':'ilyang','tel':'','website':'http://www.ilyangexpress.com/','alphabet':'ilyang','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'先锋快递','id':'xianfeng','tel':'0311-69046652','website':'http://xianfengex.com/','alphabet':'xianfengkuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'万家通快递','id':'timedg','tel':'','website':'','alphabet':'wanjiatongkuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'美快国际物流','id':'meiquick','tel':'020-39141092','website':'http://www.meiquick.com/','alphabet':'meikuaiguojiwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'泰中物流','id':'tny','tel':'','website':'','alphabet':'taizhongwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'美通','id':'valueway','tel':'','website':'','alphabet':'meitong','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'新速航','id':'sunspeedy','tel':'852-64797448','website':'http://www.sunspeedy.hk','alphabet':'xinsuhang','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'速方','id':'bphchina','tel':'','website':'','alphabet':'sufang','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'英超物流','id':'yingchao','tel':'(+44)01213680088','website':'http://www.51parcel.com/','alphabet':'yingchaowuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'阿根廷(Correo Argentina)','id':'correoargentino','tel':'','website':'','alphabet':'agenting(correo argentina)','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'瓦努阿图(Vanuatu Post)','id':'vanuatu','tel':'','website':'','alphabet':'wanuatu(vanuatu post)','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'巴巴多斯(Barbados Post)','id':'barbados','tel':'','website':'','alphabet':'babaduosi(barbados post)','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'萨摩亚(Samoa Post)','id':'samoa','tel':'','website':'','alphabet':'samoya(samoa post)','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'斐济(Fiji Post)','id':'fiji','tel':'','website':'','alphabet':'feiji(fiji post)','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'益递物流','id':'edlogistics','tel':'021-64050106','website':'http://www.ed-logistics.net','alphabet':'zengyiwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中外运','id':'esinotrans','tel':'','website':'','alphabet':'zhongwaiyun','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'跨畅(直邮易)','id':'kuachangwuliu','tel':'4000381917,020-38937441','website':'http://www.zhiyouyi.xin','alphabet':'kuachang(zhiyouyi)','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中澳速递','id':'cnausu','tel':'','website':'','alphabet':'zhongaosudi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'联合快递','id':'gslhkd','tel':'','website':'','alphabet':'lianhekuaidi','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'河南次晨达','id':'ccd','tel':'400-003-3506','website':'http://www.ccd56.com','alphabet':'henancichenda','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'奔腾物流','id':'benteng','tel':'0531-89005678','website':'http://www.benteng56.com','alphabet':'bentengwuliu','avatar':'#E91E63'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'今枫国际快运','id':'mapleexpress','tel':'','website':'','alphabet':'jinfengguojikuaiyun','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中运全速','id':'topspeedex','tel':'010-65175288,010-65175388','website':'http://www.topspeedex.com.cn','alphabet':'zhongyunquansu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'宜家行','id':'yjxlm','tel':'','website':'','alphabet':'yijiaxing','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中欧快运','id':'otobv','tel':'088-188-8989','website':'http://www.otobv.com','alphabet':'zhongoukuaiyun','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'金马甲','id':'jmjss','tel':'028-87058515','website':'http://www.jmjss.com','alphabet':'jinmajia','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'一号仓','id':'onehcang','tel':'0755-89391959,0755-893913','website':'http://www.1hcang.com','alphabet':'yihaocang','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'和丰同城','id':'hfwuxi','tel':'0510-82863199','website':'http://www.hfwuxi.com/','alphabet':'hefengtongcheng','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'威时沛运货运','id':'wtdchina','tel':'','website':'','alphabet':'weishipeiyunhuoyun','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'顺捷达','id':'shunjieda','tel':'','website':'','alphabet':'shunjieda','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'千顺快递','id':'qskdyxgs','tel':'4000-444-668','website':'http://www.qskdyxgs.com','alphabet':'qianshunkuaidi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'天联快运','id':'tlky','tel':'400-133-5256','website':'http://www.tl5256.com','alphabet':'tianliankuaiyun','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'CE易欧通国际速递','id':'cloudexpress','tel':'+31 367851419,+31 6280884','website':'','alphabet':'ceyioutongguojisudi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'行必达','id':'speeda','tel':'','website':'','alphabet':'xingbida','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'中通国际','id':'zhongtongguoji','tel':'','website':'','alphabet':'zhongtongguoji','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'西邮寄','id':'xipost','tel':'400-0911-882','website':'http://www.xipost.com','alphabet':'xiyouji','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'NLE','id':'','tel':'nle','website':'','alphabet':'nle','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'亚欧专线','id':'nlebv','tel':'+31 88 668 1277','website':'http://www.euasia.eu/','alphabet':'yaouzhuanxian','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'顺通快递','id':'stkd','tel':'400-113-8789','website':'http://www.st-kd.com','alphabet':'shuntongkuaidi','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'信联通','id':'sinatone','tel':'4008-290-296','website':'http://www.sinatone.com','alphabet':'xinliantong','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'澳德物流','id':'auod','tel':'','website':'','alphabet':'aodewuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'德方物流','id':'ahdf','tel':'055165883415','website':'http://www.ahdf56.com','alphabet':'defangwuliu','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'微转运','id':'wzhaunyun','tel':'4007883376','website':'http://www.wzhuanyun.com','alphabet':'weizhuanyun','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'沈阳特急送','id':'lntjs','tel':'','website':'','alphabet':'shenyangtejisong','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'iExpress','id':'iexpress','tel':'','website':'','alphabet':'iexpress','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'BCWELT','id':'bcwelt','tel':'','website':'','alphabet':'bcwelt','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'欧亚专线','id':'euasia','tel':'','website':'','alphabet':'ouyazhuanxian','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'远成快运','id':'ycgky','tel':'','website':'','alphabet':'yuanchengkuaiyun','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'凡客订单','id':'vancl','tel':'400-600-6888','website':'http://www.vancl.com/','alphabet':'fankepeisong(zuofei)','avatar':'#FFC107'}");
        rlm.createOrUpdateObjectFromJson(Company.class, "{'name':'运通中港','id':'ytkd','tel':'','website':'','alphabet':'yuntongzhonggangkuaidi(zuofei)','avatar':'#FFC107'}");
        rlm.commitTransaction();
        rlm.close();

    }

    @Override
    public Observable<List<Company>> searchCompanies(@NonNull final String keyWords) {

        return Observable.fromCallable(new Callable<List<Company>>() {
            @Override
            public List<Company> call() throws Exception {
                Realm rlm = RealmHelper.newRealmInstance();
                List<Company> companies = rlm.copyFromRealm(rlm.where(Company.class)
                        .like(
                                "name",
                                "*" + keyWords + "*",
                                Case.INSENSITIVE)
                        .or()
                        .like("tel", "*" + keyWords + "*", Case.INSENSITIVE)
                        .or()
                        .like("website", "*" + keyWords + "*", Case.INSENSITIVE)
                        .or()
                        .like("alphabet", "*" + keyWords + "*", Case.INSENSITIVE)
                        .findAllSorted("alphabet", Sort.ASCENDING));
                rlm.close();
                return companies;
            }
        });
    }

}
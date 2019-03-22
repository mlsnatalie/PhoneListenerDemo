# PhoneListenerDemo
监听电话的状态




1. 开发中遇到的来去电电话监听，可以监听电话挂断，通话，响铃等的状态。

来电监听是使用PhoneStateListener类，使用方式是，将PhoneStateListener对象（一般是自己继承PhoneStateListener类完成一些封装）注册到系统电话管理服务中去（TelephonyManager）

　　然后通过PhoneStateListener的回调方法onCallStateChanged(int state, String incomingNumber) 实现来电的监听

TelephonyManager tm=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

2.具体的代码如下

class MainActivity : AppCompatActivity() {

    private var isRegisterPhoneState: Boolean = false
    private val tag ="PhoneHelper"
    private var telephonyManager: TelephonyManager? = null
    private val phoneStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            super.onCallStateChanged(state, phoneNumber)

            when(state) {
                // 电话挂断
                TelephonyManager.CALL_STATE_IDLE -> Log.d(tag, "电话挂断...")
                //电话通话的状态
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    Log.d(tag, "正在通话...")
//                    listener.onCallRinging()
                }
                //电话响铃的状态
                TelephonyManager.CALL_STATE_RINGING -> Log.d(tag, "电话响铃")
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        btn_bind_phone.setOnClickListener { startListener() }
        btn_unbind_phone.setOnClickListener { stopListener() }
    }

    private fun startListener() {
        registerPhoneStateListener()
    }

    private fun stopListener() {
        unregisterPhoneStateListener()
    }

    private fun registerPhoneStateListener() {
        if (!isRegisterPhoneState) {

            telephonyManager?.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
            isRegisterPhoneState = true

        }
    }

    private fun unregisterPhoneStateListener() {
        telephonyManager?.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        isRegisterPhoneState = false
    }
}

3. 设置权限

<!--允许读取电话状态SIM的权限-->
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />



4.关于TelephonyManager这个类，具体的了解可以参考https://www.jianshu.com/p/4317d60a90fd

TelephonyManager类一些简单的方法

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends Activity {
    private TelephonyManager tm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        /**
         * 获取当前设备的位置
         */
        tm.getCellLocation().toString();

        /**
         * 获取数据连接状态
         *
         * DATA_CONNECTED 数据连接状态：已连接
         * DATA_CONNECTING 数据连接状态：正在连接
         * DATA_DISCONNECTED 数据连接状态：断开
         * DATA_SUSPENDED 数据连接状态：暂停
         */
        tm.getDataState();

        /**
         * 返回唯一的设备ID
         * 如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID；如果设备ID是不可用的返回null
         */
        tm.getDeviceId();

        /**
         * 返回设备的软件版本号
         * 例如：GSM手机的IMEI/SV码，如果软件版本是返回null，如果不可用返回null
         */
        tm.getDeviceSoftwareVersion();

        /**
         * 返回手机号码
         * 对于GSM网络来说即MSISDN，如果不可用返回null
         */
        tm.getLine1Number();

        /**
         * 返回当前设备附近设备的信息
         */
        List<NeighboringCellInfo> infos = tm.getNeighboringCellInfo();
        for (NeighboringCellInfo info : infos) {
            //获取邻居小区号
            int cid = info.getCid();
            //获取邻居小区LAC，LAC: 位置区域码。为了确定移动台的位置，每个GSM/PLMN的覆盖区都被划分成许多位置区，LAC则用于标识不同的位置区。
            info.getLac();
            info.getNetworkType();
            info.getPsc();
            //获取邻居小区信号强度
            info.getRssi();
        }

        /**
         * 返回ISO标准的国家码，即国际长途区号
         */
        tm.getNetworkCountryIso();

        /**
         * 返回MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI)
         */
        tm.getNetworkOperator();

        /**
         * 返回移动网络运营商的名字(SPN)
         */
        tm.getNetworkOperatorName();

        /**
         * 获取网络类型
         *
         * NETWORK_TYPE_CDMA 网络类型为CDMA
         * NETWORK_TYPE_EDGE 网络类型为EDGE
         * NETWORK_TYPE_EVDO_0 网络类型为EVDO0
         * NETWORK_TYPE_EVDO_A 网络类型为EVDOA
         * NETWORK_TYPE_GPRS 网络类型为GPRS
         * NETWORK_TYPE_HSDPA 网络类型为HSDPA
         * NETWORK_TYPE_HSPA 网络类型为HSPA
         * NETWORK_TYPE_HSUPA 网络类型为HSUPA
         * NETWORK_TYPE_UMTS 网络类型为UMTS
         *
         * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
         */
        tm.getNetworkType();

        /**
         * 返回设备的类型
         *
         * PHONE_TYPE_CDMA 手机制式为CDMA，电信
         * PHONE_TYPE_GSM 手机制式为GSM，移动和联通
         * PHONE_TYPE_NONE 手机制式未知
         */
        tm.getPhoneType();

        /**
         * 返回SIM卡提供商的国家代码
         */
        tm.getSimCountryIso();

        /**
         * 返回MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI)
         */
        tm.getSimOperator();

        /**
         * 返回服务提供者的名称(SPN)
         */
        tm.getSimOperatorName();

        /**
         * 返回SIM卡的序列号(IMEI),如果是返回null为不可用。
         */
        tm.getSimSerialNumber();

        /**
         * 返回一个常数表示默认的SIM卡的状态。
         *
         * SIM_STATE_ABSENT SIM卡未找到
         * SIM_STATE_NETWORK_LOCKED SIM卡网络被锁定，需要Network PIN解锁
         * SIM_STATE_PIN_REQUIRED SIM卡PIN被锁定，需要User PIN解锁
         * SIM_STATE_PUK_REQUIRED SIM卡PUK被锁定，需要User PUK解锁
         * SIM_STATE_READY SIM卡可用
         * SIM_STATE_UNKNOWN SIM卡未知
         */
        tm.getSimState();

        /**
         * 返回唯一的用户ID,例如,IMSI为GSM手机。
         */
        tm.getSubscriberId();

        /**
         * 获取语音信箱号码关联的字母标识
         */
        tm.getVoiceMailAlphaTag();

        /**
         * 返回语音邮件号码
         */
        tm.getVoiceMailNumber();

        /**
         * 返回手机是否处于漫游状态
         */
        tm.isNetworkRoaming();

        ((TextView) findViewById(R.id.tv_info)).setText(getInfo());
    }

    public String getInfo() {
        String info = "获取设备编号: " + tm.getDeviceId();
        info += "\n获取SIM卡提供商的国家代码: " + tm.getSimCountryIso();
        info += "\n获取SIM卡序列号: " + tm.getSimSerialNumber();
        info += "\n获取网络运营商代号: " + tm.getNetworkOperator();
        info += "\n获取网络运营商名称: " + tm.getNetworkOperatorName();
        info += "\n获取设备当前位置: " + tm.getCellLocation();
        info += "\n获取手机类型: " + tm.getPhoneType();
        info += "\n手机号码: " + tm.getLine1Number();
        info += "\n国际长途区号: " + tm.getNetworkCountryIso();
        info += "\n获取网络类型: " + tm.getNetworkType();
        info += "\n获取数据连接状态: " + tm.getDataState();
        return info;
    }
}



下面一些解释：
(1)IMSI是国际移动用户识别码的简称(International Mobile Subscriber Identity)
IMSI共有15位，其结构如下：
MCC+MNC+MIN
MCC：Mobile Country Code，移动国家码，共3位，中国为460;
MNC:Mobile NetworkCode，移动网络码，共2位
在中国，移动的代码为电00和02，联通的代码为01，电信的代码为03
合起来就是（也是Android手机中APN配置文件中的代码）：
中国移动：46000 46002
中国联通：46001
中国电信：46003

(2)IMEI是International Mobile Equipment Identity （国际移动设备标识）的简称
IMEI由15位数字组成的”电子串号”，它与每台手机一一对应，而且该码是全世界唯一的
其组成为：
1. 前6位数(TAC)是”型号核准号码”，一般代表机型
2. 接着的2位数(FAC)是”最后装配号”，一般代表产地
3. 之后的6位数(SNR)是”串号”，一般代表生产顺序号
4. 最后1位数(SP)通常是”0″，为检验码，目前暂备用




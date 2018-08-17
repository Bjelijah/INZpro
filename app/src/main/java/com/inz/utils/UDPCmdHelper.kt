package com.inz.utils

import android.util.Log
import com.howellsdk.utils.RxUtil
import com.howellsdk.utils.ThreadUtil
import com.inz.action.Config
import java.io.IOException
import java.net.*

/**
 * udp 通信 命令
 */
object UDPCmdHelper {



    fun setSSID(ssid:String):ByteArray = "nvram_set 2860 SSID1 $ssid\n".toByteArray()
    fun getSSID():ByteArray = "nvram_get 2860 SSID1\n".toByteArray()
    fun setAuthMode(authMode:String):ByteArray = "nvram_set 2860 AuthMode $authMode\n".toByteArray()
    fun setEncrypType(encrypType:String):ByteArray = "nvram_set 2860 EncrypType $encrypType\n".toByteArray()
    fun setPassword(pwd:String):ByteArray = "nvram_set 2860 WPAPSK1 $pwd\n".toByteArray()
    fun getAuthMod():ByteArray = "nvram_get 2860 AuthMode\n".toByteArray()
    fun getEncrypType() = "nvram_get 2860 EncrypType\n".toByteArray()
    fun getPassword() = "nvram_get 2860 WPAPSK1\n".toByteArray()
    fun setHideSSID():ByteArray = "nvram_set 2860 HideSSID 1\n".toByteArray()
    fun getHideSSID():ByteArray = "nvram_get 2860 HideSSID\n".toByteArray()
    fun getMacIp():ByteArray = "ifconfig\n".toByteArray()
    fun getGatewayMacIp():ByteArray = "ifconfig eth2.2\n".toByteArray()
    fun setGatewayIp(ip:String,mask:String):ByteArray="ifconfig eth2.2 $ip netmask $mask\n".toByteArray()
    fun getBrIp():ByteArray="ifconfig br0\n".toByteArray()
    fun setBrIp(ip:String,mask:String):ByteArray="ifconfig br0 $ip netmask $mask\n".toByteArray()
    fun setLanIp(ip:String):ByteArray="nvram_set 2860 lan_ipaddr $ip\n".toByteArray()
    fun getLanIp():ByteArray="nvram_get 2860 lan_ipaddr\n".toByteArray()
    fun setLanMask(mask:String):ByteArray = "nvram_set 2860 lan_netmask $mask\n".toByteArray()
    fun getLanMask():ByteArray="nvram_get 2860 lan_netmask\n".toByteArray()
    fun openDHCP():ByteArray="nvram_set 2860 dhcpEnabled 1\n".toByteArray()
    fun closeDHCP():ByteArray="nvram_set 2860 dhcpEnabled 0\n".toByteArray()
    fun getDHCP():ByteArray="nvram_get 2860 dhcpEnabled\n".toByteArray()
    fun setDHCPStart(ip:String):ByteArray = "nvram_set 2860 dhcpStart $ip\n".toByteArray()
    fun getDHCPStart():ByteArray="nvram_get 2860 dhcpStart\n".toByteArray()
    fun setDHCPEnd(ip:String):ByteArray="nvram_set 2860 dhcpEnd $ip\n".toByteArray()
    fun getDHCPEnd():ByteArray = "nvram_get 2860 dhcpEnd\n".toByteArray()
    fun setDHCPMask(mask:String):ByteArray="nvram_set 2860 dhcpMask $mask\n".toByteArray()
    fun getDHCPMask():ByteArray="nvram_get 2860 dhcpMask\n".toByteArray()
    fun setDHCPGateway(ip:String):ByteArray ="nvram_set 2860 dhcpGateway $ip\n".toByteArray()
    fun getDHCPGateway():ByteArray="nvram_get 2860 dhcpGateway\n".toByteArray()
    fun reboot():ByteArray="reboot\n".toByteArray()
    fun setWANStatic():ByteArray="nvram_set 2860 wanConnectionMode STATIC\n".toByteArray()
    fun setWANDHCP():ByteArray="nvram_set 2860 wanConnectionMode DHCP\n".toByteArray()
    fun getWANMode():ByteArray="nvram_get 2860 wanConnectionMode\n".toByteArray()
    fun setWANIp(ip:String):ByteArray="nvram_set 2860 wan_ipaddr $ip\n".toByteArray()
    fun getWANIp():ByteArray="nvram_get 2860 wan_ipaddr\n".toByteArray()
    fun setWANNetmask(mask:String):ByteArray="nvram_set 2860 wan_netmask $mask\n".toByteArray()
    fun getWANNetmask():ByteArray="nvram_get 2860 wan_netmask\n".toByteArray()
    fun setWANGateway(gateway:String):ByteArray="nvram_set 2860 wan_gateway $gateway\n".toByteArray()
    fun getWANGateway():ByteArray="nvram_get 2860 wan_gateway\n".toByteArray()
    fun setWANDns(dns:String):ByteArray="nvram_set 2860 wan_primary_dns $dns\n".toByteArray()
    fun getWANDns():ByteArray="nvram_get 2860 wan_primary_dns\n".toByteArray()
    fun setWANSecondaryDNS(dns:String):ByteArray="nvram_set 2860 wan_secondary_dns $dns\n".toByteArray()
    fun getWANSecondaryDNS():ByteArray="nvram_get 2860 wan_secondary_dns\n".toByteArray()
    fun setPortEnable():ByteArray="nvram_set 2860 PortForwardEnable 1\n".toByteArray()
    fun setPortDisable():ByteArray="nvram_set 2860 PortForwardEnable 0\n".toByteArray()
    fun getPortEnable():ByteArray="nvram_get 2860 PortForwardEnable\n".toByteArray()
    fun getPortRules():ByteArray="nvram_get 2860 PortForwardRules\n".toByteArray()

    var mSocket:DatagramSocket ?= null
    var mFinish = false
    fun init(ip:String,port:Int){
        mFinish = false
        mSocket = DatagramSocket(port)

        receMsg()

    }

    fun deInit(){
        mFinish = true
        mSocket?.close()
    }

    private fun receMsg(){
        var bs = ByteArray(255)
        ThreadUtil.cachedThreadStart {
            while (!mFinish){
                try{
                    var packet = DatagramPacket(bs,bs.size)
                    mSocket?.receive(packet)
                    Log.i("123","rec size= ${packet.length}  data=${String(packet.data,0,packet.length)}")
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }

    }

    fun sendMsg(msg:ByteArray,ip:String ,port:Int){
//        UdpTest.sendTest(msg)
        Log.i("123"," sendMsg    ip=$ip")
        Thread(Runnable {
            try {
                val socket = DatagramSocket(port)
                val serverAddress = InetAddress.getByName(ip)
                val packet = DatagramPacket(msg, msg.size, serverAddress, port)
                socket.send(packet)
                socket.close()
            } catch (e: SocketException) {
                e.printStackTrace()
            } catch (e: UnknownHostException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }

}
package com.inz.utils

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



}
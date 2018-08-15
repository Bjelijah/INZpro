package com.inz.model.net

import android.util.Log
import com.howellsdk.utils.RxUtil
import com.howellsdk.utils.ThreadUtil
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException

class UdpMgr {

    companion object {
        private var mInstance:UdpMgr?=null
        fun getInstance():UdpMgr{
            if (mInstance==null){
                mInstance = UdpMgr()
            }
            return mInstance!!
        }
    }
    private constructor()

    var mSocket: DatagramSocket?    = null
    fun sendMsg(msg:ByteArray,ip:String,port: Int,
                onRes:(Boolean,String)->Unit,
                onTimeout:()->Unit,
                onError:()->Unit){
        ThreadUtil.cachedThreadStart {
            try{

                val address =  InetAddress.getByName(ip)
                if (mSocket!=null){
                    mSocket?.disconnect()
                    mSocket?.close()
                    mSocket=null
                }
                mSocket =  DatagramSocket(port)
                mSocket?.soTimeout = 2*1000
                val bs = ByteArray(255)
                val sendPacket = DatagramPacket(msg, msg.size, address, port)
                val recvPacket = DatagramPacket(bs,bs.size)
                Log.i("123","send data=${String(msg,0,msg.size)}")
                mSocket?.send(sendPacket)
                mSocket?.receive(recvPacket)
                mSocket?.disconnect()
                mSocket?.close()
                mSocket=null
                var msg = String(recvPacket.data,0,recvPacket.length)
                Log.i("123","rec data=$msg")
                Thread.sleep(5000)
                RxUtil.doInUIThread(object :RxUtil.RxSimpleTask<Void>(){
                    override fun doTask() {
                        onRes(true,msg)
                    }

                })
            }catch(e:SocketTimeoutException){
                mSocket?.disconnect()
                mSocket?.close()
                mSocket=null
                RxUtil.doInUIThread(object :RxUtil.RxSimpleTask<Void>(){
                    override fun doTask() {
                        onTimeout()
                    }
                })
            } catch (e:Exception){
                e.printStackTrace()
                mSocket?.disconnect()
                mSocket?.close()
                mSocket=null
                RxUtil.doInUIThread(object :RxUtil.RxSimpleTask<Void>(){
                    override fun doTask() {
                        onError()
                    }
                })

            }
        }

    }
}
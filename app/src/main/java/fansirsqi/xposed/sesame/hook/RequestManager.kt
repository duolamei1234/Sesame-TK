package fansirsqi.xposed.sesame.hook

import fansirsqi.xposed.sesame.entity.RpcEntity
import fansirsqi.xposed.sesame.util.Log

/**
 * @author Byseven
 * @date 2025/1/6
 * @apiNote
 */
object RequestManager {
    private fun checkResult(result: String, method: String?): String {
        check(!(result.trim { it <= ' ' }.isEmpty())) { "Empty response from RPC method: $method" }
        return result
    }

    @JvmStatic
    fun requestString(rpcEntity: RpcEntity): String {
        val result = safeRequestString(rpcEntity, 3, -1)
        return checkResult(result, rpcEntity.methodName)
    }

    @JvmStatic
    fun requestString(rpcEntity: RpcEntity, tryCount: Int, retryInterval: Int): String {
        val result = safeRequestString(rpcEntity, tryCount, retryInterval)
        return checkResult(result, rpcEntity.methodName)
    }

    @JvmStatic
    fun requestString(method: String?, data: String?): String {
        val result = safeRequestString(method, data)
        return checkResult(result, method)
    }

    @JvmStatic
    fun requestString(method: String?, data: String?, relation: String?): String {
        val result = safeRequestString(method, data, relation)
        return checkResult(result, method)
    }

    @JvmStatic
    fun requestString(method: String?, data: String?, appName: String?, methodName: String?, facadeName: String?): String {
        val result = safeRequestString(method, data, appName, methodName, facadeName)
        return checkResult(result, method)
    }

    @JvmStatic
    fun requestString(method: String?, data: String?, tryCount: Int, retryInterval: Int): String {
        val result = safeRequestString(method, data, tryCount, retryInterval)
        return checkResult(result, method)
    }

    fun requestString(method: String?, data: String?, relation: String?, tryCount: Int, retryInterval: Int): String {
        val result = safeRequestString(method, data, relation, tryCount, retryInterval)
        return checkResult(result, method)
    }

    @JvmStatic
    fun requestObject(rpcEntity: RpcEntity?, tryCount: Int, retryInterval: Int) {
        ApplicationHook.rpcBridge?.requestObject(rpcEntity, tryCount, retryInterval)
    }

    // 安全的请求方法，添加检查和错误处理
private fun safeRequestString(rpcEntity: RpcEntity, tryCount: Int, retryInterval: Int): String {
        return try {
            if (!ApplicationHook.isRpcBridgeInitialized()) {
                Log.error("RequestManager", "RPC系统未初始化 - rpcBridge is null")
                throw IllegalStateException("RPC system not initialized")
            }
            ApplicationHook.rpcBridge!!.requestString(rpcEntity, tryCount, retryInterval) ?: throw NullPointerException("RPC response is null for method: ${rpcEntity.methodName}")
        } catch (e: NullPointerException) {
            Log.error("RequestManager", "NullPointerException in requestString for method ${rpcEntity.methodName}: ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.error("RequestManager", "Exception in requestString for method ${rpcEntity.methodName}: ${e.message}")
            throw e
        }
    }

    private fun safeRequestString(method: String?, data: String?): String {
        return safeRequestString(RpcEntity(method, data), 3, -1)
    }

    private fun safeRequestString(method: String?, data: String?, relation: String?): String {
        return safeRequestString(RpcEntity(method, data, relation), 3, -1)
    }

    private fun safeRequestString(method: String?, data: String?, appName: String?, methodName: String?, facadeName: String?): String {
        return safeRequestString(RpcEntity(method, data, appName, methodName, facadeName), 3, -1)
    }

    private fun safeRequestString(method: String?, data: String?, tryCount: Int, retryInterval: Int): String {
        return safeRequestString(RpcEntity(method, data), tryCount, retryInterval)
    }

    private fun safeRequestString(method: String?, data: String?, relation: String?, tryCount: Int, retryInterval: Int): String {
        return safeRequestString(RpcEntity(method, data, relation), tryCount, retryInterval)
    }
}

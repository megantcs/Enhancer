package ru.megantcs.enhancer;

import ru.megantcs.enhancer.platform.Resolver.ResolverClient;
import ru.megantcs.enhancer.platform.loader.LuaLoader;
import ru.megantcs.enhancer.platform.loader.LuaPreprocessor;
import ru.megantcs.enhancer.platform.toolkit.Fabric.ModEntrypoint;

public class Enhancer extends ModEntrypoint
{
    public Enhancer() {
        super("Enhancer");
    }

    @Override
    public void bootstrap()
    {
        ResolverClient client = new ResolverClient();

        client.register(LuaLoader.class);
        client.initializeAll();

        var ll = client.get(LuaLoader.class);
        ll.useDebug();
        ll.usePreprocessor();

        ll.loadCode("""
                #define DEBUG
                            #define MAX(a, b) (a) > (b) ? (a) : (b)
                
                            #ifdef DEBUG
                            print("Debug mode is ON")
                            #endif
                
                            local x = MAX(10, 20)
                            print("Max: " .. x)
                
                            #define MULTILINE \
                                print("This is") \
                                print("multiline macro")
                
                            MULTILINE
                """, "test");


    }
}

package ru.otus.objectgetsize.impl;

import ru.otus.objectgetsize.ObjectGetSize;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import com.sun.tools.attach.VirtualMachine;

/**
 * Created by sergey on 09.04.17.
 */
public class GetSizeInstrumentation implements ObjectGetSize {

    private static Instrumentation instrumentation;
    private static boolean premainExecuted = false;

    public static void premain(String args, Instrumentation pInstrumentation) {
        System.out.println("premain");
        instrumentation = pInstrumentation;
        premainExecuted = true;
    }

    public static void agentmain(String agentArgs, Instrumentation pInstrumentation) {
        System.out.println("agentmain");
        instrumentation = pInstrumentation;
    }

    private static void load(String thisJarFile) {
        String vmName = ManagementFactory.getRuntimeMXBean().getName();
        String pid = vmName.substring(0, vmName.indexOf('@'));

        try {
            VirtualMachine vm = VirtualMachine.attach(pid);
            vm.loadAgent(thisJarFile);
            vm.detach();
        } catch (Exception e) {
            System.err.println("vmName:" + vmName + ", pid:" + pid);
            throw new RuntimeException(e);
        }
        premainExecuted = true;
        System.out.println("instrumentation loading end");
    }

    @Override
    public long getSize(Object object) {
        if (!premainExecuted) {
            final String thisJarFile = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            if (!thisJarFile.contains("jar")) {
                throw new RuntimeException("Current jar file not found");
            }
            load(thisJarFile);
        }
        if (instrumentation == null) {
            throw new IllegalStateException("Instrumentation is not initialized");
        }
        return instrumentation.getObjectSize(object);
    }
}

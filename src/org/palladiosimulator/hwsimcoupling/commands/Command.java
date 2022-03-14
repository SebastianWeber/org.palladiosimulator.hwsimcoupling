package org.palladiosimulator.hwsimcoupling.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sebastian A command that can be executed with the
 *         {@link org.palladiosimulator.hwsimcoupling.util.impl.CommandExecutor}
 */
public interface Command {

    /**
     * @return a list of strings that can be used as a parameter for
     *         {@link org.palladiosimulator.hwsimcoupling.util.impl.CommandExecutor#executeCommand(List, java.util.function.Consumer, java.util.function.Consumer)}
     */
    public abstract List<String> getCommand();

    /**
     * @return a list of strings representing the shell of the current os
     */
    public default List<String> getShell() {
        List<String> shell = new ArrayList<String>();
        boolean isWindows = System.getProperty("os.name")
            .toLowerCase()
            .startsWith("windows");
        if (isWindows) {
            shell.add("cmd.exe");
            shell.add("/c");
        } else {
            shell.add("sh");
            shell.add("-c");
        }
        return shell;
    }

}

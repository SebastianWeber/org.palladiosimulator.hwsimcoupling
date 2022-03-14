package org.palladiosimulator.hwsimcoupling.commands;

/**
 * @author Sebastian A command to copy one file to a path in a container to the path available at
 *         {@link CopyCommand#getDestination()}
 */
public interface CopyCommand extends Command {

    /**
     * @return the absolute path where the file has been copied to
     */
    public String getDestination();

}

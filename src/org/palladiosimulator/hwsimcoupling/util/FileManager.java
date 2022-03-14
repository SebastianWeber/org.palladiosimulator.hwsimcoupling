package org.palladiosimulator.hwsimcoupling.util;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Sebastian Copies one or multiple files to a container
 */
public interface FileManager {

    /**
     * Copy files from the paramterMap with the
     * {@link org.palladiosimulator.hwsimcoupling.commands.CopyCommand} from the
     * {@link CommandHandler}
     * 
     * @param parameterMap
     * @param commandHandler
     * @return the paramterMap with adapted file paths
     */
    public Map<String, Serializable> copyFiles(Map<String, Serializable> parameterMap, CommandHandler commandHandler);

    /**
     * Compute or get the hash values from the files supposed to be copied
     * 
     * @param parameterMap
     * @param hashValues
     *            already computed hash values
     * @return hash values from the files supposed to be copied
     */
    public Map<String, String> getHashValuesFromFiles(Map<String, Serializable> parameterMap,
            Map<String, String> hashValues);

    /**
     * @author Sebastian Paths processed by the {@link FileManager} are prefixed by "local:" means
     *         the path is relative to a project root with which it begins "absolute:" means the
     *         path is absolute, i.e. /home/user/... or C:/User/...
     */
    public enum LOCATIONS {

        LOCAL("local:"), ABSOLUTE("absolute:");

        private final String keyword;

        private LOCATIONS(String keyword) {
            this.keyword = keyword;
        }

        public String toString() {
            return keyword;
        }

        public static String getLocationsString() {
            return "\"" + LOCATIONS.ABSOLUTE.toString() + "\" or \"" + LOCATIONS.LOCAL.toString() + "\"";
        }

    }

}

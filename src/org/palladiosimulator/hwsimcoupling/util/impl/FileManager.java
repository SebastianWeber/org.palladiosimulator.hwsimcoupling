package org.palladiosimulator.hwsimcoupling.util.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.palladiosimulator.hwsimcoupling.commands.CopyCommand;
import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;
import org.palladiosimulator.hwsimcoupling.util.CommandHandler;
import org.palladiosimulator.hwsimcoupling.util.Locations;

public class FileManager {

    /**
     * Copy files from the paramterMap with the
     * {@link org.palladiosimulator.hwsimcoupling.commands.CopyCommand} from the
     * {@link CommandHandler}
     * 
     * @param parameterMap
     * @param commandHandler
     * @return the paramterMap with adapted file paths
     */
    public static Map<String, Serializable> copyFiles(Map<String, Serializable> parameterMap,
            CommandHandler commandHandler) {
        Map<String, Serializable> strippedParameterMap = new HashMap<String, Serializable>();
        for (Entry<String, Serializable> pair : parameterMap.entrySet()) {
            strippedParameterMap.put(pair.getKey(), copyFile(parameterMap, pair, commandHandler));
        }
        return strippedParameterMap;
    }

    /**
     * Compute or get the hash values from the files supposed to be copied
     * 
     * @param parameterMap
     * @param hashValues
     *            already computed hash values
     * @return hash values from the files supposed to be copied
     */
    public static Map<String, String> getHashValuesFromFiles(Map<String, Serializable> parameterMap,
            Map<String, String> hashValues) {
        Map<String, String> newHashValues = new HashMap<String, String>();
        for (Entry<String, Serializable> pair : parameterMap.entrySet()) {
            for (String path : split(pair.getValue()
                .toString())) {
                String[] nameAndPath = getKeyAndStrippedPath(path);
                if (nameAndPath != null) {
                    if (!hashValues.containsKey(nameAndPath[0])) {
                        newHashValues.put(nameAndPath[0], hash(nameAndPath[1]));
                    } else {
                        newHashValues.put(nameAndPath[0], hashValues.get(nameAndPath[0]));
                    }
                }
            }
        }
        return newHashValues;
    }

    private static String[] getKeyAndStrippedPath(String prefixedPath) {
        String name = "";
        String path = "";
        if (prefixedPath.startsWith(Locations.ABSOLUTE.toString())) {
            name = new File(stripPath(prefixedPath)).getName();
            path = stripPath(path);
        } else if (prefixedPath.startsWith(Locations.LOCAL.toString())) {
            IResource resource = ResourcesPlugin.getWorkspace()
                .getRoot()
                .findMember(stripPath(prefixedPath));
            name = resource.getName();
            path = stripPath(resource.getLocation()
                .toPortableString());
        } else {
            return null;
        }
        return new String[] { name + "MD5Hash", path };
    }

    private static String copyFile(Map<String, Serializable> parameterMap, Entry<String, Serializable> pair,
            CommandHandler commandHandler) {
        String paths = pair.getValue()
            .toString();
        String strippedPaths = "";
        for (String path : split(paths)) {
            if (path.startsWith(Locations.ABSOLUTE.toString())) {
                strippedPaths += copyAbsolute(parameterMap, path, commandHandler);
            } else if (path.startsWith(Locations.LOCAL.toString())) {
                strippedPaths += copyLocal(parameterMap, path, commandHandler);
            } else {
                strippedPaths += path;
            }
            strippedPaths += " ";
        }
        return strippedPaths.substring(0, strippedPaths.length() - 1);
    }

    private static List<String> split(String parameters) {
        List<String> parametersSplit = new ArrayList<String>();
        // Split at space but preserve strings with quotation marks
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*")
            .matcher(parameters);
        while (m.find()) {
            // Add strings to the list and remove surrounding quotation marks
            parametersSplit.add(m.group(1)
                .replace("\"", ""));
        }
        return parametersSplit;
    }

    private static String stripPath(String path) {
        if (path.startsWith(Locations.LOCAL.toString())) {
            return path.replaceFirst(Locations.LOCAL.toString(), "");
        } else if (path.startsWith(Locations.ABSOLUTE.toString())) {
            return path.replaceFirst(Locations.ABSOLUTE.toString(), "");
        } else {
            return path;
        }
    }

    private static String hash(String path) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            InputStream fis = new FileInputStream(path);
            byte[] buffer = new byte[1024];
            int nread;
            while ((nread = fis.read(buffer)) != -1) {
                md.update(buffer, 0, nread);
            }
            fis.close();
            StringBuilder result = new StringBuilder();
            for (byte b : md.digest()) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            return "Hash calculation failed with message: " + e.getMessage();
        }
    }

    private static String copyLocal(Map<String, Serializable> parameterMap, String path,
            CommandHandler commandHandler) {
        try {
            IResource resource = ResourcesPlugin.getWorkspace()
                .getRoot()
                .findMember(stripPath(path));
            String sourcePath = resource.getLocation()
                .toPortableString();
            CopyCommand copyCommand = commandHandler.getCopyCommand(parameterMap, sourcePath);
            CommandExecutor.executeCommand(copyCommand, commandHandler.getOutputConsumer(),
                    commandHandler.getErrorConsumer());
            return copyCommand.getDestination();
        } catch (IOException | InterruptedException e) {
            throw new DemandCalculationFailureException("Failed to copy file " + path + ": " + e.getMessage());
        }
    }

    private static String copyAbsolute(Map<String, Serializable> parameterMap, String path,
            CommandHandler commandHandler) {
        try {
            CopyCommand copyCommand = commandHandler.getCopyCommand(parameterMap, stripPath(path));
            CommandExecutor.executeCommand(copyCommand, commandHandler.getOutputConsumer(),
                    commandHandler.getErrorConsumer());
            return copyCommand.getDestination();
        } catch (IOException | InterruptedException e) {
            throw new DemandCalculationFailureException("Failed to copy file " + path + ": " + e.getMessage());
        }
    }

}

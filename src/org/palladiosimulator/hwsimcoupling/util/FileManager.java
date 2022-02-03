package org.palladiosimulator.hwsimcoupling.util;

public interface FileManager {

	public String sync_file(String path);
	public String[] sync_files(String[] paths);
	public String strip_path(String path);
	
}

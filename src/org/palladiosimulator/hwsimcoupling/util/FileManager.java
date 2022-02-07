package org.palladiosimulator.hwsimcoupling.util;

/**
 * @author Sebastian
 * Copies one or multiple files to a container
 */
public interface FileManager {

	/**
	 * @param source_path
	 * @return the destination path of the given source_path
	 */
	public String copy_file(String source_path);
	
	/**
	 * @param source_paths
	 * @return the destination paths of the given source_paths
	 */
	public String[] copy_files(String[] source_paths);
	
	/**
	 * @param path
	 * @return the path stripped from the keywords of {@link LOCATIONS}
	 */
	public String strip_path(String path);
	
	/**
	 * @author Sebastian
	 * Paths processed by the {@link FileManager} are prefixed by
	 * "local:" means the path is relative to a project root with which it begins
	 * "absolute:" means the path is absolute, i.e. /home/user/... or C:/User/...
	 * "manual:" means the path is managed manually and wont be adapted besides {@link FileManager#strip_path(String)}
	 */
	public enum LOCATIONS {
		
		LOCAL("local:"), ABSOLUTE("absolute:"), MANUAL("manual:");
		
		private final String keyword;
		
		private LOCATIONS(String keyword) {
			this.keyword = keyword;
		}
		
		public String toString() {
			return keyword;
		}
		
		public static String get_locations_string() {
			return "\"" + LOCATIONS.ABSOLUTE.toString() + "\" or \"" + LOCATIONS.LOCAL.toString() + "\" or \"" + LOCATIONS.MANUAL.toString() + "\"";
		}
		
	}
	
}

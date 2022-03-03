package org.palladiosimulator.hwsimcoupling.configuration;

public enum Parameter {

	HWSIM("hwsim"),
	CONTAINERID("containerID"),
	CLASS("class"),
	NAME("name"),
	PROCESSINGRATE("processingrate"),
	PROFILE("profile");

	private String keyword;
	
	private Parameter(String keyword) {
		this.keyword = keyword;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
}

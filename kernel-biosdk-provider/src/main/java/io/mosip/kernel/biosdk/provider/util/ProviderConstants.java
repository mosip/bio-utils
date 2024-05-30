package io.mosip.kernel.biosdk.provider.util;

public class ProviderConstants {
	private ProviderConstants() {
		throw new IllegalStateException("ProviderConstants class");
	}

	public static final String CLASSNAME = "classname";
	public static final String VERSION = "version";
	public static final String ARGUMENTS = "args";
	public static final String THRESHOLD = "threshold";

	public static final String LOGGER_SESSIONID = "BIO-SDK-PROVIDER";
	public static final String LOGGER_IDTYPE = "REGISTRATION / AUTH";
	public static final String LOGGER_EMPTY = "";
}
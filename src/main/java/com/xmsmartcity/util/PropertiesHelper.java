package com.xmsmartcity.util;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesHelper {
	/**
	 * 默认 classLoder 路径下
	 * 
	 * @throws Exception
	 * @param fileName
	 */
	public PropertiesHelper(String fileName){
		try{
			properties = new Properties();
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
			properties.load(in);
			in.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 自己指定路径
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public PropertiesHelper(final String path, final String fileName) throws Exception {
		File file = new File(path + fileName);
		properties = new Properties();
		final FileReader reader = new FileReader(file);
		properties.load(reader);
		reader.close();
	}

	public PropertiesHelper(final Properties properties) {
		this.properties = properties;
	}

	public boolean contains(final Object key) {
		return properties.containsKey(key);
	}

	public <V> V get(final Converter<V> converter, final String key) {
		final String value = properties.getProperty(key);
		if (value == null)
			throw new IllegalArgumentException(key + " is missing.");
		return converter.convert(value.trim());
	}

	public <V> V get(final Converter<V> converter, final String key, final String defaultValue) {
		if (!properties.containsKey(key))
			return converter.convert(defaultValue);
		return converter.convert((properties.getProperty(key).trim()));
	}

	public boolean getBoolean(final String key) {
		final String value = properties.getProperty(key);
		if (value == null)
			throw new IllegalArgumentException(key + " is missing.");
		return parseBoolean(value.trim());
	}

	public boolean getBoolean(final String key, final boolean defaultValue) {
		if (!properties.containsKey(key))
			return defaultValue;
		return parseBoolean(properties.getProperty(key).trim());
	}

	public int getInt(final String key) {
		final String value = properties.getProperty(key);
		if (value == null)
			throw new IllegalArgumentException(key + " is missing.");
		return parseInt(value.trim());
	}

	public int getInt(final String key, final int defaultValue) {
		if (!properties.containsKey(key))
			return defaultValue;
		return parseInt(properties.getProperty(key).trim());
	}

	public long getLong(final String key) {
		final String value = properties.getProperty(key);
		if (value == null)
			throw new IllegalArgumentException(key + " is missing.");
		return parseLong(value.trim());
	}

	public long getLong(final String key, final long defaultValue) {
		if (!properties.containsKey(key))
			return defaultValue;
		return parseLong(properties.getProperty(key).trim());
	}

	public String getString(final String key) {
		final String value = properties.getProperty(key);
		if (value == null || value.length() == 0)
			throw new IllegalArgumentException(key + " is missing.");
		return value.trim();
	}

	public String getString(final String key, final String defaultValue) {
		return properties.getProperty(key, defaultValue).trim();
	}

	private Properties properties;

	public static interface Converter<V> {
		V convert(String value);
	}
	
	
	public String getCHString(final String key) {
		final String value = properties.getProperty(key);
		if (value == null || value.length() == 0)
			throw new IllegalArgumentException(key + " is missing.");
		return value.trim().split(",")[0];
	}
	
	
	public String getENString(final String key) {
		final String value = properties.getProperty(key);
		if (value == null || value.length() == 0)
			throw new IllegalArgumentException(key + " is missing.");
		return value.trim().split(",")[1];
	}
	
}

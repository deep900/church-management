/**
 * 
 */
package com.church.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
public class PropertiesLoader implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private String filePath = "/opt/tomcat/conf/cm/application.properties";

	private HashMap<String, Object> loadPropertiesMap() {
		Properties prop = new Properties();
		HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
		File file = new File(filePath);
		try (FileInputStream fis = new FileInputStream(file)) {
			prop.load(fis);
			prop.keySet().forEach(x -> {
				propertiesMap.put(x.toString(), prop.get(x));
			});
		} catch (FileNotFoundException err) {
			log.error("Error while loading the properties file", err);
		} catch (IOException err) {
			log.error("Error while loading the properties file", err);
		}
		return propertiesMap;
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		log.info("Loading properties file at" + this.getClass().getName());
		applicationContext.getEnvironment().getPropertySources()
				.addLast(new MapPropertySource("application.properties", loadPropertiesMap()));
	}

}

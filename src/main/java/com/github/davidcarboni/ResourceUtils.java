package com.github.davidcarboni;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Utility functions for accessing project resources.
 * 
 * @author David Carboni
 * 
 */
public class ResourceUtils {

	/** The Class on which <code>getResourceAsStream(...)</code> will be called. */
	public static Class<?> classLoaderClass = ResourceUtils.class;

	/**
	 * Get a resource as an {@link InputStream}.
	 * 
	 * @param name
	 *            The name of a resource.
	 * @return An {@link InputStream} for the resource.
	 * @throws IOException
	 *             If an error occurs.
	 */
	public static InputStream getStream(String name) throws IOException {

		// Try a couple of strategies to get the resource:
		InputStream stream = classLoaderClass.getResourceAsStream(name);
		if (stream == null) {
			throw new IOException("Unable to locate resource " + name);
		}

		return stream;
	}

	/**
	 * 
	 * @param name
	 *            The name of a resource.
	 * @return A {@link Reader} for the resource.
	 * @throws IOException
	 *             If an error occurs.
	 */
	public static Reader getReader(String name) throws IOException {
		InputStream input = getStream(name);
		return new InputStreamReader(input, "UTF-8");
	}

	/**
	 * 
	 * @param name
	 *            The name of a resource.
	 * @return A String containing the contents of the resource in UTF-8
	 *         encoding.
	 * @throws IOException
	 *             If an error occurs.
	 */
	public static String getString(String name) throws IOException {
		InputStream input = getStream(name);
		try {
			return IOUtils.toString(input, "UTF-8");
		} catch (IOException e) {
			throw new IOException("Error reading resource to String.", e);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Extracts the named resource as a temporary file. The file is not set to
	 * be deleted on exit, so you are free to move, rename, etc.
	 * 
	 * @param name
	 *            The name of a resource.
	 * @return A temp file containing the contents of the resource.
	 * @throws IOException
	 *             If an error occurs.
	 */
	public static File getFile(String name) throws IOException {
		InputStream input = getStream(name);
		try {
			File file = File.createTempFile("extracted", "resource");
			file.deleteOnExit();
			FileUtils.copyInputStreamToFile(input, file);
			return file;
		} catch (IOException e) {
			throw new IOException("Error copying resource to file.", e);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Extracts the named resource as a temporary file. The file is not set to
	 * be deleted on exit, so you are free to move, rename, etc.
	 * 
	 * @param name
	 *            The name of a resource.
	 * @return A temp file containing the contents of the resource.
	 * @throws IOException
	 *             If an error occurs.
	 */
	public static Path getPath(String name) throws IOException {
		URL url = classLoaderClass.getResource(name);
		try {
			URI uri = url.toURI();
			Path path = Paths.get(uri);
			return path;
		} catch (URISyntaxException e) {
			throw new IOException("Error copying resource to file.", e);
		}
	}

	/**
	 * @param name
	 *            The name of a properties resource.
	 * @return A {@link Properties} instance, populated with the content of the
	 *         resource.
	 * @throws IOException
	 *             If an error occurs in locating the resource or reading the
	 *             stream.
	 */
	public static Properties getProperties(String name) throws IOException {
		// The properties class handles null defaults:
		return getProperties(name, null);
	}

	/**
	 * @param name
	 *            The name of a properties resource.
	 * @param defaults
	 *            A set of default properties. This can be null as
	 *            {@link Properties} handles this.
	 * @return A {@link Properties} instance, populated with the content of the
	 *         resource.
	 * @throws IOException
	 *             If an error occurs in locating the resource or reading the
	 *             stream.
	 */
	public static Properties getProperties(String name, Properties defaults) throws IOException {
		InputStream input = getStream(name);
		try {
			Properties properties = new Properties(defaults);
			properties.load(input);
			return properties;
		} catch (IOException e) {
			throw new IOException("Error reading properties file.", e);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Parses the named resource as an XML {@link Document}.
	 * 
	 * @param name
	 *            The name of a resource.
	 * @return An XML {@link Document}.
	 * @throws IOException
	 *             If an error occurs.
	 */
	public static Document getXml(String name) throws IOException {

		Document result;

		InputStream input = null;
		try {

			// Get an input stream for the resource:
			input = getStream(name);

			// Parse the stream:
			// Adapted from:
			// http://www.java-samples.com/showtutorial.php?tutorialid=152
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilderdocumentBuilder = documentBuilderFactory.newDocumentBuilder();
			result = documentBuilderdocumentBuilder.parse(input);

		} catch (ParserConfigurationException e) {
			throw new IOException("Error reading XML resource: " + name, e);
		} catch (SAXException e) {
			throw new IOException("Error reading XML resource: " + name, e);
		} catch (IOException e) {
			throw new IOException("Error reading XML resource: " + name, e);
		} finally {
			IOUtils.closeQuietly(input);
		}

		return result;
	}
}

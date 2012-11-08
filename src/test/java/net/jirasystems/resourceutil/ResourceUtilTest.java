package net.jirasystems.resourceutil;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ResourceUtilTest {

	@Test
	public void testGetStream() throws IOException {

		// Given
		String name = "/stream.txt";

		// When
		InputStream stream = ResourceUtil.getStream(name);
		stream.close();

		// Then
		assertNotNull(stream);
	}

	@Test(expected = IOException.class)
	public void testGetStreamFail() throws IOException {

		// Given
		String name = "/nonexistent";

		// When
		ResourceUtil.getStream(name);

		// Then
		// We should have an exception.
	}

	@Test
	public void testGetString() throws IOException {

		// Given
		String name = "/quote.txt";
		String expected = "Antoine de Saint-Exupéry, author of The Little Prince, said, "
				+ "\"If you want to build a ship, don't drum up the men to gather wood, divide the "
				+ "work and give orders. Instead, teach them to yearn for the vast and endless sea.\"";

		// When
		String actual = ResourceUtil.getString(name);

		// Then
		assertEquals(expected, actual);
	}

	@Test
	public void testGetFile() throws IOException {

		// Given
		String name = "/holiday11-hp.png";
		InputStream resource = ResourceUtil.getStream(name);
		long expected = checksumCRC32(resource);

		// When
		File file = ResourceUtil.getFile(name);
		long actual = FileUtils.checksumCRC32(file);

		// Then
		assertEquals(expected, actual);
	}

	@Test
	public void testGetXml() throws IOException {

		// Given
		String name = "/note.xml";

		// When
		Document document = ResourceUtil.getXml(name);

		// Then 
		Element firstChild = document.getDocumentElement();
		assertNotNull(firstChild);
		assertEquals("note", firstChild.getNodeName());
		NodeList childNodes = firstChild.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {

			Node node = childNodes.item(i);

			if ("to".equals(node.getNodeName())) {
				assertEquals("Tove", node.getTextContent());

			} else if ("from".equals(node.getNodeName())) {
				assertEquals("Jani", node.getTextContent());

			} else if ("heading".equals(node.getNodeName())) {
				assertEquals("Reminder", node.getTextContent());

			} else if ("body".equals(node.getNodeName())) {
				assertEquals("Don't forget me this weekend!", node.getTextContent());

			} else {
				// An element we're not expecting?
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					fail(node.getNodeName());
				} else if (node.getNodeType() != Node.TEXT_NODE) {
					System.out.println("Found a note of type: " + node.getNodeType());
				}
			}

		}
	}

	@Test(expected = IOException.class)
	public void testGetXmlFail() throws IOException {

		// Given
		String name = "/note_invalid.xml";

		// When
		ResourceUtil.getXml(name);

		// Then 
		// We should have an exception
	}

//	@Test
//	public void testGetCsv() throws IOException {
//
//		// Given
//		String name = "/hens.csv";
//
//		// When
//		List<Map<String, String>> csv = ResourceUtil.getCsv(name);
//
//		// Then
//		assertEquals(4, csv.size());
//		for (Map<String, String> row : csv) {
//
//			if ("Masie".equals(row.get("name"))) {
//
//				assertEquals("Speckled", row.get("breed"));
//				assertEquals("Survivor of a fox attack. Grumpy but a good mother hen.", row.get("notes"));
//
//			} else if ("Savvy".equals(row.get("name"))) {
//
//				assertEquals("Golden", row.get("breed"));
//				assertEquals("Great layer but not terribly bright.", row.get("notes"));
//
//			} else if ("Barley".equals(row.get("name"))) {
//
//				assertEquals("Silverlink", row.get("breed"));
//				assertEquals("Clearly the brains of the flock", row.get("notes"));
//
//			} else if ("Treacle".equals(row.get("name"))) {
//
//				assertEquals("Black", row.get("breed"));
//				assertEquals("Pretty bird with a lovely voice.", row.get("notes"));
//
//			} else {
//				// Something else entirely?
//				fail();
//			}
//		}
//	}

	/**
	 * Calculates a CRC32 value for the given input stream.
	 * 
	 * @param input
	 *            The stream to be read to compute the CRC32. This stream will be closed by this
	 *            method.
	 * @return The CRC32.
	 * @throws IOException .
	 */
	private long checksumCRC32(InputStream input) throws IOException {
		CRC32 crc32 = new CRC32();
		try {
			int b;
			while ((b = input.read()) != -1) {
				crc32.update(b);
			}
		} finally {
			IOUtils.closeQuietly(input);
		}
		return crc32.getValue();
	}

}

[![Build Status](https://travis-ci.org/davidcarboni/resource-utils.png?branch=master)](https://travis-ci.org/davidcarboni/resource-utils)
Resource Utils
----------


### What is it?

Resource Utils provides straightforward access to Java resources in several formats so you don't have to write boilerplate conversions.

To get a resource as `Properties`:

    Properties properties = ResourceUtils.getProperties("/com/github/davidcarboni/app.properties");

To get a resource as an XML `Document`:

    Document document = ResourceUtils.getXml("/com/github/davidcarboni/app.xml");

To get a resource as a `String`:

    String string = ResourceUtils.getString("/com/github/davidcarboni/string.resource");

To extract a resource to a temp `File`:

    File temp = ResourceUtils.getFile("/com/github/davidcarboni/file.resource");

To get a resource as a good old `InputStream`:

    InputStream input = ResourceUtils.getStream("/com/github/davidcarboni/resource.data");


### Maven usage

To use `resource-utils` in your project:

		<dependency>
			<groupId>com.github.davidcarboni</groupId>
			<artifactId>resource-utils</artifactId>
			<version>1.1.0</version>
		</dependency>

		
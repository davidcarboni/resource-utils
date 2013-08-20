[![Build Status](https://travis-ci.org/davidcarboni/resource-utils.png?branch=master)](https://travis-ci.org/davidcarboni/resource-utils)
Resource Utils
----------


### What is it?

Resource Utils provides straightforward access to Java resources in several formats so you don't have to write boilerplate conversions.

To get a resource as `Properties`:

    Properties properties = ResourceUtil.getProperties("/com/github/davidcarboni/application.properties");

To get a resource as an XML `Document`:

    Document document = ResourceUtil.getXml("/com/github/davidcarboni/application.xml");

To get a resource as a `String`:

    String string = ResourceUtil.getString("/com/github/davidcarboni/string.resource");

To extract a resource to a temp `File`:

    File temp = ResourceUtil.getFile("/com/github/davidcarboni/file.resource");

To get a resource as a good old `InputStream`:

    InputStream input = ResourceUtil.getStream("/com/github/davidcarboni/resource.data");


### Maven usage

To use `resource-utils` in your project:

		<dependency>
			<groupId>com.github.davidcarboni</groupId>
			<artifactId>resource-utils</artifactId>
			<version>1.0.7</version>
		</dependency>

		
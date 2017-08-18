package org.diagnoseit.spike.inspectit.trace.importer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.esotericsoftware.kryo.io.Input;

import info.novatec.inspectit.storage.serializer.schema.SchemaManagerTestProvider;
import rocks.inspectit.shared.all.cmr.model.PlatformIdent;
import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;
import rocks.inspectit.shared.all.serializer.SerializationException;
import rocks.inspectit.shared.all.serializer.impl.SerializationManager;
import rocks.inspectit.shared.all.serializer.schema.ClassSchemaManager;
import rocks.inspectit.shared.all.util.KryoNetNetwork;

public class SerializerWrapper extends SerializationManager {

	public SerializerWrapper() throws IOException {
		super();
		ClassSchemaManager schemaManager = SchemaManagerTestProvider.getClassSchemaManagerForTests();

		setSchemaManager(schemaManager);
		setKryoNetNetwork(new KryoNetNetwork());
		initKryo();
	}

	/**
	 * Reads Invocation sequences from the specified source directory or zipped
	 * file.
	 * 
	 * @param source
	 *            directory or file where to read the sequences from
	 * @return {@link InvocationSequences} object
	 * @throws IOException
	 */
	public InvocationSequences readInvocationSequences(final String source) throws IOException {
		File dir = new File(source);
		if (dir.isDirectory()) {
			return readInvocationSequencesFromDir(source);
		}

		InvocationSequences result = new InvocationSequences();

		try (ZipFile zipFile = new ZipFile(source)) {

			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();

				if (!this.isAllowedFile(entry.getName())) {
					continue;
				}

				byte[] bytes = this.getBytesOfStreamWithGZip(zipFile.getInputStream(entry));
				this.storeInvocationSequenceInput(result, bytes);
			}
		}

		return result;
	}

	/**
	 * Reads Invocation sequences from the specified source directory.
	 * 
	 * @param srcDirectory
	 *            directory where to read the sequences from
	 * @return {@link InvocationSequences} object
	 * @throws IOException
	 */
	private InvocationSequences readInvocationSequencesFromDir(final String srcDirectory) throws IOException {
		File dir = new File(srcDirectory);
		if (!dir.isDirectory()) {
			throw new IOException("Path is not a directory!");
		}

		InvocationSequences result = new InvocationSequences();

		for (File file : dir.listFiles()) {
			if(this.isAllowedDatasource(file.getName())) {
				List<InvocationSequenceData> fromOneDir = readInvocationSequences(file.getAbsolutePath()).getInvocationSequences();
				for (InvocationSequenceData invocationSequenceData : fromOneDir) {
					result.addInvocationSequence(invocationSequenceData);
				}
			} else if (!file.isFile() || !this.isAllowedFile(file.getName())) {
				continue;
			} else {
				byte[] bytes = this.getBytesOfStreamWithGZip(Files.newInputStream(file.toPath()));
				this.storeInvocationSequenceInput(result, bytes);
			}
		}

		return result;
	}

	/**
	 * Stores the provided bytes into the provided InvocationSequences if the
	 * input matches to the required structure of InvocationSequences.
	 * 
	 * @param result
	 *            - Container to store the provided bytes.
	 * @param bytes
	 *            - Bytes to store.
	 */
	private void storeInvocationSequenceInput(final InvocationSequences result, final byte[] bytes) {

		Input input = new Input(bytes);

		Object inputObject = null;
		while (!input.eof()) {
			try {
				inputObject = deserialize(input);
				if (inputObject instanceof PlatformIdent) {
					result.setPlatformIdent((PlatformIdent) inputObject);
				} else if (inputObject instanceof InvocationSequenceData) {
					InvocationSequenceData isd = ((InvocationSequenceData) inputObject);
					if (isd.getChildCount() == 0 || !isd.getNestedSequences().isEmpty()) {
						result.addInvocationSequence((InvocationSequenceData) inputObject);
					}
				}
			} catch (SerializationException e) {
				throw new IllegalArgumentException(
						"Failed deserializing Invocation Sequences! inspectIT version of input file does not match to inspectIT version of adapter!", e);
			}
		}
	}

	/**
	 * Check if file ends with ".itdata" or ".agent".
	 * 
	 * @param name
	 *            - Filename
	 * @return - True if filename ends with ".itdata" or ".agent".
	 */
	private boolean isAllowedFile(final String name) {
		return (name.endsWith(".itdata") || name.endsWith(".agent"));
	}

	/**
	 * Check if it is a ".itds" file.
	 * @param name File name
	 * @return true if it is itds file, false if not
	 */
	private boolean isAllowedDatasource(final String name) {
		return name.endsWith(".itds");
	}

	/**
	 * Return the bytes of a provided stream but is the stream gzipped the
	 * result are the ungzipped bytes. The provided stream is closed after
	 * executing.
	 * 
	 * @param fileStream
	 *            - Get bytes from this stream.
	 * @return - If the stream is not gzipped the bytes of the stream. If the
	 *         stream is gzipped the ungzipped bytes.
	 * @throws IOException
	 */
	private byte[] getBytesOfStreamWithGZip(final InputStream fileStream) throws IOException {
		byte[] bytes = this.getBytesOfStream(fileStream);
		if (checkFileIsGzipped(bytes)) {
			return this.getBytesOfStream(new GZIPInputStream(new ByteArrayInputStream(bytes)));
		}
		return bytes;
	}

	/**
	 * Return the bytes of a provided stream. The provided stream is closed
	 * after executing.
	 * 
	 * @param fileStream
	 *            - Get bytes from this stream.
	 * @return - Bytes of the stream.
	 * @throws IOException
	 */
	private byte[] getBytesOfStream(final InputStream fileStream) throws IOException {
		try (InputStream stream = fileStream) {
			try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
				byte[] buffer = new byte[1024];
				int readBytes = 0;
				while ((readBytes = stream.read(buffer)) != -1) {
					byteStream.write(buffer, 0, readBytes);
				}
				return byteStream.toByteArray();
			}
		}
	}

	/**
	 * Check if a file is gzipped or not.
	 * 
	 * @param bytes
	 *            - All or the first two bytes of a file.
	 * @return - True if file is gzipped.
	 * @throws IOException
	 */
	private boolean checkFileIsGzipped(final byte[] bytes) throws IOException {
		final byte FIRST_BYTE = (byte) 0x1f;
		final byte SECOND_BYTE = (byte) 0x8b;
		return bytes.length >= 2 && bytes[0] == FIRST_BYTE && bytes[1] == SECOND_BYTE;
	}

}
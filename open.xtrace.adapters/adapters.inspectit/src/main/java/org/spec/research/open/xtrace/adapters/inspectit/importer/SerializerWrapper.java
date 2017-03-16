package org.spec.research.open.xtrace.adapters.inspectit.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import rocks.inspectit.shared.all.cmr.model.PlatformIdent;
import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;
import rocks.inspectit.shared.all.serializer.SerializationException;
import rocks.inspectit.shared.all.serializer.impl.SerializationManager;
import rocks.inspectit.shared.all.serializer.schema.ClassSchemaManager;
import rocks.inspectit.shared.all.util.KryoNetNetwork;

import com.esotericsoftware.kryo.io.Input;

public class SerializerWrapper extends SerializationManager {

	public SerializerWrapper() throws IOException {
		super();
		ClassSchemaManager schemaManager = SchemaManagerTestProvider.getClassSchemaManagerForTests();

		setSchemaManager(schemaManager);
		setKryoNetNetwork(new KryoNetNetwork());
		initKryo();
	}

	/**
	 * Reads Invocation sequences from the specified source directory.
	 * 
	 * @param srcDirectory
	 *            directory where to read the sequences from
	 * @return {@link InvocationSequences} object
	 * @throws IOException
	 */
	public InvocationSequences readInvocationSequencesFromDir(String srcDirectory) throws IOException {
		InvocationSequences result = new InvocationSequences();
		File dir = new File(srcDirectory);
		if (!dir.isDirectory()) {
			throw new IOException("Path is not a directory!");
		}

		for (File file : dir.listFiles()) {
			if (!file.isFile()) {
				continue;
			}
			InputStream fis = Files.newInputStream(file.toPath(), StandardOpenOption.READ);
			Input input = new Input(fis);
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
					throw new IllegalArgumentException("Failed deserializing Invocation Sequences! Potentially an old version (< 1.6) of inspectIT has been used to record the files.", e);
				}
			}
		}

		return result;
	}

}

package org.spec.research.open.xtrace.adapters.introscope.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class SignatureUtils {

	public static List<String> getParameterTypes(String methodDescriptor) {
		String parameterStr = methodDescriptor.substring(methodDescriptor.indexOf('(') + 1, methodDescriptor.indexOf(')'));
		if (parameterStr.isEmpty()) {
			return Collections.emptyList();
		} else {
			List<String> parameters = new ArrayList<String>();
			int idx = 0;
			String typeDescriptor = "";
			while (idx < parameterStr.length()) {
				if (parameterStr.charAt(idx) == 'L') {
					int nextIdx = parameterStr.indexOf(';', idx) + 1;
					typeDescriptor += parameterStr.substring(idx, nextIdx);
					idx = nextIdx;
				} else if (parameterStr.charAt(idx) == '[') {
					typeDescriptor += "[";
					idx++;
					continue;
				} else {
					typeDescriptor += parameterStr.charAt(idx);
					idx++;
				}
				parameters.add(getType(typeDescriptor));
				typeDescriptor = "";

			}
			return parameters;
		}
	}

	public static String getReturnType(String methodDescriptor) {
		String returnTypeDescriptor = methodDescriptor.substring(methodDescriptor.indexOf(')') + 1);
		return getType(returnTypeDescriptor);
	}

	public static String getType(String typeDescriptor) {
		String arrayPostFix = "";
		while (typeDescriptor.charAt(0) == '[') {
			arrayPostFix += "[]";
			typeDescriptor = typeDescriptor.substring(1);
		}
		String type = "";

		char firstCharacter = typeDescriptor.charAt(0);

		switch (firstCharacter) {
		case 'B':
			type = "byte";
			break;
		case 'C':
			type = "char";
			break;
		case 'D':
			type = "double";
			break;
		case 'F':
			type = "float";
			break;
		case 'I':
			type = "int";
			break;
		case 'J':
			type = "long";
			break;
		case 'S':
			type = "short";
			break;
		case 'Z':
			type = "boolean";
			break;
		case 'V':
			type = "void";
			break;
		case 'L':
			type = typeDescriptor.substring(1, typeDescriptor.length() - 1).replace("/", ".");
			break;
		default:
			throw new RuntimeException("Invalid type :" + firstCharacter);
		}

		return type + arrayPostFix;

	}
}

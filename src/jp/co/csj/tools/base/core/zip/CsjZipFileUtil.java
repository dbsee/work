package jp.co.csj.tools.base.core.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;

import org.mydbsee.common.CmnStrUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jConstants;

public class CsjZipFileUtil {

	private static int compressionMethod = Zip4jConstants.COMP_DEFLATE;
	private static int compressionLevel = Zip4jConstants.DEFLATE_LEVEL_NORMAL;
	private static int encryptionMethod = Zip4jConstants.ENC_METHOD_STANDARD;
	private static int aesKeyStrength = Zip4jConstants.AES_STRENGTH_256;

	public CsjZipFileUtil() {
	}

	public CsjZipFileUtil(int encryptionMethod, int aesKeyStrength) {
		this.encryptionMethod = encryptionMethod;
		this.aesKeyStrength = aesKeyStrength;
	}

	public static void zip(String input, String output, String password)
			throws ZipException, IOException, CloneNotSupportedException {
		zip(input, output, password, InternalZipConstants.CHARSET_UTF8);
	}

	public static void zip(String input, String output, String password,
			String fileNameCharset)
			throws ZipException, IOException, CloneNotSupportedException {

		ZipModel zipModel = new ZipModel();
		// If the fileNameCharset is empty then charset is detected
		// automatically
		// Try with Cp850 and UTF8 or OS default
		if (!fileNameCharset.isEmpty()) {
			zipModel.setFileNameCharset(fileNameCharset);
		}

		ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(compressionMethod);
		parameters.setCompressionLevel(compressionLevel);
		
        parameters.setEncryptFiles(false);
        if (CmnStrUtils.isNotEmpty(password)) {
    		parameters.setEncryptFiles(true);
    		parameters.setEncryptionMethod(encryptionMethod);
    		parameters.setAesKeyStrength(aesKeyStrength);
    		parameters.setPassword(password);
		}
        

		parameters.setDefaultFolderPath(new File(input).getParent());

		ZipOutputStream outputStream = new ZipOutputStream(
				new FileOutputStream(new File(output)), zipModel);
		LinkedList<File> remained = new LinkedList();
		remained.add(new File(input));
		while (!remained.isEmpty()) {
			File file = remained.pollFirst();
			outputStream.putNextEntry(file, (ZipParameters) parameters.clone());
			if (file.isDirectory()) {
				remained.addAll(0, Arrays.asList(file.listFiles()));
			} else {
				InputStream inputStream = new FileInputStream(file);
				byte[] readBuff = new byte[4096];
				int readLen = -1;
				while ((readLen = inputStream.read(readBuff)) != -1) {
					outputStream.write(readBuff, 0, readLen);
				}
			}
		}
		outputStream.closeEntry();
		outputStream.finish();
	}

	public static void unzip(String input, String output, String password)
			throws ZipException {
		ZipFile zipFile = new ZipFile(input);
		if (zipFile.isEncrypted()) {
			zipFile.setPassword(password);
		}
		zipFile.extractAll(output);
	}
}
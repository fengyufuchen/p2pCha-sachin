package com.chat.fun;

import java.io.File;
import java.util.LinkedList;

public class ShareFileContext {

	private File shareDirectory;
	private LinkedList<File> fileList = new LinkedList<>();

	public ShareFileContext() {

	}

	public String[] getSharedFilePath() {

		String[] sharedFilePath = new String[fileList.size()];
		for (int i = 0; i < fileList.size(); i++) {
			sharedFilePath[i] = fileList.get(i).getAbsolutePath()
					.substring(shareDirectory.getAbsolutePath().length() + 1);

		}
		return sharedFilePath;

	}

	public void addShareDirectory(File pShareDirectory) {
		if (pShareDirectory.isFile()) {

			fileList.add(pShareDirectory);
		} else {
			File[] files = pShareDirectory.listFiles();
			for (File file : files) {
				addShareDirectory(file);
			}
		}

	}

}

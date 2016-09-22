package com.zncm.dminter.mmhelper.autocommand;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShellLoader {
	private List<File> shellFiles;
	private File files;

	public List<File> getShellFiles() {
		return shellFiles;
	}

	public File getFiles() {
		return files;
	}

	public ShellLoader() {
		clear();
	}

	public void clear() {
		shellFiles = new ArrayList<File>();
		files = new File("/sdcard/AutoCommand");
	}

	public String readAllShells() {
		if (shellFiles == null) {
			readShellFiles();
		}
		if (!shellFiles.isEmpty()) {
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader;
			String line = null;
			for (File file : shellFiles) {
				try {
					reader = new BufferedReader(new FileReader(file));
					while ((line = reader.readLine()) != null) {
						if (line != null) {
							buffer.append(line + "\n");
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return buffer.toString();
		} else {
			return "";
		}
	}

	public void readShellFiles() {
		if(files == null) {
			files = new File("/sdcard/AutoCommand");
		}
		if (!files.exists()) {
			boolean success = files.mkdir();
			if (!success){
				Log.v("ShellLoader","目录创建失败!");
				return;
			}	
		}
		Log.e("ShellLoader", files.getName());
		Log.e("ShellLoader", files.canRead()?"canRead":"can'tRead");
		if (files.canRead()) {
			if (files.isDirectory()) {
				File[] shfiles = files.listFiles(new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						Log.v("ShellLoader-Filter",pathname.getName());
						if (pathname.getName().contains(".sh")) {
							return true;
						} else {
							return false;
						}
					}

				});
				if (shellFiles == null) {
					shellFiles = new ArrayList<File>();
				}
				if (shfiles.length > 0) {
					for (File file : shfiles) {
						shellFiles.add(file);
					}
				} else {
					Log.v("ShellLoader","目录无文件!");
				}
			}
		}
	}
}

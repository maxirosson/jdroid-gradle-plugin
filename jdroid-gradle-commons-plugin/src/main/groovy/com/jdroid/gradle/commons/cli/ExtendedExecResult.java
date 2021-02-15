package com.jdroid.gradle.commons.cli;

import org.gradle.process.ExecResult;
import org.gradle.process.internal.ExecException;

import java.io.ByteArrayOutputStream;

public class ExtendedExecResult implements ExecResult {
	
	private ExecResult execResult;
	private ByteArrayOutputStream standardOutputStream;
	private ByteArrayOutputStream errorOutputStream;
	
	public ExtendedExecResult(ExecResult execResult, ByteArrayOutputStream standardOutputStream, ByteArrayOutputStream errorOutputStream) {
		this.execResult = execResult;
		this.standardOutputStream = standardOutputStream;
		this.errorOutputStream = errorOutputStream;
	}
	
	@Override
	public int getExitValue() {
		return execResult.getExitValue();
	}
	
	public Boolean isSuccessful() {
		return getExitValue() == 0;
	}
	
	@Override
	public ExecResult assertNormalExitValue() throws ExecException {
		return execResult.assertNormalExitValue();
	}
	
	@Override
	public ExecResult rethrowFailure() throws ExecException {
		return execResult.rethrowFailure();
	}
	
	public String getStandardOutput() {
		return standardOutputStream.toString();
	}
	
	public String getErrorOutput() {
		return errorOutputStream.toString();
	}
}

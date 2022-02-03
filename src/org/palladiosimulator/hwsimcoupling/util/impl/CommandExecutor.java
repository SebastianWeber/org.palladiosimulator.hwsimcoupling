package org.palladiosimulator.hwsimcoupling.util.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;

public class CommandExecutor {

	public static void execute_command(List<String> command, Consumer<String> outputConsumer, Consumer<String> errorConsumer) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		Process process = builder.start();
		
		try {
			CompletableFuture.runAsync(new StreamGobbler(process.getInputStream(), outputConsumer)).get();
			CompletableFuture.runAsync(new StreamGobbler(process.getErrorStream(), errorConsumer)).get();
		} catch (InterruptedException | ExecutionException e) {
			for (StackTraceElement s : e.getStackTrace()) {
				System.out.println(s);
			}
			throw new DemandCalculationFailureException(e.getMessage());
		}
		int exitCode = process.waitFor();
		assert exitCode == 0;
	}
	
	private static class StreamGobbler implements Runnable {
	    private InputStream inputStream;
	    private Consumer<String> consumer;

	    public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
	        this.inputStream = inputStream;
	        this.consumer = consumer;
	    }

	    @Override
	    public void run() throws DemandCalculationFailureException{
	        new BufferedReader(new InputStreamReader(inputStream)).lines()
	          .forEach(consumer);
	    }
	}
	
}

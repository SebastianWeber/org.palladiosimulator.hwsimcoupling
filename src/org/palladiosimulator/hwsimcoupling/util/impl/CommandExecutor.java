package org.palladiosimulator.hwsimcoupling.util.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.palladiosimulator.hwsimcoupling.commands.Command;
import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;

/**
 * @author Sebastian
 * Executes {@link org.palladiosimulator.hwsimcoupling.commands.Command}
 */
public class CommandExecutor {

	/**
	 * Executes the given {@link org.palladiosimulator.hwsimcoupling.commands.Command} 
	 * and connects the output and error streams to the given consumers
	 * @param command
	 * @param outputConsumer
	 * @param errorConsumer
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void execute_command(Command command, Consumer<String> outputConsumer, Consumer<String> errorConsumer) throws IOException, InterruptedException {
		System.out.print("Executing command: ");
		command.get_command().forEach(x -> System.out.print(x + " "));
		System.out.println("");
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command.get_command());
		Process process = builder.start();
		
		try {
			CompletableFuture.runAsync(new StreamGobbler(process.getInputStream(), outputConsumer)).get();
			CompletableFuture.runAsync(new StreamGobbler(process.getErrorStream(), errorConsumer)).get();
		} catch (InterruptedException | ExecutionException e) {
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

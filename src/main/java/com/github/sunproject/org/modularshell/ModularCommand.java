package com.github.sunproject.org.modularshell;


import java.util.ArrayList;
import java.util.HashMap;
import xyz.sunproject.modularframework.core.events.RunEvent;

@SuppressWarnings("unused")
public class ModularCommand {
    private String commandName;
    private String commandDetails;
    private static final HashMap<String, ModularCommand> commandsMap = new HashMap<>();
    private RunEvent eventHandler;
    private static ModularCmdArgs commandArgs = new ModularCmdArgs();

    public ModularCommand(String cmdName) {
        this.commandName = cmdName;
    }


    public void launchCommand() {
        eventHandler.runEvent();
        if (!getCommandArgs().isEmpty()) getCommandArgs().clear(); // For Resolve a bug with cmd args, force reset args
    }

	public String getCommandDetails() {
		if (commandDetails == null || commandDetails.isEmpty()) commandDetails = "No Details provided.";
		return commandDetails;
	}


	public static void setCommandArgs(ArrayList<String> args) {
		commandArgs.setCmdArgs(args);
	}

	public static ArrayList<String> getCommandArgs() {
		return commandArgs.getCmdArgs();
	}

	public void setCommandDetails(String details) {
		commandDetails = details;
	}

	public static void unregisterCommand(ModularCommand command) {
		getCommands().remove(command.getCommandName());
	}

	public static void unregisterAllCommands() {
		System.out.println("Unregistering All Commands ...");
		getCommands().clear();
		System.out.println("...done !");
	}

	public static void registerCommand(ModularCommand command) {
		getCommands().put(command.getCommandName(), command);
	}

	public static HashMap<String, ModularCommand> getCommands() {
		return commandsMap;
	}

	public RunEvent getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(RunEvent eventHandler) {
		this.eventHandler = eventHandler;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}

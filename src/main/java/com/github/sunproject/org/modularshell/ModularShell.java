package com.github.sunproject.org.modularshell;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;
import com.github.sunproject.org.modularframework.events.ModularEventHandler;
import com.github.sunproject.org.modularframework.providers.modulemanager.ModularModule;
import com.github.sunproject.org.modularshell.builtin.ModularAboutShCmd;
import com.github.sunproject.org.modularshell.builtin.ModularHelpCmd;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

@SuppressWarnings("unused")
public class ModularShell extends ModularModule {

	public static final String moduleName = "ModularShell";
	private static final String moduleVersion = "1.1.7";
	private final Scanner cliListener;
	private final ModularEventHandler preInitTaskEvent;

	private String prompt = Ansi.colorize("~", new AnsiFormat(Attribute.RED_TEXT()))
			+ Ansi.colorize(">", new AnsiFormat(Attribute.BOLD(), Attribute.ITALIC())) + " ";

	private static Thread shellThread;

	public ModularShell(ModularEventHandler preInitTask) {
		super(moduleName, 1);
		this.setModuleVersion(moduleVersion);
		this.preInitTaskEvent = preInitTask;
		cliListener = new Scanner(System.in);

		shellThread = new Thread(() -> {
			System.out.println("\n");
			callInterpreter(getPrompt());
		});
		shellThread.setName(moduleName + "_MainThread");
	}

	private void callInterpreter(String prompt) {
		while (true) {
			System.out.print(prompt);
			String[] args = cliListener.nextLine().split(" ");
			sendCommand(args);
		}
	}

	private void sendCommand(String[] cmdName) {
		if (!cmdName[0].isEmpty()) {
			if (ModularCommand.getCommands().get(cmdName[0]) == null) {
				System.err.println("\nCommand " + "\"" + cmdName[0] + "\"" + " not found !");
			} else {
				ModularCommand command = ModularCommand.getCommands().get(cmdName[0]);
				if (cmdName.length > 1) {
					ArrayList<String> list = new ArrayList<>(Arrays.asList(cmdName));
					list.remove(0);
					command.setCommandArgs(list);
				}
				command.launchCommand();
			}
		}
	}


	@Override
	public void onDisable() {
		ModularCommand.unregisterAllCommands();
		shellThread.stop();
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	@Override
	public void onEnable() {
		if (preInitTaskEvent != null) preInitTaskEvent.onEvent();

		// Init built-in commands
		ModularCommand.registerCommand(new ModularHelpCmd());
		ModularCommand.registerCommand(new ModularAboutShCmd());

		shellThread.start();
	}
}
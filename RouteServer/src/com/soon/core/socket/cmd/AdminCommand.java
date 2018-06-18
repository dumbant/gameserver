package com.soon.core.socket.cmd;

import java.io.BufferedWriter;

public interface AdminCommand {

	public void execute(String params[], BufferedWriter writer);
}

package org.nasdanika.launcher.demo.tests;

import java.io.IOException;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

public class TestJLine {
	
	@Test
	@Disabled
	public void testJLineTerminal() throws IOException {
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            terminal.enterRawMode();

            terminal.writer().println("Terminal: " + terminal);
            terminal.writer()
                    .println("Type characters, which will be echoed to the terminal. Q will also exit this example.");
            terminal.writer().println();
            terminal.writer().flush();

            while (true) {
                int c = terminal.reader().read(16);
                if (c >= 0) {
                    terminal.writer().write(c);
                    terminal.writer().flush();

                    // Use "q" to quit early
                    if (c == 81 || c == 113) break;
                } else {
                    if (c == -1) break; // Got EOF
                }
            }
        }		
	}
	
	@Test
	@Disabled
	public void testLineReder() throws IOException {
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
        	LineReader lineReader = LineReaderBuilder
        			.builder()
                    .terminal(terminal)
                    .build();
        	
        	String prompt = "nsd>";
            while (true) {
                String line = null;
                line = lineReader.readLine(prompt);
                System.out.println("Got --- " + line);
                if ("exit".equals(line)) {
                	break;
                }
            }
        }		
	}	

}

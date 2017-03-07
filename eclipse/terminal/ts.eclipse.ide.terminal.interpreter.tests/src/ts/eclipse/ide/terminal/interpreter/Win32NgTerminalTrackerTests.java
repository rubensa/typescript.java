/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *  
 */package ts.eclipse.ide.terminal.interpreter;

import org.junit.Assert;
import org.junit.Test;

/**
 * Win32 tracker test.	
 *
 */
public class Win32NgTerminalTrackerTests {

	@Test
	public void ngClass() {
		TrackerTest test = new TrackerTest("C:\\Users\\azerr\\WS\\abcd\\src\\app", "ng generate class c1  --spec false");
		test.processText("Microsoft Windows [version 6.1.7601]");
		test.processCarriageReturnLineFeed();
		test.processText("Copyright (c) 2009 Microsoft Corporation. Tous droits réservés.");
		test.processCarriageReturnLineFeed();
		test.processCarriageReturnLineFeed();
		test.processText("C:\\Users\\azerr\\WS\\abcd\\src\\app>ng generate class c1  --spec false");
		test.processCarriageReturnLineFeed();
		test.processText("installing class");
		test.processCarriageReturnLineFeed();
		test.processText("  ");
		test.processText("create");
		test.processText(" src\\app\\c1.ts");
		test.processCarriageReturnLineFeed();
		test.processCarriageReturnLineFeed();
		test.processText("C:\\Users\\azerr\\WS\\abcd\\src\\app>");

		String expected = "" + 
				"SUBMIT: workingDir=C:\\Users\\azerr\\WS\\abcd\\src\\app, command=ng generate class c1  --spec false\n" + 
				"EXECUTING:installing class\n" + 
				"EXECUTING:  \n" +   
				"EXECUTING:create\n" + 
				"EXECUTING: src\\app\\c1.ts\n" + 
				"TERMINATE: workingDir=C:\\Users\\azerr\\WS\\abcd\\src\\app, command=ng generate class c1  --spec false"; 
				
				Assert.assertEquals(expected, test.toString());
	}
	
//	@Test
//	public void ngTwoClasses() {
//		CommandTerminalTracker test = new TrackerTest("C:\\Users\\azerr\\WS\\a2\\src\\app", "ng generate class c6  --spec true");
//		test.processLines(Arrays.asList(), true);
//		test.processLines(Arrays.asList(), false);
//		test.processLines(Arrays.asList("Microsoft Windows [version 6.1.7601]", "Copyright (c) 2009 Microsoft Corporation. Tous droits réservés.", "C:\\Users\\azerr\\WS\\a2\\src\\app>ng generate class c6  --spec true"), false);
//		test.processLines(Arrays.asList(), false);
//		test.processLines(Arrays.asList("installing class", "  ", "create", " src\\app\\c6.spec.ts", "  ", "create", " src\\app\\c6.ts"), false);
//		test.processLines(Arrays.asList("C:\\Users\\azerr\\WS\\a2\\src\\app>"), false);
//		test.processLines(Arrays.asList("C:\\Users\\azerr\\WS\\a2\\src\\app>cd C:\\Users\\azerr\\WS\\a2\\src\\app\\about"), true);
//		test.processLines(Arrays.asList("C:\\Users\\azerr\\WS\\a2\\src\\app\\about>ng generate class c8  --spec false"), false);
//		test.processLines(Arrays.asList(), false);
//		test.processLines(Arrays.asList("installing class", "  ", "create", " src\\app\\about\\c8.ts"), false);
//		test.processLines(Arrays.asList("C:\\Users\\azerr\\WS\\a2\\src\\app\\about>"), false);
//		
//		System.err.println(test);
//	}
}
/*******************************************************************************
 * Copyright (c) 2012 TODO COMPANY NAME and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    srikanth (TODO COMPANY NAME) - Initial API and implementation
 *******************************************************************************/
package org.eclipse.photran.ui.swttests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.eclipse.core.internal.content.Activator;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;
import junit.framework.TestCase;

@RunWith(SWTBotJunit4ClassRunner.class)
public class ScalabilityTests
{

    private static SWTWorkbenchBot bot;

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        bot = new SWTWorkbenchBot();
       
    }

    private static SWTBotView getPackageExplorer()
    {
        SWTBotView view = bot.viewByTitle("Package Explorer");
        return view;
    }

    
    @Test
    public void testSimulateHumanTyping() throws Exception
    {
    	TestFileGenerator.generateTestData(75000);
    	
    	//To Simulate we will assume avg WPM is 50, avg chars per word is 6
    	//Meaning a character every 200 millisecond
    	//Asuuming it takes 1 millisecond to type the character, we will wait for 199 milli
    	
    	
        SWTBotEditor editor = bot.editorByTitle("Test.f90");
        editorRefresh();
        //bot.waitUntil(Conditions.shellIsActive("Test.f90"));
        //sleep();
        int line = 800;
        String text = "A(I) = 1.0E0\n";
        int sentLength = 3;
        long start = System.currentTimeMillis();
        for(int j = 0; j < sentLength; j ++)
        {
        	line = (500 + (int)(Math.random() * ((800 - 500) + 1)));
        	for (int i = 0; i < text.length(); i++)
        	{
        		editor.toTextEditor().typeText(line - 2, i, String.valueOf(text.charAt(i)));
        		sleep(450);
        	}
        }
        long end = System.currentTimeMillis();
        editor.save();
        System.out.println("Total Time taken is: " + (end - start) + "(in ms)");
    }

    
    @Test
    public void testrandomwaitTyping() throws Exception
    {
    	TestFileGenerator.generateTestData(75000);
    	
    	//To Simulate we will assume avg WPM is 50, avg chars per word is 6
    	//Meaning a character every 200 millisecond
    	//Asuuming it takes 1 millisecond to type the character, we will wait for 199 milli
    	
    	
        SWTBotEditor editor = bot.editorByTitle("Test.f90");
        editorRefresh();
        //bot.waitUntil(Conditions.shellIsActive("Test.f90"));
        //sleep();
        int line = 800;
        int delay;
        String text = "A(I) = 1.0E0\n";
        int sentLength = 3;
        long start = System.currentTimeMillis();
        for(int j = 0; j < sentLength; j ++)
        {
        	line = (500 + (int)(Math.random() * ((800 - 500) + 1)));
        	delay = (450 + (int)(Math.random() * ((800 - 450) + 1)));
        	for (int i = 0; i < text.length(); i++)
        	{
        		editor.toTextEditor().typeText(line - 2, i, String.valueOf(text.charAt(i)));
        		sleep(delay);
        	}
        }
        long end = System.currentTimeMillis();
        editor.save();
        System.out.println("Total Time taken is: " + (end - start) + "(in ms)");
    }
    
    /**
     * 
     */
    

    private void editorRefresh()
    {
        bot.menu("File").menu("Refresh").click();
    }

    @AfterClass
    public static void sleep()
    {

    	bot.sleep(5000);
    }

    public static void sleep(int time)
    {   
    	bot.sleep(time);
    }
}
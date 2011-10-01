/**
 *     Copyright (C) 2010 Julien SMADJA <julien dot smadja at gmail dot com> - Arnaud LEMAIRE <alemaire at norad dot fr>
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package net.awired.visuwall.cli;

import java.io.File;
import java.io.IOException;
import net.awired.visuwall.core.application.common.ApplicationHelper;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.io.Files;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private final ArgumentManager argManager = new ArgumentManager(this);

    public static void main(String[] args) {
        new Main().run(args);
    }

    public void run(String[] args) {
        AnsiConsole.systemInstall();
        argManager.parse(args);

        System.setProperty(ApplicationHelper.HOME_KEY, ApplicationHelper.findHomeDir());
        System.setProperty(ApplicationHelper.LOG_LVL_KEY, argManager.logLevel.getParamOneValue().toString());
        //ApplicationHelper.changeLogLvl();

        if (argManager.displayFile.isSet()) {
            argManager.displayFile.getParamOneValue().display();
            System.exit(0);
        }

        if (argManager.info.isSet()) {
            printInfo();
            System.exit(0);
        }

        if (argManager.clearDb.isSet()) {
            cleanDB();
            System.exit(0);
        }

    }

    public void cleanDB() {
        String home = ApplicationHelper.findHomeDir();
        try {
            System.out.println("clearing database in home folder : " + home);
            Files.deleteRecursively(new File(home + "/db"));
        } catch (IOException e) {
        }
        System.exit(0);
    }

    public void printInfo() {
        System.out.println("Visuall version : " + ApplicationHelper.findVersion(null));
        System.out.println("Visuall home : " + ApplicationHelper.findHomeDir());
        // TODO list plugin founds
    }

}

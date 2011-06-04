/**
 * Copyright (C) 2010 Julien SMADJA <julien dot smadja at gmail dot com> - Arnaud LEMAIRE <alemaire at norad dot fr>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.awired.visuwall.hudsonclient.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import net.awired.visuwall.hudsonclient.generated.hudson.mavenmodulesetbuild.HudsonMavenMavenModuleSetBuild;
import net.awired.visuwall.hudsonclient.generated.hudson.mavenmodulesetbuild.HudsonModelUser;

import org.junit.Test;
import org.w3c.dom.Node;

public class HudsonXmlHelperTest {

    @Test
    public void testIsSuccessful() {
        Node firstChild = mock(Node.class);
        when(firstChild.getNodeValue()).thenReturn("SUCCESS");

        Node result = mock(Node.class);
        when(result.getFirstChild()).thenReturn(firstChild);

        HudsonMavenMavenModuleSetBuild job = mock(HudsonMavenMavenModuleSetBuild.class);
        when(job.getResult()).thenReturn(result);

        boolean isSuccessful = HudsonXmlHelper.isSuccessful(job);

        assertTrue(isSuccessful);
    }

    @Test
    public void testGetCommiters() {
        List<HudsonModelUser> users = new ArrayList<HudsonModelUser>();
        users.add(new HudsonModelUser());
        users.add(new HudsonModelUser());
        users.get(0).setFullName("dude");
        users.get(1).setFullName("sweet");

        HudsonMavenMavenModuleSetBuild setBuild = mock(HudsonMavenMavenModuleSetBuild.class);
        when(setBuild.getCulprit()).thenReturn(users);

        String[] usersFromModel = HudsonXmlHelper.getCommiters(setBuild);

        assertArrayEquals(new String[] { "dude", "sweet" }, usersFromModel);
    }

    @Test
    public void testGetState() {
        Node firstChild = mock(Node.class);
        when(firstChild.getNodeValue()).thenReturn("FAILURE");

        Node result = mock(Node.class);
        when(result.getFirstChild()).thenReturn(firstChild);

        HudsonMavenMavenModuleSetBuild setBuild = mock(HudsonMavenMavenModuleSetBuild.class);
        when(setBuild.getResult()).thenReturn(result);

        String state = HudsonXmlHelper.getState(setBuild);

        assertEquals("FAILURE", state);
    }

}

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

package net.awired.visuwall.plugin.bamboo;

import static org.junit.Assert.assertEquals;
import java.net.URL;
import net.awired.visuwall.Urls;
import org.junit.Test;

public class BambooVersionExtractorIT {

    @Test
    public void should_extract_version() throws Exception {
        URL url = new URL(Urls.AWIRED_BAMBOO);
        String version = BambooVersionExtractor.extractVersion(url);
        assertEquals("2.7.1", version);
    }

    @Test(expected = BambooVersionNotFoundException.class)
    public void should_not_fail_with_jenkins_url() throws Exception {
        URL url = new URL(Urls.AWIRED_JENKINS);
        BambooVersionExtractor.extractVersion(url);
    }
}

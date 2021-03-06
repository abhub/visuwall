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

package net.awired.visuwall.api.plugin.capability;

import java.util.Date;
import java.util.List;
import net.awired.visuwall.api.domain.BuildTime;
import net.awired.visuwall.api.domain.Commiter;
import net.awired.visuwall.api.domain.SoftwareProjectId;
import net.awired.visuwall.api.domain.BuildState;
import net.awired.visuwall.api.exception.BuildNotFoundException;
import net.awired.visuwall.api.exception.BuildIdNotFoundException;
import net.awired.visuwall.api.exception.ProjectNotFoundException;

public interface BuildCapability extends BasicCapability {

    /**
     * Return a list of commiters
     * 
     * @param softwareProjectId
     * @param buildId
     * @return
     * @throws BuildNotFoundException
     * @throws ProjectNotFoundException
     */
    List<Commiter> getBuildCommiters(SoftwareProjectId softwareProjectId, String buildId)
            throws BuildNotFoundException, ProjectNotFoundException;

    /**
     * Return build time information
     * 
     * @param softwareProjectId
     * @param buildId
     * @return
     * @throws ProjectNotFoundException
     */
    BuildTime getBuildTime(SoftwareProjectId softwareProjectId, String buildId) throws BuildNotFoundException,
            ProjectNotFoundException;

    /**
     * Returns the build ids order by integer ASC
     * Pending builds not included.
     * 
     * @param softwareProjectId
     * @return
     * @throws ProjectNotFoundException
     */
    List<String> getBuildIds(SoftwareProjectId softwareProjectId) throws ProjectNotFoundException;

    /**
     * Builds are in a certain state which may vary between software You'll have to try to associate them with common
     * States
     * 
     * @param projectId
     * @param buildId
     * @return
     * @throws ProjectNotFoundException
     * @throws BuildNotFoundException
     */
    BuildState getBuildState(SoftwareProjectId projectId, String buildId) throws ProjectNotFoundException,
            BuildNotFoundException;

    /**
     * If a project is building, plugin can calculate the estimated finish time
     * 
     * @param projectId
     * @param buildId
     * @return
     * @throws ProjectNotFoundException
     * @throws BuildNotFoundException
     */
    Date getEstimatedFinishTime(SoftwareProjectId projectId, String buildId) throws ProjectNotFoundException,
            BuildNotFoundException;

    /**
     * Return true if project is building
     * 
     * @param projectId
     * @param buildId
     * @return
     * @throws ProjectNotFoundException
     * @throws BuildNotFoundException
     */
    boolean isBuilding(SoftwareProjectId projectId, String buildId) throws ProjectNotFoundException,
            BuildNotFoundException;

    /**
     * Return the last build id of a project
     * Pending builds not included.
     * 
     * @param softwareProjectId
     * @return buildId
     * @throws ProjectNotFoundException
     * @throws BuildIdNotFoundException
     */
    String getLastBuildId(SoftwareProjectId softwareProjectId) throws ProjectNotFoundException,
            BuildIdNotFoundException;

}

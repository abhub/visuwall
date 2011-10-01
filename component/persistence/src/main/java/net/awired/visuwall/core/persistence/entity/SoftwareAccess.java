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

package net.awired.visuwall.core.persistence.entity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import net.awired.visuwall.api.domain.SoftwareProjectId;
import net.awired.visuwall.api.plugin.capability.BasicCapability;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.CollectionOfElements;
import org.springframework.util.AutoPopulatingList;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

@Entity
public class SoftwareAccess {

    @Id
    @GeneratedValue
    private Long id;

    private String url;

    @CollectionOfElements
    private Map<String, String> properties = new HashMap<String, String>();

    private boolean allProject;

    @NotNull
    private Integer projectFinderDelaySecond = 30;
    @NotNull
    private Integer projectStatusDelaySecond = 10;

    @CollectionOfElements
    private List<String> projectNames = new AutoPopulatingList<String>(String.class);
    @CollectionOfElements
    private List<String> viewNames = new AutoPopulatingList<String>(String.class);

    @Transient
    private ScheduledFuture<Object> projectFinderTask;
    @Transient
    private BasicCapability connection;

    public SoftwareAccess() {

    }

    public SoftwareAccess(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SoftwareAccess other = (SoftwareAccess) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        ToStringHelper toString = Objects.toStringHelper(this) //
                .add("id", id) //
                .add("url", url) //
                .add("allProject", allProject) //
                .add("projectNames", projectNames).add("viewNames", viewNames); //
        return toString.toString();
    }

    public List<SoftwareProjectId> getProjectIds() {
    	ArrayList<SoftwareProjectId> softwareProjectIds = new ArrayList<SoftwareProjectId>();
    	for (String projectIdSerialized : projectNames) {
			softwareProjectIds.add(new SoftwareProjectId(projectIdSerialized));
		}
        return softwareProjectIds;
    }

    public void setProjectIds(List<SoftwareProjectId> projectIds) {
    	ArrayList<String> res = new ArrayList<String>();
    	for (SoftwareProjectId softwareProjectId : projectIds) {
			res.add(softwareProjectId.getProjectId());
		}
    	projectNames = res;
    }
    
    // ///////////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public URL getUrl() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("software access url is malformed" + this, e);
        }
    }

    public void setUrl(URL url) {
        this.url = url.toString();
    }

    public boolean isAllProject() {
        return allProject;
    }

    public void setAllProject(boolean allProject) {
        this.allProject = allProject;
    }

    public List<String> getViewNames() {
        return viewNames;
    }

    public void setViewNames(List<String> viewNames) {
        this.viewNames = viewNames;
    }

    @JsonIgnore
    public BasicCapability getConnection() {
        return connection;
    }

    @JsonIgnore
    public void setConnection(BasicCapability connection) {
        this.connection = connection;
    }

    public Integer getProjectFinderDelaySecond() {
        return projectFinderDelaySecond;
    }

    public void setProjectFinderDelaySecond(Integer projectFinderDelaySecond) {
        this.projectFinderDelaySecond = projectFinderDelaySecond;
    }

    @JsonIgnore
    public ScheduledFuture<Object> getProjectFinderTask() {
        return projectFinderTask;
    }

    @JsonIgnore
    public void setProjectFinderTask(ScheduledFuture<Object> projectFinderTask) {
        this.projectFinderTask = projectFinderTask;
    }

    public void setProjectStatusDelaySecond(Integer projectStatusDelaySecond) {
        this.projectStatusDelaySecond = projectStatusDelaySecond;
    }

    public Integer getProjectStatusDelaySecond() {
        return projectStatusDelaySecond;
    }

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public List<String> getProjectNames() {
		return projectNames;
	}

	public void setProjectNames(List<String> projectNames) {
		this.projectNames = projectNames;
	}

}

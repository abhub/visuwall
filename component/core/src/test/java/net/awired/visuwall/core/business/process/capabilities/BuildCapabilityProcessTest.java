package net.awired.visuwall.core.business.process.capabilities;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.util.Date;
import net.awired.visuwall.api.domain.ProjectId;
import net.awired.visuwall.api.domain.SoftwareProjectId;
import net.awired.visuwall.api.plugin.capability.BasicCapability;
import net.awired.visuwall.api.plugin.capability.BuildCapability;
import net.awired.visuwall.core.business.domain.Build;
import net.awired.visuwall.core.business.domain.ConnectedProject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.scheduling.TaskScheduler;

public class BuildCapabilityProcessTest {

    private BuildCapabilityProcess buildCapabilityProcess = new BuildCapabilityProcess();

    private static ConnectedProject initProject(int previousBuildId, int newBuildId, boolean building)
            throws Exception {
        SoftwareProjectId projectId = new SoftwareProjectId("name");
        BuildCapability buildCapability = Mockito.mock(BuildCapability.class);
        Mockito.when(buildCapability.getLastBuildNumber(projectId)).thenReturn(newBuildId);
        Mockito.when(buildCapability.isBuilding(projectId, 0)).thenReturn(building);
        ConnectedProject project = new ConnectedProject(projectId, buildCapability);
        project.setCurrentBuildId(previousBuildId);
        return project;
    }

    @Before
    public void initProcess() {
        buildCapabilityProcess = new BuildCapabilityProcess();
        TaskScheduler taskScheduler = Mockito.mock(TaskScheduler.class);
        buildCapabilityProcess.scheduler = taskScheduler;
    }

    @Test
    public void should_return_update_needed_when_new_build_already_done_in_software() throws Exception {
        ConnectedProject project = initProject(42, 43, false);

        boolean updateNeeded = buildCapabilityProcess.updateStatusAndReturnFullUpdateNeeded(project);

        Assert.assertTrue(updateNeeded);
        Assert.assertFalse(project.getCurrentBuild().isBuilding());
        Assert.assertEquals(43, project.getCurrentBuildId());
        Mockito.verify(buildCapabilityProcess.scheduler, Mockito.times(0)).schedule(Matchers.any(Runnable.class),
                Matchers.any(Date.class));
    }

    @Test
    public void should_not_return_update_needed_when_new_build_building_in_software() throws Exception {
        ConnectedProject project = initProject(42, 43, true);

        boolean updateNeeded = buildCapabilityProcess.updateStatusAndReturnFullUpdateNeeded(project);

        Assert.assertFalse(updateNeeded);
        Assert.assertTrue(project.getCurrentBuild().isBuilding());
        Assert.assertEquals(43, project.getCurrentBuildId());
        Mockito.verify(buildCapabilityProcess.scheduler).schedule(Matchers.any(Runnable.class),
                Matchers.any(Date.class));
    }

    @Test
    public void should_not_return_update_needed_when_no_new_build_in_software() throws Exception {
        ConnectedProject project = initProject(42, 42, false);

        boolean updateNeeded = buildCapabilityProcess.updateStatusAndReturnFullUpdateNeeded(project);

        Assert.assertFalse(updateNeeded);
        Assert.assertFalse(project.getCurrentBuild().isBuilding());
        Assert.assertEquals(42, project.getCurrentBuildId());
        Mockito.verify(buildCapabilityProcess.scheduler, Mockito.times(0)).schedule(Matchers.any(Runnable.class),
                Matchers.any(Date.class));
    }

    @Test
    public void should_not_return_update_needed_when_still_building_in_software() throws Exception {
        ConnectedProject project = initProject(42, 42, true);

        boolean updateNeeded = buildCapabilityProcess.updateStatusAndReturnFullUpdateNeeded(project);

        Assert.assertFalse(updateNeeded);
        Assert.assertTrue(project.getCurrentBuild().isBuilding());
        Assert.assertEquals(42, project.getCurrentBuildId());
        Mockito.verify(buildCapabilityProcess.scheduler).schedule(Matchers.any(Runnable.class),
                Matchers.any(Date.class));
    }

    ////////////////////////////////

    BuildCapabilityProcess projectEnhancerService = new BuildCapabilityProcess();

    ConnectedProject projectToEnhance;

    @Before
    public void init() {
        ProjectId projectId = new ProjectId();
        projectId.addId("id", "value");
        projectToEnhance = new Project(projectId);
    }

    @Ignore
    @Test
    public void should_merge_with_one_build_plugin() throws Exception {
        BasicCapability buildPlugin = Mockito.mock(BasicCapability.class);

        Build completedBuild = new Build();
        Build currentBuild = new Build();

        ConnectedProject projectFromBuildPlugin = new ConnectedProject("name");
        projectFromBuildPlugin.setBuildNumbers(new int[] { 1, 2, 3 });
        projectFromBuildPlugin.setCompletedBuild(completedBuild);
        projectFromBuildPlugin.setCurrentBuild(currentBuild);
        projectFromBuildPlugin.setDescription("description");

        ProjectId projectId = projectToEnhance.getProjectId();
        // when(buildPlugin.findProject(projectId)).thenReturn(projectFromBuildPlugin);
        //        projectEnhancerService.enhanceWithBuildInformations(projectToEnhance, buildPlugin);

        assertArrayEquals(new int[] { 1, 2, 3 }, projectToEnhance.getBuildNumbers());
        assertEquals(completedBuild, projectToEnhance.getCompletedBuild());
        assertEquals(currentBuild, projectToEnhance.getCurrentBuild());
        assertEquals("description", projectToEnhance.getDescription());
        assertEquals("name", projectToEnhance.getName());
    }

    @Ignore
    @Test
    public void should_merge_with_two_build_plugins() throws Exception {
        BasicCapability buildPlugin1 = Mockito.mock(BasicCapability.class);
        BasicCapability buildPlugin2 = Mockito.mock(BasicCapability.class);

        ConnectedProject projectFromBuildPlugin1 = new ConnectedProject("name1");
        projectFromBuildPlugin1.setDescription("description");

        ConnectedProject projectFromBuildPlugin2 = new ConnectedProject("name2");

        ProjectId projectId = projectToEnhance.getProjectId();
        // when(buildPlugin1.findProject(projectId)).thenReturn(projectFromBuildPlugin1);
        // when(buildPlugin2.findProject(projectId)).thenReturn(projectFromBuildPlugin2);

        //        projectEnhancerService.enhanceWithBuildInformations(projectToEnhance, buildPlugin1);
        //        projectEnhancerService.enhanceWithBuildInformations(projectToEnhance, buildPlugin2);

        assertEquals("description", projectToEnhance.getDescription());
        assertEquals("name2", projectToEnhance.getName());
    }

    @Ignore
    @Test
    public void last_plugin_is_always_right() throws Exception {
        BasicCapability buildPlugin1 = Mockito.mock(BasicCapability.class);
        BasicCapability buildPlugin2 = Mockito.mock(BasicCapability.class);

        ConnectedProject projectFromBuildPlugin1 = new ConnectedProject("name1");
        projectFromBuildPlugin1.setDescription("description1");

        ConnectedProject projectFromBuildPlugin2 = new ConnectedProject("name2");
        projectFromBuildPlugin2.setDescription("description2");

        ProjectId projectId = projectToEnhance.getProjectId();
        // when(buildPlugin1.findProject(projectId)).thenReturn(projectFromBuildPlugin1);
        // when(buildPlugin2.findProject(projectId)).thenReturn(projectFromBuildPlugin2);

        //        projectEnhancerService.enhanceWithBuildInformations(projectToEnhance, buildPlugin1);
        //        projectEnhancerService.enhanceWithBuildInformations(projectToEnhance, buildPlugin2);

        assertEquals("description2", projectToEnhance.getDescription());
    }

    @Ignore
    @Test
    public void should_not_fail_if_project_is_not_found() throws Exception {
        BasicCapability buildPlugin = Mockito.mock(BasicCapability.class);
        ProjectId projectId = projectToEnhance.getProjectId();
        // when(buildPlugin.findProject(projectId)).thenThrow(new ProjectNotFoundException("project not found"));
        //        projectEnhancerService.enhanceWithBuildInformations(projectToEnhance, buildPlugin);
    }
}

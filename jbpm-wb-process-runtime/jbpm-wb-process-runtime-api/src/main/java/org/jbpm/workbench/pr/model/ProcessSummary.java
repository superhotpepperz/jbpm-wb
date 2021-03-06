/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.workbench.pr.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jboss.errai.common.client.api.annotations.NonPortable;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.workbench.common.model.GenericSummary;

@Portable
public class ProcessSummary extends GenericSummary<String> {

    private String processDefId;
    private String processDefName;
    private String version;
    private String deploymentId;
    private boolean dynamic;
    private Map<String, String[]> associatedEntities;
    private Map<String, String> serviceTasks;
    private Map<String, String> processVariables;
    private Collection<String> reusableSubProcesses;
    private boolean dynamicFormsEnabled;
    private Collection<ProcessNodeSummary> nodes;
    private Collection<TimerSummary> timers;

    public ProcessSummary() {
    }

    public ProcessSummary(String processDefId,
                          String processDefName,
                          String deploymentId,
                          String version,
                          boolean dynamic) {
        super(processDefId,
              processDefName);
        this.processDefId = processDefId;
        this.processDefName = processDefName;
        this.deploymentId = deploymentId;
        this.version = version;
        this.dynamic = dynamic;
    }

    public String getProcessDefId() {
        return processDefId;
    }

    public void setProcessDefId(String processDefId) {
        this.processDefId = processDefId;
    }

    public String getProcessDefName() {
        return processDefName;
    }

    public void setProcessDefName(String processDefName) {
        this.processDefName = processDefName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public Map<String, String[]> getAssociatedEntities() {
        return associatedEntities;
    }

    public void setAssociatedEntities(Map<String, String[]> associatedEntities) {
        this.associatedEntities = associatedEntities;
    }

    public Map<String, String> getServiceTasks() {
        return serviceTasks;
    }

    public void setServiceTasks(Map<String, String> serviceTasks) {
        this.serviceTasks = serviceTasks;
    }

    public Map<String, String> getProcessVariables() {
        return processVariables;
    }

    public void setProcessVariables(Map<String, String> processVariables) {
        this.processVariables = processVariables;
    }

    public Collection<String> getReusableSubProcesses() {
        return reusableSubProcesses;
    }

    public void setReusableSubProcesses(Collection<String> reusableSubProcesses) {
        this.reusableSubProcesses = reusableSubProcesses;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(Boolean dynamic) {
        this.dynamic = dynamic;
    }

    public boolean isDynamicFormsEnabled() {
        return dynamicFormsEnabled;
    }

    public void setDynamicFormsEnabled(boolean dynamicFormsEnabled) {
        this.dynamicFormsEnabled = dynamicFormsEnabled;
    }

    public Collection<ProcessNodeSummary> getNodes() {
        return nodes;
    }

    public void setNodes(Collection<ProcessNodeSummary> nodes) {
        this.nodes = nodes;
    }

    public void setTimers(Collection<TimerSummary> timers) {
        this.timers = timers;
    }

    public Collection<TimerSummary> getTimers() {
        return timers;
    }

    @Override
    public String toString() {
        return "ProcessSummary{" +
                "processDefId='" + processDefId + '\'' +
                ", processDefName='" + processDefName + '\'' +
                ", version='" + version + '\'' +
                ", deploymentId='" + deploymentId + '\'' +
                ", dynamic=" + dynamic +
                ", associatedEntities=" + associatedEntities +
                ", serviceTasks=" + serviceTasks +
                ", processVariables=" + processVariables +
                ", reusableSubProcesses=" + reusableSubProcesses +
                ", dynamicFormsEnabled=" + dynamicFormsEnabled +
                ", nodes=" + nodes +
                ", timers=" + timers +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    @NonPortable
    public static final class Builder {

        private ProcessSummary processSummary;

        private Builder() {
            processSummary = new ProcessSummary();
        }

        public Builder processDefId(String processDefId) {
            processSummary.setProcessDefId(processDefId);
            return this;
        }

        public Builder processDefName(String processDefName) {
            processSummary.setProcessDefName(processDefName);
            return this;
        }

        public Builder version(String version) {
            processSummary.setVersion(version);
            return this;
        }

        public Builder deploymentId(String deploymentId) {
            processSummary.setDeploymentId(deploymentId);
            return this;
        }

        public Builder dynamic(boolean dynamic) {
            processSummary.setDynamic(dynamic);
            return this;
        }

        public Builder id(String id) {
            processSummary.setId(id);
            return this;
        }

        public Builder name(String name) {
            processSummary.setName(name);
            return this;
        }

        public Builder associatedEntities(Map<String, String[]> associatedEntities) {
            processSummary.setAssociatedEntities(associatedEntities);
            return this;
        }

        public Builder callbacks(List<LabeledCommand> callbacks) {
            processSummary.setCallbacks(callbacks);
            return this;
        }

        public Builder serviceTasks(Map<String, String> serviceTasks) {
            processSummary.setServiceTasks(serviceTasks);
            return this;
        }

        public Builder processVariables(Map<String, String> processVariables) {
            processSummary.setProcessVariables(processVariables);
            return this;
        }

        public Builder reusableSubProcesses(Collection<String> reusableSubProcesses) {
            processSummary.setReusableSubProcesses(reusableSubProcesses);
            return this;
        }

        public Builder nodes(Collection<ProcessNodeSummary> nodes) {
            processSummary.setNodes(nodes);
            return this;
        }

        public Builder timers(Collection<TimerSummary> timers) {
            processSummary.setTimers(timers);
            return this;
        }

        public ProcessSummary build() {
            return processSummary;
        }
    }
}

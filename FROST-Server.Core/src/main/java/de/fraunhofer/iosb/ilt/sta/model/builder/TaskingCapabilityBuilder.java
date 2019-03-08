/*
 * Copyright (C) 2016 Fraunhofer Institut IOSB, Fraunhoferstr. 1, D 76131
 * Karlsruhe, Germany.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fraunhofer.iosb.ilt.sta.model.builder;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.fraunhofer.iosb.ilt.sta.model.Actuator;
import de.fraunhofer.iosb.ilt.sta.model.Task;
import de.fraunhofer.iosb.ilt.sta.model.TaskingCapability;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import de.fraunhofer.iosb.ilt.sta.model.builder.core.NamedEntityBuilder;
import de.fraunhofer.iosb.ilt.sta.model.core.EntitySet;
import de.fraunhofer.iosb.ilt.sta.model.core.EntitySetImpl;
import de.fraunhofer.iosb.ilt.sta.path.EntityType;

/**
 * Builder class for Datastream objects.
 *
 * @author jab
 */
public class TaskingCapabilityBuilder extends NamedEntityBuilder<TaskingCapability, TaskingCapabilityBuilder> {

    private ObjectNode taskingParameters;
    private Actuator actuator;
    private EntitySet<Task> tasks;
    private Thing thing;

    public TaskingCapabilityBuilder() {
        taskingParameters = JsonNodeFactory.instance.objectNode();
        tasks = new EntitySetImpl<>(EntityType.TASK);
    }

    public TaskingCapabilityBuilder setTaskingParameters(ObjectNode taskingParameters) {
        this.taskingParameters = taskingParameters;
        return this;
    }

    public TaskingCapabilityBuilder setActuator(Actuator actuator) {
        this.actuator = actuator;
        return this;
    }

    public TaskingCapabilityBuilder setTasks(EntitySet<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public TaskingCapabilityBuilder addTask(Task task) {
        this.tasks.add(task);
        return this;
    }

    public TaskingCapabilityBuilder setThing(Thing thing) {
        this.thing = thing;
        return this;
    }

    @Override
    protected TaskingCapabilityBuilder getThis() {
        return this;
    }

    @Override
    public TaskingCapability build() {
        TaskingCapability taskingCapability = new TaskingCapability();
        super.build(taskingCapability);
        taskingCapability.setTaskingParameters(taskingParameters);
        taskingCapability.setActuator(actuator);
        taskingCapability.setThing(thing);
        taskingCapability.setTasks(tasks);
        return taskingCapability;
    }

}

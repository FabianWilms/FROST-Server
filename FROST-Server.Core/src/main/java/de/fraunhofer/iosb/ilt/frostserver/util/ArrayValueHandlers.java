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
package de.fraunhofer.iosb.ilt.frostserver.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.fraunhofer.iosb.ilt.frostserver.model.FeatureOfInterest;
import de.fraunhofer.iosb.ilt.frostserver.model.Observation;
import de.fraunhofer.iosb.ilt.frostserver.model.core.Id;
import de.fraunhofer.iosb.ilt.frostserver.model.ext.TimeInstant;
import de.fraunhofer.iosb.ilt.frostserver.model.ext.TimeInterval;
import de.fraunhofer.iosb.ilt.frostserver.persistence.IdManager;
import de.fraunhofer.iosb.ilt.frostserver.persistence.PersistenceManagerFactory;
import static de.fraunhofer.iosb.ilt.frostserver.property.SpecialNames.AT_IOT_ID;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author scf
 */
public class ArrayValueHandlers {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayValueHandlers.class);

    /**
     * Our default handlers.
     */
    private static final Map<String, ArrayValueHandler> HANDLERS = new HashMap<>();

    public static ArrayValueHandler getHandler(String component) {
        if (HANDLERS.isEmpty()) {
            createDefaults();
        }
        return HANDLERS.get(component);
    }

    private static synchronized void createDefaults() {
        if (!HANDLERS.isEmpty()) {
            return;
        }

        final IdManager idManager = PersistenceManagerFactory.getInstance().getIdManager();
        ArrayValueHandler idHandler = (JsonNode value, Observation target) -> target.setId(idManager.parseId(value.toString()));
        HANDLERS.put("id", idHandler);
        HANDLERS.put(AT_IOT_ID, idHandler);
        HANDLERS.put(
                "result",
                (JsonNode value, Observation target) -> target.setResult((JsonNode) value)
        );
        HANDLERS.put(
                "resultQuality",
                (JsonNode value, Observation target) -> target.setResultQuality((JsonNode) value)
        );
        HANDLERS.put(
                "parameters",
                (JsonNode value, Observation target) -> {
                    if (value instanceof ObjectNode) {
                        target.setParameters((ObjectNode) value);
                        return;
                    }
                    throw new IllegalArgumentException("parameters has to be an ObjectNode.");
                });
        HANDLERS.put(
                "phenomenonTime",
                (JsonNode value, Observation target) -> {
                    try {
                        TimeInstant time = TimeInstant.parse(value.textValue());
                        target.setPhenomenonTime(time);
                        return;
                    } catch (Exception e) {
                        LOGGER.trace("Not a time instant: {}.", value);
                    }
                    try {
                        TimeInterval time = TimeInterval.parse(value.textValue());
                        target.setPhenomenonTime(time);
                        return;
                    } catch (Exception e) {
                        LOGGER.trace("Not a time interval: {}.", value);
                    }
                    throw new IllegalArgumentException("phenomenonTime could not be parsed as time instant or time interval.");
                });
        HANDLERS.put(
                "resultTime",
                (JsonNode value, Observation target) -> {
                    try {
                        TimeInstant time = TimeInstant.parse(value.textValue());
                        target.setResultTime(time);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("resultTime could not be parsed as time instant or time interval.", e);
                    }
                });
        HANDLERS.put(
                "validTime",
                (JsonNode value, Observation target) -> {
                    try {
                        TimeInterval time = TimeInterval.parse(value.textValue());
                        target.setValidTime(time);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("resultTime could not be parsed as time instant or time interval.", e);
                    }
                });
        HANDLERS.put(
                "FeatureOfInterest/id",
                (JsonNode value, Observation target) -> {
                    Id foiId = idManager.parseId(value.asText());
                    target.setFeatureOfInterest(new FeatureOfInterest(foiId));
                });

    }

    public interface ArrayValueHandler {

        public void handle(JsonNode value, Observation target);
    }
}

/*
 * Copyright (C) 2020 Fraunhofer Institut IOSB, Fraunhoferstr. 1, D 76131
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
package de.fraunhofer.iosb.ilt.frostserver.formatter;

import de.fraunhofer.iosb.ilt.frostserver.service.PluginResultFormat;
import de.fraunhofer.iosb.ilt.frostserver.settings.CoreSettings;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author scf
 */
public class PluginResultFormatDefault implements PluginResultFormat {

    /**
     * The "name" of the default resultFormatter.
     */
    public static final String DEFAULT_FORMAT_NAME = "default";

    private CoreSettings settings;

    @Override
    public void init(CoreSettings settings) {
        this.settings = settings;
        settings.getPluginManager().registerPlugin(this);
    }

    @Override
    public Collection<String> getFormatNames() {
        return Arrays.asList(DEFAULT_FORMAT_NAME);
    }

    @Override
    public ResultFormatter getResultFormatter() {
        return new ResultFormatterDefault(settings);
    }

}

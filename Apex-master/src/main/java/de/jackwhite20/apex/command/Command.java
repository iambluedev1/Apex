/*
 * Copyright (c) 2017 "JackWhite20"
 *
 * This file is part of Apex.
 *
 * Apex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.jackwhite20.apex.command;

/**
 * Created by JackWhite20 on 29.10.2016.
 */
public abstract class Command {

    private String name;
    private String[] aliases;
    private String description;

    public Command(String name, String description, String... aliases) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
    }

    public boolean isValidAlias(String cmd) {
        for (String alias : this.aliases) {
            if (alias.equals(cmd))
                return true;
        }

        return false;
    }

    public abstract boolean execute(String[] args);

    public String getName() {
        return this.name;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public String getDescription() {
        return this.description;
    }
}

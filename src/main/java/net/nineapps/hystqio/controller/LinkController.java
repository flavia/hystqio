/*
Copyright (C) 2010 Viral Patel
Copyright (C) 2011 9Apps.net

This file is part of hystqio.

hystqio is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

hystqio is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with hystqio. If not, see <http://www.gnu.org/licenses/>.
*/

package net.nineapps.hystqio.controller;

import net.nineapps.hystqio.model.Link;

/**
 * Interface for persisting and retrieving links
 * in the persistence layer (be it RDS, SimpleDB, plain MySQL, etc.)
 */
public interface LinkController {

	public Link get(String shortCode);
	public Link add(Link link);
	public void incrementClicks(Link link);
}

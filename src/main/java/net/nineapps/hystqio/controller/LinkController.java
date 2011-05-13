package net.nineapps.hystqio.controller;

import net.nineapps.hystqio.model.Link;

public interface LinkController {

	public Link get(String shortCode);
	public Link add(Link link);
}

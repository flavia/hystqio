<!--
    Copyright (C) 2010 Viral Patel
    Copyright (C) 2011 9Apps.net

    This file is part of hystqio.

    hystqio is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    hystqio is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with hystqio. If not, see <http://www.gnu.org/licenses/>.
-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Shorty - An Open Source URL Shortner in Struts2/Hibernate</title>
<link href="css/style.css" rel="stylesheet" />
</head>
<body>
	<h1 class="title">Shorty</h1>

	Short Code: <s:property value="link.shortCode" /> <br />
	Original URL: <s:property value="link.url" /> <br />
	Clicks: <s:property value="link.clicks" /> <br />
	Created On: <s:property value="link.created" /> <br />

</body>
</html>

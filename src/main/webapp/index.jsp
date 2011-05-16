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
<link href="css/style.css" rel="stylesheet"/>
</head>
<body>
<h1 class="title">Shorty</h1>
<br>
<p>A Simple URL Shortner in Struts2/Hibernate/MySQL</p>
<br><br>
<div id="link-container">

	<s:form action="add" method="post">
		<s:actionerror/>
		<s:textfield name="url" cssClass="link"/>
		<s:submit value="Shorten"/>
	</s:form>
	<s:if test="link.shortCode != null">
			<h3><s:text name="shorty.base.url"/><s:property value="link.shortCode"/></h3>
			<a href="<s:text name="shorty.base.url"/><s:property value="link.shortCode"/>+">[details]</a>
	</s:if>
	
</div>
</body>
</html>

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
  <meta charset="utf-8">

  <!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame
       Remove this if you use the .htaccess -->
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="description" content="A Simple URL Shortner in Struts2/Hibernate/MySQL">
  <meta name="author" content="9Apps">

  <!-- Mobile viewport optimized: j.mp/bplateviewport -->
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Shorty - An Open Source URL Shortner in Struts2/Hibernate</title>
<!-- CSS: implied media="all" -->
<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/yui/2.8.1/build/reset-fonts-grids/reset-fonts-grids.css"/> 
<link rel="stylesheet" href="css/style.css?v=2">

<!-- All JavaScript at the bottom, except for Modernizr which enables HTML5 elements & feature detects -->
<script src="js/libs/modernizr-1.7.min.js"></script>

</head>
<body>
<div id="container">

    <header>
		<hgroup>
			<h1>Shorty - An Open Source URL Shortner in Struts2/Hibernate</h1>
			<h3>Simply paste your URL. <em>We'll shrink it!</em></h3>
		</hgroup>
    </header>

    <div id="main" role="main">
		<div class="front-page">
					<s:form action="add" method="post" id="signup-form" cssClass="signup">
						<s:actionerror/>
						<div class="search">
							<div class="holding">
								<s:textfield name="url" cssClass="link"/>
							</div>
						</div>
						<div class="front-signup">
							<s:submit value="Shorty me" id="add_0" cssClass="promotional submit button" />
						</div>
					</s:form>
			<s:if test="link.shortCode != null">
				<h3><s:text name="shorty.base.url"/><s:property value="link.shortCode"/></h3>
				<a href="<s:text name="shorty.base.url"/><s:property value="link.shortCode"/>+">[details]</a>
			</s:if>
		</div>
	</div>
	
</div>

<!-- JavaScript at the bottom for fast page loading -->

<!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if necessary -->
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.js"></script>
<script>window.jQuery || document.write("<script src='js/libs/jquery-1.5.1.min.js'>\x3C/script>")</script>


<!-- scripts concatenated and minified via ant build script-->
<script src="js/plugins.js"></script>
<script src="js/script.js"></script>
<script src="js/libs/jquery.infieldlabel.min.js"></script>
	<script>var $j=jQuery.noConflict();</script>
	<script>
	$j(document).ready(function(){
		$j("#signup-form label").inFieldLabels();
	});
	</script>
<!-- end scripts-->

<!--[if lt IE 7 ]>
  <script src="js/libs/dd_belatedpng.js"></script>
  <script>DD_belatedPNG.fix("img, .png_bg"); // Fix any <img> or .png_bg bg-images. Also, please read goo.gl/mZiyb </script>
<![endif]-->
</body>
</html>

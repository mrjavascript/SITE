<div id="main-content" role="main">
	<div class="portlet-layout row">
		<div class="col-lg-12 portlet-column-only" id="column-1">
			$processor.processColumn("column-1", "portlet-column-content portlet-column-content-only")
		</div>
	</div>
	<div class="portlet-layout row">
		<div class="col-lg-8 portlet-column-first" id="column-2">
			$processor.processColumn("column-2", "portlet-column-content portlet-column-content-first")
		</div>
		<div class="col-lg-4" id="column-3">
			$processor.processColumn("column-3", "portlet-column-content")
		</div>
	</div>
</div>
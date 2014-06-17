function loadStatistics(data, statsTitle, statsDivId, width, height) {
	
			var margin = {
				top : 60,
				right : 20,
				bottom : 70,
				left : 30
			};
			
			width = width - margin.left - margin.right;
			height = height - margin.top - margin.bottom;

			// Parse the Week / time
			var parseWeek = d3.time.format("%Y-%m-%d").parse;

			var color = d3.scale.category10();
			
			// Set the ranges
			var x = d3.time.scale().range([ 0, width ]);
			var y = d3.scale.linear().range([ height, 0 ]);

			var formatDate = d3.time.format("%e %b");

			// Define the axes
			var xAxis = d3.svg.axis().scale(x).orient("bottom").tickFormat(formatDate);

			var yAxis = d3.svg.axis().scale(y).orient("left").ticks(5).tickFormat(
					d3.format("2s"));

			
			var line = d3.svg.line()
		    .interpolate("basis")
		    .x(function(d) { return x(d.Week); })
		    .y(function(d) { return y(d.value); });
			
			// Adds the svg canvas
			var svg = d3.select(statsDivId).append("svg").attr("width",
					width + margin.left + margin.right).attr("height",
					height + margin.top + margin.bottom).append("g").attr("transform",
					"translate(" + margin.left + "," + margin.top + ")");
		
		color.domain(d3.keys(data[0]).filter(function(key) { 
			return key !== "Week"; 
		}));
		
		data.forEach(function(d) {
			d.Week = parseWeek(d.Week);
			d.Total = +d.Total;
		});

		
		y.domain([ 0, d3.max(data, function(d) {
			return d.Total;
		}) ]);
		
		var items = color.domain().map(function(name) {
		    return {
		      name: name,
		      values: data.map(function(d) {
		        return {Week: d.Week, value: +d[name]};
		      })
		    };
		  });
		
		// Scale the range of the data
		x.domain(d3.extent(data, function(d) {
			return d.Week;
		}));

		  y.domain([
		    0,
		    d3.max(items, function(c) { return d3.max(c.values, function(v) { return v.value; }); })
		  ]);
		

		// Add the X Axis
		svg.append("g").attr("class", "x axis xaxis").attr("transform",
				"translate(0," + height + ")").call(xAxis).selectAll("text")
				.style("text-anchor", "end").style("font-size", ".7em").attr(
						"dx", "-.8em").attr("dy", ".15em").attr("transform",
						function(d) {
							return "rotate(-65)";
						});
		
		svg.selectAll(".xaxis").append("text")
	  	.attr("dy", "60")
	  	.attr("dx", width/2)
	    .style("text-anchor", "middle")
	    .text(statsTitle);

		// Add the Y Axis
		svg.append("g").attr("class", "y axis").call(yAxis).selectAll("text")
				.style("font-size", ".7em");
		
		
		var item = svg.selectAll(".item")
	      .data(items)
	    .enter().append("g")
	      .attr("class", "item");

	  item.append("path")
	      .attr("class", "line")
	      .attr("d", function(d) { return line(d.values); })
	      .style("stroke", function(d) { return color(d.name); });

	  var legend = svg.selectAll(".legend")
      .data(color.domain().slice())
      .enter().append("g")
      .attr("class", "legend")
      .attr("y", "10")
      .attr("transform", function(d, i) { return "translate("+ ((i > 2)?-250:0) +"," + ((i > 2)?(-50 + ((i-3) * 15)):(-50 + (i * 15))) + ")"; });

  legend.append("rect")
      .attr("x", width - 18)
      .attr("width", 18)
      .attr("height", 3)
      .style("fill", color);

  legend.append("text")
      .attr("x", width - 24)
      .attr("y", 9)
      .attr("dy", "-.35em")
      .style("text-anchor", "end")
      .style("font-size", ".75em")
      .text(function(d) { return d; });

}




function loadStatistics(actionURL, statsTitle, statsTitleDx, statsColorArray) {
var margin = {top: 60, right: 20, bottom: 100, left: 30},
    width = 330 - margin.left - margin.right,
    height = 300 - margin.top - margin.bottom;

var x = d3.scale.ordinal()
    .rangeRoundBands([0, width], .1);

var y = d3.scale.linear()
    .rangeRound([height, 0]);

var color = d3.scale.ordinal()
    .range(statsColorArray);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
    .tickFormat(d3.format("2s"));

var svg = d3.select("#canvas-svg").append("svg")
    .attr("viewBox", "0 0 " + (width + margin.left + margin.right) + " " + (height + margin.top + margin.bottom))
    .attr("preserveAspectRatio", "xMidYMid meet")
    .attr("height", (height + margin.top + margin.bottom))
    .attr("width", (width + margin.left + margin.right) )
  	.append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

d3.csv(actionURL, function(error, data) {
  color.domain(d3.keys(data[0]).filter(function(key) { return key !== "Week"; }));

  data.forEach(function(d) {
    var y0 = 0;
    d.ages = color.domain().map(function(name) { return {name: name, y0: y0, y1: y0 += +d[name]}; });
    d.total = d.ages[d.ages.length - 1].y1;
  });

  x.domain(data.reverse().map(function(d) { return d.Week; }));
  y.domain([0, d3.max(data, function(d) { return d.total; })]);

  svg.append("g")
      .attr("class", "x axis xaxis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis)
      .selectAll("text")  
      .style("text-anchor", "end")
      .style("font-size", ".7em")
      .attr("dx", "-.8em")
      .attr("dy", ".15em")
      .attr("transform", function(d) {
          return "rotate(-65)"; 
          });
  
  svg.selectAll(".xaxis").append("text")
  	.attr("dy", "90")
  	.attr("dx", statsTitleDx)
  	.style("text-align", "center")
    .style("text-anchor", "end")
    .text(statsTitle);

  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
      .selectAll("text")  
      .style("font-size", ".7em");

  var week = svg.selectAll(".week")
      .data(data)
    .enter().append("g")
      .attr("class", "g")
      .attr("transform", function(d) { return "translate(" + x(d.Week) + ",0)"; });

  week.selectAll("rect")
      .data(function(d) { return d.ages; })
    .enter().append("rect")
      .attr("width", x.rangeBand())
      .attr("y", function(d) { return y(d.y1); })
      .attr("height", function(d) { return y(d.y0) - y(d.y1); })
      .style("fill", function(d) { return color(d.name); });

  var legend = svg.selectAll(".legend")
      .data(color.domain().slice())
    .enter().append("g")
      .attr("class", "legend")
      .attr("y", "10")
      .attr("transform", function(d, i) { return "translate(0," + (-50 + (i * 20)) + ")"; });

  legend.append("rect")
      .attr("x", width - 18)
      .attr("width", 18)
      .attr("height", 18)
      .style("fill", color);

  legend.append("text")
      .attr("x", width - 24)
      .attr("y", 9)
      .attr("dy", ".35em")
      .style("text-anchor", "end")
      .text(function(d) { return d; });

});

}
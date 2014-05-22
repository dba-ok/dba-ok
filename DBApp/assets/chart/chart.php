<html>
  <head>
      <title></title>      
  </head>    
<?php

 // PHP variable to store the host address
 $db_host  = "putyourhostname";
 // PHP variable to store the username
 $db_uid  = "putyourusername";
 // PHP variable to store the password
 $db_pass = "yourpassword";
 // PHP variable to store the Database name  
 $db_name  = "yourdatabasename";
 // PHP variable to store the result of the PHP function 'mysql_connect()' which establishes the PHP & MySQL connection 

 $chart_type = $_POST['selectcharttype']; 
 $game_name = $_POST['game'];
 $chart_title = $chart_type.' for '.$game_name;
 
 $db_con = mysql_connect($db_host,$db_uid,$db_pass) or die('could not connect');
 mysql_select_db($db_name);

 // Query to check
 $sql = "SELECT * FROM ChartData WHERE Game ='". $_POST["game"]."'" ;
 
 // Executing the query
 $result = mysql_query($sql);
 $row=mysql_fetch_assoc($result);

        // storing results to pass to javascript 
 $mon = $row['Mon'];
 $tue = $row['Tue'];
 $wed = $row['Wed'];
 $thu = $row['Thu'];
 $fri = $row['Fri'];
 $sat = $row['Sat'];
 $sun = $row['Sun'];
         
 mysql_close();   
?>

<!--Load the AJAX API-->

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">

 var chartType = new String("<?php echo $chart_type; ?>");
 var persons = new Array();
 var chartTitle = new String();
 chartTitle = "<?php echo $chart_title; ?>"; 

 persons[0] = <?php echo $mon; ?>;
 persons[1] = <?php echo $tue; ?>;
 persons[2] = <?php echo $wed; ?>;
 persons[3] = <?php echo $thu; ?>;
 persons[4] = <?php echo $fri; ?>;
 persons[5] = <?php echo $sat; ?>;
 persons[6] = <?php echo $sun; ?>;


      // Load the Visualization API and the piechart package.
      google.load('visualization', '1.0', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.setOnLoadCallback(drawChart);

      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawChart() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Day');
        data.addColumn('number', 'Persons');
        data.addRows([
          ['Mon', persons[0]],
          ['Tue', persons[1]],
          ['Wed', persons[2]],
          ['Thu', persons[3]],
          ['Fri', persons[4]],
   ['Sat', persons[5]],
   ['Sun', persons[6]] 
        ]);        
               
        // Instantiate and draw our chart, passing in some options.

        if(chartType == "Pie Chart"){           
            var options = {'title':chartTitle,'width':400,'height':400,"backgroundColor": { fill: "black"}, 'is3D': true, 'titleTextStyle': {color: 'white', fontSize: '16'}, 'legend': {textStyle: {color: 'white'}} };
            var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        }
        else if (chartType == "Bar Chart"){
            var options = {'title':chartTitle,'width':400,'height':400,"backgroundColor": { fill: "black"}, 'titleTextStyle': {color: 'white', fontSize: '16'}, 'hAxis': {title: 'Count of Persons', titleTextStyle: {color: 'white'}, textStyle: {color: 'white'} }, 'vAxis': {title: 'Days', titleTextStyle: {color: 'white'}, textStyle: {color: 'white'}} , 'hAxis.textStyle': {color: 'white'}, 'legend': {position:'bottom', textStyle: {color: 'white'}} };
            var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
        }
        
        chart.draw(data, options);
      }
    </script>  

  <body>      
    <!--Div that will hold the chart-->
    <div id="chart_div" ></div>
  </body>
</html>
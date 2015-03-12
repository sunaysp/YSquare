1. extractData.java -- Extract data from Four Square API (provided 41 zip codes and categories and extract data from users’ likes)
2. ExtractFourSquareData.zip  -- Map Reduce Program on Four Square data and aggregate likes and categories per location
3. Hive Queries - LoadYSquareData.sql     Load the data from Yelp API
                - getRecommendation.sql   Get top 3 recommendations
4. YSquare.zip 
    ---------
    1. YelpAPI.java  - Extract data from Yelp 
    2. GoogleFusion.java - Data generation for Google fusion maps (final output)

5. chart.html - Generates Pie chart


Steps
-----

1. Open extractData.java file. Give output file path in WriteToFile method. Run extractData.java to get different categories. After the program runs successfully, the output file is located in the path given. 
2. Unzip ExtractRourSquareData.zip folder. Give the output file from step 1 as the input to Mapper and set the output file location. After MR job is successful, the output file is located in output folder.
3. The output of Map Reduce program in step2 is given as input to YelpAPI.java. Copy the output of MR file into data folder inside YSquare project and set the file name(path) in program.
4. Run YelpAPI.java program to get the data from yelp. The output is saved inside data folder. 
5. Load this output to a hive table using LoadYSquareData.sql 
	i.e.$ hive -f "LoadYSquareData.sql"
6. Run getRecommendation.sql file to get the final recommendations as shown below.
	$ hive -f "getRecommendation.sql" > output.txt 
7. output.txt file will have the recommendations for each zipcode. This needs to be shown on map. 
8. GoogleFusion.java will format the data in the way needed to display on map. Give output.txt path in the code GoogleFusion.java and run the code. 

	Analytics Part
	--------------
9. Finally, the output of GoogleFusion.java is loaded to Google Fusion (steps given in main website) 
	i. https://support.google.com/fusiontables/answer/2571232
	ii. Add heading to each column in tab seperated value(For ex, zipcode, category, averageRating1, likes1, averageRating2, likes2, averageRating3, likes3). Load the output as tab seperated value to fusion table. 
The info window is given below.
<font size="3"><b>Recommendations for {ZipCode}</b></font>
<div style="width:400px; height:200px">
<img src="//charts.brace.io/bar.svg?{Category1}={Average Rating I}&{Category2}={Average Rating II}&{Category3}={Average Rating III}&_style=light&_height=400px"></img>
</div>
<div class='googft-info-window' style="background-color:lightgrey">
<b>ZipCode: {ZipCode}</b><br>
<font color=red>Category I: <b>{Category1}</b><br></font>
<font color=red>Likes I: <b>{Likes I}</b><br></font>
<font color=blue>Category II:<b> {Category2}</b><br></font>
<font color=blue>Likes II:<b> {Likes II}</b><br></font>
<font color=green>Category III:<b> {Category3}</b> <br></font>
<font color=green>Likes III:{Likes III}</b><br></font>
</div>

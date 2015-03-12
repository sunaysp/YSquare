create table ysquare(zipcode string, category string, rating float, likes int)
row format delimited 
fields terminated by '\t';

load data local inpath 'YelpRatings.txt'
overwrite into table ysquare;
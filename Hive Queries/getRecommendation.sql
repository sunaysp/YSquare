select zipcode, category, average_rating, likes from (
		select zipcode, category, average_rating, likes, rank() over (partition by zipcode order by likes desc) A
	from 
		(select zipcode, category, AVG(rating) as average_rating, likes
		from ysquare group by zipcode, category, likes having average_rating < 3.7) T
) T2 where A <= 3 group by zipcode, category, average_rating, likes;

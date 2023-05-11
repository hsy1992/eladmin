# SELECT cate_name,count(1) FROM `ts_product_base_info` GROUP BY cate_name;


DROP PROCEDURE IF EXISTS init_classify;
CREATE PROCEDURE init_classify()
BEGIN
	# 定义变量
	DECLARE s int Default 0;
	DECLARE cateName varchar(255);
	DECLARE countInfo varchar(255);
	DECLARE list CURSOR FOR SELECT cate_name, count(1) FROM `ts_product_base_info` GROUP BY cate_name;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET s=1;
	-- 打开游标
	open list;
		-- 将游标中的值赋值给变量，注意：变量名不要和返回的列名同名，变量顺序要和sql结果列的顺序一致
		fetch list into cateName, countInfo;
		-- 当s不等于1，也就是未遍历完时，会一直循环
		while s<>1 do
			-- 执行业务逻辑
			insert into ts_product_classify(is_del, createD_time,updated_time,pid,cate_name,sort, icon, is_show) VALUES(0, NOW(), NOW(), 0, cateName, 0, "", 1);

			-- 将游标中的值再赋值给变量，供下次循环使用
			fetch list into cateName, countInfo;
		-- 当s等于1时表明遍历以完成，退出循环
		end while;
	-- 关闭游标
	close list;
END;


call init_classify()
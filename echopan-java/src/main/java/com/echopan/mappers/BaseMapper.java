package com.echopan.mappers;


 import org.apache.ibatis.annotations.Param;

 import java.util.List;

 interface BaseMapper<T, P> {
	/**
	 * insert:(插入). <br/>
	 */
	 Integer insert(@Param("bean") T t);


	/**
	 * insertOrUpdate:(插入或者更新). <br/>
	 */
	 Integer insertOrUpdate(@Param("bean") T t);


	/**
	 * insertBatch:(批量插入). <br/>
	 */
	 Integer insertBatch(@Param("list") List<T> list);


    /**
	 * insertOrUpdateBatch:(批量插入或更新). <br/>
	 */
	 Integer insertOrUpdateBatch(@Param("list") List<T> list);


	/**
	 * selectList:(根据参数查询集合). <br/>
	 */
	 List<T> selectList(@Param("query") P p);

	/**
	 * selectCount:(根据集合查询数量). <br/>
	 */
	 Integer selectCount(@Param("query") P p);
}

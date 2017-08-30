package com.z.quick.orm.session;

import java.util.List;

import com.z.quick.orm.connection.JDBCConfig;

public interface DataBaseManipulation<T> {
	
	JDBCConfig getJdbcConfig();

	int save(Object o);

	int delete(Object o);

	int update(Object o);

	T get(Object o);

	T get(Object o, Class<?> clzz);

	List<T> list(Object o);

	List<T> list(Object o, Class<?> clzz);

	int save(String sql, Object... params);

	int delete(String sql, Object... params);
	
	int update(String sql, Object... params);

	T get(String sql, Class<?> clzz, Object... params);

	List<T> list(String sql, Class<?> clzz, Object... params);

	void start();

	void rollback();

	void commit();

	void close();

}
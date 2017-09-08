package com.z.quick.orm.sql.builder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.z.quick.orm.annotation.PrimaryKey;
import com.z.quick.orm.annotation.Table;
import com.z.quick.orm.cache.ClassCache;
import com.z.quick.orm.common.Constants;
import com.z.quick.orm.exception.SqlBuilderException;
import com.z.quick.orm.sql.SqlInfo;
/**
 * ******************  类说明  *********************
 * class       :  DefaultCreateTableSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成create table SQL
 * @see        :                        
 * ***********************************************
 */
public class DefaultCreateTableSqlBuilder extends AbstractSqlBuilder {
	private static final Log log = LogFactory.get();
	private static final String CREATE_TABLE_TEMPLATE = "create table #tableName(#columns primary key(#primaryKey))";
	@Override
	public SqlInfo builderSql(Object o) {
		Class<?> clzz = (Class<?>) o;
		String tableName = clzz.getAnnotation(Table.class).tableName();
		List<Field> fieldList = ClassCache.getInsert(clzz);
		StringBuffer columns = new StringBuffer();
		fieldList.forEach(f->{
			if (typeConvert(f) == null) {
				log.info("未找到{}类型对应的数据库类型",f.getType());
				return;
			}
			columns.append(f.getName()).append(Constants.SPACE).append(typeConvert(f)).append(",");
		});
		fieldList = ClassCache.getPrimaryKey(clzz);
		StringBuffer primaryKey = new StringBuffer();
		if (fieldList.size() == 0) {
			throw new SqlBuilderException("No primaryKey");
		}
		fieldList.forEach(f->{
			primaryKey.append(f.getName()).append(",");
		});
		primaryKey.deleteCharAt(primaryKey.lastIndexOf(","));
		
		String sql = CREATE_TABLE_TEMPLATE.replace("#tableName", tableName);
		sql = sql.replace("#columns", columns);
		sql = sql.replace("#primaryKey", primaryKey);
		return new SqlInfo(sql, new ArrayList<>());
	}
	
	public String typeConvert(Field f){
		Class<?> type = f.getType();
		String value = null;
		if (String.class == type) {
			value = "varchar(64)";
		}else if(Boolean.class == type){
			value = "smallint";
		}else if(Integer.class == type || Short.class == type){
			value = "integer";
		}else if(Double.class == type || Float.class == type){
			value = "double";
		}else if(BigDecimal.class == type){
			value = "decimal(12,2)";
		}else if(Long.class == type){
			value = "bigint";
		}else if(Timestamp.class == type){
			value = "timestamp";
		}
		if (value != null) {
			if (f.getAnnotation(PrimaryKey.class) != null) {
				value = value + " " + "not null";	
			}
		}
		return value;
	}
}





/*
 * 
 */
package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.Category;

// TODO: Auto-generated Javadoc
/**
 * The Interface CategoryMapper.
 */
public interface CategoryMapper {
	
	/**
	 * Gets the.
	 *
	 * @param categoryID the category id
	 * @return the category
	 * @throws Exception the exception
	 */
	Category get(int categoryID) throws Exception;

	/**
	 * Post.
	 *
	 * @param category the category
	 * @return the int
	 * @throws Exception the exception
	 */
	int post(Category category) throws Exception;

	/**
	 * Delete.
	 *
	 * @param categoryID the category id
	 * @return the int
	 * @throws Exception the exception
	 */
	int delete(int categoryID) throws Exception;

	/**
	 * Gets the all categories.
	 *
	 * @return the all categories
	 * @throws Exception the exception
	 */
	Category[] getAllCategories() throws Exception;
}

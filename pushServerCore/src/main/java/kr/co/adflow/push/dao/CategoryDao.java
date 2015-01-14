/*
 * 
 */
package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Category;

// TODO: Auto-generated Javadoc
/**
 * The Interface CategoryDao.
 *
 * @author nadir93
 * @date 2014. 7. 7.
 */
public interface CategoryDao {
	
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

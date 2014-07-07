package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Category;

/**
 * @author nadir93
 * @date 2014. 7. 7.
 * 
 */
public interface CategoryDao {
	Category get(int categoryID) throws Exception;

	int post(Category category) throws Exception;

	int delete(int categoryID) throws Exception;

	Category[] getAllCategories() throws Exception;
}

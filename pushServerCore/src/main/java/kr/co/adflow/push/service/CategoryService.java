package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Category;

/**
 * @author nadir93
 * @date 2014. 7. 7.
 * 
 */
public interface CategoryService {
	Category get(int categoryID) throws Exception;

	Category[] getAllCategories() throws Exception;

	int post(Category category) throws Exception;

	int delete(int categoryID) throws Exception;

}

package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.Category;

public interface CategoryMapper {
	Category get(int categoryID) throws Exception;

	int post(Category category) throws Exception;

	int delete(int categoryID) throws Exception;

	Category[] getAllCategories() throws Exception;
}
